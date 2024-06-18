package dong.datn.tourify.utils

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.ui.theme.white
import dong.duan.livechat.widget.GradientProgressIndicator

fun NavigateTo(navController: NavController, router: String) {
    navController.navigate(router) {
        popUpTo(router)
        launchSingleTop = true
    }
}

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
fun ListChatItem(imgUrl: String? = "", name: String? = "", onItemClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable { onItemClick.invoke() }) {
        CommonImage(
            data = imgUrl,
            Modifier
                .width(75.dp)
                .height(75.dp)
                .padding(3.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = name ?: "Error")
    }
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomTextField() {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        Modifier
            .height(48.dp)
            .padding(10.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(Modifier.background(white)) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
                BasicTextField(
                    value = name,
                    keyboardOptions = KeyboardOptions.Default,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    )
                )
                Box(
                    Modifier
                        .padding(vertical = 2.dp)
                        .width(1.dp)
                )
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
        }
    }
}


@Composable
fun DotsFlashing(dotSize: Dp = 18.dp, color: Color = Color.Blue, delayUnit: Int = 750) {
    val minAlpha = 0.1f

    @Composable
    fun Dot(
        alpha: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .alpha(alpha)
            .background(
                color = color, shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateAlphaWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = minAlpha,
        animationSpec = infiniteRepeatable(animation = keyframes {
            durationMillis = delayUnit * 4
            minAlpha at delay with LinearEasing
            1f at delay + delayUnit with LinearEasing
            minAlpha at delay + delayUnit * 2
        }),
        label = ""
    )

    val alpha1 by animateAlphaWithDelay(0)
    val alpha2 by animateAlphaWithDelay(delayUnit)
    val alpha3 by animateAlphaWithDelay(delayUnit * 2)
    val alpha4 by animateAlphaWithDelay(delayUnit * 3)

    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        val spaceSize = 12.dp

        Dot(alpha1)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha2)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha3)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha4)
    }
}








