package dong.datn.tourify.screen.staff

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Sale
import dong.datn.tourify.model.SaleType
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.blueViolet
import dong.datn.tourify.ui.theme.ghostWhite
import dong.datn.tourify.ui.theme.gold
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.HorScrollView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParentContent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.travelapp.model.Tour

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ModifySaleScreen(nav: NavController, boxScop: () -> Unit) {
    val currentSale = remember {
        mutableStateOf(viewModels.currentSale.value)
    }
    val stateBottomSheet = remember { mutableStateOf(false) }

    val context = LocalContext.current
    ViewParentContent(onBack = {
        if (viewModels.prevScreen.value != "") {
            nav.navigationTo(viewModels.prevScreen.value)
        } else {
            nav.popBackStack()
        }
    }) {

        Column(
            Modifier
                .fillMaxWidth()
        ) {
            HorScrollView(
                Modifier
                    .fillMaxWidth()
                    .background(blueViolet)
                    .heightPercent(22f)
            ) {

                Row(
                    Modifier
                        .fillMaxHeight()
                        .wrapContentWidth()
                        .background(ghostWhite), verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentSale.value != null) {
                        RoundedImage(
                            currentSale.value!!.saleImage,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(4.dp)
                                .border(
                                    width = 1.dp,
                                    color = appColor,
                                    RoundedCornerShape(8.dp)
                                )
                                .fillMaxHeight()
                                .width(220.dp)
                        )
                    }
                    SpaceW(w = 8)
                    Box(
                        Modifier
                            .padding(4.dp)
                            .border(width = 1.dp, color = appColor, RoundedCornerShape(8.dp))
                            .widthPercent(60f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            TextView(
                                text = currentSale.value?.saleName ?: "",
                                modifier = Modifier,
                                color = textColor(),
                                font = Font(
                                    R.font.poppins_medium
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextView(
                                text = context.getString(R.string.status) + ": ${
                                    getSaleTypeString(
                                        context,
                                        currentSale.value?.status ?: SaleType.RUNNING
                                    )
                                }",
                                modifier = Modifier,
                                textSize = 14,
                                font = Font(R.font.poppins_regular),
                                color = if (currentTheme == 1) Color.Red else white
                            )


                            SpaceH(h = 6)
                            TextView(
                                text = "${currentSale.value?.percent ?: 0f}%",
                                modifier = Modifier,
                                textSize = 14,
                                font = Font(R.font.poppins_regular),
                                color = if (currentTheme == 1) Color.Red else white
                            )
                            SpaceH(h = 6)

                            TextView(
                                text = "${context.getString(R.string.from)} ${currentSale.value?.startDate ?: ""}",
                                modifier = Modifier,
                                textSize = 13,
                                font = Font(R.font.poppins_regular),
                                color = textColor()
                            )
                            SpaceH(h = 3)
                            TextView(
                                text = "${context.getString(R.string.to)} ${currentSale.value!!.endDate}",
                                modifier = Modifier,
                                textSize = 13,
                                font = Font(R.font.poppins_regular),
                                color = textColor()
                            )

                        }
                    }

                    SpaceW(w = 8)
                    Box(
                        Modifier
                            .padding(4.dp)
                            .border(width = 1.dp, color = appColor, RoundedCornerShape(8.dp))
                            .widthPercent(80f)
                            .fillMaxHeight()
                    ) {
                        VerScrollView(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            TextView(
                                text = currentSale.value?.description ?: "",
                                modifier = Modifier,
                                color = textColor(),
                                maxLine = 1000,
                                textSize = 14,
                                font = Font(
                                    R.font.poppins_regular
                                )
                            )
                        }
                    }

                }
            }

            SpaceH(h = 8)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextView(
                    text = context.getString(R.string.tour_running),
                    modifier = Modifier,
                    font = Font(R.font.poppins_semibold),
                    color = appColor
                )
            }


            VerScrollView(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    viewModels.listTourStaff.value.toMutableList().forEachIndexed { index, tour ->
                        TourSaleItemManager(tour, currentSale.value?.percent ?: 0f,currentSale.value!!)
                        SpaceH(h = 8)
                    }

                }
            }
            SpaceH(h = 16)
        }
    }
}



@Composable
fun TourSaleItemManager(tour: Tour, discount: Float, value: Sale) {

    val saleTourPrice = remember {
        mutableStateOf((tour.tourPrice * (100f - discount) / 100))
    }
    val stateSelector = remember {
        mutableStateOf(tour.saleId==value.saleId)
    }
    val colorBoxSelect: Color = if (stateSelector.value) {
        appColor
    } else {
        lightGrey.opacity(0.5f)
    }
    val colorIconSelect: Color = if (stateSelector.value) {
        white
    } else {
        black
    }

    Row(
        Modifier
            .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .background(if (currentTheme == -1) iconBackground else white)
            .clip(RoundedCornerShape(4.dp)), verticalAlignment = Alignment.CenterVertically

    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightPercent(12f)
                    .padding(6.dp)

            ) {
                Row(
                    Modifier
                        .heightPercent(10f)
                        .widthPercent(25f)
                ) {
                    RoundedImage(
                        tour.tourImage.get(0),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = RoundedCornerShape(6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    TextView(
                        text = tour.tourName,
                        modifier = Modifier,
                        textSize = 14,
                        font = Font(R.font.poppins_medium)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        TextView(
                            text = saleTourPrice.value.toCurrency(),
                            modifier = Modifier,
                            textSize = 13,
                            font = Font(R.font.poppins_medium),
                            color = if (currentTheme == 1) Color.Red else white
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextView(
                            text = tour.tourPrice.toCurrency(),
                            modifier = Modifier.drawBehind {
                                val strokeWidthPx = 1.dp.toPx()
                                val verticalOffset = size.height / 2
                                drawLine(
                                    color = gray,
                                    strokeWidth = strokeWidthPx,
                                    start = Offset(0f, verticalOffset),
                                    end = Offset(size.width, verticalOffset)
                                )
                            },
                            font = Font(R.font.poppins_medium),
                            color = gray, textSize = 13

                        )
                    }
                    SpaceH(h = 6)
                    Row(Modifier.fillMaxWidth()) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Star",
                                modifier = Modifier.size(18.dp),
                                tint = if (currentTheme == 1) gold else white
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            TextView(
                                text = String.format("%.1f", tour.star),
                                modifier = Modifier,
                                textSize = 13,
                                color = gray,
                                font = Font(R.font.poppins_medium)
                            )
                        }
                    }
                }

            }
        }

        Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .wrapContentSize()
                    .size(32.dp)
                    .background(colorBoxSelect, shape = RoundedCornerShape(6.dp))
                    .onClick {
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Rounded.Check,
                    tint = colorIconSelect,
                    contentDescription = ""
                )
            }
            Box(Modifier
                .matchParentSize()
                .fillMaxWidth()
                .onClick {
                    stateSelector.value = !stateSelector.value
                    Firestore.updateAsync("$TOUR/${tour.tourID}", hashMapOf<String, Any>().apply {
                        put("saleId", if (stateSelector.value == true) value.saleId else "")
                    })
                }
                .padding(6.dp)) {}
        }
    }
}
