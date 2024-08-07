package dong.datn.tourify.screen.client

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.percentDeposit
import dong.datn.tourify.firebase.RealTime
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.colorByTheme
import dong.datn.tourify.ui.theme.gold
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.limeGreen
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.CommonDivider
import dong.datn.tourify.utils.SERVICE
import dong.datn.tourify.utils.Space
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.utility.toJson
import dong.duan.livechat.widget.InputValue
import dong.duan.travelapp.model.Tour
import dong.duan.travelapp.model.TourTime
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun BookingNowScreen(nav: NavController, viewModels: AppViewModel) {
    val context = LocalContext.current



    val tour = remember {
        mutableStateOf(viewModels.bookingTourNow.value)
    }

    val isDisable = remember {
        mutableStateOf(true)
    }
    val isShowDialog = remember {
        mutableStateOf(false)
    }
    val tourTimeService = remember {
        mutableStateOf<String>("")
    }


    val maxSelectedCount = remember {
        mutableStateOf(0)
    }

    val coroutineScope = rememberCoroutineScope()

    if (tour.value != null) {
        RealTime.fetchById<String>("$SERVICE/${tour.value?.tourID}/time") {
            tourTimeService.value = it.toString()
        }
        coroutineScope.launch {
           viewModels.salePrice.value = salePriceByTour(tour.value!!)
        }
        viewModels.tourTimeSelected.value = tour.value!!.tourTime.get(0)

    }



    ViewParent(onBack = {
        nav.navigationTo(ClientScreen.DetailTourScreen.route)
    }) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    nav.navigationTo(ClientScreen.DetailTourScreen.route)
                }

                TextView(
                    context.getString(R.string.booking_now),
                    Modifier.weight(1f),
                    textSize = 20,
                    appColor,
                    font = Font(R.font.poppins_semibold),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(50.dp))
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )
            VerScrollView(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    Modifier
                        .fillMaxSize(1f)
                        .imePadding()
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth()) {
                        RoundedImage(
                            data = tour.value?.tourImage!!.get(0),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .widthPercent(40f)
                                .height(100.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            TextView(
                                text = tour.value?.tourName.toString(),
                                modifier = Modifier,
                                font = Font(R.font.poppins_semibold),
                                color = textColor(context)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            TextView(
                                text = tourTimeService.value,
                                modifier = Modifier,
                                font = Font(R.font.poppins_regular),
                                color = textColor(context)
                            )
                            Row {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = "Star",
                                    tint = if (currentTheme == 1) gold else white
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                TextView(
                                    text = tour.value?.star.toString(),
                                    modifier = Modifier,
                                    textSize = 18,
                                    color = gray,
                                    font = Font(R.font.poppins_medium)
                                )
                            }
                            TextView(
                                text =viewModels. salePrice.value.toCurrency(),
                                modifier = Modifier,
                                textSize = 18,
                                color = if (currentTheme == 1) red else white,
                                font = Font(R.font.poppins_medium)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    CommonDivider(Modifier.padding(horizontal = 24.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    TextView(
                        text = context.getString(R.string.choose_time_and_count) +" "+(maxSelectedCount.value),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium),
                        textSize = 18,
                        color = if (currentTheme == 1) appColor else white
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    DropdownSelectTime(
                        tour = tour.value!!, modifier = Modifier.fillMaxWidth()
                    ) {

                        viewModels.tourTimeSelected.value = it
                        showToast(it.count)
                        maxSelectedCount.value = it.count
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextView(
                        context.getString(R.string.above_10_age),
                        color = red,
                        textSize = 14,
                        font = Font(R.font.poppins_regular)
                    )
                    ArrowValue(
                        name = context.getString(R.string.adult),
                        price = viewModels.salePrice.value,
                        max = maxSelectedCount.value
                    ) { count, _ ->
                        viewModels.countAdult.value = count
                        viewModels.totalPrice.value =
                            (viewModels.salePrice.value * viewModels.countAdult.value) +
                                    (viewModels.salePrice.value * 0.5f * viewModels.countChild.value)
                        isDisable.value = viewModels.totalPrice.value == 0.0 || count == 0
                        maxSelectedCount.value = (viewModels.tourTimeSelected.value?.count ?: 0) - count
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    TextView(
                        context.getString(R.string.above_2_to_10_age),
                        color = red,
                        textSize = 14,
                        font = Font(R.font.poppins_regular)
                    )
                    ArrowValue(
                        name = context.getString(R.string.child),
                        price = viewModels.salePrice.value * 0.75,
                        max = maxSelectedCount.value
                    ) { count, _ ->
                        viewModels.countChild.value = count
                        viewModels.totalPrice.value =
                            (viewModels.salePrice.value * viewModels.countAdult.value) +
                                    (viewModels.salePrice.value * 0.5f * viewModels.countChild.value)
                        isDisable.value = viewModels.totalPrice.value == 0.0 || viewModels.countAdult.value == 0
                        maxSelectedCount.value = (viewModels.tourTimeSelected.value?.count ?: 0) - count
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    TextView(
                        context.getString(R.string.under_2_age),
                        color = red,
                        textSize = 14,
                        font = Font(R.font.poppins_regular)
                    )
                    ArrowValue(
                        name = context.getString(R.string.child),
                        price = 0.0,
                        max = 100,
                        false
                    ) { it, type ->
                        viewModels.countToddle.value = it
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .heightIn(50.dp, 400.dp)
                            .background(
                                color = transparent,
                            )
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(12.dp),
                                color = textColor(context)
                            ), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        InputValue(
                            value = viewModels.notes.value,
                            modifier = Modifier.fillMaxWidth(),
                            hint = context.getString(R.string.notes) + "...",
                            font = Font(R.font.poppins_regular),
                            teAlignment = TextAlign.Start,
                            maxLines = 100

                        ) {
                          viewModels.notes.value = it
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 18.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextView(
                    text = context.getString(R.string.total_price),
                    modifier = Modifier,
                    font = Font(R.font.poppins_semibold),
                )
                TextView(
                    text = viewModels.totalPrice.value.toCurrency(),
                    modifier = Modifier,
                    color = if (currentTheme == 1) red else white,
                    font = Font(R.font.poppins_semibold)
                )
            }
            Space(h = 12)
            AppButton(
                isEnable = !isDisable.value,
                text = context.getString(R.string.confirm),
                modifier = Modifier.padding(horizontal = 16.dp), loading = null
            ) {
                tour.value?.let {
                    isShowDialog.value = true
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
    if (isShowDialog.value) {
        CustomDialog(viewModels.totalPrice.value, {
            isShowDialog.value = false
        }, {
            isShowDialog.value = false
            nav.navigationTo(ClientScreen.InvoiceOderScreen.route)
        })
    }

}


@Composable
fun CustomDialog(totalPrice: Double, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(1f)
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    FaIcon(faIcon = FaIcons.CheckCircle, tint = limeGreen, size = 32.dp)
                    Space(w = 8)
                    TextView(
                        text = LocalContext.current.getString(R.string.confirm),
                        modifier = Modifier,
                        textSize = 18,
                        font = Font(R.font.poppins_semibold),
                        color = limeGreen
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextView(
                    text = LocalContext.current.getString(R.string.confirm_message) + " ${(totalPrice * percentDeposit).toCurrency()}",
                    modifier = Modifier,
                    textSize = 18,
                    maxLine = 100,
                    font = Font(R.font.poppins_regular)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(1f)) {
                    Box(
                        Modifier
                            .onClick {
                                onDismiss()
                            }
                            .height(40.dp)
                            .weight(1f)
                            .background(lightGrey, shape = RoundedCornerShape(40.dp)),
                        contentAlignment = Alignment.Center) {
                        Text(
                            LocalContext.current.getString(R.string.cancel),
                            Modifier,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.poppins_medium))
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        Modifier
                            .onClick {

                                onConfirm.invoke()
                            }
                            .height(40.dp)
                            .weight(1f)
                            .background(limeGreen, shape = RoundedCornerShape(40.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            LocalContext.current.getString(R.string.Ok),
                            Modifier,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.poppins_medium))
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun ArrowValue(
    name: String,
    price: Double,
    max: Int = 10,
    isCreatePrice: Boolean = true,
    onTouch: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val count = remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .height(50.dp)
            .background(color = transparent)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(12.dp),
                color = textColor(context)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Space(w = 12)
        Row {
            TextView(
                text = "$name: ",
                modifier = Modifier,
                textSize = 16,
                font = Font(R.font.poppins_semibold)
            )
            Spacer(modifier = Modifier.width(6.dp))
            TextView(
                text = if (isCreatePrice) (count.value * price).toCurrency("vn") else 0.toCurrency(),
                modifier = Modifier,
                color = colorByTheme(red, white),
                textSize = 16,
                font = Font(R.font.poppins_semibold)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_remove),
                tint = textColor(context),
                contentDescription = "Remove",
                modifier = Modifier.onClick {
                    count.value = (count.value - 1).coerceAtLeast(0)
                    onTouch(count.value, 1)
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            TextView(
                text = count.value.toString(),
                modifier = Modifier,
                color = textColor(context),
                font = Font(R.font.poppins_semibold),
                textSize = 16
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_round_add),
                contentDescription = "Add",
                tint = textColor(context),
                modifier = Modifier.onClick {
                    count.value = (count.value + 1).coerceAtMost(max)
                    onTouch(count.value, -1)
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Space(w = 12)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelectTime(
    tour: Tour, modifier: Modifier, calback: (
        TourTime
    ) -> Unit
) {
    val context = LocalContext.current

    val expanded = remember { mutableStateOf(false) }

    val selectedText = remember {
        mutableStateOf(
            tour.tourTime.get(0)
        )
    }

    Log.d("TourTimeData", "DropdownSelectTime: ${tour.tourTime.toJson()}")

    Box(
        modifier = modifier
            .height(50.dp)
            .background(
                color = transparent,
            )
            .border(width = 1.dp, shape = RoundedCornerShape(12.dp), color = textColor(context)),
        contentAlignment = Alignment.CenterStart
    ) {
        ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {
            expanded.value = !expanded.value
        }) {
            Row(
                Modifier
                    .menuAnchor()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    TextView(
                        text = context.getString(R.string.from) + " " + selectedText.value.startTime,
                        modifier = Modifier,
                        textSize = 14
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    TextView(
                        text = context.getString(R.string.to) + " " + selectedText.value.endTime + " ",
                        modifier = Modifier, textSize = 14
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.DateRange,
                    contentDescription = "Selected",
                    Modifier.size(32.dp)
                )
            }

            ExposedDropdownMenu(expanded = expanded.value,
                modifier = Modifier.padding(start = 6.dp),
                onDismissRequest = { expanded.value = false }) {
                tour.tourTime.forEach { item ->
                    DropdownMenuItem(text = {
                        TextView(
                            maxLine = 2,
                            onclick = {
                                expanded.value = false
                                selectedText.value = item
                                calback.invoke(item)
                            },
                            text = context.getString(R.string.from) + item.startTime + " " + context.getString(
                                R.string.to
                            ) + item.endTime,
                            modifier = Modifier
                        )
                    }, onClick = {
                        selectedText.value = item
                        expanded.value = false
                        calback.invoke(item)
                    })
                }
            }


        }
    }
}
