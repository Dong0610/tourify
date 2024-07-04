package dong.datn.tourify.screen.client

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dong.datn.tourify.app.appViewModels
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.navigationBar
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.changeTheme
import dong.datn.tourify.widget.BottomNavigationBar
import dong.datn.tourify.widget.animComposable
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

sealed class ClientScreen(var route: String) {
    data object HomeClientScreen : ClientScreen("home_client")
    data object DiscoveryScreen : ClientScreen("discovery_client")
    data object WishlistScreen : ClientScreen("wishlist_client")
    data object NotificationScreen : ClientScreen("notification_client")
    data object ProfileScreen : ClientScreen("profile_client")
    data object UpdateProfileScreen : ClientScreen("update_profile_client")
    data object SettingScreen : ClientScreen("setting_client")
    data object BookingScreen : ClientScreen("booking_client")
    data object DetailTourScreen : ClientScreen("detail_tour_client")
    data object DetailPlaceScreen : ClientScreen("detail_place_client")
    data object BookingNowScreen : ClientScreen("booking_now_client")
   // data object ConversionScreen : ClientScreen("conversion_screen")
    data object ChatScreen : ClientScreen("chat_screen")
    data object UpdatePasswordScreen : ClientScreen("update_password_screen")
}


open class MainActivity : ComponentActivity() {
    val viewModels = appViewModels!!
    private fun getCountry(context: Context): String? {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val geocoder = Geocoder(context, Locale.getDefault())

        for (provider in lm.allProviders) {
            val location: Location? = lm.getLastKnownLocation(provider)
            if (location != null) {
                try {
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        return addresses[0].countryName
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        changeTheme(currentTheme,applicationContext)
        setContent {

            val coroutineScope = rememberCoroutineScope()
            val country = remember { mutableStateOf<String?>(null) }
            val permissionGranted = remember { mutableStateOf(false) }

            val locationPermissionRequest = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                permissionGranted.value = isGranted
                if (isGranted) {
                    coroutineScope.launch {
                        country.value = getCountry(applicationContext)
                    }
                } else {
                    country.value = "Location permission denied"
                }
            }
            if (permissionGranted.value) {
                LaunchedEffect(Unit) {
                    country.value = getCountry(applicationContext)
                }
            } else {
                LaunchedEffect(Unit) {
                    locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
            TourifyTheme {

                MainNavigation(country)

            }
        }


    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MainNavigation(
        country: MutableState<String?>
    ) {
        val systemUiController = rememberSystemUiController()
        val statusBar = if (currentTheme == 1) white else black
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = statusBar,
            )
        }
        val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
        val navController = rememberNavController()
        val keyboardController = LocalSoftwareKeyboardController.current

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = navigationBar(applicationContext)),
            bottomBar = {
                if (bottomBarState.value) {
                    BottomAppBar(containerColor = navigationBar(applicationContext)) {
                        BottomNavigationBar(
                            navController = navController,
                            viewModels, bottomBarState
                        )
                    }
                }

            }, content = { insertPadding ->
                NavHost(
                    modifier = Modifier.padding(insertPadding),
                    navController = navController,
                    startDestination = ClientScreen.HomeClientScreen.route
                ) {
                    animComposable("home_client") {
                        systemUiController.isNavigationBarVisible=false
                        LaunchedEffect(Unit) {
                            bottomBarState.value = true
                            keyboardController?.hide()
                            viewModels.currentIndex.value=0;
                        }
                        HomeClientScreen(nav = navController, viewModel = viewModels, country.value)
                    }
                    animComposable("discovery_client") {
                        LaunchedEffect(Unit) {
                            bottomBarState.value = true
                            keyboardController?.hide()
                            viewModels.currentIndex.value=1
                        }
                        DiscoverScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.WishlistScreen.route) {
                        LaunchedEffect(Unit) {
                            bottomBarState.value = false
                            keyboardController?.hide()
                        }
                        WishListScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.NotificationScreen.route) {
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = true
                            viewModels.currentIndex.value=3
                        }
                        NotificationScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.ProfileScreen.route) {
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = true
                            viewModels.currentIndex.value=4
                        }
                        ProfileScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.UpdateProfileScreen.route) {

                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        UpdateProfileScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.SettingScreen.route) {

                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        SettingScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.BookingScreen.route) {

                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        BookingScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.DetailTourScreen.route) { LaunchedEffect(Unit) {
                        keyboardController?.hide()
                        bottomBarState.value = false
                    }
                        DetailTourScreen(
                            navController,
                            viewModels,
                            ClientScreen.HomeClientScreen.route
                        )

                    }
                    animComposable(ClientScreen.DetailPlaceScreen.route) {

                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        DetailPlaceScreen(
                                navController,
                        viewModels,
                        ClientScreen.HomeClientScreen.route
                        )

                    }
                    animComposable(ClientScreen.BookingNowScreen.route) {

                        LaunchedEffect(Unit) {
                            bottomBarState.value = false
                        }
                        BookingNowScreen(navController, viewModels)
                    }

                    animComposable(ClientScreen.ChatScreen.route) {
                        systemUiController.isNavigationBarVisible=true
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()

                            viewModels.currentIndex.value=2
                            bottomBarState.value = false
                        }
                        ChatScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.UpdatePasswordScreen.route) {

                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        UpdatePassScreen(navController, viewModels)
                    }
                }


            })

    }
}
