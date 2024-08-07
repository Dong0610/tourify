package dong.datn.tourify.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.appContext
import dong.datn.tourify.screen.client.ClientScreen
import dong.datn.tourify.ui.theme.navigationBar
import dong.datn.tourify.ui.theme.navigationColor
import dong.datn.tourify.ui.theme.white

enum class BottomNavigationItem(
    var icon: Any,
    var screen: String,
    var title: Int
) {
    HOMES(
        Icons.Rounded.Home,
        ClientScreen.HomeClientScreen.route,
      R.string.home
    ),
    DISCOVER(
        Icons.Rounded.Search,
        ClientScreen.DiscoveryScreen.route,
      R.string.discover
    ),
    CHAT(
        FaIcons.FacebookMessenger,
        ClientScreen.ChatScreen.route,
        R.string.chat
    ),
    NOTIFICATION(
        Icons.Rounded.Notifications,
        ClientScreen.NotificationScreen.route,
        R.string.notification
    ),
    PROFILE(
        Icons.Rounded.Person,
        ClientScreen.ProfileScreen.route,
        R.string.profile
    ),
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    viewModel: AppViewModel,
    bottomBarState: MutableState<Boolean> = mutableStateOf(false)
) {
    val navItems = BottomNavigationItem.entries
    val context = LocalContext.current
    appContext= LocalContext.current
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

                    if (index == 3) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            BadgedBox(
                                badge = {
                                    if (viewModel.countUnReadNoti.value > 0) {
                                        Badge(
                                            containerColor = Color.Red,
                                            contentColor = Color.White
                                        ) {
                                            Text("${viewModel.countUnReadNoti.value}")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Notifications,
                                    contentDescription = "Shopping cart",
                                )
                            }

                        }
                    } else {
                        when (item.icon) {
                            is ImageVector -> Icon(
                                imageVector = item.icon as ImageVector,
                                contentDescription = LocalContext.current.getString(item.title)
                            )

                            is Painter -> Icon(
                                painter = item.icon as Painter,
                                contentDescription = LocalContext.current.getString(item.title)
                            )

                            else -> {
                                FaIcon(
                                    item.icon as FaIconType.BrandIcon,
                                    tint = navigationColor(viewModel.currentIndex.value == index)
                                )
                            }
                        }
                    }



                },
                label = {
                    Text(
                        LocalContext.current.getString(item.title), maxLines = 1
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
