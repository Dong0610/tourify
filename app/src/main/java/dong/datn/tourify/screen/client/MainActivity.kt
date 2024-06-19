package dong.datn.tourify.screen.client

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.widget.animComposable
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

sealed class ClientScreen(var route: String) {
    data object HomeClientScreen : ClientScreen("home_client")

}


open class MainActivity : ComponentActivity() {
    private lateinit var viewModels: AppViewModel


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentTheme = 1
        viewModels = ViewModelProvider(this).get(AppViewModel::class.java)
        setContent {

            TourifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                    MainNavigation(innerPadding, country)
                }
            }
        }
    }

    open fun onBack() {}

    @Composable
    fun MainNavigation(innerPadding: PaddingValues, country: MutableState<String?>) {
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
                HomeClientScreen(nav = navController, viewModel = viewModels, country.value)
            }


        }

    }
}
