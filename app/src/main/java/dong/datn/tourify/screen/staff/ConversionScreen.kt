package dong.datn.tourify.screen.staff

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Chat
import dong.datn.tourify.model.ChatType
import dong.datn.tourify.model.ConversionChat
import dong.datn.tourify.screen.client.ClientScreen
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.boxColor
import dong.datn.tourify.ui.theme.colorByTheme
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.Space
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.datn.tourify.widget.onLongClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.InputValue
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState", "CoroutineCreationDuringComposition")
@Composable
fun ConversionScreen(nav: NavController, viewModel: AppViewModel, drawerState: () -> Unit) {
    val context = LocalContext.current

    val currentConversionChat = remember {
        mutableStateOf<ConversionChat?>(null)
    }
    val lastIdTour = remember {
        mutableStateOf("")
    }
    val contentChat = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val listMessage = viewModel.listChatCurrent
    val lazyColumnListState = rememberLazyListState()
    val isChangeInfoTour = remember {
        mutableStateOf(false)
    }
    if (isChangeInfoTour.value == true) {
        if (currentConversionChat.value != null) {
            LaunchedEffect(key1 = "Listen Chat ") {
                viewModel.startListeningForNewChatsByConverId(currentConversionChat.value!!.converId)
                snapshotFlow { listMessage.value.size }
                    .distinctUntilChanged()
                    .collectLatest {
                        coroutineScope.launch {
                            if (listMessage.value.isNotEmpty()) {
                                lazyColumnListState.animateScrollToItem(0)
                            }
                        }
                    }
            }
        }
    }




    ViewParent(Modifier.fillMaxSize(), onBack = {

    }) {
        Row(
            Modifier
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .weight(3f)
                    .padding(horizontal = 4.dp)
                    .fillMaxHeight()

            ) {

                ConversionLeft(viewModel, drawerState) {
                    currentConversionChat.value = it
                    isChangeInfoTour.value = true
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(whiteSmoke)
            )
            Column(
                Modifier
                    .weight(5f)
                    .padding(horizontal = 6.dp)
                    .fillMaxHeight()
            ) {

                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = lazyColumnListState,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    this.itemsIndexed(listMessage.value) { index, it ->
                        if (it.chatType == ChatType.FIRST_MESSAGE) {
                            if (index != 0) {
                                Space(h = 12)
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    TextView(
                                        text = it.time,
                                        modifier = Modifier,
                                        font = Font(R.font.poppins_regular),
                                        color = colorByTheme(darkGray, whiteSmoke),
                                        textSize = 11, textAlign = TextAlign.Center
                                    )
                                }
                            }
                            InformationAboutTour(it.tourId) {
                                it?.let { tour ->
                                    viewModel.detailTour.value = tour
                                    nav.navigationTo(
                                        ClientScreen.DetailTourScreen.route,
                                        ClientScreen.ChatScreen.route
                                    )
                                }
                            }
                            Space(h = 6)
                            ItemMessageChat(it)
                        } else {
                            ItemMessageChat(it)
                        }
                        Space(h = 6)
                    }

                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(50.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .weight(1f)
                            .background(color = boxColor(), shape = RoundedCornerShape(40.dp))
                    ) {
                        InputValue(
                            value = contentChat.value,
                            textSize = 13.sp,
                            modifier = Modifier.fillMaxWidth(),
                            hint = context.getString(R.string.message)
                        ) {
                            contentChat.value = it.trim()
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(boxColor(), CircleShape)
                            .size(36.dp), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Send,
                            contentDescription = "Send",
                            tint = appColor,
                            modifier = Modifier
                                .rotate(-25f)
                                .size(20.dp)
                        )
                        Box(modifier = Modifier
                            .matchParentSize()
                            .onClick {
                                viewModel.sendMessage(
                                    contentChat.value,
                                    lastIdTour.value,
                                    currentConversionChat.value!!.converId
                                ) {
                                    contentChat.value = ""
                                    coroutineScope.launch {
                                        if (listMessage.value.isNotEmpty()) {
                                            lazyColumnListState.animateScrollToItem(listMessage.value.lastIndex - 1)
                                        }
                                    }
                                }
                            })
                    }
                }
                
            }
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(whiteSmoke)
            )
            Column(
                Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {

            }
        }
    }
}

@Composable
fun ConversionLeft(
    viewModel: AppViewModel,
    drawerState: () -> Unit,
    onTouchState: (ConversionChat) -> Unit
) {
    val context = LocalContext.current
    val listConversionChat = viewModel.listConversations
    LaunchedEffect(key1 = "listConversionChat") {
        viewModel.listenerCurrentChat()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(end = 12.dp)
    ) {
        Row(
            Modifier
                .background(if (currentTheme == 1) white else black)
                .fillMaxWidth()
                .height(50.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.wrapContentSize()) {
                IconView(
                    modifier = Modifier.size(34.dp),
                    icSize = 20,
                    tint = colorByTheme(appColor, white),
                    icon = Icons.Rounded.Menu
                )
                Box(modifier = Modifier
                    .matchParentSize()
                    .onClick {
                        drawerState.invoke()
                    })
            }
            Space(w = 6)
            TextView(
                text = context.getString(R.string.current_chat),
                modifier = Modifier, font = Font(R.font.poppins_semibold),
                color = colorByTheme(appColor, white), textSize = 14
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        SearchBoxConver {
            showToast(it)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            this.items(listConversionChat.value) {
                ConversionItem(it = it) {

                    onTouchState.invoke(it)
                }
            }

        }


    }




}


@Composable
fun InformationAboutTour(id: String, callback: (Tour?) -> Unit) {
    val tourData = remember {
        mutableStateOf<Tour?>(null)
    }
    Log.d("InformationAboutTour", "$id")
    Firestore.fetchById<Tour>("$TOUR/${id}") {
        tourData.value = it
    }
    if (tourData.value != null) {
        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(), contentAlignment = Alignment.CenterStart
        ) {

            Column(
                modifier = Modifier
                    .widthPercent(25f)
                    .padding(12.dp)
                    .background(boxColor(), shape = RoundedCornerShape(12.dp)),
                horizontalAlignment = Alignment.Start
            ) {

                RoundedImage(
                    tourData.value!!.tourImage.get(0),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .heightPercent(30f)
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 6.dp)
                        .fillMaxWidth()
                        .fillMaxSize(),

                    )

                TextView(
                    text = tourData.value!!.tourName ?: "Error",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textSize = 13,
                    textAlign = TextAlign.Start,
                    font = Font(R.font.poppins_medium)
                )
                Spacer(modifier = Modifier.height(3.dp))
                TextView(
                    text = (tourData.value!!.tourDescription ?: "Error") + "...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Start,
                    maxLine = 1,
                    textSize = 12,
                    font = Font(R.font.poppin_light)
                )
                Spacer(modifier = Modifier.height(6.dp))

            }

            Box(modifier = Modifier
                .matchParentSize()
                .onClick { callback(tourData.value!!) })
        }
    }

}

@Composable
fun ItemMessageChat(it: Chat) {
    val isShowTime = remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    if (authSignIn!!.UId == it.senderId) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp),
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier.background(
                    color = if (currentTheme == -1) iconBackground else whiteSmoke,
                    shape = RoundedCornerShape(32.dp, 16.dp, 0.dp, 32.dp)
                ), contentAlignment = Alignment.CenterEnd
            ) {
                TextView(
                    text = it.content,
                    modifier = Modifier
                        .padding(
                            start = 12.dp, top = 8.dp, bottom = 8.dp, end = 8.dp
                        )
                        .background(transparent),
                    color = textColor(),
                    textSize = 11
                )
                Box(modifier = Modifier
                    .matchParentSize()
                    .onLongClick {
                        isShowTime.value = true
                        coroutineScope.launch {
                            delay(2000)
                            isShowTime.value = false
                        }
                    })

            }
            if (isShowTime.value) {
                TextView(
                    text = it.time,
                    modifier = Modifier.padding(top = 4.dp),
                    color = if (currentTheme == 1) Color.LightGray else Color.White,
                    textSize = 10
                )
            }
        }

    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom
        ) {
            RoundedImage(
                data = (if (it.clientImage == "") R.drawable.img_user else it.clientImage),
                Modifier.size(32.dp),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Box(
                    modifier = Modifier.background(
                        color = if (currentTheme == -1) iconBackground else whiteSmoke,
                        shape = RoundedCornerShape(16.dp, 32.dp, 32.dp, 0.dp)
                    ), contentAlignment = Alignment.CenterEnd
                ) {
                    TextView(
                        text = it.content,
                        modifier = Modifier
                            .padding(
                                start = 12.dp, top = 8.dp, bottom = 8.dp, end = 8.dp
                            )
                            .background(transparent),
                        color = textColor(),
                        textSize = 11
                    )
                    Box(modifier = Modifier
                        .matchParentSize()
                        .onLongClick {
                            isShowTime.value = true
                            coroutineScope.launch {
                                delay(2000)
                                isShowTime.value = false
                            }
                        })

                }

                if (isShowTime.value) {
                    TextView(
                        text = it.time,
                        modifier = Modifier.padding(top = 4.dp),
                        color = if (currentTheme == 1) Color.LightGray else Color.White,
                        textSize = 10
                    )
                }
            }
        }
    }
}


@Composable
fun SearchBoxConver(
    onTouch: (String) -> Unit
) {
    val context = LocalContext.current

    val valueSearch = remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (currentTheme == 1) whiteSmoke else iconBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(16.dp),
                color = if (currentTheme == 1) transparent else lightGrey
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = valueSearch.value.text,
            onValueChange = { valueSearch.value = TextFieldValue(it) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 3.dp)
                .background(Color.Transparent),
            textStyle = TextStyle(
                color = textColor(context),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium))
            ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (valueSearch.value.text.isEmpty()) {
                        Text(
                            text = context.getString(R.string.search),
                            style = TextStyle(
                                color = darkGray,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
        Image(
            imageVector = Icons.Rounded.Search, contentDescription = "Search",
            Modifier
                .size(20.dp)
                .onClick {
                    onTouch.invoke(valueSearch.value.text)
                }, colorFilter = ColorFilter.tint(textColor(context))
        )
        Spacer(modifier = Modifier.width(12.dp))
    }
}


@Composable
fun ConversionItem(it: ConversionChat, callback: (ConversionChat) -> Unit) {
    val context = LocalContext.current
    var lastChats = remember {
        mutableStateOf(it.lastMessageSender)
    }
    if (lastChats.value.isEmpty()) {
        lastChats.value = it.clientName + ": " + context.getString(R.string.send_a_message)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(
                color = transparent,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedImage(
                data = if (it.clientImage == "") R.drawable.img_user else it.clientImage,
                Modifier.size(36.dp),
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                TextView(
                    text = it.clientName,
                    textSize = 13,
                    color = textColor(context),
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(R.font.poppins_semibold)
                )
                Spacer(modifier = Modifier.height(1.dp))
                TextView(
                    text = lastChats.value,
                    color = if (currentTheme == 1) {
                        if (it.lastMessageStaffRead) darkGray else black.opacity(0.5f)
                    } else {
                        if (it.lastMessageStaffRead) lightGrey else darkGray.opacity(0.5f)
                    },
                    maxLine = 1,
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(if (it.lastMessageStaffRead) R.font.poppins_regular else R.font.poppins_semibold),
                    textSize = 12
                )
                Spacer(modifier = Modifier.height(1.dp))
                TextView(
                    text = it.lastMessageTime,
                    color = if (currentTheme == 1) darkGray else lightGrey,
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(R.font.poppin_light),
                    textSize = 11, textAlign = TextAlign.End
                )
            }
        }
        Box(modifier = Modifier
            .matchParentSize()
            .background(transparent)
            .onClick {
                callback.invoke(it)
            })
    }
}


@Composable
fun ConversionIcon(
    data: Any? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painter,
                contentDescription = "...",
                contentScale = contentScale,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        }

    }
}