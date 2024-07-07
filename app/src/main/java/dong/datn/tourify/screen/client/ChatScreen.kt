package dong.datn.tourify.screen.client

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.lastChatTour
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Chat
import dong.datn.tourify.model.ChatType
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.boxColor
import dong.datn.tourify.ui.theme.colorByTheme
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.Space
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.datn.tourify.widget.onLongClick
import dong.duan.livechat.widget.InputValue
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
fun ChatScreen(nav: NavController, viewModel: AppViewModel) {
    val context = LocalContext.current
    val listChatCurrent = viewModel.listChatCurrent
    val tourData = remember { mutableStateOf<Tour?>(null) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(viewModel.currentChat.value) {
        viewModel.startListeningForNewChats()

        if (!viewModel.lastTourIdByChat.value.isNullOrEmpty()) {
            Firestore.fetchById<Tour>("$TOUR/${viewModel.lastTourIdByChat.value}") {
                tourData.value = it
            }
        }
    }
    val lazyColumnListState = rememberLazyListState()
    LaunchedEffect(listChatCurrent.value.size) {
        coroutineScope.launch {
            if (listChatCurrent.value.isNotEmpty()) {
                lazyColumnListState.animateScrollToItem(listChatCurrent.value.lastIndex + 4)
            }
        }
    }
    val lastIdTour = remember {
        mutableStateOf("")
    }
    val contentChat = remember { mutableStateOf("") }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val bottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()


    ViewParent(Modifier.fillMaxSize(), onBack = {
        nav.navigationTo(ClientScreen.HomeClientScreen.route)
        viewModel.resetCurrentChat()
    }, propagateMinConstraints = false) {
            Column(
                Modifier
                    .matchParentSize()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                        nav.navigationTo(ClientScreen.HomeClientScreen.route)
                        viewModel.resetCurrentChat()
                    }
                    Space(w = 8)
                    RoundedImage(
                        data = if (lastChatTour == null) R.drawable.img_test_data_2 else lastChatTour!!.tourImage.get(
                            0
                        ),
                        Modifier.size(40.dp),
                        shape = RoundedCornerShape(30.dp)
                    )
                    Space(w = 12)
                    TextView(
                        if (lastChatTour == null) "Null" else lastChatTour!!.tourName,
                        Modifier.weight(1f),
                        textSize = 16,
                        maxLine = 1,
                        color = appColor,
                        font = Font(R.font.poppins_semibold),
                        textAlign = TextAlign.Start
                    )
                    Space(w = 40)
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0xFFdddddd), shape = RectangleShape)
                )
                Space(h = 6)

                val verScrollViewHeight = if (keyboardAsState().value) {
                    screenHeight - bottomPadding - 70.dp - 20.dp
                } else {
                    screenHeight - 70.dp - 20.dp
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(verScrollViewHeight),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    LazyColumn(
                        Modifier.wrapContentHeight(),
                        state = lazyColumnListState, verticalArrangement = Arrangement.Bottom
                    ) {
                        this.itemsIndexed(listChatCurrent.value) { index, it ->
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
                                            textSize = 14, textAlign = TextAlign.Center
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
                                        lastIdTour.value = it.tourID
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
                }


                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(70.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .weight(1f)
                            .background(color = boxColor(), shape = RoundedCornerShape(40.dp))
                    ) {
                        InputValue(
                            value = contentChat.value,
                            modifier = Modifier.fillMaxWidth(),
                            hint = context.getString(R.string.message)
                        ) {
                            contentChat.value = it
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(boxColor(), CircleShape)
                            .size(42.dp), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Send,
                            contentDescription = "Send",
                            tint = appColor,
                            modifier = Modifier.rotate(-25f)
                        )
                        Box(modifier = Modifier
                            .matchParentSize()
                            .onClick {
                                viewModel.sendMessage(contentChat.value, lastIdTour.value) {
                                    coroutineScope.launch {
                                        if (listChatCurrent.value.isNotEmpty()) {
                                            lazyColumnListState.animateScrollToItem(
                                                listChatCurrent.value.lastIndex
                                            )
                                        }
                                    }
                                }
                            })
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
                .wrapContentHeight(), contentAlignment = Alignment.CenterEnd
        ) {

            Column(
                modifier = Modifier
                    .widthPercent(55f)
                    .padding(12.dp)
                    .background(boxColor(), shape = RoundedCornerShape(12.dp)),
                horizontalAlignment = Alignment.End
            ) {

                RoundedImage(
                    tourData.value!!.tourImage.get(0),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .heightPercent(18f)
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 6.dp)
                        .fillMaxWidth()
                        .fillMaxSize(),

                    )

                TextView(
                    text = tourData.value!!.tourName ?: "Error",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Start,
                    font = Font(R.font.poppins_medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextView(
                    text = (tourData.value!!.tourDescription ?: "Error") + "...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Start,
                    maxLine = 1,
                    textSize = 14,
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
                    text = it.content.trim(),
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
