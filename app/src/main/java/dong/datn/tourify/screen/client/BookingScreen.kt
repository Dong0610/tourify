package dong.datn.tourify.screen.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.navigationBar
import dong.datn.tourify.ui.theme.selectedColor
import dong.datn.tourify.ui.theme.unselectedColor
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import kotlinx.coroutines.launch

@Composable
fun BookingScreen(nav: NavController, viewModels: AppViewModel) {
    val context = LocalContext.current
    
    val isDarkTheme = remember {
        mutableStateOf(currentTheme == -1)
    }

    ViewParent(onBack = {
        nav.navigate(ClientScreen.ProfileScreen.route) {
            nav.graph.startDestinationRoute?.let { route ->
                popUpTo(route) { saveState = true }
            }
            launchSingleTop = true
            restoreState = true
        }
    }) {
        Column(
            Modifier
                .fillMaxSize()

        ) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    nav.navigate(ClientScreen.ProfileScreen.route) {
                        nav.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                TextView(
                    context.getString(R.string.booking), Modifier.weight(1f), textSize = 20,
                    appColor, font = Font(R.font.poppins_semibold), textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(50.dp))
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )
            ViewItem()
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewItem() {
    val tabData = listOf(
        "All",
        "Finish", "Cancel"
    )
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffscreenLimit = 2,
        infiniteLoop = true,
        initialPage = 0,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = tabIndex,
        modifier = Modifier
    ) {
        tabData.forEachIndexed { index, text ->
            Tab(modifier = Modifier.background(navigationBar(context = LocalContext.current)), selected = tabIndex == index, onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }, text = {
                TextView(text = text,Modifier, font = Font(R.font.poppins_medium) , color =if( tabIndex==index) selectedColor else unselectedColor
                )
            }, selectedContentColor = selectedColor, unselectedContentColor = unselectedColor)
        }
    }

    HorizontalPager(
        state = pagerState
    ) { index ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (tabData[index] == "All") {
               TextView(text = "All", modifier = Modifier)
            } else if (tabData[index] == "Finish") {
                TextView(text = "Finish", modifier = Modifier)
            } else {
                TextView(text = "Cancel", modifier = Modifier)
            }
        }
    }
}