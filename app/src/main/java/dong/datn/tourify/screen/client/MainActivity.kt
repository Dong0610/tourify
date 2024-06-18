package dong.datn.tourify.screen.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
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
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.screen.start.LanguageScreen
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.widget.animComposable

sealed class ClientScreen(var route: String) {
    data object HomeClientScreen : ClientScreen("home_client")

}


class MainActivity : ComponentActivity() {
    private lateinit var viewModels: AppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })

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

    open fun onBack() {}

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
            startDestination = ClientScreen.HomeClientScreen.route
        ) {
            animComposable(ClientScreen.HomeClientScreen.route) {
                LanguageScreen(navController, viewModels){

                }
            }


        }

    }
}
