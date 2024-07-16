package dong.datn.tourify.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.os.Looper
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import dong.datn.tourify.R
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.limeGreen
import dong.datn.tourify.ui.theme.orange
import dong.datn.tourify.ui.theme.orangeRed
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.onClick
import dong.duan.livechat.widget.GradientProgressIndicator
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
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

fun delay(time: Int, content: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        content.invoke()
    }, time.toLong())
}

enum class CallbackType {
    CONFIRM,
    SUCCESS,
    INFO,
    ERROR,
}

@Composable
fun DialogState(callbackType: CallbackType) {
    viewModels.loadingState.value = false
    val context = LocalContext.current
    val color = when (callbackType) {
        CallbackType.CONFIRM -> appColor
        CallbackType.SUCCESS -> limeGreen
        CallbackType.INFO -> orange
        CallbackType.ERROR -> orangeRed
    }
    val icon = when (callbackType) {
        CallbackType.CONFIRM -> FaIcons.QuestionCircle
        CallbackType.SUCCESS -> FaIcons.CheckCircle
        CallbackType.INFO -> FaIcons.ExclamationCircle
        CallbackType.ERROR -> R.drawable.circle_xmark_solid
    }

    val title = if (viewModels.dialogTitle.value == "") when (callbackType) {
        CallbackType.CONFIRM -> context.resources.getString(R.string.confirm)
        CallbackType.SUCCESS -> context.resources.getString(R.string.success)
        CallbackType.INFO -> context.resources.getString(R.string.warning)
        CallbackType.ERROR -> context.resources.getString(R.string.error_title)
    } else viewModels.dialogTitle.value



    Dialog(
        onDismissRequest = { viewModels.dialogState.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(1f)
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    if (icon is FaIconType) {
                        FaIcon(faIcon = icon as FaIconType, tint = color, size = 28.dp)
                    } else {
                        Icon(
                            painter = painterResource(id = icon as Int),
                            contentDescription = "Icon",
                            tint = color,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Space(w = 8)
                    TextView(
                        text = title,
                        modifier = Modifier,
                        textSize = 18,
                        font = Font(R.font.poppins_semibold),
                        color = color
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextView(
                    text = viewModels.dialogMessage.value,
                    modifier = Modifier,
                    textSize = 18,
                    font = Font(R.font.poppins_regular)
                )

                Spacer(modifier = Modifier.height(12.dp))
                if (callbackType != CallbackType.CONFIRM) {
                    Row(Modifier.fillMaxWidth(1f)) {
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            Modifier
                                .onClick {
                                    viewModels.dialogState.value = false
                                }
                                .height(40.dp)
                                .weight(1f)
                                .background(color, shape = RoundedCornerShape(40.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                LocalContext.current.getString(R.string.Ok),
                                Modifier,
                                color = white,
                                fontFamily = FontFamily(Font(R.font.poppins_medium))
                            )
                        }

                    }
                } else {
                    Row(Modifier.fillMaxWidth(1f)) {
                        Box(
                            Modifier
                                .onClick {
                                    viewModels.dialogState.value = false
                                }
                                .height(40.dp)
                                .weight(1f)
                                .background(lightGrey, shape = RoundedCornerShape(40.dp)),
                            contentAlignment = Alignment.Center) {
                            Text(
                                LocalContext.current.getString(R.string.cancel),
                                Modifier,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.poppins_medium))
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            Modifier
                                .onClick {
                                    viewModels.dialogState.value = false
                                    viewModels.dialogCallback?.invoke()
                                }
                                .height(40.dp)
                                .weight(1f)
                                .background(limeGreen, shape = RoundedCornerShape(40.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                LocalContext.current.getString(R.string.Ok),
                                Modifier,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.poppins_medium))
                            )
                        }

                    }
                }

            }
        }
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

@Composable
fun SpaceH(h: Int) {
    Spacer(
        modifier = Modifier
            .height(h.dp)
    )
}
@Composable
fun SpaceW(w: Int) {
    Spacer(
        modifier = Modifier
            .width(w.dp)
    )
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

fun timeNow(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        currentDateTime.format(formatter)
    } else {
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        formatter.format(date)
    }
}
fun isDarkMode(context: Context): Boolean {
    val currentNightMode =
        context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

fun Double.toCurrency(format: String = "vn"): String {
    val locale = when (format.lowercase()) {
        "us" -> Locale.US
        "uk" -> Locale.UK
        "vn" -> Locale("vi", "VN")
        // Add more formats as needed
        else -> Locale.getDefault()
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    return currencyFormat.format(this)
}

fun Int.toCurrency(format: String = "vn"): String {
    val locale = when (format.lowercase()) {
        "us" -> Locale.US
        "uk" -> Locale.UK
        "vn" -> Locale("vi", "VN")
        // Add more formats as needed
        else -> Locale.getDefault()
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    return currencyFormat.format(this)
}

fun Float.toCurrency(format: String = "vn"): String {
    val locale = when (format.lowercase()) {
        "us" -> Locale.US
        "uk" -> Locale.UK
        "vn" -> Locale("vi", "VN")
        else -> Locale.getDefault()
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    return currencyFormat.format(this)
}






