package dong.datn.tourify.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.model.ConversionChat
import dong.datn.tourify.model.generateRandomConversionChats
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox


@Composable
fun ConversionScreen(nav: NavController, viewModel: AppViewModel) {
    val context = LocalContext.current
    val listConverChat = remember {
        mutableStateOf(generateRandomConversionChats(8))
    }
    ViewParent(onBack = {
        nav.navigationTo(ClientScreen.ProfileScreen.route)
    }) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    nav.navigationTo(ClientScreen.ProfileScreen.route)
                }

                TextView(
                    context.getString(R.string.booking_now),
                    Modifier.weight(1f),
                    textSize = 20,
                    appColor,
                    font = Font(R.font.poppins_semibold),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(50.dp))
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )
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
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn {
                items(listConverChat.value) {

                }
            }
        }
    }
}

@Composable
fun ConversionItem(it: ConversionChat, callback: (ConversionChat) -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)

            .background(
                color = transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .onClick {

                callback.invoke(it)
            }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ConversionIcon(
                data = R.drawable.ic_notifications_active,
                Modifier
                    .padding(vertical = 2.dp)
                    .size(46.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                TextView(
                    text = it.tourName,
                    color = textColor(context),
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(R.font.poppins_semibold)
                )
                Spacer(modifier = Modifier.height(1.dp))
                TextView(
                    text = it.lastMessageSender,
                    color = if (currentTheme == 1) {
                        if (it.lastMessageStaffRead) darkGray else black.opacity(0.5f)
                    } else {
                        if (it.lastMessageStaffRead) lightGrey else darkGray.opacity(0.5f)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(if (it.lastMessageStaffRead) R.font.poppins_regular else R.font.poppins_semibold),
                    textSize = 14
                )
                Spacer(modifier = Modifier.height(1.dp))
                TextView(
                    text = it.lastMessageTime,
                    color = if (currentTheme == 1) darkGray else lightGrey,
                    modifier = Modifier.fillMaxWidth(),
                    font = Font(R.font.poppin_light),
                    textSize = 12, textAlign = TextAlign.End
                )
            }
        }
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