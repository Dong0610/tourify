package dong.datn.tourify.screen.staff

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import dong.datn.tourify.R
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.screen.client.ClientScreen
import dong.datn.tourify.screen.start.AccountActivity
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.colorByTheme
import dong.datn.tourify.ui.theme.findActivity
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.CommonImage
import dong.datn.tourify.utils.Space
import dong.datn.tourify.utils.changeTheme
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.animComposable
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import kotlinx.coroutines.launch

sealed class StaffScreen(var route: String) {
    data object HomeStaffScreen : StaffScreen("home_staff")
    data object SettingStaffScreen : StaffScreen("setting_staff")
    data object NotificationScreen : StaffScreen("notification_staff")
    data object ProfileScreen : StaffScreen("profile_staff")
    data object UpdateProfileScreen : StaffScreen("update_profile_staff")
    data object SettingScreen : StaffScreen("setting_staff")
    data object BookingScreen : StaffScreen("booking_staff")
    data object DetailTourScreen : StaffScreen("detail_tour_staff")
    data object DetailPlaceScreen : StaffScreen("detail_place_staff")
    data object BookingNowScreen : StaffScreen("booking_now_staff")
    data object ConversionScreen : StaffScreen("conversion_screen")
    data object ChatScreen : StaffScreen("chat_screen")
    data object UpdatePasswordScreen : StaffScreen("update_password_screen")
}


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SetScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    val activity = context as? Activity

    DisposableEffect(Unit) {
        val originalOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = orientation

        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            hideSystemBars(activity)
        } else {
            showSystemBars(activity)
        }

        onDispose {
            originalOrientation?.let {
                activity.requestedOrientation = it
            }
            showSystemBars(activity)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun hideSystemBars(activity: Activity?) {
    activity?.window?.let { window ->
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.let { controller ->
            controller.hide(WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun showSystemBars(activity: Activity?) {
    activity?.window?.let { window ->
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.insetsController?.let { controller ->
            controller.show(WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
open class StaffActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        changeTheme(currentTheme, applicationContext)
        setContent {
            TourifyTheme {

                StaffNavigation()

            }
        }


    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun StaffNavigation(
    ) {
        val systemUiController = rememberSystemUiController()
        val statusBar = if (currentTheme == 1) white else black
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = statusBar,
            )
        }
        val context = LocalContext.current
        val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
        val navController = rememberNavController()
        val keyboardController = LocalSoftwareKeyboardController.current
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val stateIsConversations = remember {
            mutableStateOf(false)
        }
        ModalNavigationDrawer(
            modifier = Modifier.background(if (currentTheme == 1) white else iconBackground),

            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = if (currentTheme == 1) white else iconBackground) {
                    VerScrollView(
                        Modifier
                            .padding(horizontal = 6.dp, vertical = 12.dp)
                    ) {
                        Column(
                            Modifier
                                .wrapContentSize()
                                .widthPercent(75f)
                        ) {
                            Row(
                                Modifier
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CommonImage(
                                    data = R.drawable.img_user,
                                    Modifier
                                        .size(40.dp)
                                        .background(shape = CircleShape, color = transparent)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                if (authSignIn != null) {
                                    Column {
                                        TextView(
                                            authSignIn!!.Name,
                                            Modifier.widthPercent(50f),
                                            textSize = 16,

                                            textColor(context),
                                            font = Font(R.font.poppins_medium)
                                        )
                                        TextView(
                                            authSignIn!!.Email,
                                            Modifier,
                                            textSize = 13,
                                            textColor(context),
                                            font = Font(R.font.poppins_regular)
                                        )

                                    }
                                }
                            }
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(
                                        whiteSmoke
                                    )
                            )

                            Space(h = 12)
                            ItemMenuStaff(
                                Icons.Rounded.Home,
                                LocalContext.current.getString(R.string.home)
                            ) {
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigationTo(StaffScreen.HomeStaffScreen.route)
                            }
                            Space(h = 6)

                            ItemMenuStaff(
                                Icons.Rounded.Settings,
                                LocalContext.current.getString(R.string.setting)
                            ) {
                                navController.navigationTo(StaffScreen.SettingStaffScreen.route)
                                scope.launch {
                                    drawerState.close()
                                }
                            }

                            ItemMenuStaff(
                                FaIcons.SignOutAlt,
                                LocalContext.current.getString(R.string.sign_out)
                            ) {
                                context.findActivity()
                                    ?.startActivity(Intent(context, AccountActivity::class.java))
                                authSignIn = null
                                context.findActivity()?.finishAffinity()
                            }
                        }
                    }


                }
            },
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = if (currentTheme == 1) white else black),
                content = { insertPadding ->
                    Column(

                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp)
                            .background(if (currentTheme == 1) white else black)
                            .padding(insertPadding)
                    ) {
                        if (!stateIsConversations.value) {
                            Row(
                                Modifier
                                    .background(if (currentTheme == 1) white else black)
                                    .fillMaxWidth()
                                    .height(50.dp), verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.wrapContentSize()) {
                                    IconView(
                                        modifier = Modifier.size(34.dp),
                                        icSize = 20,
                                        tint = colorByTheme(appColor, white),
                                        icon = Icons.Rounded.Menu
                                    )
                                    Box(modifier = Modifier
                                        .matchParentSize()
                                        .onClick {
                                            scope.launch { drawerState.open() }
                                        })
                                }
                                Space(w = 6)
                                TextView(
                                    text = context.getString(R.string.staff_manager),
                                    modifier = Modifier, font = Font(R.font.poppins_semibold),
                                    color = colorByTheme(appColor, white), textSize = 18
                                )

                            }
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(
                                        whiteSmoke
                                    )
                            )
                        }


                        NavHost(
                            modifier = Modifier,
                            navController = navController,
                            startDestination = StaffScreen.ConversionScreen.route
                        ) {
                            animComposable("home_staff") {
                                LaunchedEffect(Unit) {
                                    bottomBarState.value = true
                                    keyboardController?.hide()
                                    stateIsConversations.value = false
                                    viewModels.currentIndex.value = 0;
                                }
                                HomeStaffScreen(nav = navController, viewModel = viewModels) {

                                }
                            }
                            animComposable(StaffScreen.SettingStaffScreen.route) {
                                LaunchedEffect(Unit) {
                                    bottomBarState.value = true
                                    keyboardController?.hide()
                                    stateIsConversations.value = false
                                    viewModels.currentIndex.value = 1
                                }
                                SettingStaffScreen(navController, viewModels)
                            }
                            animComposable(StaffScreen.ConversionScreen.route) {
                                LaunchedEffect(Unit) {
                                    bottomBarState.value = true
                                    keyboardController?.hide()
                                    stateIsConversations.value = true
                                    viewModels.currentIndex.value = 1
                                }

                                SetScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                                ConversionScreen(navController, viewModels) {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            }
                        }
                    }

                })

        }
    }
    @Composable
    fun ItemMenuStaff(icons: Any, label: String, callback: () -> Unit) {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(40.dp)
            ) {
                if (icons is Int) {
                    Icon(
                        painter = painterResource(id = icons),
                        contentDescription = "Description",
                        modifier = Modifier.size(28.dp),
                        tint = if (currentTheme == -1) white else appColor
                    )
                } else if (icons is ImageVector) {
                    Icon(
                        imageVector = icons as ImageVector,
                        modifier = Modifier.size(28.dp),
                        contentDescription = "Description",
                        tint = if (currentTheme == -1) white else appColor
                    )
                } else {
                    FaIcon(
                        faIcon = icons as FaIconType,
                        modifier = Modifier.size(28.dp),
                        tint = if (currentTheme == -1) white else appColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(top=8.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                    color = textColor(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                )
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .onClick {
                        callback.invoke()
                    }
            )
        }
    }
}























