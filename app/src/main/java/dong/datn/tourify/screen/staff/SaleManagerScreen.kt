package dong.datn.tourify.screen.staff

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.model.Sale
import dong.datn.tourify.model.SaleType
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.greenYellow
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.orangeRed
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.SearchBox

@SuppressLint("UnrememberedMutableState")
@Composable
fun SaleManagerScreen(nav: NavController, viewModel: AppViewModel, boxScope: () -> Unit) {
    val context = LocalContext.current
    var currentSaleManager = remember {
        mutableStateOf(mutableListOf<Sale>())
    }
    val stateBottomManager = remember {
        mutableStateOf(false)
    }


    ViewParent {
        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 6.dp)
        ) {
            SpaceH(h = 12)

            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                SearchBox({}, {
                    showToast(it)
                })
            }

            SpaceH(h = 12)

            TextView(
                text = context.getString(R.string.sale_management),
                modifier = Modifier,
                textSize = 18,
                color = textColor(),
                font = Font(R.font.poppins_semibold)
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Box(
                    Modifier
                        .size(32.dp)
                        .background(appColor, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        tint = white,
                        modifier = Modifier.size(24.dp),
                        contentDescription = ""
                    )
                    Box(
                        Modifier
                            .matchParentSize()
                            .clickable {
                                nav.navigationTo(
                                    StaffScreen.AddSaleScreen.route,
                                    StaffScreen.SaleManagerScreen.route
                                )
                            })
                }
            }

            SpaceH(h = 12)
            LazyColumn {
                this.items(viewModel.listSalesManager.value, key = {
                    it.saleId
                }) {
                    SaleItemManager(sale = mutableStateOf(it), {
                       viewModel.currentSale.value = it
                        nav.navigationTo(StaffScreen.ModifySaleScreen.route,StaffScreen.SaleManagerScreen.route)
                    }, {
//                        stateBottomManager.value=true
//                        viewModel.currentSale.value = it
                        viewModel.currentSale.value = it
                        nav.navigationTo(StaffScreen.ModifySaleScreen.route,StaffScreen.SaleManagerScreen.route)
                    })
                    SpaceH(h = 6)
                }
            }
        }
    }


    if (stateBottomManager.value) {
        LocalFocusManager.current.clearFocus()
        BottomSheetView(state = { stateBottomManager.value = false }) {
            ModifierStateSales(viewModel.currentSale.value!!)
        }
    }

}

@Composable
fun ModifierStateSales(value: Sale) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
        TextView(text = value.saleName, modifier = Modifier, font = Font(R.font.poppins_medium), color = appColor)
        SpaceH(h = 6)
        
        
        
        
        SpaceH(h = 40)
    }
}

@Composable
fun SaleItemManager(sale: MutableState<Sale>, onView: (Sale) -> Unit, onModify: (Sale) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val color = when (sale.value.status) {
        SaleType.RUNNING -> Color(0xFF4CAF50)
        SaleType.EXPIRED -> Color(0xFFF44336)
        SaleType.CLOSED -> Color(0xFF9E9E9E)
        SaleType.CANCELLED -> Color(0xFF000000)
        SaleType.PAUSED -> orangeRed
    }
    Box(
        Modifier
            .border(width = 2.dp, color = color, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(4.dp)
            .heightPercent(12f)
            .background(if (currentTheme == -1) iconBackground else white)
            .clip(RoundedCornerShape(4.dp))

    ) {

        Row(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .wrapContentHeight()
                    .widthPercent(30f)
            ) {

                RoundedImage(
                    sale.value.saleImage,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(16f),
                    shape = RoundedCornerShape(6.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                Modifier
                    .fillMaxSize()) {

                TextView(
                    text = sale.value.saleName,
                    modifier = Modifier,
                    textSize = 14,
                    font = Font(R.font.poppins_regular)
                )
                Spacer(modifier = Modifier.weight(1f))
                SpaceH(h = 6)
                TextView(
                    text = "${sale.value.percent}%",
                    modifier = Modifier,
                    textSize = 14,
                    font = Font(R.font.poppins_regular),
                    color = if (currentTheme == 1) Color.Red else white
                )


                TextView(
                    text = "${context.getString(R.string.from)} ${sale.value.startDate}",
                    modifier = Modifier,
                    textSize = 13,
                    font = Font(R.font.poppins_regular),
                    color = textColor()
                )
                Spacer(modifier = Modifier.weight(1f))
                TextView(
                    text = "${context.getString(R.string.to)} ${sale.value.endDate}",
                    modifier = Modifier,
                    textSize = 13,
                    font = Font(R.font.poppins_regular),
                    color = textColor()
                )

            }

        }
        Box(Modifier.fillMaxSize(1f), contentAlignment = Alignment.TopEnd) {
            Box(
                Modifier
                    .wrapContentSize()
                    .padding(6.dp)
                    .size(28.dp)
                    .background(lightGrey.opacity(0.5f), shape = RoundedCornerShape(6.dp))
                    .onClick {
                        onView.invoke(sale.value)
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_manager_view),
                    contentDescription = ""
                )
            }
            Box(Modifier
                .matchParentSize()
                .fillMaxWidth()
                .onClick {
                    onModify.invoke(sale.value)
                }
                .padding(6.dp)) {}

        }


    }
    Spacer(modifier = Modifier.width(12.dp))
}
