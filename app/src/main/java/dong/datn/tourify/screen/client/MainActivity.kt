package dong.datn.tourify.screen.client

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import dong.datn.tourify.app.appLanguageCode
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.isShowTrailer
import dong.datn.tourify.app.language.LanguageUtil
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.firebase.NotificationHelper
import dong.datn.tourify.merchant.api.CreateOrder
import dong.datn.tourify.model.Order
import dong.datn.tourify.screen.view.CreateInvoiceOrder
import dong.datn.tourify.screen.view.DetailImageScreen
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.fromColor
import dong.datn.tourify.ui.theme.mediumBlue
import dong.datn.tourify.ui.theme.navigationBar
import dong.datn.tourify.ui.theme.orangeRed
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.DialogState
import dong.datn.tourify.utils.ORDER
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
    data object InvoiceOderScreen : ClientScreen("invoice_oder_screen")
    data object DetailImageScreen : ClientScreen("detail_image_screen")
    data object ReviewScreen : ClientScreen("review_screen")
    data object PolicyScreen : ClientScreen("policy_screen")
    data object LanguageScreen : ClientScreen("language_screen")
}

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

open class MainActivity : ComponentActivity() {

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

    fun payment(totalPrice:String){
        val orderApi = CreateOrder()
        val token = mutableStateOf("")

        try {
            val data = orderApi.createOrder(totalPrice)
            val code = data.getString("returncode")

            if (code == "1") {
                token.value=data.getString("zptranstoken")
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_LONG).show()
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
                fromColor("#00ffffff"),
                fromColor("#00ffffff")
            )
        )

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        changeTheme(currentTheme,applicationContext)
        setContent {
            LaunchedEffect(key1 = " listNotifications.value.size") {
                viewModels.listenerNotification()

                Firestore.getListData<Tour>(Firebase.firestore.collection("$TOUR")) {
                    viewModel.listTour.value = it ?: mutableListOf()
                }
                viewModels.firestore.collection("$ORDER")
                    .get()
                    .addOnSuccessListener {
                        val orderList = it.toObjects(Order::class.java)
                        val data =
                            orderList.filter { it.userOrderId == authSignIn!!.UId }.toMutableList()
                        data.sortBy { it.timeNow }
                        viewModel.listOrders.value = data

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
            if (viewModels.loadingState.value) {
                LoaddingDialog()
            }

            if (viewModels.dialogState.value) {
                DialogState(viewModels.dialogType.value)
            }

        }

    }


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
        val coroutineScope = rememberCoroutineScope()
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
                            bottomBarState.value = true
                            viewModels.currentIndex.value=4
                        }
                        ProfileScreen(navController, viewModels)
                        keyboardController?.hide()
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
                            keyboardController?.hide()
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
                    animComposable(ClientScreen.InvoiceOderScreen.route) {
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        CreateInvoiceOrder(navController, viewModels){
                            coroutineScope.launch {
                                ZaloPaySDK.init(553, Environment.SANDBOX)
                                payment(it)
                            }

                        }
                    }
                    animComposable(ClientScreen.DetailImageScreen.route) {
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        DetailImageScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.ReviewScreen.route) {
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        ReviewScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.PolicyScreen.route) {
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        PolicyScreen(navController, viewModels)
                    }
                    animComposable(ClientScreen.LanguageScreen.route) {
                        LaunchedEffect(Unit) {
                            keyboardController?.hide()
                            bottomBarState.value = false
                        }
                        LanguageScreen(navController)
                    }
                }


            })

    }
}

@Composable
fun GradientCircularProgressBar(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(white, mediumBlue, orangeRed),
    strokeWidth: Dp = 6.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ), label = ""
    )

    Canvas(modifier = modifier
        .size(46.dp)
        .graphicsLayer { rotationZ = rotation.value }) {
        val sweepAngle = 100f
        val gapAngle = 20f

        for (i in colors.indices) {
            val startAngle = i * (sweepAngle + gapAngle)
            drawArc(
                brush = Brush.linearGradient(listOf(colors[i], colors[i])),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun LoaddingDialog() {

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            GradientCircularProgressBar()
        }


    }

}