package dong.datn.tourify.screen.client

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.colorByTheme
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.IconView2
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParentContent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.datn.tourify.widget.ratting.RatingBar
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.InputValue

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ReviewScreen(nav: NavController, viewModel: AppViewModel) {
    val context = LocalContext.current
    val rating = remember {
        mutableFloatStateOf(5f)
    }
    val listImage = remember { mutableStateOf(mutableListOf<Any?>().apply { add(null) }) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            listImage.value = listImage.value.toMutableList().apply { add(uri) }
        }
    }
    val comments = remember {
        mutableStateOf("")
    }
    ViewParentContent(onBack = {
        if (viewModel.prevScreen.value != "") {
            nav.navigationTo(viewModel.prevScreen.value)
        } else {
            nav.popBackStack()
        }
    })
    {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView2(
                    modifier = Modifier, icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft
                ) {
                    if (viewModel.prevScreen.value != "") {
                        nav.navigationTo(viewModel.prevScreen.value)
                    } else {
                        nav.popBackStack()
                    }
                }

                TextView(
                    context.getString(R.string.review),
                    Modifier.weight(1f),
                    textSize = 18,
                    textColor(LocalContext.current),
                    font = Font(R.font.poppins_semibold),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(50.dp))
            }

            SpaceH(h = 18)

            RatingBar(
                rating = rating.floatValue,
                itemSize = 24.dp,
                painterEmpty = painterResource(id = R.drawable.star_background),
                painterFilled = painterResource(id = R.drawable.star_foreground)
            ) {
                rating.floatValue = it
            }

            VerScrollView(
                Modifier
                    .fillMaxSize()
            ) {
                Column(
                    Modifier
                        .padding(vertical = 16.dp)
                        .matchParentSize()
                ) {
                    Row(
                        modifier = Modifier
                            .heightIn(200.dp, 400.dp)
                            .background(
                                color = transparent,
                            )
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(12.dp),
                                color = colorByTheme(lightGrey, whiteSmoke)
                            ), verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        InputValue(
                            value = comments.value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            hint = context.getString(R.string.review) + "...",
                            font = Font(R.font.poppins_regular),
                            teAlignment = TextAlign.Justify,
                            maxLines = 100

                        ) {
                            comments.value = it
                        }
                    }

                    SpaceH(h = 16)
                    Column(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(100.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            this.items(listImage.value) { item ->

                                if (item != null) {
                                    Box(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(140.dp)
                                            .padding(4.dp)
                                            .border(
                                                1.dp,
                                                color = appColor,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                    ) {
                                        RoundedImage(
                                            modifier = Modifier.matchParentSize(),
                                            data = item,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                    }

                                } else {
                                    Box(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(140.dp)
                                            .padding(4.dp)
                                            .background(
                                                colorByTheme(whiteSmoke, iconBackground),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                1.dp,
                                                color = appColor,
                                                shape = RoundedCornerShape(12.dp)
                                            ), contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            contentDescription = "",
                                            modifier = Modifier.size(32.dp),

                                            imageVector = Icons.Rounded.Add,
                                            tint = appColor
                                        )
                                        Box(modifier = Modifier
                                            .fillMaxSize()
                                            .onClick { imagePickerLauncher.launch("image/*") })

                                    }
                                }
                            }
                        }

                    }

                    AppButton(
                        text = context.getString(R.string.save),
                        modifier = Modifier,
                        isEnable = true
                    ) {
                        if (comments.value.isNotEmpty()) {
                            viewModel.saveCommentByTour(
                                viewModel.detailTour.value!!,
                                comments.value,
                                rating.floatValue,
                                listImage.value
                            ) {
                                nav.navigationTo(ClientScreen.DetailTourScreen.route)
                            }
                        } else {
                            showToast(context.getString(R.string.unpaid_reviews))
                        }

                    }

                }

            }
        }
    }

}