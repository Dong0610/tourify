package dong.datn.tourify.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun getCurrentYearMonth(): Pair<Int, Int> {
    val now = LocalDate.now()
    val currentYear = now.year
    val currentMonth = now.monthValue
    return Pair(currentYear, currentMonth)
}

fun getDaysInMonth(year: Int, month: Int): List<String> {
    val yearMonth = YearMonth.of(year, month)
    val daysInMonth = yearMonth.lengthOfMonth()
    return (1..daysInMonth).map { it.toString() }
}

@Composable
fun DaysInCurrentMonth(): List<String> {
    val (year, month) = getCurrentYearMonth()
    return remember(year, month) {
        getDaysInMonth(year, month)
    }
}