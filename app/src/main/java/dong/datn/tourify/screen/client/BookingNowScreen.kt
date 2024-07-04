package dong.datn.tourify.screen.client

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
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
import dong.datn.tourify.app.appViewModels
import dong.datn.tourify.app.currentTheme
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
import dong.duan.livechat.widget.InputValue
import dong.duan.travelapp.model.Tour

@Composable
fun BookingNowScreen(nav: NavController, viewModels: AppViewModel) {
    val context = LocalContext.current
    

    val tour = remember {
        mutableStateOf(appViewModels?.bookingTourNow?.value)
    }
    val notes = remember {
        mutableStateOf<String>("")
    }
    val isDisable = remember {
        mutableStateOf(true)
    }
    val isShowDialog = remember {
        mutableStateOf(false)
    }

    val countChild = remember {
        mutableStateOf(0)
    }
    val countAdutl = remember {
        mutableStateOf(0)
    }

    val tourTimeService = remember {
        mutableStateOf<String>("")
    }
    val totalPrice = remember {
        mutableStateOf(0.0)
    }
    if (tour.value != null) {
        RealTime.fetchById<String>("$SERVICE/${tour.value?.tourID}/time") {
            tourTimeService.value = it.toString()
        }
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
                                text = tourTimeService.value.toString(),
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
                                    text = tour.value?.star.toString() ?: "",
                                    modifier = Modifier,
                                    textSize = 18,
                                    color = gray,
                                    font = Font(R.font.poppins_medium)
                                )
                            }
                            TextView(
                                text = tour.value?.salePrice?.toCurrency() ?: "",
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
                        text = context.getString(R.string.choose_time_and_count),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium),
                        textSize = 18,
                        color = if (currentTheme == 1) appColor else white
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    DropdownSelectTime(
                        tour = tour.value!!, modifier = Modifier.fillMaxWidth()
                    ) {

                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    ArrowValue(
                        name = context.getString(R.string.adult),
                        price = tour.value!!.salePrice!!
                    ) {
                        countAdutl.value = it
                        totalPrice.value =
                            (tour.value!!.salePrice!! * countAdutl.value) + (tour.value!!.salePrice!! * 0.75f * countChild.value)
                        if (totalPrice.value == 0.0 || it == 0) {
                            isDisable.value = true
                        } else {
                            isDisable.value = false
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    ArrowValue(
                        name = context.getString(R.string.child),
                        price = tour.value!!.salePrice!! * 0.75
                    ) {
                        countChild.value = it
                        totalPrice.value =
                            (tour.value!!.salePrice!! * countAdutl.value) + (tour.value!!.salePrice!! * 0.75f * countChild.value)
                        if (totalPrice.value == 0.0 || countAdutl.value == 0) {
                            isDisable.value = true
                        } else {
                            isDisable.value = false
                        }
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
                            ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        InputValue(
                            value = notes.value,
                            modifier = Modifier.fillMaxWidth(),
                            hint = context.getString(R.string.notes) + "...",
                            maxLines = 100
                        ) {
                            notes.value = it
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextView(
                    text = context.getString(R.string.total_price),
                    modifier = Modifier,
                    font = Font(R.font.poppins_semibold),
                )
                TextView(
                    text = totalPrice.value.toCurrency(),
                    modifier = Modifier,
                    color = if (currentTheme == 1) red else white,
                    font = Font(R.font.poppins_semibold)
                )
            }
            Space(h = 6)
            AppButton(
                isEnable = !isDisable.value,
                text = context.getString(R.string.confirm),
                modifier = Modifier.padding(horizontal = 16.dp), loading = null
            ) {
                isShowDialog.value = true
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
    if (isShowDialog.value) {
        CustomDialog(totalPrice.value) {
            isShowDialog.value = false

        }
    }

}


@Composable
fun CustomDialog(totalPrice: Double, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
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
                Spacer(modifier = Modifier.height(8.dp))
                TextView(
                    text = LocalContext.current.getString(R.string.confirm_message) + " ${totalPrice * 0.4}",
                    modifier = Modifier,
                    textSize = 18,
                    font = Font(R.font.poppins_regular)
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                                onDismiss()
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
fun ArrowValue(name: String, price: Double, max: Int = 10, onTouch: (Int) -> Unit) {
    val context = LocalContext.current
    val count = remember {
        mutableStateOf(0)
    }
    Row(
        modifier = Modifier
            .height(50.dp)
            .background(
                color = transparent,
            )
            .fillMaxWidth()
            .border(
                width = 1.dp, shape = RoundedCornerShape(12.dp), color = textColor(context)
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        Space(w = 12)
        Row(Modifier) {
            TextView(
                text = name + ": ",
                modifier = Modifier,
                textSize = 16,
                font = Font(R.font.poppins_semibold)
            )
            Spacer(modifier = Modifier.width(6.dp))
            TextView(
                text = (count.value * price).toCurrency("vn"),
                modifier = Modifier,
                color = colorByTheme(
                    red, white
                ),
                textSize = 16,
                font = Font(R.font.poppins_semibold)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(Modifier.widthPercent(30f), horizontalArrangement = Arrangement.SpaceBetween) {
            FaIcon(faIcon = FaIcons.Minus,
                tint = textColor(context = context),
                modifier = Modifier.onClick {
                    count.value--;
                    if (count.value <= 0) {
                        count.value = 0;
                    }
                    onTouch(count.value)
                })
            Spacer(modifier = Modifier.width(12.dp))
            TextView(
                text = count.value.toString(),
                modifier = Modifier,
                color = textColor(context),
                font = Font(R.font.poppins_semibold),
                textSize = 16
            )
            Spacer(modifier = Modifier.width(12.dp))
            FaIcon(faIcon = FaIcons.Plus,
                tint = textColor(context = context),
                modifier = Modifier.onClick {
                    count.value++;
                    if (count.value > max) {
                        count.value = max;
                    }
                    onTouch(count.value)
                })
            Spacer(modifier = Modifier.width(6.dp))
        }
        Space(w = 12)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelectTime(
    tour: Tour, modifier: Modifier, calback: (
        Tour

    ) -> Unit
) {
    val context = LocalContext.current

    val expanded = remember { mutableStateOf(false) }

    val selectedText = remember {
        mutableStateOf(
            tour.tourTime?.get(0)!!
        )
    }

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
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    TextView(
                        text = context.getString(R.string.to) + " " + selectedText.value.startTime,
                        modifier = Modifier
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
                tour.tourTime!!.forEach { item ->
                    DropdownMenuItem(text = {
                        TextView(
                            onclick = {
                                expanded.value = false
                                selectedText.value = item
                                calback.invoke(tour)
                            },
                            text = context.getString(R.string.from) + item.startTime + " " + context.getString(
                                R.string.to
                            ) + item.endTime,
                            modifier = Modifier
                        )
                    }, onClick = {
                        selectedText.value = item
                        expanded.value = false
                        calback.invoke(tour)
                    })
                }
            }


        }
    }
}
