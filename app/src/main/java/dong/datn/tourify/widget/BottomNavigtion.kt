package dong.datn.tourify.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.screen.client.ClientScreen
import dong.datn.tourify.ui.theme.navigationBar
import dong.datn.tourify.ui.theme.navigationColor
import dong.datn.tourify.ui.theme.white

enum class BottomNavigationItem(
    var icon: Any,
    var screen: String,
    var title: String
) {
    HOMES(Icons.Rounded.Home, ClientScreen.HomeClientScreen.route, "Home"),
    DISCOVER(Icons.Rounded.Search, ClientScreen.DiscoveryScreen.route, "Discovery"),
    CHAT(FaIcons.FacebookMessenger, ClientScreen.ChatScreen.route, "Chats"),
    NOTIFICATION(
        Icons.Rounded.Notifications,
        ClientScreen.NotificationScreen.route,
        "Notifications"
    ),
    PROFILE(Icons.Rounded.Person, ClientScreen.ProfileScreen.route, "Profile"),
}


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    viewModel: AppViewModel,
    bottomBarState: MutableState<Boolean> = mutableStateOf(false)
) {
    val navItems = BottomNavigationItem.entries
    val context = LocalContext.current

    AnimatedVisibility(visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content =
        {

    NavigationBar(
        modifier = Modifier.background(Color.Blue),
        containerColor = navigationBar(context),
        contentColor = white
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = {
                    when (item.icon) {
                        is ImageVector -> Icon(
                            item.icon as ImageVector,
                            contentDescription = item.title
                        )

                        is Painter -> Icon(
                            painter = item.icon as Painter,
                            contentDescription = item.title
                        )

                        else -> {
                            FaIcon(item.icon as FaIconType.BrandIcon, tint =  navigationColor(viewModel.currentIndex.value==index))
                        }
                    }
                },
                label = {
                    Text(
                        item.title, maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = navigationColor(viewModel.currentIndex.value==index),
                    selectedTextColor = navigationColor(viewModel.currentIndex.value==index),
                    indicatorColor = navigationBar(context) ,
                    unselectedIconColor = navigationColor(viewModel.currentIndex.value==index),
                    unselectedTextColor =navigationColor(viewModel.currentIndex.value==index),
                    disabledIconColor = Color.Cyan,
                    disabledTextColor = Color.Cyan,
                ),
                selected = viewModel.currentIndex.value == index,
                onClick = {
                    viewModel.currentIndex.value = index
                    navController.navigate(item.screen) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
        })
}
