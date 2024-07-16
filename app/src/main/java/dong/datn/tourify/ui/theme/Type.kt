package dong.datn.tourify.ui.theme

import android.content.Context
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dong.datn.tourify.app.currentTheme
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
titleLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
),
labelSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)
*/
)

fun textColor(context: Context?=null): Color {
    return when (currentTheme) {
        -1 -> white
        1 -> black
        else -> appColor
    }
}

fun navigationBar(context: Context):Color{
    return when (currentTheme) {
        1 -> white
        -1 -> black
        else -> appColor
    }
}

fun boxColor():Color{
    return when (currentTheme) {
        1 -> whiteSmoke
        -1 -> iconBackground
        else -> appColor
    }
}

fun navigationColor(isSelect:Boolean=false):Color{
    return when (currentTheme) {
        1 -> if (isSelect) appColor else lightGrey
        -1 -> if (isSelect) white else lightGrey
        else -> appColor
    }
}


fun colorByTheme(light: Color = Color.Black, dark: Color = Color.Black): Color {
    return if (currentTheme == 1) light else dark
}

fun colorByTheme(light: String = "#000000", dark: String = "#ffffff", currentTheme: Int): Int {
    return if (currentTheme == 1) fromColor(light) else fromColor(dark)
}
fun formatRelativeTime(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val dateTime = LocalDateTime.parse(timestamp, formatter)
    val zonedDateTime = dateTime.atZone(ZoneId.systemDefault())
    val now = ZonedDateTime.now(ZoneId.systemDefault())

    val duration = Duration.between(zonedDateTime, now)
    return when {
        duration.toMinutes() < 1 -> "just now"
        duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes"
        duration.toHours() < 24 -> "${duration.toHours()} hours"
        duration.toDays() < 7 -> "${duration.toDays()} days"
        duration.toDays() < 30 -> "${duration.toDays() / 7} weeks"
        duration.toDays() < 365 -> "${duration.toDays() / 30} months"
        else -> "${duration.toDays() / 365} years"
    }
}
fun getTimeBeforeHours(dateTimeString: String, hour: Int = 36): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)
    val dateTimeBefore48Hours = dateTime.minus(hour.toLong(), ChronoUnit.HOURS)
    return dateTimeBefore48Hours.format(formatter)
}

fun fromColor(code: String): Int {
    val cleanedCode = code.replace("#", "").replace(" ", "")
    return android.graphics.Color.parseColor("#$cleanedCode")
}

