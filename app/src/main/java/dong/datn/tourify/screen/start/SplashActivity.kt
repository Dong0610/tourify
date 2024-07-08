package dong.datn.tourify.screen.start

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.facebook.AccessToken
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.ContextProvider.Companion.viewModel
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.screen.client.MainActivity
import dong.datn.tourify.screen.staff.StaffActivity
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.USERS
import dong.datn.tourify.utils.heightPercent
import dong.duan.travelapp.model.Tour
import dong.duan.travelapp.model.Users
import kotlinx.coroutines.delay
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (authSignIn != null) {
            Firestore.fetchById<Users>(
                Firebase.firestore.collection(USERS).document(authSignIn!!.UId)
            ) {
                authSignIn = it
            }

        }

        val accessToken = AccessToken.getCurrentAccessToken()
        Log.d("ACC", "Access: $accessToken")
        try {
            val info: PackageInfo = packageManager.getPackageInfo(
                "dong.datn.tourify", PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        setContent {
            TourifyTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = appColor,
                    )
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen(innerPadding) {
                        if (authSignIn == null) {
                            startActivity(Intent(this, AccountActivity::class.java))
                            finish()
                        } else {
                            if (authSignIn!!.Role == "Staff") {
                                startActivity(Intent(this, StaffActivity::class.java))
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


    }


    @Composable
    fun SplashScreen(innerPadding: PaddingValues, onBack: () -> Unit) {

        Box(
            modifier = Modifier
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