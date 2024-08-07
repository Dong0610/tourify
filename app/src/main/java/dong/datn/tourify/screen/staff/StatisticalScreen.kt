package dong.datn.tourify.screen.staff

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dong.datn.tourify.R
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Order
import dong.datn.tourify.screen.client.CustomTabRow
import dong.datn.tourify.screen.client.DatePickerBox
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.unselectedColor
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.USERS
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.duan.travelapp.model.Tour
import dong.duan.travelapp.model.Users
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StatisticalScreen(drawScope: () -> Unit) {
    val context = LocalContext.current
    val tabData = listOf(
        context.getString(R.string.renuve),
        context.getString(R.string.customer),

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
    }
    ViewParent {
        Column(Modifier.matchParentSize()) {
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
                    .fillMaxWidth().height(0.dp)
            )

            HorizontalPager(
                state = pagerState
            ) { index ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (index == 0) {
                        LoadDataRenuve(viewModels.listAllOrder.value)
                    }
                }
            }
        }
    }
}

fun parseOrderDate(dateStr: String): LocalDateTime? {
    return try {
        LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
    } catch (e: Exception) {
        null
    }
}

fun parsePickerDate(dateStr: String): LocalDateTime? {
    return try {
        LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    } catch (e: Exception) {
        null
    }
}

@Composable
fun LoadDataRenuve(value: MutableList<Order>) {
    val fromTimeString = remember { mutableStateOf("") }
    val toTimeString = remember { mutableStateOf("") }

    val fromTime = remember {
        mutableStateOf<LocalDateTime?>(null)
    }
    val toTime = remember {
        mutableStateOf<LocalDateTime?>(null)
    }

    LaunchedEffect(fromTimeString.value) {
        fromTime.value = parsePickerDate(fromTimeString.value)
        Log.d("FilterDebug", "From Time: ${fromTime.value}")
    }

    LaunchedEffect(toTimeString.value) {
        toTime.value = parsePickerDate(toTimeString.value)
        Log.d("FilterDebug", "To Time: ${toTime.value}")
    }
    SpaceH(h = 12)
    Row(
        Modifier
            .wrapContentHeight()
            .background(whiteSmoke, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DatePickerBox(Modifier.weight(1f)) {
            fromTimeString.value = it
        }
        SpaceW(w = 12)
        TextView(text = R.string.to)
        SpaceW(w = 12)
        DatePickerBox(Modifier.weight(1f)) {
            toTimeString.value = it
        }
    }

    val filteredOrders = remember(value, fromTime.value, toTime.value) {
        value.filter { order ->
            val orderDate = parseOrderDate(order.orderDate)
            Log.d("FilterDebug", "Order Date: $orderDate")
            val startTimeCondition = fromTime.value?.let { orderDate?.isAfter(it) == true || orderDate?.isEqual(it) == true } ?: true
            val endTimeCondition = toTime.value?.let { orderDate?.isBefore(it) == true || orderDate?.isEqual(it) == true } ?: true
            Log.d("FilterDebug", "Start Time Condition: $startTimeCondition, End Time Condition: $endTimeCondition")
            startTimeCondition && endTimeCondition
        }
    }
    SpaceH(h = 6)
    OrderDataView(filteredOrders)

}


@Composable
fun OrderDataView(orders: List<Order>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())

    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .horizontalScroll(rememberScrollState())
        ) {
            HeaderRow()
            orders.forEachIndexed { index, order ->
                DataRow(index, order)
            }
        }
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Gray)
            .padding(vertical = 8.dp)
    ) {
        HeaderCell("STT", 15f)
        HeaderCell("Tài khoản", 50f)
        HeaderCell("Số điện thoại", 30f)
        HeaderCell("Tên tour", 50f)
        HeaderCell("Thời gian", 50f)
        HeaderCell("Tình trạng", 50f)
        HeaderCell("Tổng tiền (VND)", 50f)
    }
}

@Composable
fun HeaderCell(text: String, percent: Float = 8f) {
    Text(
        text = text,
        modifier = Modifier
            .widthPercent(percent)
            .padding(4.dp),
        color = Color.White,
        fontSize = 12.sp
    )
}

@Composable
fun DataRow(index: Int, order: Order) {
    val user = remember {
        mutableStateOf<Users?>(null)
    }
    val tours = remember {
        mutableStateOf<Tour?>(null)
    }
    LaunchedEffect(Unit) {
        Firestore.fetchById<Users>("$USERS/${order.userOrderId}") {
            user.value = it
        }
        Firestore.fetchById<Tour>("$TOUR/${order.tourID}") {
            tours.value = it
        }

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, shape = RectangleShape, color = lightGrey)
            .padding(vertical = 8.dp)
    ) {
        DataCell("$index", 15f)
        DataCell(user.value?.Name ?: "", 50f)
        DataCell(user.value?.PhoneNumber ?: "", 30f)
        DataCell(order.tourName, 50f)
        DataCell(order.orderDate, 50f)
        DataCell(order.orderStatus.toString(), 50f)
        DataCell(order.totalPrice.toCurrency(), 50f)
    }
}

@Composable
fun DataCell(text: String, percent: Float = 8f) {

    Text(
        text = text,
        modifier = Modifier
            .widthPercent(percent = percent)
            .padding(4.dp),
        fontSize = 12.sp
    )
}