package dong.datn.tourify.widget

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dong.datn.tourify.R
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ViewParent(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    onBack: (() -> Unit?)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    BackHandler(onBack = {

        softwareKeyboardController?.hide()
        coroutineScope.launch {
            onBack?.invoke()
        }
    })
    Box(
        modifier = modifier
            .background(if (currentTheme == 1) white else black)
            .fillMaxSize(1f),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        content = content
    )
}

@Composable
fun ScrollView(content: @Composable BoxScope.() -> Unit) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Box(Modifier.verticalScroll(scrollState)) {
        content()
    }
}

@Composable
fun IconView(
    modifier: Modifier,
    icon: ImageVector,
    icSize: Int = 32,
    tint: Color? = null,
    onclick: (() -> Unit?)? = null
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .background(if (currentTheme == 1) lightGrey else darkGray, shape = CircleShape)
            .size(40.dp)
            .clickable(interactionSource, null) {
                onclick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            modifier = Modifier.size(icSize.dp),
            contentDescription = "Check",
            tint = tint ?: textColor(context)
        )
    }
}

@Composable
fun TextView(
    text: String,
    modifier: Modifier,
    textSize: Int = 16,
    color: Color? = null,
    font: Font? = null,
    textAlign: TextAlign? = TextAlign.Start,
    onclick: (() -> Unit?)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    Text(
        text = text,
        fontSize = textSize.sp,
        textAlign = textAlign,
        color = color ?: textColor(context),
        fontFamily = FontFamily(
            font ?: Font(
                R.font.poppins_regular
            )
        ),
        modifier = modifier.clickable(interactionSource, null) {
            onclick?.invoke()
        }
    )
}

@Composable
fun AppButton(text: String, modifier: Modifier, isLoading: Int? = null, onClick: () -> Unit) {

    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF02FF9A),
                        Color(0xFF0622BD)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp)
            .onClick {
                onClick.invoke()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(6.dp))
        if (isLoading != null) {
            when (isLoading) {
                -1 -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_dangerous),
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Check",
                        tint = Color.White
                    )
                }

                0 -> {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                }

                1 -> {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Check",
                        tint = Color.White
                    )
                }
            }
        }

    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.onClick(onClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )
}