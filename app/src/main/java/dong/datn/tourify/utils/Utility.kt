package dong.datn.tourify.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dong.datn.tourify.app.currentTheme
import dong.duan.livechat.widget.GradientProgressIndicator
import java.text.NumberFormat
import java.util.Locale


@Composable
fun CommonProgressBar(speed: Int = 1000) {
    val indicatorSize = widthPercent(3.5f).dp
    val trackWidth: Dp = (indicatorSize * 0.1f)

    @Composable
    fun animateRotation(): Float {
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
                animation = tween(speed, easing = LinearEasing), repeatMode = RepeatMode.Restart
            ), label = ""
        )
        return rotation
    }

    val commonModifier = Modifier
        .size(indicatorSize)
        .rotate(animateRotation())
    Row(modifier = Modifier

        .background(Color(0x10000000))
        .clickable(enabled = false) {}
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        GradientProgressIndicator(
            progress = 100f,
            modifier = commonModifier,
            strokeWidth = trackWidth,
            gradientStart = ProgressBarColor.Blue.gradientStart,
            gradientEnd = ProgressBarColor.Purple.gradientEnd,
            trackColor = Color.LightGray,
        )
    }
}

@Composable
fun Space(w: Int = 0, h: Int = 0) {
    Spacer(
        modifier = Modifier
            .width(w.dp)
            .height(h.dp)
    )
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


enum class ProgressBarColor(val gradientStart: Color, val gradientEnd: Color) {
    Red(Color(254, 222, 224), Color(255, 31, 43)), Green(
        Color(168, 242, 205),
        Color(38, 222, 129)
    ),
    Blue(Color(219, 229, 251), Color(75, 123, 236)), Purple(
        Color(224, 204, 255),
        Color(98, 0, 254, 255)
    );

    operator fun invoke(): Color {
        return gradientEnd
    }
}


@Composable
fun CommonDivider(modifier: Modifier = Modifier) {
    Divider(
        color = Color.LightGray, thickness = 1.dp, modifier = modifier.alpha(0.3f)
    )
}


@Composable
fun CommonImage(
    data: Any? = null, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.Crop
) {

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = data)
            .apply(block = fun ImageRequest.Builder.() {
                allowHardware(false)
            }).build()
    )

    Card(
        shape = CircleShape, modifier = modifier.alpha(
            1f
        )
    ) {
        Image(
            painter = painter,
            contentDescription = "...",
            contentScale = contentScale,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}


fun Color.opacity(alpha: Float):Color{
    return this.copy(
        alpha = alpha
    )
}

fun Float.toCurrency(code: String = "vn"): String {
    val locale = Locale(code)
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    return currencyFormat.format(this)
}

fun Int.toCurrency(code: String = "vn"): String {
    val locale = Locale(code)
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    return currencyFormat.format(this)
}

fun Double.toCurrency(code: String = "vn"): String {
    val locale = Locale(code)
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    return currencyFormat.format(this)
}








