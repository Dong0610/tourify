package dong.datn.tourify.screen.client

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.database
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Places
import dong.datn.tourify.model.Vehicle
import dong.datn.tourify.model.getNewVehicle
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.CommonImage
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.DotIndicator
import dong.datn.tourify.widget.InnerImageIcon
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeClientScreen(nav: NavController, viewModel: AppViewModel, location: String? = null) {
    val context = LocalContext.current
    
    val listPlaces = remember {
        mutableStateOf<MutableList<Places>?>(null)
    }
    var pagerState: PagerState? = null
    val coroutineScope = rememberCoroutineScope()
    if (viewModel.listSale.value.size != 0) {
        pagerState = rememberPagerState(
            pageCount = viewModel.listSale.value.size,
            initialOffscreenLimit = 2,
            infiniteLoop = true,
            initialPage = 0,
        )
        LaunchedEffect(Unit) {
            while (true) {
                delay(3000) // Delay for 3 seconds
                coroutineScope.launch {
                    val nextPage = (pagerState.currentPage + 1) % viewModel.listSale.value.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }
    val listPlaceItem = remember {
        mutableStateOf(mutableListOf<Tour>())
    }
    if (viewModel.listTour.value.size > 0) {
        listPlaceItem.value = viewModel.listTour.value
    }


    val indexSlideImage = remember {
        mutableStateOf(1)
    }
    viewModel.getAllPlaces {
        listPlaces.value = it
    }


    ViewParent {
        Column {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommonImage(
                    data = R.drawable.img_user,
                    Modifier
                        .size(42.dp)
                        .background(shape = CircleShape, color = transparent)
                )

                Spacer(modifier = Modifier.width(6.dp))
                if (authSignIn != null) {
                    Column {
                        TextView(
                            context.getString(R.string.wellcome_back), Modifier, textSize = 13,
                            textColor(context), font = Font(R.font.poppins_regular)
                        )
                        TextView(
                            authSignIn!!.Name, Modifier.widthPercent(50f), textSize = 16,

                            textColor(context), font = Font(R.font.poppins_medium)
                        )
                    }
                } else {
                    TextView(
                        context.getString(R.string.app_name), Modifier, textSize = 18,
                        appColor, font = Font(R.font.poppins_semibold)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = "Location",
                    tint = if (currentTheme == 1) appColor else lightGrey
                )
                TextView(
                    location ?: "", Modifier, textSize = 14,
                    darkGray, font = Font(R.font.poppins_medium)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )



            VerScrollView {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    if (viewModel.listSale.value.size > 0) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {

                            HorizontalPager(
                                modifier = Modifier,
                                state = pagerState as PagerState
                            ) { page ->
                                RoundedImage(
                                    viewModel.listSale.value[page].saleImage,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.heightPercent(28f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                indexSlideImage.value = page
                            }
                        }
                        Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                            DotIndicator(pagerState!!)
                        }

                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ItemCategory(
                            getNewVehicle(context).get(0), Modifier.weight(0.3f)
                        ) { id ->
                            val listItem = viewModel.listTour.value
                            val newSearch = mutableListOf<Tour>()
                            listItem.forEach {
                                if (it.vehicleId == id.vhId) {
                                    newSearch.add(it)
                                }
                            }
                            listPlaceItem.value = newSearch
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        ItemCategory(getNewVehicle(context).get(1), Modifier.weight(0.3f)) { id ->
                            val listItem = viewModel.listTour.value
                            val newSearch = mutableListOf<Tour>()
                            listItem.forEach {
                                if (it.vehicleId == id.vhId) {
                                    newSearch.add(it)
                                }
                            }
                            listPlaceItem.value = newSearch
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        ItemCategory(getNewVehicle(context).get(2), Modifier.weight(0.3f)) { id ->
                            val listItem = viewModel.listTour.value
                            val newSearch = mutableListOf<Tour>()
                            listItem.forEach {
                                if (it.vehicleId == id.vhId) {
                                    newSearch.add(it)
                                }
                            }
                            listPlaceItem.value = newSearch
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 16.dp)
                            .heightPercent(30f)
                    ) {
                        LazyRow {
                            items(listPlaceItem.value, key = {
                                it.tourID
                            }) {
                                ItemTopBucketTour(it) {
                                    viewModel.detailTour.value = it
                                    nav.navigationTo(ClientScreen.DetailTourScreen.route)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    TextView(
                        text = context.getString(R.string.top_5_place),
                        modifier = Modifier.padding(start = 16.dp, bottom = 6.dp),
                        font = Font(R.font.poppins_semibold), textSize = 18
                    )

                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 16.dp)
                            .heightPercent(20f)
                    ) {
                        LazyRow(state = rememberLazyListState()) {
                            if (listPlaces.value != null) {
                                items(listPlaces.value!!) {
                                    ItemTopPlace(
                                        it,
                                        Modifier
                                            .widthPercent(43f)
                                            .heightPercent(18f)
                                    ) { plc ->
                                        nav.navigationTo(ClientScreen.DetailPlaceScreen.route)
                                        viewModels.detailPlace.value = plc
                                    }
                                }
                            }

                        }
                    }

                    TextView(
                        text = context.getString(R.string.top_10_booked),
                        modifier = Modifier.padding(start = 16.dp, bottom = 6.dp),
                        font = Font(R.font.poppins_semibold), textSize = 18
                    )
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 16.dp)
                            .heightPercent(30f)
                    ) {
                        LazyRow {
                            items(viewModel.listTour.value, key = {
                                it.tourID
                            }) {
                                ItemTopBucketTour(it) {

                                }
                            }
                        }
                    }

                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
            }

        }
    }
}

@Composable
fun ItemTopPlace(items: Places, modifier: Modifier, onSelect: (Places) -> Unit) {
    Box(
        modifier = modifier.border(
            width = 1.dp,
            color = lightGrey,
            shape = RoundedCornerShape(12.dp)
        )
    ) {
        RoundedImage(items.Image?.get(0), Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Column {
            Spacer(modifier = Modifier.weight(1f))
            TextView(
                text = items.placeName,
                modifier = Modifier
                    .padding(start = 12.dp),
                textSize = 16,
                font = Font(R.font.poppins_medium), color = white
            )
            TextView(
                text = items.listTour.size.toString() + " tour",
                modifier = Modifier
                    .padding(start = 12.dp),
                textSize = 14,
                font = Font(R.font.poppins_regular), color = white
            )

        }
        Box(modifier = Modifier
            .fillMaxSize()
            .onClick { onSelect.invoke(items) })
    }
    Spacer(modifier = Modifier.width(12.dp))

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemTopBucketTour(tour: Tour, onTouch: (Tour) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val salePrice = remember {
        mutableStateOf(0.0)
    }
    val loveState = remember {
        mutableStateOf(false)
    }
    coroutineScope.launch {
        loveState.value= database.loveDao().doesItemExist(tour.tourID)
        salePrice.value = salePriceByTour(tour)
    }
    val placesName = remember {
        mutableStateOf("")
    }

    Firestore.fetchById<Places>("PLACES/${tour.tourAddress}") {
        if (it != null) {
            placesName.value = it.placeName
        }
    }
    Box(
        Modifier
            .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(12.dp))
            .widthPercent(43f)
            .fillMaxHeight(1f)
            .background(if (currentTheme == -1) iconBackground else white)
            .clip(RoundedCornerShape(4.dp)), contentAlignment = Alignment.TopEnd
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            Row(Modifier.heightPercent(15f)) {
                RoundedImage(
                    tour.tourImage.get(0),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),

                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            TextView(
                text = tour.tourName,
                modifier = Modifier,
                maxLine = 1,
                font = Font(R.font.poppins_medium)
            )
            Spacer(modifier = Modifier.height(2.dp))

                TextView(
                    text = salePrice.value.toCurrency(),
                    modifier = Modifier,
                    font = Font(R.font.poppins_medium),
                    color = if (currentTheme == 1) Color.Red else white
                )
            Spacer(modifier = Modifier.width(3.dp))
                TextView(text = tour.tourPrice.toCurrency(), modifier = Modifier.drawBehind {
                    val strokeWidthPx = 1.dp.toPx()
                    val verticalOffset = size.height / 2
                    drawLine(
                        color = gray,
                        strokeWidth = strokeWidthPx,
                        start = Offset(0f, verticalOffset),
                        end = Offset(size.width, verticalOffset)
                    )
                }, font = Font(R.font.poppins_medium), color = gray)

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
                    text = placesName.value,
                    modifier = Modifier,
                    font = Font(R.font.poppins_medium),
                    color = lightGrey
                )
            }

        }
        Box(
            Modifier
                .matchParentSize()
                .onClick {
                    onTouch.invoke(tour)
                })
        InnerImageIcon(
            modifier = Modifier
                .size(32.dp),
            icon = Icons.Rounded.Favorite,
            icSize = 20,
            tint = if (loveState.value) red else black
        ){
            viewModels.onModifyLove(tour) {
                loveState.value = it
            }
        }


    }
    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
fun ItemCategory(vehicle: Vehicle, modifier: Modifier, onSelect: (Vehicle) -> Unit) {
    Box(
        modifier
            .background(color = appColor, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 6.dp)
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 6.dp),
                painter = painterResource(id = vehicle.image as Int),
                contentDescription = "Plane", tint = white
            )
            TextView(
                text = vehicle.vhName,
                modifier = Modifier,
                textSize = 16, color = white, maxLine = 1
            )
        }
        Box(modifier = Modifier
            .matchParentSize()
            .onClick { onSelect.invoke(vehicle) })
    }

}

