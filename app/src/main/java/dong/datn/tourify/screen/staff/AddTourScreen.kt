package dong.datn.tourify.screen.staff

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.model.Places
import dong.datn.tourify.model.Vehicle
import dong.datn.tourify.model.getNewVehicle
import dong.datn.tourify.screen.client.DatePickerBox
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.colorByTheme
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.iconBackground
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.delay
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.utils.timeNow
import dong.datn.tourify.utils.toCurrency
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParentContent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.livechat.widget.InputValue
import dong.duan.travelapp.model.Accommodation
import dong.duan.travelapp.model.DaySchedule
import dong.duan.travelapp.model.DetailSchedule
import dong.duan.travelapp.model.Food
import dong.duan.travelapp.model.IncludeService
import dong.duan.travelapp.model.NoteService
import dong.duan.travelapp.model.OtherServices
import dong.duan.travelapp.model.Schedule
import dong.duan.travelapp.model.Service
import dong.duan.travelapp.model.Tour
import dong.duan.travelapp.model.TourTime
import dong.duan.travelapp.model.Transport
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("MutableCollectionMutableState", "UnrememberedMutableState")
@Composable
fun AddTourScreen(nav: NavController, scope: () -> Unit) {
    val context = LocalContext.current

    val listImage = remember { mutableStateOf(mutableListOf<Any?>().apply { add(null) }) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            listImage.value = listImage.value.toMutableList().apply { addAll(uris) }
        }
    }

    val listTourTime = remember { mutableStateOf(mutableListOf<TourTime>()) }
    val indexState = remember { mutableStateOf(0) }
    val stateBottomSheet = remember { mutableStateOf(false) }

    val tourName = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
    }
    val tourPrice = remember {
        mutableStateOf(0)
    }
    val vehicleId = remember {
        mutableStateOf("")
    }
    val placeId = remember {
        mutableStateOf("")
    }

    val buttonsState = remember { mutableStateOf(false) }
    fun checkData(): Boolean {
        return listTourTime.value.size > 0 && tourPrice.value > 0 && description.value.length != 0 && placeId.value.isNotEmpty() && vehicleId.value.isNotEmpty() && tourName.value.isNotEmpty() && listImage.value.size != 0
    }


    val createTour = remember {
        mutableStateOf(0)
    }

    val createSuccess = remember { mutableStateOf(Tour()) }


    val roadValue = remember {
        mutableStateOf("")
    }
    val runningTime = remember { mutableStateOf("") }
    val acommodation = remember { mutableStateOf("") }
    val listFood = remember { mutableStateOf(mutableListOf<Food>()) }
    val listTransport = remember { mutableStateOf(mutableListOf<Transport>()) }
    val listOtherServices = remember { mutableStateOf(mutableListOf<OtherServices>()) }
    val listNote = remember { mutableStateOf(mutableListOf<NoteService>()) }

    fun checkService(): Boolean {
        return runningTime.value.isNotEmpty() && acommodation.value.isNotEmpty()
                && listFood.value.size > 0 && listTransport.value.size > 0 && listOtherServices.value.size > 0 && listNote.value.size > 0
    }

    val serviceState = remember {
        mutableStateOf(false)
    }
    val scheduleState = remember {
        mutableStateOf(false)
    }


    val listDaySchedule = remember {
        mutableStateOf(mutableListOf<DaySchedule>())
    }
    ViewParentContent(onBack = { }) {
        if (createTour.value == 0) {
            VerScrollView(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    TextView(text = R.string.choose_image_title)
                    Column(
                        Modifier
                            .heightPercent(20f)
                            .fillMaxWidth()
                    ) {
                        LazyRow(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            this.itemsIndexed(listImage.value, key = { index, item ->
                                index.toString()
                            }) { index, item ->
                                if (item != null) {
                                    Box(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(140.dp)
                                            .padding(4.dp)
                                            .border(
                                                1.dp,
                                                color = appColor,
                                                shape = RoundedCornerShape(8.dp)
                                            ), contentAlignment = Alignment.TopEnd
                                    ) {
                                        RoundedImage(
                                            modifier = Modifier.matchParentSize(),
                                            data = item,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(
                                                    black.opacity(0.5f),
                                                    shape = RoundedCornerShape(8.dp)
                                                ), contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_round_remove),
                                                modifier = Modifier.size(20.dp),
                                                contentDescription = "Remove",
                                                tint = whiteSmoke
                                            )
                                            Box(modifier = Modifier
                                                .matchParentSize()
                                                .clickable {
                                                    listImage.value = listImage.value
                                                        .toMutableList()
                                                        .apply {
                                                            removeAt(index)
                                                        }
                                                })
                                        }

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
                    TextView(text = R.string.tour_name, font = Font(R.font.poppins_medium))
                    InputValue(
                        value = tourName.value,
                        textSize = 13.sp,
                        maxLines = 100,
                        hintColor = red,
                        modifier = Modifier.border(
                            width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                        ),
                        hint = context.getString(R.string.not_empty)
                    ) {
                        tourName.value = it
                        buttonsState.value = checkData()
                    }
                    SpaceH(h = 8)
                    TextView(text = R.string.tour_description, font = Font(R.font.poppins_medium))
                    VerScrollView(
                        Modifier
                            .height(150.dp)
                            .background(white, shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray)
                    ) {
                        InputValue(
                            value = description.value,
                            textSize = 13.sp,
                            modifier = Modifier,
                            maxLines = 100,
                            hintColor = red,
                            hint = context.getString(R.string.not_empty),
                        ) {
                            description.value = it
                            buttonsState.value = checkData()
                        }
                    }
                    SpaceH(h = 8)
                    TextView(text = R.string.tour_price, font = Font(R.font.poppins_medium))

                    InputValue(
                        value = tourPrice.value.toString(),
                        textSize = 13.sp,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.border(
                            width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                        ),

                        maxLines = 1,
                        hintColor = red,
                        hint = context.getString(R.string.not_empty),
                    ) {
                        tourPrice.value = try {
                            val cleanedInput = it.replace(Regex("[^\\d]"), "")
                            if (cleanedInput.isEmpty()) {
                                0
                            } else {
                                cleanedInput.toInt()
                            }
                        } catch (e: IllegalArgumentException) {
                            0
                        }
                        buttonsState.value = checkData()
                    }
                    SpaceH(h = 8)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Column(Modifier.weight(0.5f)) {
                            TextView(text = R.string.address, font = Font(R.font.poppins_medium))

                            DropdownSelectPlace(modifier = Modifier) {
                                placeId.value = it.placeID
                                buttonsState.value = checkData()
                            }
                        }
                        SpaceW(w = 8)
                        Column(Modifier.weight(0.5f)) {
                            TextView(text = R.string.vehicle, font = Font(R.font.poppins_medium))
                            DropdownSelectVehicle(modifier = Modifier) {
                                vehicleId.value = it.vhId
                                buttonsState.value = checkData()
                            }
                        }
                    }
                    SpaceH(h = 8)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextView(text = R.string.tour_time, font = Font(R.font.poppins_medium))
                        Box(Modifier.background(appColor, shape = RoundedCornerShape(4.dp))) {
                            TextView(
                                maxLine = 1,
                                text = context.getString(R.string.create_new),
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                font = Font(R.font.poppins_regular),
                                color = white
                            )
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .onClick {
                                        stateBottomSheet.value = true
                                        indexState.value = 1
                                    })
                        }
                    }
                    SpaceH(h = 4)
                    VerScrollView(
                        Modifier
                            .height(180.dp)
                            .background(white, shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .wrapContentHeight()
                        ) {
                            listTourTime.value.forEachIndexed { index, tourTime ->
                                TourTimeItem(tourTime, index) {
                                    listTourTime.value = listTourTime.value.toMutableList().apply {
                                        removeAt(it)
                                    }
                                }

                            }
                            SpaceH(h = 4)

                        }
                    }

                    SpaceH(h = 16)

                    AppButton(
                        text = context.getString(R.string.create_new),
                        modifier = Modifier,
                        isEnable = buttonsState.value
                    ) {
                        viewModels.loadingState.value = true
                        val tour = Tour(
                            tourName = tourName.value,
                            tourPrice = tourPrice.value.toDouble(),
                            tourTime = listTourTime.value,
                            tourDescription = description.value,
                            vehicleId = vehicleId.value,
                            tourAddress = placeId.value,
                            countRating = 0,
                            saleId = "",
                            success = 0,
                            cancel = 0,
                            star = 0.0,
                            tourImage = mutableListOf()
                        )
                        viewModels.createNewTour(tour, listImage.value) { state, tour ->
                            if (state == 1) {
                                viewModels.loadingState.value = false
                                createTour.value = 1
                                createSuccess.value = tour!!

                            }
                        }
                    }
                    SpaceH(h = 40)
                }
            }
        } else if (createTour.value == 1) {
            VerScrollView {
                Column {
                    TextView(
                        text = context.getString(R.string.create_new_service),
                        font = Font(R.font.poppins_medium), color = appColor
                    )

                    SpaceH(h = 16)
                    TextView(text = R.string.road, font = Font(R.font.poppins_medium))
                    InputValue(
                        value = roadValue.value,
                        textSize = 13.sp,
                        maxLines = 100,
                        hintColor = red,
                        modifier = Modifier.border(
                            width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                        ),
                        hint = context.getString(R.string.not_empty)
                    ) {
                        roadValue.value = it
                        serviceState.value = checkService()
                    }

                    SpaceH(h = 8)
                    TextView(text = R.string.tour_time, font = Font(R.font.poppins_medium))
                    InputValue(
                        value = runningTime.value,
                        textSize = 13.sp,
                        maxLines = 100,
                        hintColor = red,
                        modifier = Modifier.border(
                            width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                        ),
                        hint = context.getString(R.string.not_empty)
                    ) {
                        runningTime.value = it
                        serviceState.value = checkService()
                    }

                    SpaceH(h = 16)
                    TextView(
                        text = context.getString(R.string.service_includes),
                        font = Font(R.font.poppins_medium), color = appColor
                    )
                    SpaceH(h = 8)

                    TextView(text = R.string.accommodation, font = Font(R.font.poppins_medium))
                    InputValue(
                        value = acommodation.value,
                        textSize = 13.sp,
                        maxLines = 100,
                        hintColor = red,
                        modifier = Modifier.border(
                            width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                        ),
                        hint = context.getString(R.string.not_empty)
                    ) {
                        acommodation.value = it
                        serviceState.value = checkService()
                    }
                    SpaceH(h = 8)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextView(text = R.string.transport, font = Font(R.font.poppins_medium))
                        Box(Modifier.background(appColor, shape = RoundedCornerShape(4.dp))) {
                            TextView(
                                text = R.string.create_new,
                                Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                font = Font(R.font.poppins_regular),
                                color = white
                            )
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .onClick {
                                        stateBottomSheet.value = true
                                        indexState.value = 2
                                    })
                        }
                    }
                    SpaceH(h = 4)
                    VerScrollView(
                        Modifier
                            .height(180.dp)
                            .background(white, shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .wrapContentHeight()
                        ) {
                            listTransport.value.forEachIndexed { index, tourTime ->
                                TourTransportItem(tourTime, index) {
                                    listTransport.value =
                                        listTransport.value.toMutableList().apply {
                                            removeAt(it)
                                        }

                                    serviceState.value = checkService()
                                }
                                SpaceH(h = 4)

                            }

                        }
                    }

                    SpaceH(h = 8)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextView(text = R.string.food, font = Font(R.font.poppins_medium))
                        Box(Modifier.background(appColor, shape = RoundedCornerShape(4.dp))) {
                            TextView(
                                text = R.string.create_new,
                                Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                font = Font(R.font.poppins_regular),
                                color = white
                            )
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .onClick {
                                        stateBottomSheet.value = true
                                        indexState.value = 3
                                    })
                        }
                    }
                    SpaceH(h = 4)
                    VerScrollView(
                        Modifier
                            .height(180.dp)
                            .background(white, shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .wrapContentHeight()
                        ) {
                            listFood.value.forEachIndexed { index, tourTime ->
                                TourFoodItem(tourTime, index) {
                                    listFood.value =
                                        listFood.value.toMutableList().apply {
                                            removeAt(it)
                                        }
                                    serviceState.value = checkService()
                                }
                                SpaceH(h = 4)
                            }

                        }
                    }

                    SpaceH(h = 8)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextView(text = R.string.other_services, font = Font(R.font.poppins_medium))
                        Box(Modifier.background(appColor, shape = RoundedCornerShape(4.dp))) {
                            TextView(
                                text = R.string.create_new,
                                Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                font = Font(R.font.poppins_regular),
                                color = white
                            )
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .onClick {
                                        stateBottomSheet.value = true
                                        indexState.value = 4
                                    })
                        }
                    }
                    SpaceH(h = 4)
                    VerScrollView(
                        Modifier
                            .height(180.dp)
                            .background(white, shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .wrapContentHeight()
                        ) {
                            listOtherServices.value.forEachIndexed { index, tourTime ->
                                TourOtherServiceItem(tourTime, index) {
                                    listOtherServices.value =
                                        listOtherServices.value.toMutableList().apply {
                                            removeAt(it)
                                        }
                                    serviceState.value = checkService()
                                }
                                SpaceH(h = 4)
                            }

                        }
                    }

                    SpaceH(h = 8)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextView(text = R.string.notes, font = Font(R.font.poppins_medium))
                        Box(Modifier.background(appColor, shape = RoundedCornerShape(4.dp))) {
                            TextView(
                                text = R.string.create_new,
                                Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                font = Font(R.font.poppins_regular),
                                color = white
                            )
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .onClick {
                                        stateBottomSheet.value = true
                                        indexState.value = 5
                                    })
                        }
                    }
                    SpaceH(h = 4)
                    VerScrollView(
                        Modifier
                            .height(180.dp)
                            .background(white, shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .wrapContentHeight()
                        ) {
                            listNote.value.forEachIndexed { index, tourTime ->
                                TourNoteItem(tourTime, index) {
                                    listNote.value =
                                        listNote.value.toMutableList().apply {
                                            removeAt(it)
                                        }
                                    serviceState.value = checkService()
                                }
                                SpaceH(h = 4)
                            }

                        }
                    }

                    SpaceH(h = 16)

                    AppButton(
                        text = context.getString(R.string.save),
                        modifier = Modifier,
                        isEnable = serviceState.value
                    ) {
                        viewModels.loadingState.value = true
                        val service = Service(
                            id = createSuccess.value.tourID,
                            time = timeNow(),
                            road = roadValue.value,
                            vehicle = getNewVehicle(context).find { it.vhId == vehicleId.value }!!.vhName,
                            name = createSuccess.value.tourName,
                            introduce = description.value,
                            includeService = IncludeService().apply {
                                id = createSuccess.value.tourID
                                accommodation = Accommodation(
                                    id = createSuccess.value.tourID + "001",
                                    description = acommodation.value
                                )
                                transport = listTransport.value
                                food = listFood.value
                                otherServices = listOtherServices.value
                            },
                            noteService = listNote.value
                        )
                        viewModels.createService(service, createSuccess.value.tourID) {
                            viewModels.loadingState.value = false
                            if (it == 1) {
                                createTour.value = 2
                            }
                        }
                    }

                    SpaceH(h = 40)

                }
            }
        } else {
            VerScrollView(Modifier.fillMaxSize()) {
                Column(Modifier.matchParentSize()) {
                    TextView(
                        text = context.getString(R.string.create_new_schedule),
                        font = Font(R.font.poppins_medium), color = appColor
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextView(
                            text = R.string.detailed_schedule,
                            font = Font(R.font.poppins_medium)
                        )
                        Box(Modifier.background(appColor, shape = RoundedCornerShape(4.dp))) {
                            TextView(
                                text = R.string.create_new,
                                Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                font = Font(R.font.poppins_regular),
                                color = white
                            )
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .onClick {
                                        stateBottomSheet.value = true
                                        indexState.value = 6
                                    })
                        }
                    }
                    SpaceH(h = 4)
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 8.dp, horizontal = 8.dp)

                    ) {
                        TourDayScheduleList(
                            listDaySchedule = listDaySchedule.value,
                            onRemoveDaySchedule = { index ->
                                listDaySchedule.value =
                                    listDaySchedule.value.toMutableList().apply { removeAt(index) }
                            },
                            onAddDetailSchedule = { index ->
                                val newDetail = DetailSchedule(
                                    Id = ((listDaySchedule.value[index].ListDetail.map {
                                        it.Id.toIntOrNull() ?: 0
                                    }.maxOrNull() ?: 0) + 1).toString(),
                                    Content = ""
                                )
                                listDaySchedule.value =
                                    listDaySchedule.value.toMutableList().apply {
                                        this[index] =
                                            this[index].copy(ListDetail = (this[index].ListDetail + newDetail).toMutableList())
                                    }
                            },
                            onRemoveDetailSchedule = { index, detailIndex ->
                                listDaySchedule.value =
                                    listDaySchedule.value.toMutableList().apply {
                                        this[index] = this[index].copy(
                                            ListDetail = this[index].ListDetail.toMutableList()
                                                .apply { removeAt(detailIndex) })
                                    }
                            },
                            onDetailContentChange = { index, detailIndex, newValue ->
                                listDaySchedule.value =
                                    listDaySchedule.value.toMutableList().apply {
                                        val newDetails = this[index].ListDetail.toMutableList()
                                        newDetails[detailIndex] =
                                            newDetails[detailIndex].copy(Content = newValue)
                                        this[index] = this[index].copy(ListDetail = newDetails)
                                    }
                            }
                        )
                    }




                    SpaceH(h = 16)

                    AppButton(
                        text = context.getString(R.string.save),
                        modifier = Modifier,
                        isEnable = scheduleState.value
                    ) {
                        viewModels.loadingState.value = true
                        val schedule = Schedule(
                            Id = createSuccess.value.tourID,
                            ListTime = listDaySchedule.value.toMutableList()
                        )
                        viewModels.createSchedule(schedule, createSuccess.value.tourID) {
                            viewModels.loadingState.value = false
                            if (it == 2) {
                                createTour.value = 2
                                viewModels.listTourStaff.value.toMutableList().apply {
                                    add(createSuccess.value)
                                }
                                delay(1500) {
                                    nav.navigationTo(StaffScreen.TourManagerScreen.route)
                                }
                            }
                        }
                    }

                    SpaceH(h = 40)

                }
            }
        }
    }

    if (stateBottomSheet.value) {
        LocalFocusManager.current.clearFocus()
        BottomSheetView(state = { stateBottomSheet.value = false }) {
            when (indexState.value) {
                1 -> {
                    BottomSheetAddTime(value = listTourTime.value) {
                        listTourTime.value = listTourTime.value.toMutableList().apply {
                            add(it)
                        }
                        stateBottomSheet.value = false
                        buttonsState.value = checkData()
                    }
                }

                2 -> {
                    BottomSheetTransport(listTransport.value) {
                        listTransport.value = listTransport.value.toMutableList().apply {
                            add(it)
                        }
                        stateBottomSheet.value = false
                        serviceState.value = checkService()
                    }
                }

                3 -> {
                    BottomSheetFood(listFood.value) {
                        listFood.value = listFood.value.toMutableList().apply {
                            add(it)
                        }
                        stateBottomSheet.value = false
                        serviceState.value = checkService()
                    }
                }

                4 -> {
                    BottomSheetOtherService(listOtherServices.value) {
                        listOtherServices.value = listOtherServices.value.toMutableList().apply {
                            add(it)
                        }
                        stateBottomSheet.value = false
                        serviceState.value = checkService()
                    }
                }

                5 -> {
                    BottomSheetNoteService(listNote.value) {
                        listNote.value = listNote.value.toMutableList().apply {
                            add(it)
                        }
                        stateBottomSheet.value = false
                        serviceState.value = checkService()
                    }
                }

                6 -> {
                    BottomSheetDaySchedule(listDaySchedule.value) {
                        listDaySchedule.value = listDaySchedule.value.toMutableList().apply {
                            add(it)
                        }
                        stateBottomSheet.value = false
                        scheduleState.value = listDaySchedule.value.size != 0
                    }
                }
            }

        }
    }
}


@Composable
fun TourDayScheduleItem(
    daySchedule: DaySchedule,
    index: Int,
    onRemoveDaySchedule: (Int) -> Unit,
    onAddDetailSchedule: (Int) -> Unit,
    onRemoveDetailSchedule: (Int, Int) -> Unit,
    onDetailContentChange: (Int, Int, String) -> Unit
) {
    val listDetailSchedule = remember(daySchedule.ListDetail) {
        mutableStateOf(daySchedule.ListDetail)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextView(
                text = daySchedule.Time,
                textSize = 14,
                modifier = Modifier
                    .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp))
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            )
            SpaceW(w = 4)
            Row {
                Box(
                    Modifier
                        .size(32.dp)
                        .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_remove_24),
                        contentDescription = "Remove",
                        modifier = Modifier.size(24.dp)
                    )
                    Box(
                        Modifier
                            .matchParentSize()
                            .clickable { onRemoveDaySchedule(index) }
                    )
                }
                SpaceW(w = 2)
                Box(
                    Modifier
                        .size(32.dp)
                        .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_add),
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp)
                    )
                    Box(
                        Modifier
                            .matchParentSize()
                            .clickable { onAddDetailSchedule(index) }
                    )
                }
            }
        }
        SpaceH(h = 6)
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            listDetailSchedule.value.forEachIndexed { detailIndex, detailSchedule ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InputValue(
                        value = detailSchedule.Content,
                        textSize = 13.sp,
                        maxLines = 100,
                        hintColor = red,
                        font = Font(R.font.poppins_regular),
                        modifier = Modifier
                            .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp))
                            .weight(1f),
                        hint = LocalContext.current.getString(R.string.not_empty)
                    ) { newValue ->
                        onDetailContentChange(index, detailIndex, newValue)
                    }

                    SpaceW(w = 4)
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_remove_24),
                            contentDescription = "Remove",
                            modifier = Modifier.size(24.dp)
                        )
                        Box(
                            Modifier
                                .matchParentSize()
                                .clickable { onRemoveDetailSchedule(index, detailIndex) }
                        )
                    }
                }
                SpaceH(h = 4)
            }
        }
    }
    SpaceH(h = 4)
}


@Composable
fun TourDayScheduleList(
    listDaySchedule: MutableList<DaySchedule>,
    onRemoveDaySchedule: (Int) -> Unit,
    onAddDetailSchedule: (Int) -> Unit,
    onRemoveDetailSchedule: (Int, Int) -> Unit,
    onDetailContentChange: (Int, Int, String) -> Unit
) {
    Column {
        listDaySchedule.forEachIndexed { index, daySchedule ->
            TourDayScheduleItem(
                daySchedule = daySchedule,
                index = index,
                onRemoveDaySchedule = onRemoveDaySchedule,
                onAddDetailSchedule = onAddDetailSchedule,
                onRemoveDetailSchedule = onRemoveDetailSchedule,
                onDetailContentChange = onDetailContentChange
            )
        }
    }
}


@Composable
fun BottomSheetDaySchedule(
    value: MutableList<DaySchedule>, callback: (DaySchedule) -> Unit
) {

    val descripton = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    fun checkState(): Boolean {
        return descripton.value.isNotEmpty()
    }

    val stateButton = remember {
        mutableStateOf(checkState())
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(white, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
            .padding(horizontal = 16.dp), horizontalAlignment = Alignment.Start
    ) {
        TextView(
            text = LocalContext.current.getString(R.string.create_new_transport),
            font = Font(R.font.poppins_semibold),
            textSize = 18,
            color = appColor
        )
        SpaceH(h = 16)
        TextView(
            text = " ${context.getString(R.string.daily_schedule)}  ${value.size}",
            font = Font(R.font.poppins_regular),
        )
        InputValue(
            value = descripton.value,
            textSize = 14.sp,
            maxLines = 100,
            hintColor = red,
            modifier = Modifier.border(
                width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
            ),
            hint = context.getString(R.string.not_empty)
        ) {
            descripton.value = it
            stateButton.value = checkState()
        }
        SpaceH(h = 16)

        AppButton(
            text = context.getString(R.string.save),
            modifier = Modifier,
            isEnable = stateButton.value
        ) {
            val tour = DaySchedule(
                "${value.size + 1}", descripton.value, mutableListOf()
            )
            callback(tour)
        }
        SpaceH(h = 40)
    }
}


@Composable
fun TourNoteItem(tourTime: NoteService, index: Int, content: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextView(
            text = tourTime.content,
            textSize = 14,
            modifier = Modifier
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp))
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )
        SpaceW(w = 4)
        Box(
            Modifier
                .size(32.dp)
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.round_remove_24),
                contentDescription = "Remove",
                modifier = Modifier.size(24.dp)
            )
            Box(
                Modifier
                    .matchParentSize()
                    .onClick { content.invoke(index) })
        }
    }
}

@Composable
fun TourOtherServiceItem(tourTime: OtherServices, index: Int, content: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextView(
            text = tourTime.description,
            textSize = 14,
            modifier = Modifier
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp))
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )
        SpaceW(w = 4)
        Box(
            Modifier
                .size(32.dp)
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.round_remove_24),
                contentDescription = "Remove",
                modifier = Modifier.size(24.dp)
            )
            Box(
                Modifier
                    .matchParentSize()
                    .onClick { content.invoke(index) })
        }
    }
}

@Composable
fun TourFoodItem(tourTime: Food, index: Int, content: (Int) -> Unit) {
    val context = LocalContext.current
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextView(
            text = tourTime.description + " " + context.getString(R.string.price)
                .lowercase() + ": ${tourTime.price.toCurrency()}/${tourTime.count}",
            textSize = 14,
            modifier = Modifier
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp))
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )
        SpaceW(w = 4)
        Box(
            Modifier
                .size(32.dp)
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.round_remove_24),
                contentDescription = "Remove",
                modifier = Modifier.size(24.dp)
            )
            Box(
                Modifier
                    .matchParentSize()
                    .onClick { content.invoke(index) })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFood(
    value: MutableList<Food>, callback: (Food) -> Unit
) {
    val descripton = remember {
        mutableStateOf("")
    }
    val priceValue = remember {
        mutableStateOf(0)
    }
    val countFood = remember {
        mutableStateOf(0)
    }
    val context = LocalContext.current

    fun checkState(): Boolean {
        return descripton.value.isNotEmpty() && priceValue.value > 0 && countFood.value > 0
    }


    val stateButton = remember {
        mutableStateOf(checkState())
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(white, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
            .padding(horizontal = 16.dp), horizontalAlignment = Alignment.Start
    ) {
        TextView(
            text = R.string.create_new_food,
            font = Font(R.font.poppins_semibold),
            textSize = 18,
            color = appColor
        )
        SpaceH(h = 16)
        TextView(
            text = R.string.content,
            font = Font(R.font.poppins_regular),
        )
        InputValue(
            value = descripton.value,
            textSize = 14.sp,
            maxLines = 100,
            hintColor = red,
            modifier = Modifier.border(
                width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
            ),
            hint = context.getString(R.string.not_empty)
        ) {
            descripton.value = it
            stateButton.value = checkState()
        }
        SpaceH(h = 16)
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(0.5f)) {
                TextView(text = R.string.tour_price, font = Font(R.font.poppins_medium))
                InputValue(
                    value = priceValue.value.toString(),
                    textSize = 13.sp,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.border(
                        width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                    ),
                    maxLines = 1,
                    hintColor = red,
                    hint = context.getString(R.string.not_empty),
                ) {

                    priceValue.value = try {
                        val cleanedInput = it.replace(Regex("[^\\d]"), "")
                        if (cleanedInput.isEmpty()) {
                            0
                        } else {
                            cleanedInput.toInt()
                        }
                    } catch (e: IllegalArgumentException) {
                        0
                    }


                    stateButton.value = checkState()
                }
            }
            SpaceW(w = 6)
            Column(Modifier.weight(0.5f)) {
                TextView(text = R.string.count, font = Font(R.font.poppins_medium))
                InputValue(
                    value = countFood.value.toString(),
                    textSize = 13.sp,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.border(
                        width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                    ),

                    maxLines = 1,
                    hintColor = red,
                    hint = context.getString(R.string.not_empty),
                ) {

                    countFood.value = try {
                        val cleanedInput = it.replace(Regex("[^\\d]"), "")
                        if (cleanedInput.isEmpty()) {
                            0
                        } else {
                            cleanedInput.toInt()
                        }
                    } catch (e: IllegalArgumentException) {
                        0
                    }


                    stateButton.value = checkState()
                }
            }
        }
        SpaceH(h = 16)
        AppButton(
            text = context.getString(R.string.save),
            modifier = Modifier,
            isEnable = stateButton.value
        ) {
            val food = Food(
                "${value.size + 1}", countFood.value, priceValue.value, descripton.value
            )
            callback(food)
        }

        SpaceH(h = 40)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetView(state: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { state.invoke() },
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier.wrapContentSize(),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        scrimColor = Color.Black.copy(alpha = .5f),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(), content = content
        )
    }
}


@Composable
fun BottomSheetTransport(
    value: MutableList<Transport>, callback: (Transport) -> Unit
) {

    val descripton = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    fun checkState(): Boolean {
        return descripton.value.isNotEmpty()
    }

    val stateButton = remember {
        mutableStateOf(checkState())
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(white, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
            .padding(horizontal = 16.dp), horizontalAlignment = Alignment.Start
    ) {
        TextView(
            text = R.string.create_new_transport,
            font = Font(R.font.poppins_semibold),
            textSize = 18,
            color = appColor
        )
        SpaceH(h = 16)
        TextView(
            text = R.string.description,
            font = Font(R.font.poppins_regular),
        )
        InputValue(
            value = descripton.value,
            textSize = 14.sp,
            maxLines = 100,
            hintColor = red,
            modifier = Modifier.border(
                width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
            ),
            hint = context.getString(R.string.not_empty)
        ) {
            descripton.value = it
            stateButton.value = checkState()
        }
        SpaceH(h = 16)

        AppButton(
            text = context.getString(R.string.save),
            modifier = Modifier,
            isEnable = stateButton.value
        ) {
            val tour = Transport(
                "${value.size + 1}", descripton.value
            )
            callback(tour)
        }
        SpaceH(h = 40)
    }
}

@Composable
fun BottomSheetNoteService(
    value: MutableList<NoteService>, callback: (NoteService) -> Unit
) {

    val descripton = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    fun checkState(): Boolean {
        return descripton.value.isNotEmpty()
    }

    val stateButton = remember {
        mutableStateOf(checkState())
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(white, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
            .padding(horizontal = 16.dp), horizontalAlignment = Alignment.Start
    ) {
        TextView(
            text = R.string.create_new_notes,
            font = Font(R.font.poppins_semibold),
            textSize = 18,
            color = appColor
        )
        SpaceH(h = 16)
        TextView(
            text = R.string.description,
            font = Font(R.font.poppins_regular),
        )
        SpaceH(h = 4)
        InputValue(
            value = descripton.value,
            textSize = 14.sp,
            maxLines = 100,
            hintColor = red,
            modifier = Modifier.border(
                width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
            ),
            hint = context.getString(R.string.not_empty)
        ) {
            descripton.value = it
            stateButton.value = checkState()
        }
        SpaceH(h = 16)

        AppButton(
            text = context.getString(R.string.save),
            modifier = Modifier,
            isEnable = stateButton.value
        ) {
            val tour = NoteService(
                "${value.size + 1}", descripton.value
            )
            callback(tour)
        }
        SpaceH(h = 40)
    }
}

@Composable
fun BottomSheetOtherService(
    value: MutableList<OtherServices>, callback: (OtherServices) -> Unit
) {

    val descripton = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    fun checkState(): Boolean {
        return descripton.value.isNotEmpty()
    }

    val stateButton = remember {
        mutableStateOf(checkState())
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(white, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
            .padding(horizontal = 16.dp), horizontalAlignment = Alignment.Start
    ) {
        TextView(
            text = R.string.create_other_services,
            font = Font(R.font.poppins_semibold),
            textSize = 18,
            color = appColor
        )
        SpaceH(h = 16)
        TextView(
            text = R.string.description,
            font = Font(R.font.poppins_regular),
        )
        SpaceH(h = 4)
        InputValue(
            value = descripton.value,
            textSize = 14.sp,
            maxLines = 100,
            hintColor = red,
            modifier = Modifier.border(
                width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
            ),
            hint = context.getString(R.string.not_empty)
        ) {
            descripton.value = it
            stateButton.value = checkState()
        }
        SpaceH(h = 16)

        AppButton(
            text = context.getString(R.string.save),
            modifier = Modifier,
            isEnable = stateButton.value
        ) {
            val tour = OtherServices(
                "${value.size + 1}", descripton.value
            )
            callback(tour)
        }
        SpaceH(h = 40)
    }
}

@Composable
fun TourTransportItem(tourTime: Transport, index: Int, content: (Int) -> Unit) {
    val context = LocalContext.current
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextView(
            text = tourTime.description,
            textSize = 14,
            modifier = Modifier
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp))
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )
        SpaceW(w = 4)
        Box(
            Modifier
                .size(32.dp)
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.round_remove_24),
                contentDescription = "Remove",
                modifier = Modifier.size(24.dp)
            )
            Box(
                Modifier
                    .matchParentSize()
                    .onClick { content.invoke(index) })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelectPlace(
    modifier: Modifier, calback: (
        Places
    ) -> Unit
) {

    val expanded = remember { mutableStateOf(false) }
    val itemSelectVehicle =
        remember { mutableStateOf(viewModels.listPlaces.value.toMutableList().get(0)) }
    Box(
        modifier = modifier
            .height(41.dp)
            .background(
                color = transparent,
            )
            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray),
        contentAlignment = Alignment.CenterStart
    ) {
        ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {
            expanded.value = !expanded.value
        }) {
            Row(
                Modifier
                    .menuAnchor()
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(Modifier.weight(1f)) {
                    RoundedImage(
                        data = itemSelectVehicle.value.Image!!.get(0),
                        modifier = Modifier.size(24.dp),
                        shape = CircleShape
                    )
                    SpaceW(w = 4)
                    TextView(
                        text = itemSelectVehicle.value.placeName, modifier = Modifier
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "Selected",
                    Modifier.size(24.dp)
                )
            }

            ExposedDropdownMenu(expanded = expanded.value,
                modifier = Modifier.padding(start = 6.dp),
                onDismissRequest = { expanded.value = false }) {
                viewModels.listPlaces.value.toMutableList().forEach { item ->
                    DropdownMenuItem(text = {
                        Row(Modifier.fillMaxWidth()) {
                            RoundedImage(
                                data = item.Image!!.get(0),
                                modifier = Modifier.size(24.dp),
                                shape = CircleShape
                            )
                            SpaceW(w = 4)
                            TextView(
                                text = item.placeName, modifier = Modifier
                            )
                        }
                    }, onClick = {
                        itemSelectVehicle.value = item
                        expanded.value = false
                        calback.invoke(item)
                    })
                }
            }
        }
    }
}

@Composable
fun TourTimeItem(item: TourTime, index: Int, remove: (Int) -> Unit) {
    val context = LocalContext.current
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextView(
            text = context.getString(R.string.from) + item.startTime + " " + context.getString(
                R.string.to
            ) + item.endTime,
            textSize = 14,
            modifier = Modifier
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp))
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        )
        SpaceW(w = 4)
        Box(
            Modifier
                .size(32.dp)
                .background(Color(0xFFE4F7FF), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.round_remove_24),
                contentDescription = "Remove",
                modifier = Modifier.size(24.dp)
            )
            Box(
                Modifier
                    .matchParentSize()
                    .onClick { remove.invoke(index) })
        }
    }
}

fun checkTime(startDate: String, endDate: String): Boolean {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try {
        val start = dateFormat.parse(startDate)
        val end = dateFormat.parse(endDate)
        start!! <= end
    } catch (e: Exception) {
        false
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAddTime(
    value: MutableList<TourTime>, callback: (TourTime) -> Unit
) {

    val timeName = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val startTime = remember {
        mutableStateOf("")
    }
    val endTime = remember {
        mutableStateOf("")
    }

    fun checkState(): Boolean {
        if (timeName.value.isEmpty()) {
            return false
        }
        if (startTime.value.isEmpty()) {
            return false
        }
        if (endTime.value.isEmpty()) {
            return false
        }
        if (!checkTime(startTime.value, endTime.value)) {
            return false
        }
        return true
    }

    val stateButton = remember {
        mutableStateOf(checkState())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(white, shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
            .padding(horizontal = 16.dp), horizontalAlignment = Alignment.Start
    ) {
        TextView(
            text = R.string.create_new_time,
            font = Font(R.font.poppins_semibold),
            textSize = 18,
            color = appColor
        )
        SpaceH(h = 16)
        TextView(
            text = R.string.title,
            font = Font(R.font.poppins_regular),
        )
        InputValue(
            value = timeName.value,
            textSize = 14.sp,
            maxLines = 100,
            hintColor = red,
            modifier = Modifier.border(
                width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
            ),
            hint = context.getString(R.string.not_empty)
        ) {
            timeName.value = it
            stateButton.value = checkState()
        }

        SpaceH(h = 6)
        TextView(
            text = R.string.start_time,
            font = Font(R.font.poppins_regular),
        )

        Row(
            Modifier
                .wrapContentHeight()
                .background(whiteSmoke, shape = RoundedCornerShape(8.dp))
                .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray)
        ) {
            DatePickerBox(Modifier.fillMaxWidth()) {
                startTime.value = it
                stateButton.value = checkState()
            }
        }
        SpaceH(h = 6)
        TextView(
            text = R.string.start_time,
            font = Font(R.font.poppins_regular),
        )

        Row(
            Modifier
                .wrapContentHeight()
                .background(whiteSmoke, shape = RoundedCornerShape(8.dp))
                .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray)
        ) {
            DatePickerBox(Modifier.fillMaxWidth()) {
                endTime.value = it
                stateButton.value = checkState()
            }
        }

        SpaceH(h = 16)

        AppButton(
            text = context.getString(R.string.save),
            modifier = Modifier,
            isEnable = stateButton.value
        ) {
            val tour = TourTime(
                "${value.size + 1}", timeName.value, startTime.value, endTime.value, 10
            )
            callback(tour)

        }

        SpaceH(h = 40)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelectVehicle(
    modifier: Modifier, calback: (
        Vehicle
    ) -> Unit
) {
    val context = LocalContext.current

    val expanded = remember { mutableStateOf(false) }

    val listVehicle = remember {
        mutableStateOf(
            getNewVehicle(context)
        )
    }

    val itemSelectVehicle = remember { mutableStateOf(listVehicle.value.toMutableList().get(0)) }
    Box(
        modifier = modifier
            .height(41.dp)
            .background(
                color = transparent,
            )
            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray),
        contentAlignment = Alignment.CenterStart
    ) {
        ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {
            expanded.value = !expanded.value
        }) {
            Row(
                Modifier
                    .menuAnchor()
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(Modifier.weight(1f)) {
                    Icon(
                        painter = painterResource(
                            id = itemSelectVehicle.value.image!!.toString().toInt()
                        ), contentDescription = "Icon", tint = appColor
                    )
                    TextView(
                        text = itemSelectVehicle.value.vhName, modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "Selected",
                    Modifier.size(24.dp)
                )
            }

            ExposedDropdownMenu(expanded = expanded.value,
                modifier = Modifier.padding(start = 6.dp),
                onDismissRequest = { expanded.value = false }) {
                listVehicle.value.toMutableList().forEach { item ->
                    DropdownMenuItem(text = {
                        Row(Modifier.fillMaxWidth()) {
                            Icon(
                                painter = painterResource(
                                    id = item.image as Int
                                ), contentDescription = "Icon", tint = appColor
                            )
                            TextView(
                                text = item.vhName, modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }, onClick = {
                        itemSelectVehicle.value = item
                        expanded.value = false
                        calback.invoke(item)
                    })
                }
            }
        }
    }
}



