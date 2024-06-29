package dong.datn.tourify.screen.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import dong.datn.tourify.model.generateRandomConversionChats
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox

@Composable
fun ChatScreen(nav:NavController,viewModel: AppViewModel) {
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
                    ConversionItem(it) {
                        nav.navigationTo(ClientScreen.ChatScreen.route)
                        viewModel.currentChat.value = it
                    }
                }
            }
        }
    }
}