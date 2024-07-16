package dong.datn.tourify.screen.client

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.ContextProvider.Companion.viewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Order
import dong.datn.tourify.model.OrderStatus
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.getTimeBeforeHours
import dong.datn.tourify.ui.theme.gold
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.unselectedColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.ORDER
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingScreen(nav: NavController, viewModels: AppViewModel) {
    val context = LocalContext.current


    LaunchedEffect(key1 = "GetAllTours") {
        dong.datn.tourify.app.viewModels.firestore.collection("$ORDER")
            .get()
            .addOnSuccessListener {
                val orderList = it.toObjects(Order::class.java)
                viewModel.listOrders.value =
                    orderList.filter { it.userOrderId == authSignIn!!.UId }.toMutableList()
            }
    }

    ViewParent(onBack = {
        nav.navigate(ClientScreen.ProfileScreen.route) {
            nav.graph.startDestinationRoute?.let { route ->
                popUpTo(route) { saveState = true }
            }
            launchSingleTop = true
            restoreState = true
        }
    }) {
        Column(
            Modifier
                .fillMaxSize()

        ) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    nav.navigate(ClientScreen.ProfileScreen.route) {
                        nav.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                TextView(
                    context.getString(R.string.booking), Modifier.weight(1f), textSize = 20,
                    appColor, font = Font(R.font.poppins_semibold), textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(50.dp))
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )
            ViewItem(viewModels.listOrders.value, nav)
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewItem(value: MutableList<Order>, nav: NavController) {
    val context = LocalContext.current
    val tabData = listOf(
        context.getString(R.string.all),
        context.getString(R.string.upcoming),
        context.getString(R.string.finish), context.getString(R.string.cancel)
    )
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffscreenLimit = 2,
        infiniteLoop = true,
        initialPage = 0,
    )
    val tabIndex = remember { mutableStateOf(0) }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { tabIndex.value = it }
    }
    CustomTabRow(
        tabData = tabData,
        tabIndex = tabIndex.value,
        onItemClick = { index ->
            tabIndex.value = index
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }
        },
        selectedColor = appColor,
        unselectedColor = unselectedColor,
        modifier = Modifier
            .fillMaxWidth()
    )

    HorizontalPager(
        state = pagerState
    ) { index ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (index == 0) {
                LoadAllData(value, nav)
            } else if (index == 1) {
                LoadUpComingData(value.filter { it -> (it.orderStatus != OrderStatus.CANCELED && it.orderStatus != OrderStatus.FINISH) }
                    .toMutableList(), nav)
            } else if (index == 2) {
                LoadFinishData(value.filter { it -> it.orderStatus == OrderStatus.FINISH }
                    .toMutableList(), nav)
            } else {
                LoadCancelData(value.filter { it -> it.orderStatus == OrderStatus.CANCELED }
                    .toMutableList(), nav)
            }
        }
    }
}

@Composable
fun LoadCancelData(value: MutableList<Order>, nav: NavController) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(value) {
                OrderUpComingData(
                    it,
                    { url ->
                        nav.navigationTo(
                            ClientScreen.DetailImageScreen.route,
                            ClientScreen.BookingScreen.route
                        )
                        viewModel.currentImage.value = url
                    },
                    { it ->
                        Firestore.fetchById<Tour>("$TOUR/${it.tourID}") { tour ->
                            viewModel.detailTour.value = tour
                            if (tour != null) {
                                nav.navigationTo(
                                    ClientScreen.DetailTourScreen.route,
                                    ClientScreen.BookingScreen.route
                                )
                            }

                        }


                    })
            }
        }
    }
}

@Composable
fun LoadFinishData(value: MutableList<Order>, nav: NavController) {
    val isBotomSheet = remember {
        mutableStateOf(false)
    }
    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(value) {
                OrderUpComingData(it, { url ->
                    nav.navigationTo(
                        ClientScreen.DetailImageScreen.route,
                        ClientScreen.BookingScreen.route
                    )
                    viewModel.currentImage.value = url
                }, {
                    Firestore.fetchById<Tour>("$TOUR/${it.tourID}") { tour ->
                        viewModel.detailTour.value = tour
                        if (tour != null) {
                            nav.navigationTo(
                                ClientScreen.ReviewScreen.route,
                                ClientScreen.BookingScreen.route
                            )
                        }

                    }

                })
            }
        }
    }
}



@Composable
fun LoadUpComingData(value: MutableList<Order>, nav: NavController) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(value) {
                OrderUpComingData(it, { url ->
                    nav.navigationTo(
                        ClientScreen.DetailImageScreen.route,
                        ClientScreen.BookingScreen.route
                    )
                    viewModel.currentImage.value = url
                }, {})
            }
        }
    }
}

@Composable
fun LoadAllData(value: MutableList<Order>, nav: NavController) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(value) {
                OrderUpComingData(it, { url ->
                    nav.navigationTo(
                        ClientScreen.DetailImageScreen.route,
                        ClientScreen.BookingScreen.route
                    )
                    viewModel.currentImage.value = url
                }, {
                    if(it.orderStatus==OrderStatus.FINISH){
                        Firestore.fetchById<Tour>("$TOUR/${it.tourID}") { tour ->
                            viewModel.detailTour.value = tour
                            if (tour!= null) {
                                nav.navigationTo(
                                    ClientScreen.ReviewScreen.route,
                                    ClientScreen.BookingScreen.route
                                )
                            }

                        }
                    }

                })
            }
        }
    }
}




@Composable
fun OrderUpComingData(it: Order, onViewOrder: (String) -> Unit, onItemClick: (Order) -> Unit) {
    val context = LocalContext.current
    val imageUrl = remember {
        mutableStateOf("")
    }
    Firestore.getValue<List<String>>(
        "$TOUR/${it.tourID}",
        "tourImage", { imageUrl.value = it?.get(0) ?: "" }, {}
    )
    val tourTime = getTimeBeforeHours(it.tourTime.startTime)
    val totalPrice = remember {
        mutableStateOf((it.adultCount * it.totalPrice) + (it.childCount * it.totalPrice))
    }

    val borderColor = when (it.orderStatus) {
        OrderStatus.PENDING ->  Color(0xFF9C27B0)
        OrderStatus.PAID -> gold
        OrderStatus.RUNNING -> Color(0xFF2196F3)
        OrderStatus.FINISH -> Color(0xff4caf50)
        OrderStatus.CANCELED -> Color(0xfff44336)
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                borderColor.opacity(0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(BorderStroke(2.dp, borderColor), shape = RoundedCornerShape(12.dp))

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (it.orderStatus != OrderStatus.FINISH && it.orderStatus != OrderStatus.CANCELED) {
                TextView(
                    text = context.getString(R.string.note_cancel_before) + " $tourTime",
                    color = Color.Red,
                    textSize = 13,
                    maxLine = 3,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                RoundedImage(
                    modifier = Modifier
                        .widthPercent(40f)
                        .height(120.dp),
                    contentScale = ContentScale.Crop,
                    data = imageUrl.value, shape = RoundedCornerShape(8.dp)
                )
                SpaceW(w = 8)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TextView(
                        text = it.tourName,
                        textSize = 16,
                        font = Font(R.font.poppins_semibold),
                        color = textColor(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .background(
                                color = transparent,
                            )
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(8.dp),
                                color = textColor(context)
                            )
                    ) {
                        SpaceH(h = 3)
                        TextView(
                            text = "${context.getString(R.string.from)} ${it.tourTime.startTime}",
                            textSize = 12,
                            color = textColor(),
                            font = Font(R.font.poppins_regular),
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        TextView(
                            text = "${context.getString(R.string.to)} ${it.tourTime.endTime}",
                            textSize = 12,
                            color = textColor(),
                            modifier = Modifier.padding(horizontal = 6.dp),
                            font = Font(R.font.poppins_regular),
                        )
                        SpaceH(h = 3)
                    }
                }
            }
            SpaceH(h = 12)


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = transparent,
                    )
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp),
                        color = textColor(context)
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(R.string.adult),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                )
                Text(
                    text = context.getString(R.string.price) + ": " + it.tourPrice.toCurrency(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                )
                Text(
                    text = context.getString(R.string.count) + ": ${it.adultCount}",
                    fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.poppins_regular))
                )
            }
            if (it.childCount != 0) {
                SpaceH(h = 6)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = transparent,
                        )
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = textColor(context)
                        )
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = context.getString(R.string.child),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    )
                    Text(
                        text = context.getString(R.string.price) + ": " + (it.tourPrice * 0.75f).toCurrency(),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    )
                    Text(
                        text = context.getString(R.string.count) + ": ${it.childCount}",
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.poppins_regular))
                    )
                }
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = context.getString(R.string.total_price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                )
                Text(
                    text = totalPrice.value.toCurrency(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Text(
                        text = context.getString(R.string.order_time),
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium))
                    )

                    Text(
                        text = it.orderDate,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    )
                    SpaceH(h = 6)
                    TextView(
                        text = context.getString(R.string.bill),
                        textSize = 14,
                        color = red,
                        font = Font(R.font.poppins_regular)
                    ) {
                        onViewOrder.invoke(it.invoiceUrl)
                    }

                }

                when (it.orderStatus) {
                    OrderStatus.PENDING -> {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF673AB7), Color(0xFF0622BD)
                                        )
                                    ), shape = RoundedCornerShape(12.dp)
                                )
                                .defaultMinSize(minWidth = 80.dp)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                fontSize = 14.sp,
                                text = context.getString(R.string.cancel),
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = white
                            )
                            Box(modifier = Modifier
                                .matchParentSize()
                                .onClick { onItemClick.invoke(it) })
                        }
                    }

                    OrderStatus.PAID -> {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF7ED321), Color(0xFFFFC700)
                                        )
                                    ), shape = RoundedCornerShape(12.dp)
                                )
                                .defaultMinSize(minWidth = 80.dp)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                fontSize = 14.sp,
                                text = context.getString(R.string.pay),
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = white
                            )
                            Box(modifier = Modifier
                                .matchParentSize()
                                .onClick { onItemClick.invoke(it) })
                        }
                    }

                    OrderStatus.CANCELED -> {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFF9A825), Color(0xFFFF4500)
                                        )
                                    ), shape = RoundedCornerShape(12.dp)

                                )
                                .defaultMinSize(minWidth = 80.dp)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                fontSize = 14.sp,
                                text = context.getString(R.string.detail),
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = white
                            )
                            Box(modifier = Modifier
                                .matchParentSize()
                                .onClick { onItemClick.invoke(it) })
                        }
                    }

                    OrderStatus.FINISH -> {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF4CAF50), Color(0xFF009688)
                                        )
                                    ), shape = RoundedCornerShape(12.dp)
                                )
                                .defaultMinSize(minWidth = 80.dp)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = context.getString(R.string.review),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = white
                            )
                            Box(modifier = Modifier
                                .matchParentSize()
                                .onClick { onItemClick.invoke(it) })
                        }
                    }
                    OrderStatus.RUNNING -> {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF4CAF50), Color(0xFF009688)
                                        )
                                    ), shape = RoundedCornerShape(12.dp)
                                )
                                .defaultMinSize(minWidth = 80.dp)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = context.getString(R.string.review),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = white
                            )
                            Box(modifier = Modifier
                                .matchParentSize()
                                .onClick { onItemClick.invoke(it) })
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CustomTabRow(
    tabData: List<String>,
    tabIndex: Int,
    onItemClick: (Int) -> Unit,
    selectedColor: Color = Color.Blue,
    unselectedColor: Color = Color.Gray,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        tabData.forEachIndexed { index, text ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .onClick { onItemClick(index) }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = text,
                        fontSize = 16.sp,
                        color = if (tabIndex == index) selectedColor else unselectedColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .widthPercent(25f)
                            .padding(horizontal = 12.dp)
                            .height(4.dp)
                            .background(
                                color = if (tabIndex != index) transparent else selectedColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                }
                Box(modifier = Modifier
                    .matchParentSize()
                    .onClick { onItemClick.invoke(index) })
            }
        }
    }
}