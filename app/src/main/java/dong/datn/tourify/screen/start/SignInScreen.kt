package dong.datn.tourify.screen.start

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.screen.client.MainActivity
import dong.datn.tourify.screen.staff.StaffActivity
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.findActivity
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.ui.theme.transparent
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.SpaceW
import dong.datn.tourify.utils.heightPercent
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.RoundedImage
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.VerScrollView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.onClick
import dong.duan.ecommerce.library.showToast
import dong.duan.livechat.widget.InputValue

@Composable
fun SignInScreen(navController: NavHostController, viewModels: AppViewModel, onFacebook:() -> Unit,onGoogle:() -> Unit) {


    val context = LocalContext.current
    var email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val stateButton = remember { mutableStateOf<Int?>(null) }
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

            VerScrollView(modifier = Modifier.weight(1f)) {

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
                        value = email.value, hint = "Email", keyboardType = KeyboardType.Email
                    ) {
                        email.value = it
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    InputValue(
                        value = password.value,
                        hint = "Password",
                        keyboardType = KeyboardType.Password
                    ) {
                        password.value = it
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
                            viewModels.fogetPassword(email.value)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))


                    AppButton(
                        text = context.getString(R.string.sign_in),
                        modifier = Modifier,
                        stateButton.value,
                        isEnable = true
                    ) {
                        stateButton.value = 0
                        if (email.value.isEmpty()) {
                            showToast("Enter email")
                        } else if (email.value.isEmpty()) {
                            showToast("Enter password")
                        } else {
                            viewModels.signInWithEmailPassword(
                                email.value, password.value
                            ) { state ->
                                if (state == 1) {
                                    if (authSignIn!!.Role == "Staff") {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                StaffActivity::class.java
                                            )
                                        )
                                        context.findActivity()!!.finishActivity(1);
                                    } else {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                MainActivity::class.java
                                            )
                                        )
                                        context.findActivity()!!.finishActivity(1);
                                    }

                                }

                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        TextView(
                            text = context.getString(R.string.don_have_account),
                            modifier = Modifier,
                            color = textColor()
                        )
                        SpaceW(w = 6)
                        TextView(
                            text = context.getString(R.string.sign_up),
                            modifier = Modifier,
                            color = appColor
                        ) {
                            navController.navigate(AccountScreen.SignUpScreen.route) {
                                popUpTo(0)
                            }
                        }
                    }
                    SpaceH(h = 12)
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Spacer(
                            Modifier
                                .weight(1f)
                                .padding(start = 32.dp)
                                .background(lightGrey)
                                .height(1.dp)
                        )
                        SpaceW(6)
                        TextView(
                            text = context.getString(R.string.or),
                            modifier = Modifier,
                            font = Font(R.font.poppins_medium),
                            color = textColor()
                        )
                        SpaceW(6)
                        Spacer(
                            Modifier
                                .weight(1f)
                                .padding(end = 32.dp)
                                .background(lightGrey)
                                .height(1.dp)
                        )

                    }

                    SpaceH(h = 12)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        RoundedImage(
                            data = R.drawable.img_facebook, modifier = Modifier
                                .size(46.dp)
                                .onClick {
                                    onFacebook.invoke()
                                }, shape = CircleShape
                        )
                        SpaceW(w = 12)
                        RoundedImage(
                            data = R.drawable.img_google, modifier = Modifier
                                .background(transparent)
                                .size(46.dp)
                                .onClick {
                                    onGoogle.invoke()
                                }, shape = CircleShape
                        )
                    }

                }
            }
        }
    }

}
