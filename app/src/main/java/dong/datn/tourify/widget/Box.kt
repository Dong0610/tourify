package dong.datn.tourify.widget

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import dong.datn.tourify.R
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.opacity
import kotlinx.coroutines.launch

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
fun ViewParentContent(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    onBack: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    BackHandler(onBack = {

        softwareKeyboardController?.hide()
        coroutineScope.launch {
            onBack.invoke()
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
fun HorScrollView(content: @Composable BoxScope.() -> Unit) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Box(Modifier.horizontalScroll(scrollState)) {
        Row {
            content
        }
    }
}
@Composable
fun HorScrollView(modifier: Modifier=Modifier,content: @Composable BoxScope.() -> Unit) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Box(modifier.horizontalScroll(scrollState),content=content)
}

@Composable
fun VerScrollView(content: @Composable BoxScope.() -> Unit) {

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
fun VerScrollView(modifier: Modifier,content: @Composable BoxScope.() -> Unit) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Box(modifier.verticalScroll(scrollState)) {
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
            .background(if (currentTheme == 1) whiteSmoke else iconBackground, shape = CircleShape)
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
fun IconView2(
    modifier: Modifier,
    icon: ImageVector,
    icSize: Int = 32,
    tint: Color? = null,
    onclick:() -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .background(if (currentTheme == 1) whiteSmoke else iconBackground, shape = CircleShape)
            .size(40.dp)
            .clickable(interactionSource, null) {
                onclick.invoke()
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
fun IconView(
    item: Any,
    tint: Color = black,
    icSize: Int = 24,
    modifier: Modifier,
    onclick: (() -> Unit?)? = null
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (item) {
            is ImageVector -> Icon(
                item as ImageVector,
                contentDescription = "Image Vector",
                tint = tint,
                modifier = Modifier.size(icSize.dp)
            )

            is Painter -> Icon(
                painter = item as Painter,
                contentDescription = "Painter",
                modifier = Modifier.size(icSize.dp),
                tint = tint
            )


            else -> {
                FaIcon(
                    item as FaIconType.BrandIcon,
                    tint = tint,
                    modifier = Modifier.size(icSize.dp)
                )
            }
        }
        Box(modifier = Modifier
            .matchParentSize()
            .onClick { onclick?.invoke() })
    }

}

@Composable
fun InnerImageIcon(
    modifier: Modifier,
    icon: ImageVector,
    icSize: Int = 32,
    tint: Color? = null,
    onclick: (() -> Unit?)? = null
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .background(Color(0x90FFFFFF), shape = CircleShape)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            modifier = Modifier.size(icSize.dp),
            contentDescription = "Check",
            tint = tint ?: textColor(context)
        )
        Box(modifier = Modifier
            .matchParentSize()
            .onClick { onclick?.invoke() })
    }
}


@Composable
fun IconView(
    modifier: Modifier,
    icon: Int,
    icSize: Int = 32,
    tint: Color? = null,
    onclick: (() -> Unit?)? = null
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .background(if (currentTheme == 1) whiteSmoke else iconBackground, shape = CircleShape)
            .size(40.dp)
            .clickable(interactionSource, null) {
                onclick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.size(icSize.dp),
            contentDescription = "Check",
            tint = tint ?: textColor(context)
        )
    }
}

@Composable
fun TextView(
    text: Any,
    modifier: Modifier= Modifier,
    textSize: Int = 16,
    color: Color? = null,
    font: Font? = null,
    textAlign: TextAlign? = TextAlign.Start,
    onclick: (() -> Unit?)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val value = when (text){
        is String -> text
        is Int -> context.getString(text)
        else -> ""
    }
    Text(
        text = value,
        fontSize = textSize.sp,
        textAlign = textAlign,
        color = color ?: textColor(context),
        fontFamily = FontFamily(
            font ?: Font(
                R.font.poppins_regular
            )
        ),
        style = TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        ),
        modifier = modifier.clickable(interactionSource, null) {
            onclick?.invoke()
        }
    )
}


@Composable
fun TextView(
    text: String,
    modifier: Modifier=Modifier,
    textSize: Int = 16,
    color: Color? = null,
    font: Font? = null,
    maxLine:Int =100,
    textAlign: TextAlign? = TextAlign.Start,
    onclick: (() -> Unit?)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    Text(
        text = text,
        fontSize = textSize.sp,
        textAlign = textAlign,
        maxLines = maxLine,
        color = color ?: textColor(context),
        fontFamily = FontFamily(
            font ?: Font(
                R.font.poppins_regular
            )
        ),
        style = TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        ),
        modifier = modifier.clickable(interactionSource, null) {
            onclick?.invoke()
        }
    )
}


@Composable
fun AppButton(
    text: String,
    modifier: Modifier,
    loading: Int? = null,
    isEnable: Boolean = false,
    onClick: () -> Unit
) {
    val isLoading = remember {
        mutableStateOf(loading)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (isEnable) listOf(
                        Color(0xFF02FF9A), Color(0xFF0622BD)
                    ) else listOf(lightGrey, lightGrey)
                ), shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp), contentAlignment = Alignment.Center
    ) {
        Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (isEnable) Color.White else black,
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(6.dp))
        if (isLoading.value != null) {
            when (isLoading.value) {
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
        Box(modifier = Modifier
            .matchParentSize()
            .onClick {
                if (isEnable) {
                    if (isLoading.value != null) {
                        isLoading.value = 0
                    }
                    onClick.invoke()
                }

            })
    }
}



@Composable
fun ButtonNext(text: String,  isEnable: Boolean = true,modifier: Modifier,onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (isEnable) listOf(
                        Color(0xFF02FF9A), Color(0xFF0622BD)
                    ) else listOf(lightGrey, lightGrey)
                ), shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp),
       contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.poppins_bold)),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
        Box(modifier = Modifier
            .matchParentSize()
            .onClick {
                if(isEnable){
                    onClick.invoke()
                }
            })

    }
}

@Composable
fun ButtonNext2(text: String, modifier: Modifier,onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF02FF9A).opacity(1f), Color(0xFF0622BD).opacity(1f)
                    )
                ), shape = RoundedCornerShape(12.dp)
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


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.onLongClick(onLongClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }

    this.combinedClickable(
        onClick = {

        },
        onLongClick = {
            onLongClick()
        },
        indication = null,
        interactionSource = interactionSource
    )
}

@Composable
fun RoundedImage(
    data: Any? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(12),
) {

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = data)
            .apply(block = fun ImageRequest.Builder.() {
                allowHardware(false)
            }).build()
    )

    Card(
        shape = shape, modifier = modifier.alpha(
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

@Composable
fun DotIndicator(count: Int = 3, current: Int = 0) {
    val activeDotSize = 16.dp
    val inactiveDotSize = 8.dp
    val activeDotColor = Color.Blue
    val inactiveDotColor = Color.Gray
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier
                    .width(if (i == current) activeDotSize else inactiveDotSize)
                    .height(inactiveDotSize)
                    .background(
                        color = if (i == current) activeDotColor else inactiveDotColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DotIndicator(pagerState: PagerState, count: Int = pagerState.pageCount) {
    val activeDotSize = 16.dp
    val inactiveDotSize = 8.dp
    val activeDotColor = Color.Blue
    val inactiveDotColor = Color.Gray

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 2.dp, horizontal = 16.dp)
    ) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier
                    .width(if (i == pagerState.currentPage) activeDotSize else inactiveDotSize)
                    .height(inactiveDotSize)
                    .background(
                        color = if (i == pagerState.currentPage) activeDotColor else inactiveDotColor,
                        shape = CircleShape
                    )
            )
        }
    }
}


















































