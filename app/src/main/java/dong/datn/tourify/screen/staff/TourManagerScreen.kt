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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.ManageHistory
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Sale
import dong.datn.tourify.screen.client.salePriceByTour
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.darkGray
import dong.datn.tourify.ui.theme.gold
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.utils.PLACES
import dong.datn.tourify.utils.SALES
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.utils.widthPercent
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.onClick
import dong.duan.livechat.widget.SearchBox
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun TourManagerScreen(nav: NavController, viewModel: AppViewModel, scope: () -> Unit) {
    val context = LocalContext.current
    val currentTour = remember { mutableStateOf(Tour()) }
    val stateTour = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }
    val filteredList = remember { mutableStateOf(mutableListOf<Tour>()) }
    if (viewModel.listTourStaff.value.size != 0) {
        LaunchedEffect(searchText.value) {
            filteredList.value = if (searchText.value.trim().isEmpty()) {
                viewModel.listTourStaff.value.toMutableList()
            } else {
                viewModel.listTourStaff.value.filter { tour ->
                    tour.tourName.contains(searchText.value, ignoreCase = true)
                }.toMutableList()
            }
        }
    }



    viewModel.updateTourTimeData(viewModel.listTourStaff.value)

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
                SearchBox(
                    onValueChange = {
                        searchText.value = it
                    },
                    onTouch = {
                        filteredList.value =
                            viewModel.listTourStaff.value.filter { tour ->
                                tour.tourName.contains(searchText.value, ignoreCase = true)
                            }.toMutableList()
                    }
                )
            }

            SpaceH(h = 12)

            TextView(
                text = context.getString(R.string.travel_managment),
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
                                nav.navigate(
                                    StaffScreen.AddTourScreen.route,
                                    NavOptions
                                        .Builder()
                                        .setPopUpTo(
                                            StaffScreen.TourManagerScreen.route,
                                            inclusive = true
                                        )
                                        .build()
                                )
                            }
                    )
                }
            }

            SpaceH(h = 12)
            LazyColumn {
                items(filteredList.value, key = { it.tourID }) {
                    TourItemManager(
                        tour = mutableStateOf(it),
                        onEdit = {
                            currentTour.value = it
                        },
                        onSelect = {
                            currentTour.value = it
                            stateTour.value = true
                        }
                    )
                    SpaceH(h = 6)
                }
            }
        }
    }

    if (stateTour.value) {
        BottomSheetManagerTour(tour = currentTour.value) {
            stateTour.value = false
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetManagerTour(
    tour: Tour,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val replyValue = remember {
        mutableStateOf("")
    }
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        scrimColor = Color.Black.copy(alpha = .5f),
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(white, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val coroutineScope = rememberCoroutineScope()
            val saleTourPrice = remember {
                mutableStateOf(0.0)
            }
            val place = remember {
                mutableStateOf("")
            }
            val saleRunning = remember {
                mutableStateOf(Sale())
            }
            LaunchedEffect("Sales of the tour") {
                coroutineScope.launch {
                    saleTourPrice.value = salePriceByTour(tour)
                    place.value =
                        Firestore.getValue<String>("$PLACES/${tour.tourAddress}", "placeName")
                            .toString()
                    Firestore.fetchById<Sale>("$SALES/${tour.saleId}") {
                        saleRunning.value = it ?: Sale()
                    }
                }
            }

            val context = LocalContext.current
            Box(
                Modifier
                    .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .heightPercent(18f)
                    .background(if (currentTheme == -1) iconBackground else white)
                    .clip(RoundedCornerShape(4.dp))

            ) {

                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .heightPercent(12f)
                            .padding(6.dp)

                    ) {
                        Row(
                            Modifier
                                .heightPercent(12f)
                                .widthPercent(25f)
                        ) {
                            RoundedImage(
                                tour.tourImage.get(0),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize(),
                                shape = RoundedCornerShape(6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            TextView(
                                text = tour.tourName,
                                modifier = Modifier,
                                textSize = 14,
                                font = Font(R.font.poppins_medium)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                TextView(
                                    text = saleTourPrice.value.toCurrency(),
                                    modifier = Modifier,
                                    textSize = 13,
                                    font = Font(R.font.poppins_medium),
                                    color = if (currentTheme == 1) Color.Red else white
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                TextView(
                                    text = tour.tourPrice.toCurrency(),
                                    modifier = Modifier.drawBehind {
                                        val strokeWidthPx = 1.dp.toPx()
                                        val verticalOffset = size.height / 2
                                        drawLine(
                                            color = gray,
                                            strokeWidth = strokeWidthPx,
                                            start = Offset(0f, verticalOffset),
                                            end = Offset(size.width, verticalOffset)
                                        )
                                    },
                                    font = Font(R.font.poppins_medium),
                                    color = gray, textSize = 13

                                )
                            }
                            SpaceH(h = 6)
                            Row(Modifier.fillMaxWidth()) {
                                Row {
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = "Star",
                                        modifier = Modifier.size(18.dp),
                                        tint = if (currentTheme == 1) gold else white
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    TextView(
                                        text = String.format("%.1f", tour.star),
                                        modifier = Modifier,
                                        textSize = 13,
                                        color = gray,
                                        font = Font(R.font.poppins_medium)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Row {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        modifier = Modifier.size(16.dp),
                                        contentDescription = "Location",
                                        tint = if (currentTheme == 1) darkGray else white
                                    )
                                    TextView(
                                        text = place.value,
                                        textSize = 13,
                                        modifier = Modifier,
                                        font = Font(R.font.poppins_medium),
                                        color = lightGrey
                                    )
                                }
                            }
                        }

                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 6.dp)
                    ) {
                        Row(
                            Modifier
                                .weight(0.4f)
                                .fillMaxHeight(1f)

                        ) {
                            Column(Modifier.weight(0.5f)) {
                                TextView(
                                    text = context.getString(R.string.success),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    textSize = 12,
                                    font = Font(R.font.poppins_regular)
                                )
                                TextView(
                                    text = tour.success.toString(),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    textSize = 13,
                                    font = Font(R.font.poppins_medium)
                                )
                            }
                            Column(Modifier.weight(0.5f)) {
                                TextView(
                                    text = context.getString(R.string.cancel),
                                    modifier = Modifier.fillMaxWidth(),
                                    textSize = 12,
                                    textAlign = TextAlign.Center,
                                    font = Font(R.font.poppins_regular)
                                )
                                TextView(
                                    text = tour.cancel.toString(),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    textSize = 13,
                                    font = Font(R.font.poppins_medium)
                                )
                            }
                        }
                        SpaceW(w = 4)
                        Column(
                            Modifier.weight(0.6f),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            TextView(
                                text = saleRunning.value.saleName,
                                modifier = Modifier.fillMaxWidth(),
                                textSize = 12,
                                textAlign = TextAlign.Center,
                                font = Font(R.font.poppins_regular)
                            )
                            TextView(
                                text = context.getString(R.string.from) + saleRunning.value.startDate + " " + context.getString(
                                    R.string.to
                                ) + " " + saleRunning.value.endDate,
                                color = red,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                textSize = 11,
                                font = Font(R.font.poppins_medium)
                            )
                        }
                    }

                }
                Box(Modifier.fillMaxSize(1f), contentAlignment = Alignment.TopEnd) {
                    Box(
                        Modifier
                            .wrapContentSize()
                            .padding(6.dp)
                            .size(32.dp)
                            .background(lightGrey.opacity(0.5f), shape = RoundedCornerShape(6.dp))
                            .onClick {

                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.ManageHistory,
                            contentDescription = ""
                        )
                    }
                    Box(
                        Modifier
                            .matchParentSize()
                            .fillMaxWidth()

                            .padding(6.dp)
                    ) {}

                }


            }
            Spacer(modifier = Modifier.width(12.dp))
        }
        SpaceH(h = 40)
    }
}


@Composable
fun TourItemManager(tour: State<Tour>, onSelect: (Tour) -> Unit, onEdit: (Tour) -> Unit) {

    val coroutineScope = rememberCoroutineScope()
    val saleTourPrice = remember {
        mutableStateOf(0.0)
    }
    val place = remember {
        mutableStateOf("")
    }
    val saleRunning = remember {
        mutableStateOf(Sale())
    }
    LaunchedEffect("Sales of the tour") {
        coroutineScope.launch {
            saleTourPrice.value = salePriceByTour(tour.value)
            if (tour.value.tourAddress != "") {
                place.value =
                    Firestore.getValue<String>("$PLACES/${tour.value.tourAddress}", "placeName")
                        .toString()
            }
            if (tour.value.saleId != "") {
                Firestore.fetchById<Sale>("$SALES/${tour.value.saleId}") {
                    saleRunning.value = it ?: Sale()
                }
            }

        }
    }

    val context = LocalContext.current
    Box(
        Modifier
            .border(width = 1.dp, color = lightGrey, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .heightPercent(18f)
            .background(if (currentTheme == -1) iconBackground else white)
            .clip(RoundedCornerShape(4.dp))

    ) {

        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightPercent(12f)
                    .padding(6.dp)

            ) {
                Row(
                    Modifier
                        .heightPercent(12f)
                        .widthPercent(25f)
                ) {
                    RoundedImage(
                        tour.value.tourImage.get(0),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = RoundedCornerShape(6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    TextView(
                        text = tour.value.tourName,
                        modifier = Modifier,
                        textSize = 14,
                        font = Font(R.font.poppins_medium)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        TextView(
                            text = saleTourPrice.value.toCurrency(),
                            modifier = Modifier,
                            textSize = 13,
                            font = Font(R.font.poppins_medium),
                            color = if (currentTheme == 1) Color.Red else white
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextView(
                            text = tour.value.tourPrice.toCurrency(),
                            modifier = Modifier.drawBehind {
                                val strokeWidthPx = 1.dp.toPx()
                                val verticalOffset = size.height / 2
                                drawLine(
                                    color = gray,
                                    strokeWidth = strokeWidthPx,
                                    start = Offset(0f, verticalOffset),
                                    end = Offset(size.width, verticalOffset)
                                )
                            },
                            font = Font(R.font.poppins_medium),
                            color = gray, textSize = 13

                        )
                    }
                    SpaceH(h = 6)
                    Row(Modifier.fillMaxWidth()) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "Star",
                                modifier = Modifier.size(18.dp),
                                tint = if (currentTheme == 1) gold else white
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            TextView(
                                text = String.format("%.1f", tour.value.star),
                                modifier = Modifier,
                                textSize = 13,
                                color = gray,
                                font = Font(R.font.poppins_medium)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                modifier = Modifier.size(16.dp),
                                contentDescription = "Location",
                                tint = if (currentTheme == 1) darkGray else white
                            )
                            TextView(
                                text = place.value,
                                textSize = 13,
                                modifier = Modifier,
                                font = Font(R.font.poppins_medium),
                                color = lightGrey
                            )
                        }
                    }
                }

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 6.dp)
            ) {
                Row(
                    Modifier
                        .weight(0.4f)
                        .fillMaxHeight(1f)

                ) {
                    Column(Modifier.weight(0.5f)) {
                        TextView(
                            text = context.getString(R.string.success),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 12,
                            font = Font(R.font.poppins_regular)
                        )
                        TextView(
                            text = tour.value.success.toString(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 13,
                            font = Font(R.font.poppins_medium)
                        )
                    }
                    Column(Modifier.weight(0.5f)) {
                        TextView(
                            text = context.getString(R.string.cancel),
                            modifier = Modifier.fillMaxWidth(),
                            textSize = 12,
                            textAlign = TextAlign.Center,
                            font = Font(R.font.poppins_regular)
                        )
                        TextView(
                            text = tour.value.cancel.toString(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 13,
                            font = Font(R.font.poppins_medium)
                        )
                    }
                }
                SpaceW(w = 4)
                Column(Modifier.weight(0.6f), verticalArrangement = Arrangement.SpaceAround) {
                    if (tour.value.saleId != "") {
                        TextView(
                            text = saleRunning.value.saleName,
                            modifier = Modifier.fillMaxWidth(),
                            textSize = 12,
                            textAlign = TextAlign.Center,
                            font = Font(R.font.poppins_regular)
                        )
                        TextView(
                            text = context.getString(R.string.from) + saleRunning.value.startDate + " " + context.getString(
                                R.string.to
                            ) + " " + saleRunning.value.endDate,
                            color = red,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            textSize = 11,
                            font = Font(R.font.poppins_medium)
                        )
                    }

                }
            }

        }
        Box(Modifier.fillMaxSize(1f), contentAlignment = Alignment.TopEnd) {
            Box(
                Modifier
                    .wrapContentSize()
                    .padding(6.dp)
                    .size(32.dp)
                    .background(lightGrey.opacity(0.5f), shape = RoundedCornerShape(6.dp))
                    .onClick {
                        onEdit.invoke(tour.value)
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Rounded.ManageHistory,
                    contentDescription = ""
                )
            }
            Box(Modifier
                .matchParentSize()
                .fillMaxWidth()
                .onClick {
                    onSelect.invoke(tour.value)
                }
                .padding(6.dp)) {}

        }


    }
    Spacer(modifier = Modifier.width(12.dp))


}
