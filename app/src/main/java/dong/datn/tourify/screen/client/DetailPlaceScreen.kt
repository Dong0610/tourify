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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.LocationOn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.gold
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.CommonDivider
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.DotIndicator
import dong.datn.tourify.widget.InnerImageIcon
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.travelapp.model.Tour

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DetailPlaceScreen(nav: NavController, viewModel: AppViewModel, route: String) {
   
    val context = LocalContext.current
    val place = viewModel.detailPlace.value
    val pagerState = rememberPagerState(
        pageCount = place?.Image!!.size,
        initialOffscreenLimit = 2,
        infiniteLoop = true,
        initialPage = 0,
    )

    ViewParent(onBack = {
        nav.navigationTo(route, ClientScreen.DetailPlaceScreen.route)

    }) {

        Box(
            Modifier
                .fillMaxWidth(1f)

        ) {
            VerScrollView {
                Column(Modifier.fillMaxSize(1f)) {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightPercent(36f),
                        state = pagerState
                    ) { index ->
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RoundedImage(
                                place.Image!!.get(index),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier,
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }


                    Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                        DotIndicator(pagerState)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    TextView(
                        place.placeName,
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        textSize = 20,
                        color = if (currentTheme == 1) appColor else white,
                        font = Font(R.font.poppins_semibold),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextView(
                        text = place.description,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = textColor(context),
                        font = Font(R.font.poppins_regular)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TextView(
                        text = context.getString(R.string.all_tour),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = textColor(context),
                        font = Font(R.font.poppins_semibold)
                    )

                    VerScrollView(
                        Modifier
                            .fillMaxWidth()
                            .heightPercent(100f)
                            .padding(horizontal = 16.dp)
                    ) {
                        Column {
                            place.listTour.forEach { data ->
                                TourByPlace(data) {
                                    nav.navigationTo(
                                        ClientScreen.DetailTourScreen.route,
                                        ClientScreen.DetailPlaceScreen.route
                                    )
                                    viewModel.detailTour.value = it
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InnerImageIcon(
                    modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft
                ) {
                    nav.navigationTo(route)
                }

                Spacer(modifier = Modifier.width(50.dp))
            }
        }


    }
}

@Composable
fun TourByPlace(data: String, onSelect: (Tour) -> Unit) {
    val tour = remember {
        mutableStateOf<Tour?>(null)
    }
    Firestore.fetchById<Tour>("$TOUR/$data/") {
        tour.value = it
    }
    val context= LocalContext.current
    if (tour.value != null) {
        Box(
            Modifier
                .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .heightPercent(18f)
                .background(if (currentTheme == -1) iconBackground else white)
                .clip(RoundedCornerShape(4.dp))

        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            ) {
                Row(
                    Modifier
                        .heightPercent(20f)
                        .widthPercent(40f)
                ) {
                    RoundedImage(
                        tour.value!!.tourImage?.get(0),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {


                    TextView(
                        text = tour.value!!.tourName ?: context.getString(R.string.error),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium)
                    )
                    Spacer(modifier = Modifier.height(2.dp))

                    TextView(
                        text = tour.value!!.salePrice.toString(),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium),
                        color = if (currentTheme == 1) Color.Red else white
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TextView(
                        text = tour.value!!.tourPrice.toString(),
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
                        color = gray
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "Star",
                            tint = if (currentTheme == 1) gold else white
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        TextView(
                            text = tour.value!!.star.toString() ?: "",
                            modifier = Modifier,
                            textSize = 18,
                            color = gray,
                            font = Font(R.font.poppins_medium)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Row(Modifier.fillMaxWidth(1f)) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Location",
                            tint = if (currentTheme == 1) darkGray else white
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextView(
                            text = "100",
                            modifier = Modifier,
                            font = Font(R.font.poppins_medium),
                            color = lightGrey
                        )
                    }
                }

            }
            Column(Modifier.fillMaxSize(1f)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .onClick {

                        }, horizontalArrangement = Arrangement.End
                ) {
                    InnerImageIcon(
                        modifier = Modifier.size(32.dp), icon = Icons.Rounded.Favorite, icSize = 20
                    )
                }
                Row(Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .onClick {
                        onSelect.invoke(tour.value!!)
                    }
                    .padding(6.dp)) {}

            }


        }
        Spacer(modifier = Modifier.width(12.dp))
    }
}
