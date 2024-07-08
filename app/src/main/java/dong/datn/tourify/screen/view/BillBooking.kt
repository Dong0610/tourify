package dong.datn.tourify.screen.view


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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dong.datn.tourify.R
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.timeNow
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParentContent
import dong.duan.travelapp.model.TourTime

@Composable
fun BillBooking(navController: NavHostController) {

    viewModels.currentTourTime.value = TourTime("tour", "TEST", timeNow(), timeNow(), -1)
    if (viewModels.listTour.value.size != 0) {
        viewModels.bookingTourNow.value = viewModels.listTour.value.get(0)
    }

    val context = LocalContext.current
    ViewParentContent(
        onBack = {}, modifier = Modifier
            .background(white)
            .padding(2.dp)
            .border(border = BorderStroke(width = 1.dp, color = black))
    ) {
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.Top
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
                            + context.getString(R.string.from) + viewModels.currentTourTime.value?.startTime + ": " + context.getString(
                        R.string.to
                    ) + viewModels.currentTourTime.value?.endTime,
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

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .wrapContentHeight()
                    .border(border = BorderStroke(width = 1.dp, color = black))
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(1f)
                        .height(32.dp)
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .weight(2f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(4f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(2f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(3f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(3f)
                            .wrapContentHeight()) {
                        TextView(
                            text = context.getString(R.string.total_price),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 11,
                            font = Font(R.font.poppins_semibold)
                        )
                    }


                }
                Row(
                    Modifier
                        .fillMaxWidth(1f)
                        .height(32.dp)
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .weight(2f)
                            .wrapContentHeight()) {
                        TextView(
                            text = "1",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 11,
                            font = Font(R.font.poppins_semibold)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(4f)
                            .wrapContentHeight()) {
                        TextView(
                            text = context.getString(R.string.adult),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 11,
                            font = Font(R.font.poppins_semibold)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(2f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(3f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(3f)
                            .wrapContentHeight()) {
                        TextView(
                            text = context.getString(R.string.total_price),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 11,
                            font = Font(R.font.poppins_semibold)
                        )
                    }


                }
                Row(
                    Modifier
                        .fillMaxWidth(1f)
                        .height(32.dp)
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .weight(2f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(4f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(2f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(3f)
                            .wrapContentHeight()) {
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
                            .background(black).fillMaxHeight()
                    )
                    Box(
                        Modifier
                            .weight(3f)
                            .wrapContentHeight()) {
                        TextView(
                            text = context.getString(R.string.total_price),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 11,
                            font = Font(R.font.poppins_semibold)
                        )
                    }


                }
            }

        }
    }
}