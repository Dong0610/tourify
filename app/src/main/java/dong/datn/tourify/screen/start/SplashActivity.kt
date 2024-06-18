package dong.datn.tourify.screen.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dong.datn.tourify.R
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.screen.client.MainActivity
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.heightPercent
import kotlinx.coroutines.delay


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
        setContent {

            TourifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen(innerPadding) {
                        if (authSignIn == null) {
                            startActivity(Intent(this, AccountActivity::class.java))
                            finish()
                        } else {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                    }
                }
            }
        }
    }


    @Composable
    fun SplashScreen(innerPadding: PaddingValues, onBack: () -> Unit) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = appColor,
            )
        }
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(appColor)
        ) {
            Column(Modifier.fillMaxSize(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.heightPercent(25f))
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_logo_app),
                    contentDescription = "Logo app",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(120.dp),
                    colorFilter = ColorFilter.tint(
                        white
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Tourify",
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 24.sp,
                    color = white
                )

                Spacer(modifier = Modifier.weight(1f, true))
                LinearProgressIndicator()
                Spacer(modifier = Modifier.height(32.dp))
            }

        }
        LaunchedEffect(key1 = true) {
            delay(1500)
            onBack()
        }
    }
}