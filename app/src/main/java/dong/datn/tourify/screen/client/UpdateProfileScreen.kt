package dong.datn.tourify.screen.client

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
import dong.datn.tourify.ui.theme.whiteSmoke
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.onClick
import dong.duan.livechat.widget.InputValue
import java.util.Calendar

@Composable
fun UpdateProfileScreen(nav: NavController, viewModels: AppViewModel) {
    val context = LocalContext.current
    

    val userName = remember { mutableStateOf(TextFieldValue(authSignIn?.Name ?: "")) }
    val addresses = remember { mutableStateOf(TextFieldValue(authSignIn?.Address ?: "")) }
    val phoneNumber = remember { mutableStateOf(TextFieldValue(authSignIn?.PhoneNumber ?: "")) }
    val email = remember { mutableStateOf(TextFieldValue(authSignIn?.Email ?: "")) }

    val imageUri = remember {
        mutableStateOf(
            if (authSignIn?.Image.isNullOrEmpty()) null else Uri.parse(authSignIn?.Image)
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
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
        Column(Modifier.fillMaxSize()) {
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
                    context.getString(R.string.update_profile), Modifier.weight(1f), textSize = 20,
                    appColor, font = Font(R.font.poppins_semibold), textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(50.dp))
            }

            VerScrollView {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.heightPercent(5f))

                    Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                        Box(Modifier.size(150.dp), contentAlignment = Alignment.BottomEnd) {
                            RoundedImage(
                                data = if (imageUri.value == null) R.drawable.img_user else imageUri.value,
                                Modifier.size(150.dp),
                                shape = CircleShape
                            )
                            IconView(
                                modifier = Modifier.size(40.dp),
                                icon = Icons.Rounded.Edit,
                                tint = appColor,
                                icSize = 24
                            ) {
                                launcher.launch("image/*")
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    TextView(
                        text = userName.value.text,
                        modifier = Modifier.fillMaxWidth(),
                        color = textColor(context),
                        textAlign = TextAlign.Center,
                        textSize = 20,
                        font = Font(R.font.poppins_semibold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TextView(
                        text = email.value.text,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = darkGray,
                        textSize = 18,
                        font = Font(R.font.poppins_medium)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    TextView(
                        text = context.getString(R.string.user_name),
                        modifier = Modifier,
                        font = Font(R.font.poppins_semibold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InputValue(
                        value = userName.value.text,
                        hint = context.getString(R.string.user_name),
                        keyboardType = KeyboardType.Password
                    ) {
                        userName.value = TextFieldValue(it)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    TextView(
                        text = context.getString(R.string.address),
                        modifier = Modifier,
                        font = Font(R.font.poppins_semibold)
                    )
                    InputValue(
                        value = addresses.value.text,
                        hint = context.getString(R.string.address),
                        keyboardType = KeyboardType.Password
                    ) {
                        addresses.value = TextFieldValue(it)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    TextView(
                        text = context.getString(R.string.phone_number),
                        modifier = Modifier,
                        font = Font(R.font.poppins_semibold)
                    )
                    InputValue(
                        value = phoneNumber.value.text,
                        hint = context.getString(R.string.phone_number),
                        keyboardType = KeyboardType.Password
                    ) {
                        phoneNumber.value = TextFieldValue(it)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(Modifier.fillMaxSize(1f)) {
                        Column (Modifier.weight(1f)){
                            TextView(
                                text = context.getString(R.string.sex),
                                modifier = Modifier,
                                font = Font(R.font.poppins_semibold)
                            )
                            DropdownMenuBoxSex(Modifier.fillMaxSize()){

                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))
                        Column (Modifier.weight(1f)){
                            TextView(
                                text = context.getString(R.string.birthday),
                                modifier = Modifier,
                                font = Font(R.font.poppins_semibold)
                            )
                           DatePickerBox(Modifier.fillMaxSize()){

                           }
                        }

                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    AppButton(
                        text = context.getString(R.string.save), modifier = Modifier, null
                    ) {

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBoxSex(modifier: Modifier,calback:(String)->Unit) {
    val context = LocalContext.current
    val coffeeDrinks = arrayOf(
        context.getString(R.string.male),
        context.getString(R.string.fe_male),
        context.getString(R.string.undisclosed)
    )
    val expanded = remember { mutableStateOf(false) }

    val selectedText = remember { mutableStateOf(coffeeDrinks[0]) }

    Box(
        modifier = modifier
            .height(50.dp)
            .background(
                color = if (currentTheme == 1) whiteSmoke else lightGrey,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = {
                expanded.value = !expanded.value
            }
        ) {
            Row(
                Modifier
                    .menuAnchor()
                    .padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                TextView(
                    text = selectedText.value,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = "Selected", Modifier.size(32.dp))
            }

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText.value = item
                            expanded.value = false
                            calback.invoke(selectedText.value)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun DatePickerBox(modifier: Modifier, callback: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val selectedDate = remember { mutableStateOf("") }
    val datePickerDialog = remember {
        android.app.DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            selectedDate.value = formattedDate
            callback.invoke(formattedDate)
        }, year, month, day)
    }

    Box(
        modifier = modifier
            .height(50.dp)
            .background(
                color = if (currentTheme == 1) whiteSmoke else lightGrey,
                shape = RoundedCornerShape(12.dp)
            )
            .onClick { datePickerDialog.show() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (selectedDate.value.isEmpty()) "Select Date" else selectedDate.value)
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date", Modifier.size(24.dp))
        }
    }
}