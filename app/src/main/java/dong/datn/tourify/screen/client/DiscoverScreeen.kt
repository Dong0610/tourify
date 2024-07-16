package dong.datn.tourify.screen.client

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.database
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.InnerImageIcon
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.livechat.widget.SearchBox
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun DiscoverScreen(navController: NavHostController, viewModels: AppViewModel) {
    val context = LocalContext.current
    val listCurrent = viewModels.listTour
    val listSearch = remember {
        mutableStateOf(viewModels.listTour.value)
    }
    ViewParent(onBack = {
        viewModels.currentIndex.value = 0
        navController.navigate(ClientScreen.HomeClientScreen.route) {
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
                    viewModels.currentIndex.value = 0
                    navController.navigate(ClientScreen.HomeClientScreen.route) {
                        popUpTo(0)
                    }
                }
                TextView(
                    context.getString(R.string.discover), Modifier.fillMaxWidth(), textSize = 20,
                    appColor, font = Font(R.font.poppins_semibold), textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(56.dp))

            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SearchBox({
                    if (it.trim().isEmpty()) {
                        listSearch.value = listCurrent.value
                    }
                }, {
                    listSearch.value = if (it.isEmpty()) {
                        listCurrent.value
                    } else {
                        listCurrent.value.filter { item ->
                            item.tourName.contains(
                                it,
                                ignoreCase = true
                            )
                        }.toMutableList()
                    }
                })
            }
            Spacer(modifier = Modifier.height(6.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    this.itemsIndexed(listSearch.value) { index, it ->
                        ItemDiscover(it) {
                            viewModels.detailTour.value = it
                            navController.navigationTo(ClientScreen.DetailTourScreen.route,ClientScreen.DiscoveryScreen.route)
                        }
                    }
                }
            )
        }


    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ItemDiscover(tour: Tour, onSelect: (Tour) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val loveState = remember {
        mutableStateOf(false)
    }

    coroutineScope.launch {
        loveState.value= database.loveDao().doesItemExist(tour.tourID)
    }
    Box(
        Modifier
            .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(12.dp))
            .background(if (currentTheme == 1) white else iconBackground)
            .heightPercent(30f)
            .clip(RoundedCornerShape(12.dp)), contentAlignment = Alignment.TopEnd

    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            Row(Modifier.heightPercent(16f)) {
                RoundedImage(
                    tour.tourImage.get(0),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            TextView(
                text = tour.tourName,
                maxLine = 2,
                modifier = Modifier,
                font = Font(R.font.poppins_medium)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(Modifier.fillMaxWidth()) {
                TextView(
                    text = tour.tourPrice.toCurrency() ,
                    modifier = Modifier,
                    font = Font(R.font.poppins_medium),
                    color = Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextView(text = tour.tourPrice.toCurrency(), modifier = Modifier, font = Font(R.font.poppins_medium))
            }
            Spacer(modifier = Modifier.height(2.dp))

            Row(Modifier.fillMaxWidth(1f)) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Location",
                    tint = if (currentTheme == 1) darkGray else white
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextView(
                    text = tour.tourAddress,
                    modifier = Modifier,
                    font = Font(R.font.poppins_medium),
                    color = lightGrey
                )
            }

        }
        Box(Modifier.matchParentSize().onClick {
            onSelect.invoke(tour)
        })
        InnerImageIcon(
            modifier = Modifier
                .size(32.dp),
            icon = Icons.Rounded.Favorite,
            icSize = 20,
            tint = if (loveState.value) red else black
        ){
            viewModels.onModifyLove(tour) {
                loveState.value = it
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
    }

}
