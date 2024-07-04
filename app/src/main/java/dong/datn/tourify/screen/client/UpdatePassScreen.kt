package dong.datn.tourify.screen.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.lawnGreen
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.red
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.utils.Space
import dong.datn.tourify.utils.checkPassword
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick
import dong.duan.livechat.widget.InputValue

@Composable
fun UpdatePassScreen(nav: NavController, viewModel: AppViewModel) {
   
    val isShowDialog = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val currentPass = remember {
        mutableStateOf("")
    }
    val newPass = remember {
        mutableStateOf("")
    }
    val reNewPass = remember {
        mutableStateOf("")
    }
    val loadingState = remember {
        mutableStateOf(2)
    }
    val checkingState = remember {
        mutableStateOf(false)
    }
    ViewParent(onBack = {
        nav.navigationTo(ClientScreen.ProfileScreen.route)
    }) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    nav.navigationTo(ClientScreen.ProfileScreen.route)
                }

                TextView(
                    context.getString(R.string.forget_pass), Modifier.weight(1f), textSize = 20,
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
            Spacer(modifier = Modifier.height(12.dp))

            VerScrollView(Modifier.padding(horizontal = 16.dp)) {
                Column(Modifier.fillMaxSize()) {
                    TextView(
                        text = context.getString(R.string.enter_your_pass),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium),
                        color = textColor(),
                        textAlign = TextAlign.Start
                    )
                    Space(h = 6)
                    InputValue(value =currentPass.value) {
                        currentPass.value = it
                        checkingState.value =
                            checkPassword(currentPass.value, newPass.value, reNewPass.value)

                    }

                    TextView(
                        text = context.getString(R.string.enter_new_pass),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium),
                        color = textColor(),
                        textAlign = TextAlign.Start
                    )
                    Space(h = 6)
                    InputValue(value = newPass.value) {
                        newPass.value = it
                        checkingState.value =
                            checkPassword(currentPass.value, newPass.value, reNewPass.value)
                    }
                    TextView(
                        text = context.getString(R.string.enter_pass_again),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium),
                        color = textColor(),
                        textAlign = TextAlign.Start
                    )
                    Space(h = 6)
                    InputValue(value = reNewPass.value) {
                        reNewPass.value = it
                        checkingState.value =
                            checkPassword(currentPass.value, newPass.value, reNewPass.value)
                    }


                    Spacer(modifier = Modifier.height(12.dp))
                    AppButton(
                        text = context.getString(R.string.update),
                        modifier = Modifier,
                        loadingState.value,
                        isEnable = checkingState.value
                    ) {
                        loadingState.value = 0
                        viewModel.updatePassword(newPass.value) {
                            loadingState.value = it;
                            isShowDialog.value=true
                        }
                    }
                }
            }
        }
    }

    if (loadingState.value != 0) {
        UpdateResultDialog(showDialog = isShowDialog, state = loadingState.value) {
            nav.navigationTo(ClientScreen.ProfileScreen.route)
        }
    }


}

@Composable
fun UpdateResultDialog(showDialog: MutableState<Boolean> = mutableStateOf(false), state: Int, onDismiss: () -> Unit) {

    if (showDialog.value) {
        val color = if (state == 1) lawnGreen else red
        val content = if (state == 1) {
            LocalContext.current.getString(R.string.your_pass_has_updated)
        } else {
            LocalContext.current.getString(R.string.error)
        }

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(1f)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FaIcon(
                            faIcon = if (state == 1) FaIcons.CheckCircle else FaIcons.ExclamationCircle,
                            tint = color,
                            size = 32.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = LocalContext.current.getString(if (state == 1) R.string.confirm else R.string.error_title),
                            modifier = Modifier,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                            color = color
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = content,
                        modifier = Modifier,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(Modifier.fillMaxWidth(1f)) {
                        Box(
                            Modifier
                                .onClick {
                                    showDialog.value = false
                                }
                                .height(40.dp)
                                .weight(1f)
                                .background(lightGrey, shape = RoundedCornerShape(40.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = LocalContext.current.getString(R.string.cancel),
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.poppins_medium))
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            Modifier
                                .onClick {
                                    showDialog.value = false
                                    onDismiss()
                                }
                                .height(40.dp)
                                .weight(1f)
                                .background(color, shape = RoundedCornerShape(40.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = LocalContext.current.getString(R.string.Ok),
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.poppins_medium))
                            )
                        }
                    }
                }
            }
        }
    }
}

































