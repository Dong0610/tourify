package dong.datn.tourify.screen.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.HorScrollView
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.InputValue

@Composable
fun SignInScreen(navController: NavHostController, viewModels: AppViewModel) {
    val context = LocalContext.current
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val stateButton = remember { mutableStateOf(0) }
    ViewParent(onBack = {
        navController.navigate(AccountScreen.SignUpScreen.route) {
            popUpTo(0)
        }
    }) {
        Column(
            Modifier
                .fillMaxSize(1f)
                .padding(16.dp)
        ) {

            Row(
                Modifier
                    .fillMaxWidth(1f)
            ) {
                IconView(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                    navController.navigate(AccountScreen.SignUpScreen.route) {
                        popUpTo(0)
                    }
                }
            }

            HorScrollView {

                Column {
                    Spacer(modifier = Modifier.heightPercent(15f))
                    Row(
                        Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center
                    ) {
                        TextView(
                            text = context.getString(R.string.sign_in),
                            textSize = 24,
                            modifier = Modifier,
                            font = Font(
                                R.font.poppins_bold
                            )
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center
                    ) {
                        TextView(
                            text = context.getString(R.string.signin_to_continue),

                            textSize = 18,
                            color = Color(0xFF7D848D),
                            modifier = Modifier,
                            font = Font(
                                R.font.poppins_regular
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    InputValue(
                        value = email.value.text, hint = "Email", keyboardType = KeyboardType.Email
                    ) {
                        email.value = TextFieldValue(it)
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    InputValue(
                        value = password.value.text,
                        hint = "Password",
                        keyboardType = KeyboardType.Password
                    ) {
                        password.value = TextFieldValue(it)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Absolute.Right,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextView(
                            text = context.getString(R.string.forget_pass),
                            modifier = Modifier,
                            color = appColor
                        ) {
                            viewModels.fogetPassword(email.value.text)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    AppButton(
                        text = context.getString(R.string.sign_in), modifier = Modifier, null
                    ) {
                        stateButton.value = 0

                        if (email.value.toString().isEmpty()) {
                            showToast("Enter email")

                        } else if (email.value.toString().isEmpty()) {
                            showToast("Enter password")
                        } else {
                            viewModels.signInWithEmailPassword(
                                email.value.toString(), password.value.toString()
                            ) {
                                if (it == 1) {
                                    stateButton.value == 1
                                } else {
                                    stateButton.value = -1
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}