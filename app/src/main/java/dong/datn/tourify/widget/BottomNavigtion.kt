package dong.datn.tourify.widget

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.screen.client.ClientScreen
import dong.datn.tourify.ui.theme.navigationBar
import dong.datn.tourify.ui.theme.navigationColor
import dong.datn.tourify.ui.theme.white

enum class BottomNavigationItem(
    var icon: ImageVector,
    var screen: String,
    var title: String
) {
    HOMES(Icons.Rounded.Home, ClientScreen.HomeClientScreen.route, "Home"),
    DISCOVER(Icons.Rounded.Search, ClientScreen.DiscoveryScreen.route, "Discovery"),
    WISHLIST(Icons.Rounded.Favorite, ClientScreen.WishlistScreen.route, "Favorite"),
    NOTIFICATION(
        Icons.Rounded.Notifications,
        ClientScreen.NotificationScreen.route,
        "Notifications"
    ),
    PROFILE(Icons.Rounded.Person, ClientScreen.ProfileScreen.route, "Profile"),
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navItems = BottomNavigationItem.entries
    val selectedItem = rememberSaveable { mutableStateOf(0) }
    val context = LocalContext.current


    NavigationBar(
        modifier = Modifier.background(Color.Blue),
        containerColor = navigationBar(context),
        contentColor = white
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = {
                    Text(
                        item.title, maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = navigationColor(selectedItem.value==index),
                    selectedTextColor = navigationColor(selectedItem.value==index),
                    indicatorColor = navigationBar(context) ,
                    unselectedIconColor = navigationColor(selectedItem.value==index),
                    unselectedTextColor =navigationColor(selectedItem.value==index),
                    disabledIconColor = Color.Cyan,
                    disabledTextColor = Color.Cyan,
                ),
                selected = selectedItem.value == index,
                onClick = {
                    selectedItem.value = index
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
}