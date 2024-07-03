package dong.datn.tourify.screen.client

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.screen.start.AccountActivity
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.findActivity
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick

@Composable
fun ProfileScreen(navController: NavHostController, viewModels: AppViewModel) {
    val context = LocalContext.current
    viewModels.isKeyboardVisible.value = false
    ViewParent(onBack = {
        viewModels.currentIndex.value = 3
        navController.navigate(ClientScreen.NotificationScreen.route) {
            popUpTo(0)
        }


    }) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    viewModels.currentIndex.value = 3
                    navController.navigate(ClientScreen.NotificationScreen.route) {
                        popUpTo(0)
                    }
                }

                TextView(
                    context.getString(R.string.profile), Modifier.weight(1f), textSize = 20,
                    appColor, font = Font(R.font.poppins_semibold), textAlign = TextAlign.Center
                )
                IconView(modifier = Modifier, icon = R.drawable.ic_round_logout, icSize = 24) {
                    context.findActivity()
                        ?.startActivity(Intent(context, AccountActivity::class.java))
                    authSignIn = null
                    context.findActivity()?.finishAffinity()
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )
            VerScrollView {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.heightPercent(5f))
                    Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                        RoundedImage(
                            data = R.drawable.img_user,
                            Modifier.size(150.dp),
                            shape = CircleShape
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    TextView(
                        text = authSignIn?.Name.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        color = textColor(context),
                        textAlign = TextAlign.Center,
                        textSize = 20,
                        font = Font(R.font.poppins_semibold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextView(
                        text = authSignIn?.Email.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = darkGray,
                        textSize = 18,
                        font = Font(R.font.poppins_medium)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    ItemMenuProfie(
                        icon = R.drawable.ic_rounded_users,
                        label = context.getString(R.string.your_profile)
                    ) {
                        navController.navigate(ClientScreen.UpdateProfileScreen.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ItemMenuProfie(
                        icon = R.drawable.ic_nav_booking,
                        label = context.getString(R.string.booking)
                    ) {
                        navController.navigate(ClientScreen.BookingScreen.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ItemMenuProfie(
                        icon = Icons.Rounded.Favorite,
                        label = context.getString(R.string.favorite)
                    ) {
                        navController.navigationTo(ClientScreen.WishlistScreen.route)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ItemMenuProfie(
                        icon = R.drawable.ic_rounder_setting,
                        label = context.getString(R.string.setting)
                    ) {
                        navController.navigate(ClientScreen.SettingScreen.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ItemMenuProfie(
                        icon = R.drawable.ic_rounder_payment,
                        label = context.getString(R.string.payment_medthod)
                    ) {

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ItemMenuProfie(
                        icon = R.drawable.ic_rounder_update,
                        label = context.getString(R.string.change_password)
                    ) {
                        navController.navigationTo(ClientScreen.UpdatePasswordScreen.route)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ItemMenuProfie(
                        icon = R.drawable.ic_rounder_security,
                        label = context.getString(R.string.security)
                    ) {

                    }


                }
            }

        }
    }
}

@Composable
fun ItemMenuProfie(icon: Any, label: String, callback: () -> Unit) {
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxSize(1f)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = if (currentTheme == 1) whiteSmoke else Color(0xCB333333),
                    shape = RoundedCornerShape(8.dp)
                )
                .height(52.dp)
                .padding(horizontal = 12.dp)

        ) {
            if (icon is Int) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "Description",
                    tint = if (currentTheme == -1) white else black
                )
            } else {
                Icon(
                    imageVector = icon as ImageVector,
                    contentDescription = "Description",
                    tint = if (currentTheme == -1) white else black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            TextView(
                text = label,
                modifier = Modifier
                    .weight(1f),
                color = textColor(context),
                textSize = 18,
                font = Font(R.font.poppins_medium)
            )
            Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = "Next",tint = if(currentTheme==-1) white else black)
        }
        Box(modifier = Modifier
            .matchParentSize()
            .onClick {
                callback.invoke()
            })

    }



}