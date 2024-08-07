package dong.datn.tourify.screen.staff

import android.annotation.SuppressLint
import android.text.TextPaint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.model.Order
import dong.datn.tourify.ui.theme.boxColor
import dong.datn.tourify.ui.theme.lightBlue
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.limeGreen
import dong.datn.tourify.ui.theme.orangeRed
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.toDp
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.onClick
import dong.duan.travelapp.model.Tour
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


fun createChartData(orders: MutableList<Order>, currentYearMonth: YearMonth): List<ChartData> {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
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
fun HomeStaffScreen(nav: NavController, viewModel: AppViewModel, openDrawer: () -> Unit) {

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
                        .background(boxColor())
                        .border(
                            width = 1.dp, color = lightGrey, shape = RoundedCornerShape(8.dp)
                        )
                        .heightPercent(32f), contentAlignment = Alignment.TopCenter
                ) {
                    OrderByMonth(viewModel.ordersByMonth.value)
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(boxColor())
                        .border(
                            width = 1.dp, color = lightGrey, shape = RoundedCornerShape(8.dp)
                        )
                        .heightPercent(32f), contentAlignment = Alignment.TopStart
                ) {
                    TourRatioPercent(viewModel.listTourStaff.value)
                }

            }
        }

    }

}


@Composable
fun TourRatioPercent(list: MutableList<Tour>) {
    val cancel = remember { mutableStateOf(0) }
    val success = remember { mutableStateOf(0) }

    list.forEach {
                cancel.value += it.cancel
                success.value += it.success
            }

    val total = cancel.value + success.value
    val cancelRatio = if (total != 0) cancel.value.toFloat() / total else 0f
    val successRatio = if (total != 0) success.value.toFloat() / total else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 3.dp)
    ) {
        TextView(
            text = LocalContext.current.getString(R.string.chart_ratio_value),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            textSize = 12,
            textAlign = TextAlign.Center,
            font = Font(R.font.poppins_medium)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {
            drawPieChart(
                cancelRatio = cancelRatio,
                successRatio = successRatio
            )
        }

            Column(Modifier.padding(end = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(18.dp)
                            .background(orangeRed, shape = CircleShape),
                    )
                    SpaceW(w = 2)
                    Column {
                        TextView(
                            text = LocalContext.current.getString(R.string.cancel),
                            modifier = Modifier,
                            textSize = 12,
                            font = Font(R.font.poppins_regular)
                        )
                        SpaceW(w = 2)
                    }

                }
                SpaceH(h = 6)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(18.dp)
                            .background(limeGreen, shape = CircleShape),
                    )
                    SpaceW(w = 2)
                    Column {
                        TextView(
                            text = LocalContext.current.getString(R.string.success),
                            modifier = Modifier,
                            textSize = 12,
                            font = Font(R.font.poppins_regular)
                        )
                        SpaceW(w = 2)
                    }

                }
            }
        }


    }
}


fun DrawScope.drawPieChart(cancelRatio: Float, successRatio: Float) {
    val sweepAngleCancel = 360 * cancelRatio
    val sweepAngleSuccess = 360 * successRatio

    // Define colors
    val orangeRed = Color(0xFFFF4500)
    val limeGreen = Color(0xFF32CD32)

    // Define text paint for labels
    val textPaint = TextPaint().apply {
        color = android.graphics.Color.WHITE
        textSize = 13.sp.toPx()
        textAlign = android.graphics.Paint.Align.CENTER
        typeface = android.graphics.Typeface.DEFAULT
    }

    withTransform({
        rotate(-90f, pivot = center)
    }) {
        // Draw the arcs
        drawArc(
            color = orangeRed,
            startAngle = 0f,
            sweepAngle = sweepAngleCancel,
            useCenter = true
        )

        drawArc(
            color = limeGreen,
            startAngle = sweepAngleCancel,
            sweepAngle = sweepAngleSuccess,
            useCenter = true
        )

        // Calculate the position for cancel ratio text
        val cancelAngle = sweepAngleCancel / 2
        val cancelTextX =
            center.x + (size.width / 2 * 0.5f * kotlin.math.cos(Math.toRadians(cancelAngle.toDouble()))).toFloat() + 10f
        val cancelTextY =
            center.y + (size.height / 2 * 0.5f * kotlin.math.sin(Math.toRadians(cancelAngle.toDouble()))).toFloat() + 10f

        // Draw the cancel ratio text
        drawContext.canvas.nativeCanvas.drawText(
            "${(cancelRatio * 100).toInt()}%",
            cancelTextX,
            cancelTextY,
            textPaint
        )

        // Calculate the position for success ratio text
        val successAngle = sweepAngleCancel + (sweepAngleSuccess / 2)
        val successTextX =
            center.x + (size.width / 2 * 0.5f * kotlin.math.cos(Math.toRadians(successAngle.toDouble()))).toFloat()
        val successTextY =
            center.y + (size.height / 2 * 0.5f * kotlin.math.sin(Math.toRadians(successAngle.toDouble()))).toFloat()

        // Draw the success ratio text
        drawContext.canvas.nativeCanvas.drawText(
            "${(successRatio * 100).toInt()}%",
            successTextX,
            successTextY,
            textPaint
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
    val currentMonth = remember {
        mutableStateOf(YearMonth.now())
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 3.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowLeft,
                contentDescription = "Test",
                tint = textColor(),
                modifier = Modifier.onClick {
                    currentMonth.value = currentMonth.value.minusMonths(1)
                    viewModels.ordersByMonth.value = createChartData(
                        viewModels.listAllOrder.value,
                        currentMonth.value
                    ).toMutableList()
                })
            TextView(
                text = LocalContext.current.getString(R.string.daily_sale) + " " + currentMonth.value,
                modifier = Modifier, textSize = 11, font = Font(R.font.poppins_regular)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowRight,
                contentDescription = "Test",
                tint = textColor(),
                modifier =   Modifier.onClick {
                    currentMonth.value = currentMonth.value.plusMonths(1)
                    viewModels.ordersByMonth.value = createChartData(
                        viewModels.listAllOrder.value,
                        currentMonth.value
                    ).toMutableList()
                })
        }


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
                        color = textColor()
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





