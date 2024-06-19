package dong.datn.tourify.screen.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.screen.client.MainActivity
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.widget.animComposable


sealed class AccountScreen(var route: String) {
    data object LanguageScreen : AccountScreen("language")
    data object SignUpScreen : AccountScreen("signup")
    data object SignInScreen : AccountScreen("signin")
}

@AndroidEntryPoint
open class AccountActivity : ComponentActivity() {
    private lateinit var viewModels: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentTheme = 1
        viewModels = ViewModelProvider(this).get(AppViewModel::class.java)

        setContent {

            TourifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigation(innerPadding)
                }
            }
        }
    }


    @Composable
    fun MainNavigation(innerPadding: PaddingValues) {
        val navController = rememberNavController()
        val systemUiController = rememberSystemUiController()
        val statusBar = if (currentTheme == 1) white else black
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = statusBar,
            )
        }
        NavHost(
            modifier = Modifier.padding(paddingValues = innerPadding),
            navController = navController,
            startDestination = AccountScreen.LanguageScreen.route
        ) {
            animComposable(AccountScreen.LanguageScreen.route) {

                LanguageScreen(navController) {
                    startActivity(Intent(this@AccountActivity, MainActivity::class.java))
                    finish()
                }
            }
            animComposable(AccountScreen.SignUpScreen.route) {
                SignUpScreen(navController, viewModels)
            }
            animComposable(AccountScreen.SignInScreen.route) {
                SignInScreen(navController, viewModels)

            }


        }
    }
}