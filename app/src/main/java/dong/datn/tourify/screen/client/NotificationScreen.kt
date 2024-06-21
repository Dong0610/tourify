package dong.datn.tourify.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.model.Notification
import dong.datn.tourify.model.getListNotification
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.azure
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.ghostWhite
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox

@Composable
fun NotificationScreen(navController: NavHostController, viewModels: AppViewModel) {
    val context = LocalContext.current
    val listNotifications = getListNotification()
    ViewParent(onBack = {
        viewModels.currentIndex.value = 2
        navController.navigate(ClientScreen.WishlistScreen.route) {
            popUpTo(0)
        }

    }) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    viewModels.currentIndex.value = 2
                    navController.navigate(ClientScreen.WishlistScreen.route) {
                        popUpTo(0)
                    }
                }
                TextView(
                    context.getString(R.string.notification), Modifier.weight(1f), textSize = 20,
                    appColor, font = Font(R.font.poppins_semibold), textAlign = TextAlign.Center
                )
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    viewModels.currentIndex.value = 2
                    navController.navigate(ClientScreen.WishlistScreen.route) {
                        popUpTo(0)
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )


            val list = (1..10).map { it.toString() }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SearchBox {
                    showToast(it)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(listNotifications, key = { it ->
                        (it as Notification).notiId
                    }) {
                        ItemNotification(it) {

                        }
                    }
                }
            )
        }
    }
}


@Composable
fun NotificationIcon(
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

@Composable
fun ItemNotification(notification: Notification, callback: (Notification) -> Unit) {
    val isReadNoti = remember {
        mutableStateOf(notification.isRead)
    }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)

            .background(
                color = if (isReadNoti.value) ghostWhite else azure,
                shape = RoundedCornerShape(8.dp)
            )
            .onClick {
                if (isReadNoti.value == false) {
                    isReadNoti.value = true
                }
                callback.invoke(notification)
            }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NotificationIcon(
                data = R.drawable.ic_notifications_active,
                Modifier
                    .padding(vertical = 6.dp)
                    .size(46.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                TextView(
                    text = notification.title,
                    color = textColor(context),
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(R.font.poppins_medium)
                )
                Spacer(modifier = Modifier.height(1.dp))
                TextView(
                    text = notification.content,
                    color = if (currentTheme == 1) darkGray else lightGrey,
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(R.font.poppins_regular),
                    textSize = 14
                )
                Spacer(modifier = Modifier.height(1.dp))
                TextView(
                    text = notification.content,
                    color = if (currentTheme == 1) darkGray else lightGrey,
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(R.font.poppin_light),
                    textSize = 12, textAlign = TextAlign.End
                )
            }
        }
    }
}
