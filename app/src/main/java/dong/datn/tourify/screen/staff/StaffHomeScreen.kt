package dong.datn.tourify.screen.staff

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Order
import dong.datn.tourify.ui.theme.lightBlue
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.toDp
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.duan.travelapp.model.Tour
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale


fun createChartData(orders: MutableList<Order>): List<ChartData> {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val currentYearMonth = YearMonth.now()
    val daysInMonth = 1..currentYearMonth.lengthOfMonth()

    val priceByDay = mutableMapOf<Int, Double>().apply {
        daysInMonth.forEach { day ->
            put(day, 0.0)
        }
    }

    orders.forEach { order ->
        val dateTime = LocalDateTime.parse(order.orderDate, dateFormatter)
        if (dateTime.year == currentYearMonth.year && dateTime.month == currentYearMonth.month) {
            val dayOfMonth = dateTime.dayOfMonth
            priceByDay[dayOfMonth] = priceByDay.getOrDefault(dayOfMonth, 0.0) + order.prePayment
        }
    }

    return daysInMonth.map { day ->
        ChartData(label = day.toString(), value = priceByDay[day]?.toFloat() ?: 0f)
    }
}
@Composable
fun HomeStaffScreen(nav: NavController, viewModel: AppViewModel,opneDrawer: ()->Unit){

    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    val listAllOrder = remember { mutableStateOf(mutableListOf<Order>()) }
    val ordersByMonth = remember { mutableStateOf(listOf<ChartData>()) }

    LaunchedEffect(Unit) {
        Firestore.getListData<Order>("ORDER") {
            listAllOrder.value = it?.toMutableList() ?: mutableListOf()
            ordersByMonth.value = createChartData(it?.toMutableList() ?: mutableListOf())

        }
    }
    ViewParent(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(lightBlue)
    ) {

        VerScrollView(Modifier.fillMaxSize()) {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(white)
                        .border(
                            width = 1.dp, color = lightGrey, shape = RoundedCornerShape(8.dp)
                        )
                        .heightPercent(32f), contentAlignment = Alignment.TopCenter
                ) {
                    OrderByMonth(ordersByMonth.value)
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(white)
                        .border(
                            width = 1.dp, color = lightGrey, shape = RoundedCornerShape(8.dp)
                        )
                        .heightPercent(32f), contentAlignment = Alignment.TopStart
                ) {
                    TourRatioPercent()
                }
            }
        }

    }
}


@Composable
fun TourRatioPercent() {
    val cancel = remember { mutableStateOf(0) }
    val success = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        Firestore.getListData<Tour>("$TOUR") {
            it?.forEach {
                cancel.value += it.cancel
                success.value += it.success
            }
        }

    }

    val total = cancel.value + success.value
    val cancelRatio = if (total != 0) cancel.value.toFloat() / total else 0f
    val successRatio = if (total != 0) success.value.toFloat() / total else 0f

    Box(modifier = Modifier.fillMaxSize().padding(start = 12.dp), contentAlignment = Alignment.CenterStart) {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawPieChart(
                cancelRatio = cancelRatio,
                successRatio = successRatio
            )
        }
    }
}
fun DrawScope.drawPieChart(cancelRatio: Float, successRatio: Float) {
    val sweepAngleCancel = 360 * cancelRatio
    val sweepAngleSuccess = 360 * successRatio

    withTransform({
        rotate(-90f, pivot = center)
    }) {
        drawArc(
            color = Color.Red,
            startAngle = 0f,
            sweepAngle = sweepAngleCancel,
            useCenter = true
        )

        drawArc(
            color = Color.Green,
            startAngle = sweepAngleCancel,
            sweepAngle = sweepAngleSuccess,
            useCenter = true
        )
    }
}

@Composable
fun calculateTotalPrice(value: MutableList<ChartData>): State<Double> {
    val totalPrice = remember {
        mutableStateOf(value.sumOf { it.value.toDouble() })
    }
    DisposableEffect(value) {
        totalPrice.value = value.sumOf { it.value.toDouble() }
        onDispose { }
    }

    return totalPrice
}


@Composable
fun OrderByMonth(value: List<ChartData>) {

    val totalPrice = calculateTotalPrice(value = value.toMutableList())

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 3.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextView(
            text = LocalContext.current.getString(R.string.daily_sale) + " " + YearMonth.now(),
            modifier = Modifier, textSize = 11, font = Font(R.font.poppins_regular)
        )
        Spacer(modifier = Modifier.weight(1f))
        TextView(
            text = LocalContext.current.getString(R.string.total_price) + ": ${totalPrice.value.toCurrency()}",
            modifier = Modifier, textSize = 11, font = Font(R.font.poppins_regular)
        )

    }
    ColumnChart(data = value, modifier = Modifier.padding(16.dp))

}

data class ChartData(val label: String, val value: Float)

fun getMaxValue(data: List<ChartData>): Float {
    return data.maxOfOrNull { it.value }
        ?: 1f
}

@SuppressLint("DefaultLocale")
@Composable
fun ColumnChart(data: List<ChartData>, modifier: Modifier = Modifier) {
    val maxValue = getMaxValue(data)
    val barSpacing = 3.dp
    val barWidth = 12.dp
    val availableHeight = remember { mutableStateOf(0) }
    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                availableHeight.value = coordinates.size.height
            }
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.Bottom
    ) {
        if (availableHeight.value > 0) {
            data.forEach { entry ->
                val clh = ((entry.value / maxValue) * (availableHeight.value - dpToPx(dp = 32.dp)))
                Column(
                    Modifier
                        .fillMaxHeight()
                        .padding(horizontal = barSpacing / 2),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(barWidth)

                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF02FF9A),
                                        Color(0xFF0622BD)
                                    )
                                ),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .height(clh.toDp()),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Text(
                            text = String.format("%.1f", entry.value),
                            fontSize = 6.sp,
                            color = white
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.label,
                        modifier = Modifier.height(20.dp),
                        fontSize = 11.sp,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
    }

}

@Composable
fun dpToPx(dp: Dp): Float {
    return with(LocalDensity.current) { dp.toPx() }
}





