package dong.datn.tourify.screen.client

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.model.PaymentMethod
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo

@Composable
fun PaymentMethodScreen(nav: NavController, viewModel: AppViewModel) {
    val context = LocalContext.current
    val htmlFileName = "policy.html"
    ViewParent(onBack = {
        viewModel.currentIndex.value = 3
        nav.navigate(ClientScreen.NotificationScreen.route) {
            popUpTo(0)
        }
    })
    {
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
                    context.getString(R.string.privacy_policy), Modifier.weight(1f), textSize = 20,
                    appColor, font = Font(R.font.poppins_semibold), textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(50.dp))
            }

            VerScrollView(Modifier.weight(1f)) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            // Enable JavaScript if needed
                            settings.javaScriptEnabled = true
                            // Set WebView client
                            webViewClient = WebViewClient()
                            loadUrl("file:///android_res/raw/$htmlFileName")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }


        }
    }
}