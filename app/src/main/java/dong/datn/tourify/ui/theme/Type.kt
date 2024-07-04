package dong.datn.tourify.ui.theme

import android.content.Context
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dong.datn.tourify.app.currentTheme

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

fun fromColor(code: String): Int {
    val cleanedCode = code.replace("#", "").replace(" ", "")
    return android.graphics.Color.parseColor("#$cleanedCode")
}

