package dong.datn.tourify.screen.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.model.Places
import dong.datn.tourify.model.getListPlaces
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.CommonImage
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.DotIndicator
import dong.datn.tourify.widget.InnerImageIcon
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeClientScreen(nav: NavController, viewModel: AppViewModel, location: String? = null) {
    val context = LocalContext.current
    val places = getListPlaces()
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
                                shape = RoundedCornerShape(8.dp)
                            )
                            indexSlideImage.value = page
                        }
                    }
                    Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                        DotIndicator(listImage.size, indexSlideImage.value)
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ItemCategory(
                            Modifier.weight(0.3f),
                            content = context.getString(R.string.str_ct_plane),
                            icon = R.drawable.ic_round_airplane
                        ) {

                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        ItemCategory(
                            Modifier.weight(0.3f),
                            content = context.getString(R.string.str_ct_car),
                            icon = R.drawable.ic_round_directions_car
                        ) {

                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        ItemCategory(
                            Modifier.weight(0.3f),
                            content = context.getString(R.string.str_ct_train),
                            icon = R.drawable.ic_directions_transit
                        ) {

                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TextView(
                        text = context.getString(R.string.top_international),
                        modifier = Modifier.padding(start = 16.dp, bottom = 6.dp),
                        font = Font(R.font.poppins_semibold), textSize = 18
                    )
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 16.dp)
                            .heightPercent(28f)
                    ) {
                        LazyRow {
                            items(listImage.size + 4) {
                                ItemTopBucketTour()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    TextView(
                        text = context.getString(R.string.top_5_place),
                        modifier = Modifier.padding(start = 16.dp, bottom = 6.dp),
                        font = Font(R.font.poppins_semibold), textSize = 18
                    )

                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 16.dp)
                            .heightPercent(20f)
                    ) {
                        LazyRow(state = rememberLazyListState()) {
                            items(places) {
                                ItemTopPlace(
                                    it,
                                    Modifier
                                        .widthPercent(45f)
                                        .heightPercent(18f)
                                )
                            }
                        }
                    }

                    TextView(
                        text = context.getString(R.string.top_10_booked),
                        modifier = Modifier.padding(start = 16.dp, bottom = 6.dp),
                        font = Font(R.font.poppins_semibold), textSize = 18
                    )
                    Row(
                        Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 16.dp)
                            .heightPercent(28f)
                    ) {
                        LazyRow {
                            items(listImage.size + 4) {
                                ItemTopBucketTour()
                            }
                        }
                    }

                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
            }

        }
    }
}

@Composable
fun ItemTopPlace(items: Places, modifier: Modifier) {
    Box(
        modifier = modifier.border(
            width = 1.dp,
            color = lightGrey,
            shape = RoundedCornerShape(12.dp)
        )
    ) {
        RoundedImage(items.Image, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Column {
            Spacer(modifier = Modifier.weight(1f))
            TextView(
                text = items.placeName,
                modifier = Modifier
                    .padding(start = 12.dp),
                textSize = 16,
                font = Font(R.font.poppins_medium), color = white
            )
            TextView(
                text = items.tourCount.toString() + " tour",
                modifier = Modifier
                    .padding(start = 12.dp),
                textSize = 14,
                font = Font(R.font.poppins_regular), color = white
            )

        }
    }
    Spacer(modifier = Modifier.width(12.dp))

}

@Composable
fun ItemTopBucketTour() {
    Box(
        Modifier
            .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(12.dp))
            .widthPercent(42f)
            .fillMaxHeight(1f)
            .background(if(currentTheme==-1) iconBackground else white)
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
            Row(Modifier.heightPercent(15f)) {
                RoundedImage(
                    R.drawable.img_test_data_1,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),

                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            TextView(text = "Travel name", modifier = Modifier, font = Font(R.font.poppins_medium))
            Spacer(modifier = Modifier.height(2.dp))
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
            Spacer(modifier = Modifier.weight(1f))
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

    }
    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
fun ItemCategory(modifier: Modifier, content: String, icon: Int, onSelect: () -> Unit) {
    Box(
        modifier
            .background(color = appColor, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 6.dp)
            .onClick {
                onSelect.invoke()
            }) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(horizontal = 6.dp),
                painter = painterResource(id = icon),
                contentDescription = "Plane", tint = white
            )
            TextView(
                text = content,
                modifier = Modifier,
                textSize = 16, color = white
            )
        }
    }

}

