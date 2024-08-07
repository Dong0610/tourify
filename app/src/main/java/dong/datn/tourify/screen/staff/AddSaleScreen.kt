package dong.datn.tourify.screen.staff

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import dong.datn.tourify.model.Sale
import dong.datn.tourify.model.SaleType
import dong.datn.tourify.model.getNewVehicle
import dong.datn.tourify.screen.client.DatePickerBox
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.black
import dong.datn.tourify.ui.theme.gray
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.ui.theme.white
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.CallbackType
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.utils.opacity
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParentContent
import dong.datn.tourify.widget.navigationTo
import dong.duan.livechat.widget.InputValue
import dong.duan.travelapp.model.Tour

@SuppressLint("MutableCollectionMutableState", "UnrememberedMutableState")
@Composable
fun AddSaleScreen(nav: NavController, scope: () -> Unit) {
    val context = LocalContext.current

    val uriImage = remember { mutableStateOf<Any?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uris: Uri? ->
        if (uris != null) {
            uriImage.value = uris
        }
    }

    val indexState = remember { mutableStateOf(0) }
    val stateBottomSheet = remember { mutableStateOf(false) }

    val saleName = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
    }
    val discount = remember {
        mutableStateOf(0f)
    }
    val startDate = remember {
        mutableStateOf("")
    }
    val endDate = remember {
        mutableStateOf("")
    }

    val buttonsState = remember { mutableStateOf(false) }
    val saleType = remember { mutableStateOf<SaleType>(SaleType.RUNNING) }

    val createSuccess = remember { mutableStateOf(Tour()) }
    fun checkData(): Boolean {
        return uriImage.value != null && checkTime(
            startDate.value,
            endDate.value
        ) && description.value.length > 0 && saleName.value.length > 0 && discount.value > 0f

    }

    val focus = LocalFocusManager.current
    ViewParentContent(onBack = { }) {

        VerScrollView(
            Modifier
                .fillMaxSize()
                .padding(WindowInsets.ime.asPaddingValues())
        ) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextView(text = R.string.choose_image_title, font = Font(R.font.poppins_medium))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .heightPercent(28f)
                            .padding(4.dp)
                            .border(
                                1.dp,
                                color = appColor,
                                shape = RoundedCornerShape(16.dp)
                            ), contentAlignment = Alignment.TopEnd
                    ) {
                        if (uriImage.value != null) {
                            RoundedImage(
                                modifier = Modifier.matchParentSize(),
                                data = uriImage.value!!,
                                shape = RoundedCornerShape(16.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    black.opacity(0.5f),
                                    shape = RoundedCornerShape(8.dp)
                                ), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_change_circle),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "Remove",
                                tint = whiteSmoke
                            )
                            Box(modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    imagePickerLauncher.launch("image/*")
                                })
                        }

                    }
                }

                TextView(text = R.string.sale_name, font = Font(R.font.poppins_medium))
                InputValue(
                    value = saleName.value,
                    textSize = 13.sp,
                    maxLines = 100,
                    hintColor = red,
                    modifier = Modifier.border(
                        width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                    ),
                    hint = context.getString(R.string.not_empty)
                ) {
                    saleName.value = it
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
                TextView(text = R.string.discount, font = Font(R.font.poppins_medium))

                InputValue(
                    value = discount.value.toString(),
                    textSize = 13.sp,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.border(
                        width = 1.dp, shape = RoundedCornerShape(8.dp), color = gray
                    ),

                    maxLines = 1,
                    hintColor = red,
                    hint = context.getString(R.string.not_empty),
                ) {
                    discount.value = try {
                        val cleanedInput = it.replace(Regex("[^\\d.]"), "")
                        val integerPart = cleanedInput.split(".")[0]
                        val parsedValue = if (integerPart.isEmpty()) {
                            0f
                        } else {
                            integerPart.toFloatOrNull() ?: 0f
                        }
                        if (parsedValue > 100) 100f else parsedValue
                    } catch (e: IllegalArgumentException) {
                        0f
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
                        TextView(text = R.string.start_time, font = Font(R.font.poppins_medium))
                        DatePickerBox(Modifier.fillMaxWidth()) {
                            startDate.value = it
                            focus.clearFocus()
                            buttonsState.value = checkData()
                        }
                    }
                    SpaceW(w = 8)
                    Column(Modifier.weight(0.5f)) {
                        TextView(text = R.string.end_time, font = Font(R.font.poppins_medium))
                        DatePickerBox(Modifier.fillMaxWidth()) {
                            endDate.value = it
                            focus.clearFocus()
                            buttonsState.value = checkData()
                        }
                    }
                }
                SpaceH(h = 8)

                TextView(text = R.string.status, font = Font(R.font.poppins_medium))
                DropdownSelectStatus(Modifier) {
                    saleType.value = it
                }
                SpaceH(h = 16)

                AppButton(
                    text = context.getString(R.string.save),
                    modifier = Modifier,
                    isEnable = buttonsState.value
                ) {
                    viewModels.loadingState.value = true
                    val saleProgram = Sale(
                        saleId = "",
                        saleName = saleName.value,
                        saleImage = "",
                        percent = discount.value,
                        startDate = startDate.value,
                        endDate = endDate.value,
                        status = saleType.value,
                        description = description.value,
                    )
                    viewModels.saveNewSales(saleProgram, uriImage.value) { state, value ->
                        viewModels.loadingState.value = false
                        if (state == 1) {
                            if (value != null) {
                                viewModels.listSalesManager.value =
                                    viewModels.listSalesManager.value.apply {
                                        add(0, value)
                                    }
                            }
                        }
                        viewModels.showDialog(
                            context.getString(R.string.create_success_sale),
                            title = context.getString(R.string.success),
                            type = CallbackType.SUCCESS
                        ) {
                            nav.navigationTo(StaffScreen.SaleManagerScreen.route)
                        }
                    }
                }
                SpaceH(h = 40)
            }
        }
    }
}
fun getSaleTypeString(context: Context, saleType: SaleType): String {
    return when (saleType) {
        SaleType.RUNNING -> context.getString(R.string.sale_type_running)
        SaleType.EXPIRED -> context.getString(R.string.sale_type_expired)
        SaleType.CLOSED -> context.getString(R.string.sale_type_closed)
        SaleType.CANCELLED -> context.getString(R.string.sale_type_cancelled)
        SaleType.PAUSED -> context.getString(R.string.sale_type_paused)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelectStatus(
    modifier: Modifier, calback: (
        SaleType
    ) -> Unit
) {
    val context = LocalContext.current

    val expanded = remember { mutableStateOf(false) }

    val listVehicle = remember {
        mutableStateOf(
            getNewVehicle(context)
        )
    }





    val itemSelectSaleType = remember { mutableStateOf(SaleType.RUNNING) }
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

                    TextView(
                        text = getSaleTypeString(LocalContext.current,itemSelectSaleType.value), modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "Selected",
                    Modifier.size(24.dp)
                )
            }
            val saleTypes = SaleType.entries
            ExposedDropdownMenu(expanded = expanded.value,
                modifier = Modifier.padding(start = 6.dp),
                onDismissRequest = { expanded.value = false }) {
                saleTypes.toMutableList().forEach { item ->
                    DropdownMenuItem(text = {
                        Row(Modifier.fillMaxWidth()) {
                            TextView(
                                text = getSaleTypeString(LocalContext.current,item), modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }, onClick = {
                        itemSelectSaleType.value = item
                        expanded.value = false
                        calback.invoke(item)
                    })
                }
            }
        }
    }
}


