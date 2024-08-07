package dong.datn.tourify.screen.client

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.Reply
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.smarttoolfactory.ratingbar.model.GestureStrategy
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Comment
import dong.datn.tourify.model.CommentType
import dong.datn.tourify.model.Reply
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.boxColor
import dong.datn.tourify.ui.theme.formatRelativeTime
import dong.datn.tourify.ui.theme.gold
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.COMMENT
import dong.datn.tourify.utils.CommonProgressBar
import dong.datn.tourify.utils.SALES
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.widget.ButtonNext
import dong.datn.tourify.widget.DotIndicator
import dong.datn.tourify.widget.InnerImageIcon
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.datn.tourify.widget.ratting.RatingBar
import dong.duan.livechat.utility.formats
import dong.duan.livechat.widget.InputValue
import dong.duan.travelapp.model.DetailSchedule
import dong.duan.travelapp.model.Schedule
import dong.duan.travelapp.model.Service
import dong.duan.travelapp.model.Tour
import dong.duan.travelapp.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun salePriceByTour(tour: Tour): Double {
    var discountedPrice = 0.0

    val path = "$SALES/${tour.saleId}"
    try {
        val percent = withContext(Dispatchers.IO) {
            Firestore.getValue<Double>(path, "percent")
        }
        percent?.let {
            discountedPrice = tour.tourPrice * (100 - it) / 100
        } ?: run {
            discountedPrice = tour.tourPrice
        }
    } catch (e: Exception) {
        e.printStackTrace()
        discountedPrice = tour.tourPrice
    }
    return discountedPrice
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun DetailTourScreen(nav: NavController, viewModel: AppViewModel, router: String) {
    val tour = viewModel.detailTour.value!!
    val context = LocalContext.current
   
    val pagerState = rememberPagerState(
        pageCount = tour.tourImage.size,
        initialOffscreenLimit = 2,
        infiniteLoop = true,
        initialPage = 0,
    )

    val detailState = rememberPagerState(
        pageCount = 3,
        initialOffscreenLimit = 2,
        infiniteLoop = true,
        initialPage = 0,
    )

    val coroutineScope = rememberCoroutineScope()
    val saleTourPrice = remember {
        mutableStateOf(0.0)
    }
    LaunchedEffect(pagerState) {
        coroutineScope.launch {
            saleTourPrice.value = salePriceByTour(tour)
        }
    }

    Box(Modifier.fillMaxSize(1f), contentAlignment = Alignment.BottomEnd) {
        ViewParent(onBack = {
            if (viewModel.prevScreen.value != "") {
                nav.navigate(viewModel.prevScreen.value) {
                    nav.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            } else {
                nav.navigate(router) {
                    nav.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

        }) {
            VerScrollView {
                Column(
                    Modifier.fillMaxSize()

                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(1f)
                            .heightPercent(36f)
                    ) {

                        HorizontalPager(
                            state = pagerState
                        ) { index ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                RoundedImage(
                                    tour.tourImage.get(index),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier,
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth(1f)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InnerImageIcon(
                                modifier = Modifier,
                                icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft
                            ) {
                                if (viewModel.prevScreen.value != "") {
                                    nav.navigate(viewModel.prevScreen.value) {
                                        nav.graph.startDestinationRoute?.let { route ->
                                            popUpTo(route) { saveState = true }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else {
                                    nav.navigate(router) {
                                        nav.graph.startDestinationRoute?.let { route ->
                                            popUpTo(route) { saveState = true }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }

                            TextView(
                                context.getString(R.string.detail),
                                Modifier.weight(1f),
                                textSize = 20,
                                textColor(LocalContext.current),
                                font = Font(R.font.poppins_semibold),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.width(50.dp))
                        }
                    }
                    Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                        DotIndicator(pagerState)
                    }
                    TextView(
                        text = tour.tourName,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        textSize = 18,
                        color = textColor(context),
                        font = Font(R.font.poppins_semibold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            TextView(
                                text = saleTourPrice.value.toCurrency(),
                                modifier = Modifier,
                                textSize = 18,
                                color = if (currentTheme == 1) red else white,
                                font = Font(R.font.poppins_medium)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            TextView(
                                text = tour.tourPrice.toCurrency(),
                                modifier = Modifier.drawBehind {
                                    val strokeWidthPx = 1.dp.toPx()
                                    val verticalOffset = size.height / 2
                                    drawLine(
                                        color = gray,
                                        strokeWidth = strokeWidthPx,
                                        start = Offset(0f, verticalOffset),
                                        end = Offset(size.width, verticalOffset)
                                    )
                                },
                                textSize = 18,
                                color = gray,
                                font = Font(R.font.poppins_medium)
                            )
                        }
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Star",
                                tint = if (currentTheme == 1) gold else white
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            TextView(
                                text = tour.star.formats().toString(),
                                modifier = Modifier,
                                textSize = 18,
                                color = gray,
                                font = Font(R.font.poppins_medium)
                            )
                        }
                    }
                    TextView(
                        text = tour.tourDescription,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textSize = 14,
                        color = textColor(context),
                        font = Font(R.font.poppins_regular)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ButtonNext(
                        text = context.getString(R.string.booking_now),
                        isEnable = true,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        nav.navigationTo(ClientScreen.BookingNowScreen.route)
                        viewModel.bookingTourNow.value = tour
                    }


                    Column(
                        Modifier
                            .fillMaxWidth()
                            .heightPercent(108f)
                            .padding(16.dp)
                    ) {
                        CustomTabLayout(
                            mutableListOf(
                                context.getString(R.string.service),
                                context.getString(R.string.schedule),
                                context.getString(R.string.review)
                            ), detailState
                        ) {
                            coroutineScope.launch {
                                detailState.scrollToPage(it)
                            }

                        }
                        HorizontalPager(
                            modifier = Modifier.fillMaxSize(), state = detailState
                        ) { index ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (index == 0) {
                                    LoadService(tour)
                                } else if (index == 1) {
                                    LoadSchedule(tour)
                                } else {
                                    LoadReview(tour)
                                }
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .size(80.dp)
        ) {
            Box(modifier = Modifier
                .size(54.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF02FF9A), Color(0xFF0622BD)
                        )
                    ), shape = CircleShape
                ), contentAlignment = Alignment.Center) {
                FaIcon(faIcon = FaIcons.FacebookMessenger, tint = white)
                Box(modifier = Modifier
                    .matchParentSize()
                    .onClick {
                        viewModel.gotoChatByTour(tour, nav)
                    })
            }
        }
    }

}

@Composable
fun LoadReview(tour: Tour) {
    val isLoading = remember {
        mutableStateOf(true)
    }
    val listComments = remember {
        mutableStateOf(mutableListOf<Comment>())
    }
    LaunchedEffect("LoadingComments") {
        val dbReference = viewModels.realtime.getReference(COMMENT).child(tour.tourID)
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = mutableListOf<Comment>()
                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.getValue(Comment::class.java)
                    comment?.let { comments.add(it) }
                }
                listComments.value = comments
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            this.items(listComments.value) { comments ->
                ItemComment(comment = comments)
                        Spacer(modifier = Modifier.height(6.dp))
                if (comments.response.size != 0) {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(start = 32.dp)
                            ) {
                                comments.response.forEach { data ->
                                    ItemReply(data, comments)
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            }
                        }
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        if (!isLoading.value) {
            CommonProgressBar()
        }

    }
}

@Composable
fun ItemReply(reply: Reply, comments: Comment) {
    val userComment = remember {
        mutableStateOf<Users?>(null)
    }
    val context = LocalContext.current
    viewModels.getUserComment(reply.uId) {
        userComment.value = it!!
    }
    val isReplyState = remember {
        mutableStateOf(false)
    }

    Row(Modifier.fillMaxSize()) {
        if (userComment.value != null) {
            RoundedImage(
                data = if (userComment.value!!.Image == "") R.drawable.img_user else userComment.value?.Image,
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                Modifier
                    .fillMaxWidth(1f)

            ) {
                Column(
                    Modifier
                        .fillMaxWidth(1f)
                        .background(
                            color = if (currentTheme == 1) whiteSmoke else iconBackground,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    TextView(
                        text = userComment.value?.Name.toString(), modifier = Modifier,
                        color = textColor(context), font = Font(R.font.poppins_semibold)
                    )
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        TextView(
                            text = formatRelativeTime(reply.timeComment),
                            modifier = Modifier,
                            color = textColor(context),
                            font = Font(R.font.poppin_light),
                            textSize = 13
                        )
                    }
                    TextView(
                        text = reply.content,
                        modifier = Modifier,
                        color = textColor(context),
                        font = Font(R.font.poppins_regular),
                        textSize = 14
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    Modifier
                        .fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        Modifier
                            .wrapContentSize()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        TextView(
                            text = context.getString(R.string.reply),
                            modifier = Modifier,
                            textSize = 14
                        )
                        Box(modifier = Modifier
                            .matchParentSize()
                            .onClick { isReplyState.value = true })
                    }

                }

            }
        }
    }
    if (isReplyState.value) {
        BottomSheetReply(comments, userComment.value!!, 1) {
            isReplyState.value = false;
        }
    }
}

@Composable
fun LoadSchedule(tour: Tour) {
    val isLoading = remember {
        mutableStateOf(false)
    }
    val schedule = remember {
        mutableStateOf<Schedule?>(null)
    }
    viewModels.loadScheduleByTour(tour.tourID) {
        isLoading.value = true
        schedule.value = it
    }

    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (schedule.value != null) {
            VerScrollView(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {

                    TextView(
                        text = context.getString(R.string.detailed_schedule),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        color = if (currentTheme == 1) appColor else white,
                        textAlign = TextAlign.Start,
                        font = Font(R.font.poppins_semibold),
                        textSize = 16
                    )
                    schedule.value?.ListTime!!.forEach { it ->
                        TextView(
                            text = "  * " + it.Time + ":",
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            color = textColor(context),
                            textAlign = TextAlign.Start,
                            font = Font(R.font.poppins_semibold),
                            textSize = 16
                        )
                        it.ListDetail.forEach { data ->
                            StyledText(data = data)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                TextView(
                    text = if (isLoading.value) {
                        if (schedule.value == null) {
                            LocalContext.current.getString(R.string.cause_error_try_again)
                        } else {
                            ""
                        }
                    } else "",
                    modifier = Modifier,
                    color = if (currentTheme == 1) red else white,
                    textAlign = TextAlign.Center
                )
            }
        }
        if (!isLoading.value) {
            CommonProgressBar()
        }

    }
}

@Composable
fun ItemComment(comment: Comment) {
    val userComment = remember {
        mutableStateOf<Users?>(null)
    }
    val context = LocalContext.current
    viewModels.getUserComment(comment.uId) {
        userComment.value = it!!
    }
    val isReplyState = remember {
        mutableStateOf(false)
    }

    Row(Modifier.fillMaxSize()) {
        if (userComment.value != null) {
            RoundedImage(
                data = if (userComment.value!!.Image == "") R.drawable.img_user else userComment.value?.Image,
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                Modifier
                    .fillMaxWidth(1f)

            ) {
                Column(
                    Modifier
                        .fillMaxWidth(1f)
                        .background(
                            color = if (currentTheme == 1) whiteSmoke else iconBackground,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    TextView(
                        text = userComment.value?.Name.toString(), modifier = Modifier,
                        color = textColor(context), font = Font(R.font.poppins_semibold)
                    )
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        if (comment.commentType == CommentType.COMMENT) {
                            RatingBar(
                                rating = comment.ratting,
                                tintFilled = gold,
                                tintEmpty = gold,
                                itemSize = 13.dp,
                                gestureStrategy = GestureStrategy.None,
                                painterEmpty = painterResource(id = R.drawable.star_background),
                                painterFilled = painterResource(id = R.drawable.star_foreground)
                            ) {
                            }
                        }


                        Spacer(modifier = Modifier.width(12.dp))
                        TextView(
                            text = formatRelativeTime(comment.timeComment),
                            modifier = Modifier,
                            color = textColor(context),
                            font = Font(R.font.poppin_light),
                            textSize = 13
                        )
                    }
                    TextView(
                        text = comment.content,
                        modifier = Modifier,
                        color = textColor(context),
                        font = Font(R.font.poppins_regular),
                        textSize = 14
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (comment.listImage.size != 0) {

                    LazyRow(
                        Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        items(comment.listImage) {
                            RoundedImage(
                                data = it,
                                Modifier
                                    .width(80.dp)
                                    .fillMaxHeight(1f)
                                    .border(1.dp, appColor, shape = RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                shape = RoundedCornerShape(8.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))

                        }
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        Modifier
                            .wrapContentSize()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        TextView(
                            text = context.getString(R.string.reply),
                            modifier = Modifier,
                            textSize = 14
                        )
                        Box(modifier = Modifier
                            .matchParentSize()
                            .onClick { isReplyState.value = true })
                    }

                }

            }
        }
    }
    if (isReplyState.value) {
        BottomSheetReply(comment, userComment.value!!, 1) {
            isReplyState.value = false;
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetReply(
    comment: Comment,
    userComment: Users,
    level: Int,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val replyValue = remember {
        mutableStateOf("")
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        scrimColor = Color.Black.copy(alpha = .5f),
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(white, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextView(
                LocalContext.current.getString(R.string.reply),
                textSize = 18,
                font = Font(R.font.poppins_semibold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RoundedImage(
                    data = if (userComment.Image == "") R.drawable.img_user else userComment.Image,
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                )
                TextView(
                    userComment.Name,
                    textSize = 18,

                    font = Font(R.font.poppins_semibold)
                )
            }
            SpaceH(h = 8)
            Row {
                Icon(imageVector = Icons.AutoMirrored.Rounded.Reply, contentDescription = "reply")
                SpaceW(w = 6)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(boxColor(), shape = RoundedCornerShape(24.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextView(
                        comment.content,
                        textSize = 18,
                        font = Font(R.font.poppins_regular)
                    )
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
                        value = replyValue.value,
                        modifier = Modifier.fillMaxWidth(),
                        hint = LocalContext.current.getString(R.string.message)
                    ) {
                        replyValue.value = it
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
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .onClick {
                                if (level == 1) {
                                    viewModels.replyComment(replyValue.value, comment) {
                                        onDismiss()
                                    }
                                } else if (level == 2) {
                                    viewModels.replyReplyComment(
                                        replyValue.value,
                                        comment,
                                        comment.commentId
                                    ) {
                                        comment.response.add(it)
                                        onDismiss()
                                    }
                                }
                            }
                    )
                }
            }
        }
        SpaceH(h = 40)
    }
}

@Composable
fun StyledText(
    data: DetailSchedule,
) {
    val annotatedString = buildAnnotatedString {
        append("- ")
        withStyle(style = SpanStyle(color = if (currentTheme == 1) Color.Red else whiteSmoke)) {
            append(data.Time.toString().replace(" ", ""))
        }
        append(data.Content)
    }

    TextView(
        text = annotatedString.toString(),
        modifier = Modifier
            .padding(top = 4.dp, start = 24.dp)
            .fillMaxWidth(),

        textAlign = TextAlign.Start,
        font = Font(R.font.poppins_regular),
        textSize = 16
    )
}


@Composable
fun LoadService(tour: Tour) {
    val isLoading = remember {
        mutableStateOf(false)
    }
    val service = remember {
        mutableStateOf<Service?>(null)
    }
    viewModels.loadServiceByTour(tour.tourID) {
        isLoading.value = true
        service.value = it
    }

    val context = LocalContext.current


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (service.value != null) {
            VerScrollView(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    TextView(
                        text = service.value?.road.toString(),
                        modifier = Modifier.padding(top = 12.dp),
                        color = if (currentTheme == 1) red else white,
                        textAlign = TextAlign.Center,
                        font = Font(R.font.poppins_medium),
                        textSize = 18
                    )

                    TextView(
                        text = "( ${context.getString(R.string.time)}: ${service.value?.time} - ${
                            context.getString(
                                R.string.vehicle
                            )
                        }: ${service.value?.vehicle} )",
                        modifier = Modifier.padding(top = 4.dp),
                        color = textColor(context = LocalContext.current),
                        textAlign = TextAlign.Center,
                        font = Font(R.font.poppins_regular),
                        textSize = 16
                    )
                    TextView(
                        text = service.value?.introduce.toString(),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        color = textColor(context = LocalContext.current),
                        textAlign = TextAlign.Start,
                        font = Font(R.font.poppins_regular),
                        textSize = 16
                    )

                    TextView(
                        text = context.getString(R.string.service_includes),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        color = if (currentTheme == 1) appColor else white,
                        textAlign = TextAlign.Start,
                        font = Font(R.font.poppins_semibold),
                        textSize = 16
                    )

                    TextView(
                        text = "  * " + context.getString(R.string.transport) + ":",
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        color = textColor(context),
                        textAlign = TextAlign.Start,
                        font = Font(R.font.poppins_semibold),
                        textSize = 16
                    )
                    service.value?.includeService?.transport!!.forEach {
                        TextView(
                            text = "${it.description}",
                            modifier = Modifier
                                .padding(top = 4.dp, start = 24.dp)
                                .fillMaxWidth(),
                            color = textColor(context),
                            textAlign = TextAlign.Start,
                            font = Font(R.font.poppins_regular),
                            textSize = 16
                        )
                    }

                    TextView(
                        text = "  * " + context.getString(R.string.food) + ":",
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        color = textColor(context),
                        textAlign = TextAlign.Start,
                        font = Font(R.font.poppins_semibold),
                        textSize = 16
                    )
                    service.value?.includeService?.food!!.forEach {
                        TextView(
                            text = "${it.description}",
                            modifier = Modifier
                                .padding(top = 4.dp, start = 24.dp)
                                .fillMaxWidth(),
                            color = textColor(context),
                            textAlign = TextAlign.Start,
                            font = Font(R.font.poppins_regular),
                            textSize = 16
                        )
                    }
                    TextView(
                        text = "  * " + context.getString(R.string.other_services) + ":",
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        color = textColor(context),
                        textAlign = TextAlign.Start,
                        font = Font(R.font.poppins_semibold),
                        textSize = 16
                    )
                    service.value?.includeService?.otherServices!!.forEach {
                        TextView(
                            text = "${it.description}",
                            modifier = Modifier
                                .padding(top = 4.dp, start = 24.dp)
                                .fillMaxWidth(),
                            color = textColor(context),
                            textAlign = TextAlign.Start,
                            font = Font(R.font.poppins_regular),
                            textSize = 16
                        )
                    }

                    TextView(
                        text = context.getString(R.string.notes),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        color = if (currentTheme == 1) appColor else white,
                        textAlign = TextAlign.Start,
                        font = Font(R.font.poppins_semibold),
                        textSize = 16
                    )

                    service.value?.noteService?.forEach {
                        TextView(
                            text = "${it.content}",
                            modifier = Modifier
                                .padding(top = 4.dp, start = 24.dp)
                                .fillMaxWidth(),
                            color = textColor(context),
                            textAlign = TextAlign.Start,
                            font = Font(R.font.poppins_regular),
                            textSize = 16
                        )
                    }

                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                TextView(
                    text = if (isLoading.value) {
                        if (service.value == null) {
                            LocalContext.current.getString(R.string.cause_error_try_again)
                        } else {
                            ""
                        }
                    } else "",
                    modifier = Modifier,
                    color = if (currentTheme == 1) red else white,
                    textAlign = TextAlign.Center
                )
            }
        }
        if (!isLoading.value) {
            CommonProgressBar()
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CustomTabLayout(
    listTitle: MutableList<String>, pagerState: PagerState, onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .height(36.dp)
    ) {
        for (i in 0 until listTitle.count()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextView(
                        text = listTitle[i],
                        color = if (pagerState.currentPage == i) appColor else Color.Gray,
                        textSize = 16,
                        maxLine = 1,
                        font = Font(R.font.poppins_medium),
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                            .fillMaxWidth(1f),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .padding(horizontal = 6.dp)

                            .fillMaxWidth()
                            .background(
                                if (pagerState.currentPage == i) appColor else Color.Transparent,
                                shape = RoundedCornerShape(2.dp, 2.dp, 0.dp, 0.dp)
                            )
                    )

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .onClick { onClick.invoke(i) })
            }
            Spacer(modifier = Modifier.width(3.dp))
        }
    }
}