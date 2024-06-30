package dong.datn.tourify.screen.client

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.model.Chat
import dong.datn.tourify.model.generateChatData
import dong.datn.tourify.model.generateRandomConversionChats
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.Space
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.datn.tourify.widget.onLongClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(nav: NavController, viewModel: AppViewModel) {
    val context = LocalContext.current
    val listConverChat = remember {
        mutableStateOf(generateChatData(30))
    }
    val chatCurrent = remember {
        mutableStateOf(viewModel.currentChat.value!!)
    }
    val contentChat= remember {
        mutableStateOf("")
    }
    viewModel.isKeyboardVisible.value = true
    val scrollState =
        rememberLazyListState(initialFirstVisibleItemIndex = listConverChat.value.size)
    ViewParent(onBack = {
        nav.navigationTo(ClientScreen.ConversionScreen.route)
    }) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    nav.navigationTo(ClientScreen.ConversionScreen.route)
                }
                Space(w = 8)
                RoundedImage(
                    data = chatCurrent.value.tourImage,
                    Modifier.size(40.dp),
                    shape = RoundedCornerShape(30.dp)
                )
                Space(w = 12)
                TextView(
                    chatCurrent.value.tourName,
                    Modifier.weight(1f),
                    textSize = 16,
                    maxLine = 1,
                    color = appColor,
                    font = Font(R.font.poppins_semibold),
                    textAlign = TextAlign.Start
                )

                IconView(modifier = Modifier, icon = Icons.Rounded.Info) {
                    nav.navigationTo(ClientScreen.ConversionScreen.route)
                }

            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )
            Space(h = 6)
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(listConverChat.value) {
                    ItemMessageChat(it)
                    Space(h = 6)
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(60.dp)
            ) {

            }


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
                    textSize = 15
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
                    textSize = 13
                )
            }
        }

    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            RoundedImage(
                data = it.staffImage, Modifier.size(32.dp), shape = RoundedCornerShape(16.dp)
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
                        textSize = 15
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
                        textSize = 13
                    )
                }
            }
        }
    }
}

@Composable
private fun LazyListState.isAtBottom(): Boolean {

    return remember(this) {
        derivedStateOf {
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount && lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }.value
}