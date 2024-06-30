package dong.datn.tourify.screen.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.utils.Space
import dong.datn.tourify.utils.checkEmail
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.duan.livechat.widget.InputValue

@Composable
fun ForgetPassScreen(nav: NavController, viewModel: AppViewModel) {
    viewModel.isKeyboardVisible.value = true
    val context = LocalContext.current
    val email = remember {
        mutableStateOf("")
    }
    ViewParent(onBack = {
        nav.navigationTo(ClientScreen.SettingScreen.route)
    }) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    nav.navigationTo(ClientScreen.SettingScreen.route)
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
                        text = context.getString(R.string.title_foget_pass),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium),
                        color = textColor(),
                        textAlign = TextAlign.Center
                    )

                    Space(h = 12)

                    InputValue(value = email.value, hint = context.getString(R.string.email)) {
                        email.value = it
                    }

                    Spacer(modifier =Modifier.height(12.dp))
                    AppButton(
                        text = context.getString(R.string.continues),
                        modifier = Modifier,
                        isDisable = !checkEmail(email.value)
                    ) {
                        viewModel.sendVerificationEmail(email.value,nav)
                    }
                }
            }
        }
    }
}


@Composable
fun EnterOtpScreen(nav: NavController, viewModel: AppViewModel) {
    viewModel.isKeyboardVisible.value = true
    val context = LocalContext.current
    val email = remember {
        mutableStateOf("")
    }

    var titelMessage = context.getString(R.string.tt_mess_1) + viewModel.currentEmailVerify+ context.getString(R.string.tt_mess_2)

    ViewParent(onBack = {
        nav.navigationTo(ClientScreen.SettingScreen.route)
    }) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    nav.navigationTo(ClientScreen.SettingScreen.route)
                }

                TextView(
                    context.getString(R.string.otp_verify), Modifier.weight(1f), textSize = 20,
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
                        text = context.getString(R.string.title_foget_pass),
                        modifier = Modifier,
                        font = Font(R.font.poppins_medium),
                        color = textColor(),
                        textAlign = TextAlign.Center
                    )

                    Space(h = 12)

                    InputValue(value = email.value, hint = context.getString(R.string.email)) {
                        email.value = it
                    }

                    Spacer(modifier =Modifier.height(12.dp))
                    AppButton(
                        text = context.getString(R.string.continues),
                        modifier = Modifier,
                        isDisable = !checkEmail(email.value)
                    ) {
                        viewModel.sendVerificationEmail(email.value,nav)
                    }
                }
            }
        }
    }
}







































