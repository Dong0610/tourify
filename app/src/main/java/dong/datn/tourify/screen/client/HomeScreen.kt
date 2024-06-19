package dong.datn.tourify.screen.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.utils.CommonImage
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.widget.DotIndicator
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeClientScreen(nav: NavController, viewModel: AppViewModel, location: String? = null) {
    val context = LocalContext.current
    val listImage =
        listOf(R.drawable.img_test_data_1, R.drawable.img_test_data_2, R.drawable.img_test_data_3)
    val indexSlideImage = remember {
        mutableStateOf(1)
    }
    ViewParent {
        Column {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommonImage(
                    data = R.drawable.img_user,
                    Modifier
                        .size(42.dp)
                        .background(shape = CircleShape, color = transparent)
                )

                Spacer(modifier = Modifier.width(6.dp))
                if (authSignIn == null) {
                    Column {
                        TextView(
                            context.getString(R.string.wellcome_back), Modifier, textSize = 13,
                            textColor(context), font = Font(R.font.poppins_regular)
                        )
                        TextView(
                            context.getString(R.string.wellcome_back), Modifier, textSize = 16,
                            textColor(context), font = Font(R.font.poppins_medium)
                        )
                    }
                } else {
                    TextView(
                        context.getString(R.string.app_name), Modifier, textSize = 18,
                        appColor, font = Font(R.font.poppins_semibold)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = "Location",
                    tint = if (currentTheme == 1) appColor else lightGrey
                )
                TextView(
                    location ?: "", Modifier, textSize = 14,
                    darkGray, font = Font(R.font.poppins_medium)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xFFdddddd), shape = RectangleShape)
            )
            VerScrollView {
                Column {
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
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {

                        val pagerState = rememberPagerState(pageCount = {
                            listImage.size
                        })
                        HorizontalPager(state = pagerState) { page ->
                            RoundedImage(
                                listImage.get(page),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.heightPercent(28f),
                                rounded = 8
                            )
                            indexSlideImage.value = page
                        }
                    }
                    Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                        DotIndicator(listImage.size, indexSlideImage.value)
                    }


                }
            }

        }
    }
}

