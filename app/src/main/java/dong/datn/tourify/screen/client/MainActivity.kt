package dong.datn.tourify.screen.client

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dong.datn.tourify.R
import dong.datn.tourify.app.ContextProvider.Companion.viewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.isShowTrailer
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.merchant.api.CreateOrder
import dong.datn.tourify.screen.view.BillBooking
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.navigationBar
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.utils.changeTheme
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.BottomNavigationBar
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.animComposable
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
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
    data object ChatScreen : ClientScreen("chat_screen")
    data object UpdatePasswordScreen : ClientScreen("update_password_screen")
    data object BillBooking : ClientScreen("bill_booking_client")
}


open class MainActivity : ComponentActivity() {
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

    fun payment(totalPrice:String){
        val orderApi = CreateOrder()
        showToast("Create order")
        val token = mutableStateOf("")
        try {
            val data: JSONObject = orderApi.createOrder(totalPrice)
            val code = data.getString("returncode")
            Toast.makeText(applicationContext, "return_code: $code", Toast.LENGTH_LONG).show()
            if (code == "1") {
                token.value=(data.getString("zptranstoken"))
            }
            token.value = data.getString("zptranstoken")
        } catch (e: Exception) {
            showToast("Error: "+e.message.toString())
            e.printStackTrace()
        }
        if(token.value!=""){
            showToast(token.value)
            ZaloPaySDK.getInstance()
                .payOrder(this@MainActivity, token.value, "demode://app", object : PayOrderListener {
                    override fun onPaymentSucceeded(
                        transactionId: String?,
                        transToken: String?,
                        appTransID: String?
                    ) {
                        runOnUiThread {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Payment Success")
                                .setMessage(
                                    String.format(
                                        "TransactionId: %s - TransToken: %s",
                                        transactionId,
                                        transToken
                                    )
                                )
                                .setPositiveButton(
                                    "OK"
                                ) { dialog, which -> }
                                .setNegativeButton("Cancel", null).show()
                        }

                    }

                    override fun onPaymentCanceled(zpTransToken: String?, appTransID: String?) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("User Cancel Payment")
                            .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                            .setPositiveButton(
                                "OK"
                            ) { dialog, which -> }
                            .setNegativeButton("Cancel", null).show()
                    }

                    override fun onPaymentError(
                        zaloPayError: ZaloPayError,
                        zpTransToken: String?,
                        appTransID: String?
                    ) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Payment Fail")
                            .setMessage(
                                java.lang.String.format(
                                    "ZaloPayErrorCode: %s \nTransToken: %s",
                                    zaloPayError.toString(),
                                    zpTransToken
                                )
                            )
                            .setPositiveButton(
                                "OK"
                            ) { dialog, which -> }
                            .setNegativeButton("Cancel", null).show()
                    }
                })
        }


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

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ZaloPaySDK.init(553,Environment.SANDBOX );

       // payment("1000")
        changeTheme(currentTheme,applicationContext)
        setContent {
            LaunchedEffect(key1 = " listNotifications.value.size") {
                viewModels.listenerNotification()

                Firestore.getListData<Tour>(Firebase.firestore.collection("$TOUR")) {
                    viewModel.listTour.value = it ?: mutableListOf()
                }
            }

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
            if (isShowTrailer) {
                TrailerImageDialog()
            }

        }

    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun TrailerImageDialog() {
        val showDialog = remember { mutableStateOf(true) }

        if (showDialog.value) {
            Dialog(onDismissRequest = { showDialog.value = false }) {

                TrailerImageContent(
                    onDismiss = {
                        showDialog.value = false
                        isShowTrailer = false
                    }
                )

            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun TrailerImageContent(onDismiss: () -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        val listImage =
            listOf(
                R.drawable.trailer_img_1,
                R.drawable.trailer_img_2,
                R.drawable.trailer_img_3,
                R.drawable.trailer_img_4,
                R.drawable.trailer_img_5
            )
        val pagerState = rememberPagerState(
            pageCount = listImage.size,
            initialOffscreenLimit = 2,
            infiniteLoop = true,
            initialPage = 0,
        )
        LaunchedEffect(Unit) {
            while (true) {
                delay(3000)
                coroutineScope.launch {
                    val nextPage = (pagerState.currentPage + 1) % listImage.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }

        val indexSlideImage = remember {
            mutableStateOf(1)
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(modifier = Modifier, state = pagerState) { page ->
                RoundedImage(
                    listImage.get(page),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.heightPercent(80f),
                    shape = RoundedCornerShape(6.dp)
                )
                indexSlideImage.value = page
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(), horizontalArrangement = Arrangement.End
        ) {
            Box(
                Modifier
                    .widthPercent(40f)
                    .height(40.dp)
                    .background(white, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                TextView(
                    text = getString(R.string.continues),
                    Modifier,
                    color = black,
                    font = Font(R.font.poppins_semibold)
                )
                Box(modifier = Modifier
                    .matchParentSize()
                    .onClick { onDismiss.invoke() })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

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
                    animComposable(ClientScreen.BillBooking.route) {
                        BillBooking(navController)
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
