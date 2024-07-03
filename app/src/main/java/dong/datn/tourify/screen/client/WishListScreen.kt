package dong.datn.tourify.screen.client

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.InnerImageIcon
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox

@Composable
fun WishListScreen(navController: NavHostController, viewModels: AppViewModel) {
    val context = LocalContext.current
    ViewParent(onBack = {
        viewModels.currentIndex.value = 1
        navController.navigate(ClientScreen.ProfileScreen.route) {
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
                    viewModels.currentIndex.value = 1
                    navController.navigate(ClientScreen.ProfileScreen.route) {
                        popUpTo(0)
                    }
                }
                TextView(
                    context.getString(R.string.favorite), Modifier.weight(1f), textSize = 20,
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


            val list = (1..10).map { it.toString() }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SearchBox {
                    showToast(it)
                }
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
                    items(list.size) { index ->
                        ItemWishList()
                    }
                }
            )
        }


    }
}

@Composable
fun ItemWishList() {
    Box(
        Modifier
            .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(12.dp))
            .background(white)
            .heightPercent(30f)
            .clip(RoundedCornerShape(4.dp))
            .shadow(
                spotColor = Color(0xD5ECE9E9),
                ambientColor = Color(0xD5ECE9E9),
                elevation = 2.dp,
                shape = RoundedCornerShape(2.dp)
            )

    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            Row(Modifier.heightPercent(16f)) {
                RoundedImage(
                    R.drawable.img_test_data_1,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),

                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            TextView(text = "Travel name", modifier = Modifier, font = Font(R.font.poppins_medium))
            Spacer(modifier = Modifier.weight(1f))
            Row(Modifier.fillMaxWidth()) {
                TextView(
                    text = "100",
                    modifier = Modifier,
                    font = Font(R.font.poppins_medium),
                    color = Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextView(text = "100", modifier = Modifier, font = Font(R.font.poppins_medium))
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
                    text = "100",
                    modifier = Modifier,
                    font = Font(R.font.poppins_medium),
                    color = lightGrey
                )
            }

        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            InnerImageIcon(
                modifier = Modifier.size(32.dp),
                icon = Icons.Rounded.Favorite,
                icSize = 20
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
    }

}
