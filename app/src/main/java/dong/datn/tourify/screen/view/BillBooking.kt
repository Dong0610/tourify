package dong.datn.tourify.screen.view


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.isPostNotification
import dong.datn.tourify.app.percentDeposit
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.firebase.NotificationHelper
import dong.datn.tourify.firebase.RealTime
import dong.datn.tourify.firebase.putImgToStorage
import dong.datn.tourify.screen.client.ClientScreen
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.CallbackType
import dong.datn.tourify.utils.NOTIFICATION
import dong.datn.tourify.utils.ORDER
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.delay
import dong.datn.tourify.utils.timeNow
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.ButtonNext2
import dong.datn.tourify.widget.IconView2
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParentContent
import dong.datn.tourify.widget.navigationTo
import dong.duan.travelapp.model.TourTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume


@Composable
fun CreateInvoiceOrder(nav: NavController, viewModels: AppViewModel, payment: (String) -> Unit) {
    val context = LocalContext.current

    val salePrice = remember {
        mutableStateOf(dong.datn.tourify.app.viewModels.salePrice.value)
    }
    val coroutineScope = rememberCoroutineScope()
    val picture = remember { Picture() }
    val tour = remember {
        mutableStateOf(viewModels.bookingTourNow.value)
    }

    fun createUriFromView(callback: (String) -> Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val bitmap = createBitmapFromPicture(picture)
            val uri = bitmap.saveToDisk(context)
            viewModels.storage.getReference("INVOICE")
                .putImgToStorage(context, uri) {
                    callback(it.toString())
                }
        }
    }
    ViewParentContent(onBack = {
        nav.popBackStack()
    }) {

        Column(
            modifier = Modifier
                .background(darkGray)
                .fillMaxSize()
                .drawWithCache {
                    val width = this.size.width.toInt()
                    val height = this.size.height.toInt()
                    onDrawWithContent {
                        val pictureCanvas = androidx.compose.ui.graphics.Canvas(
                            picture.beginRecording(width, height)
                        )
                        draw(this, this.layoutDirection, pictureCanvas, this.size) {
                            this@onDrawWithContent.drawContent()
                        }
                        picture.endRecording()
                        drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                    }
                }
        ) {
            BillBooking(salePrice,viewModels.tourTimeSelected.value!!)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconView2(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                nav.popBackStack()
            }

            Modifier.weight(1f)

            Row(Modifier.fillMaxWidth()) {
                ButtonNext2(
                    text = context.getString(R.string.zalopay),
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f)
                ) {
                    payment.invoke("${prePayment.value.toInt()}")
                }
                SpaceW(w = 12)
                ButtonNext2(
                    text = context.getString(R.string.continues),
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f)
                ) {
                    viewModels.loadingState.value = true
                    viewModels.creteOrderByTour(
                        tour.value!!, viewModels.notes.value, salePrice.value,
                        viewModels.tourTimeSelected.value!!
                    ) { it, id, notiId ->
                        if (it == CallbackType.SUCCESS) {
                            if (isPostNotification) {
                                NotificationHelper(context = context).sendNotificationBooking(tour.value!!)
                            }

                            createUriFromView { url ->
                                Firestore.updateAsync("$ORDER/$id", hashMapOf<String, Any>().apply {
                                    put("invoiceUrl", url)
                                })
                                viewModels.sendMailOrder(id, url)
                                RealTime.udapte(
                                    "$NOTIFICATION/${authSignIn!!.UId}/${notiId.notiId}",
                                    notiId.apply {
                                        link = url
                                    },
                                    {},
                                    {})

                            }
                            delay(1500) {
                                nav.navigationTo(ClientScreen.BookingScreen.route)
                                viewModels.loadingState.value = false
                            }
                        } else if (it == CallbackType.INFO) {
                            viewModels.loadingState.value = false
                        }
                    }
                }
            }


        }
    }

}

private fun createBitmapFromPicture(picture: Picture): Bitmap {
    val bitmap = Bitmap.createBitmap(
        picture.width,
        picture.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)
    canvas.drawPicture(picture)
    return bitmap
}

private suspend fun Bitmap.saveToDisk(context: Context): Uri {
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "screenshot-${System.currentTimeMillis()}.png"
    )

    file.writeBitmap(this, Bitmap.CompressFormat.PNG, 100)
    Log.d("TAG", "${file.absoluteFile}")
    return scanFilePath(context, file.path) ?: throw Exception("File could not be saved")
}

private suspend fun scanFilePath(context: Context, filePath: String): Uri? {
    return suspendCancellableCoroutine { continuation ->
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath),
            arrayOf("image/png")
        ) { _, scannedUri ->
            if (scannedUri == null) {
                continuation.cancel(Exception("File $filePath could not be scanned"))
            } else {
                continuation.resume(scannedUri)
            }
        }
    }
}

private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

val prePayment = mutableStateOf(0.0)

@Composable
fun BillBooking(salePrice: MutableState<Double>, value: TourTime) {
    val adultPrice = remember {
        mutableStateOf(salePrice.value * viewModels.countAdult.value)
    }
    val childPrice = remember {
        mutableStateOf(salePrice.value * 0.75f * viewModels.countChild.value)
    }
    val context = LocalContext.current


    prePayment.value =
        (childPrice.value + adultPrice.value) - ((adultPrice.value + childPrice.value) * percentDeposit)
    ViewParentContent(
        onBack = {}, modifier = Modifier
            .background(white)
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp), verticalArrangement = Arrangement.Top
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_golden_sea),
                    contentDescription = "",
                    Modifier.widthPercent(32f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                TextView(
                    text = "CÔNG TY TNHH GOLDEN SEA",
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Center,
                    textSize = 13,

                    font = Font(R.font.poppins_semibold)
                )
            }
            SpaceH(h = 6)
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    Modifier.widthPercent(32f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                ) {
                    TextView(
                        text = context.getString(R.string.address) + ": Tầng 8 tòa NewSala khu đô thị Xa La, Tân Triều Hà Đông, Hà Nội",
                        modifier = Modifier,
                        textSize = 11,
                        font = Font(R.font.poppins_regular)
                    )
                    TextView(
                        text = context.getString(R.string.phone_number) + ": (028) 38682388",
                        modifier = Modifier,
                        textSize = 11,
                        font = Font(R.font.poppins_regular)
                    )
                    TextView(
                        text = context.getString(R.string.email) + ": haitrinh@gsttour.com",
                        modifier = Modifier,
                        textSize = 11,
                        font = Font(R.font.poppins_regular)
                    )
                }

            }
            SpaceH(h = 24)
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), horizontalArrangement = Arrangement.Center
            ) {
                TextView(
                    text = context.getString(R.string.payment_invoice),
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Center,
                    textSize = 15,
                    font = Font(R.font.poppins_bold)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), horizontalArrangement = Arrangement.Center
            ) {
                TextView(
                    text = "(${timeNow()})",
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 2.dp),
                    textAlign = TextAlign.Center,
                    textSize = 11,
                    font = Font(R.font.poppins_regular)
                )
            }
            SpaceH(h = 24)
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                TextView(
                    text = context.getString(R.string.user_name) + ": ${authSignIn!!.Name}",
                    modifier = Modifier,
                    textSize = 12,
                    font = Font(R.font.poppins_regular)
                )
                SpaceH(h = 3)
                TextView(
                    text = context.getString(R.string.tour_name) + ": " + viewModels.bookingTourNow.value?.tourName,
                    modifier = Modifier,
                    textSize = 12,
                    font = Font(R.font.poppins_regular)
                )
                SpaceH(h = 3)
                TextView(
                    text = context.getString(R.string.tour_time) + ": "
                            + context.getString(R.string.from) + value.startTime + ": " + context.getString(
                        R.string.to
                    ) + value.endTime,
                    modifier = Modifier,
                    textSize = 12,
                    font = Font(R.font.poppins_regular)
                )
                SpaceH(h = 3)
                TextView(
                    text = context.getString(R.string.phone_number) + ": ${authSignIn!!.PhoneNumber}",
                    modifier = Modifier,
                    textSize = 12,
                    font = Font(R.font.poppins_regular)
                )
                SpaceH(h = 3)
                TextView(
                    text = context.getString(R.string.email) + ": ${authSignIn!!.Email}",
                    modifier = Modifier,
                    textSize = 12,
                    font = Font(R.font.poppins_regular)
                )
                SpaceH(h = 3)
                TextView(
                    text = context.getString(R.string.address) + ": ${authSignIn!!.Address}",
                    modifier = Modifier,
                    textSize = 12,
                    font = Font(R.font.poppins_regular)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(border = BorderStroke(width = 1.dp, color = black))
            ) {
                Column {
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .height(32.dp)
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .weight(2f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.number),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(4f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.content),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(2f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.count),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.price),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.total_price),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }


                    }
                    // header
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(black)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .height(32.dp)
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .weight(2f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = "1",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(4f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.adult),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(2f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = viewModels.countAdult.value.toString(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = salePrice.value.toCurrency(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = adultPrice.value.toCurrency(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }


                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(black)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .height(32.dp)
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .weight(2f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = "2",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(4f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.child),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(2f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = viewModels.countChild.value.toString(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = (salePrice.value * 0.75f).toCurrency(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = childPrice.value.toCurrency(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }


                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(black)
                    )

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    ) {
                        TextView(
                            text = context.getString(R.string.service_includes),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            textSize = 11,
                            font = Font(R.font.poppins_semibold)
                        )
                        SpaceH(h = 2)
                        TextView(
                            text = context.getString(R.string.bill_include),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            textSize = 11,
                            font = Font(R.font.poppins_regular)
                        )
                        SpaceH(h = 2)
                        TextView(
                            text = context.getString(R.string.bill_include_other),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            textSize = 11,
                            font = Font(R.font.poppins_regular)
                        )
                        SpaceH(h = 6)
                        TextView(
                            text = context.getString(R.string.not_incluted),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            textSize = 11,
                            font = Font(R.font.poppins_semibold)
                        )
                        SpaceH(h = 2)
                        TextView(
                            text = context.getString(R.string.bill_un_include),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            textSize = 11,
                            font = Font(R.font.poppins_regular)
                        )

                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(black)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .height(32.dp)
                            .padding(start = 6.dp)
                            .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            Modifier
                                .weight(11f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.total_price),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = (adultPrice.value + childPrice.value).toCurrency(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }


                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(black)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .height(32.dp)
                            .padding(start = 6.dp)
                            .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            Modifier
                                .weight(11f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.deposit),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = ((adultPrice.value + childPrice.value) * percentDeposit).toCurrency(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }


                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(black)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .height(32.dp)
                            .padding(start = 6.dp)
                            .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            Modifier
                                .weight(11f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = context.getString(R.string.pre_payment),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                textSize = 11,
                                font = Font(R.font.poppins_semibold)
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .width(1.dp)
                                .background(black)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier
                                .weight(3f)
                                .wrapContentHeight()
                        ) {
                            TextView(
                                text = ((childPrice.value + adultPrice.value) - ((adultPrice.value + childPrice.value) * percentDeposit)).toCurrency(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_regular)
                            )
                        }


                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Column(
                    Modifier
                        .wrapContentHeight()
                        .background(gray)
                        .weight(1.3f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextView(
                        text = context.getString(R.string.customer_name),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        textSize = 11,
                        font = Font(R.font.poppins_semibold)
                    )
                }
                Spacer(modifier = Modifier.weight(0.2f))
                Column(
                    Modifier.weight(1.3f), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextView(
                        text = context.getString(R.string.artist),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        textSize = 11,
                        font = Font(R.font.poppins_semibold)
                    )
                }
                Spacer(modifier = Modifier.weight(0.2f))
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), horizontalArrangement = Arrangement.Absolute.Center
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Column(
                    Modifier.weight(1.3f), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextView(
                        text = authSignIn!!.Name,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        textSize = 11,
                        font = Font(R.font.poppins_regular)
                    )
                }
                Spacer(modifier = Modifier.weight(0.2f))
                Column(
                    Modifier.weight(1.3f), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextView(
                        text = context.getString(R.string.artist),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        textSize = 11,
                        font = Font(R.font.poppins_semibold)
                    )
                }
                Spacer(modifier = Modifier.weight(0.2f))
            }

        }
    }
}