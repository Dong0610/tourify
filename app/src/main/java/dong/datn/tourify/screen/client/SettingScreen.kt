package dong.datn.tourify.screen.client

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dong.datn.tourify.R
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.app.isPostNotification
import dong.datn.tourify.ui.theme.appColor
import dong.datn.tourify.ui.theme.lightGrey
import dong.datn.tourify.ui.theme.textColor
import dong.datn.tourify.utils.changeTheme
import dong.datn.tourify.widget.IconView
import dong.datn.tourify.widget.TextView
import dong.datn.tourify.widget.ViewParent
import dong.datn.tourify.widget.navigationTo
import dong.datn.tourify.widget.onClick

@Composable
fun SettingScreen(nav: NavController, viewModels: AppViewModel) {
    val context = LocalContext.current
    
    val isDarkTheme = remember {
        mutableStateOf(currentTheme == -1)
    }
    ViewParent(onBack = {
        nav.navigationTo(ClientScreen.ProfileScreen.route)
    }) {
        Column(
            Modifier
                .fillMaxSize()

        ) {
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
                    context.getString(R.string.setting), Modifier.weight(1f), textSize = 20,
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

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextView(
                    context.getString(R.string.change_theme),
                    Modifier
                        .wrapContentSize(),
                    textSize = 16,
                    textColor(context),
                    font = Font(R.font.poppins_regular),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))
                SwitchThemeValue(context)

            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextView(
                    context.getString(R.string.notification_post),
                    Modifier
                        .wrapContentSize(),
                    textSize = 16,
                    textColor(context),
                    font = Font(R.font.poppins_regular),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))
                SwitchNotificationValue()

            }


        }

    }
}

@Composable
fun SwitchThemeValue(context: Context) {
    val check = currentTheme == -1
    val checked = remember { mutableStateOf(check) }
    CustomSwitch(
        checked = checked.value,
        modifier = Modifier,
        onCheckedChange = {
            checked.value = it
            if (checked.value) {
                changeTheme(-1, context)
            } else {
                changeTheme(1, context)
            }
        }
    )
}

@Composable
fun SwitchNotificationValue() {
    val checked = remember { mutableStateOf(isPostNotification) }
    CustomSwitch(
        checked = checked.value,
        modifier = Modifier,
        onCheckedChange = {
            checked.value = it
            if (checked.value) {
                isPostNotification = true
            } else {
                isPostNotification = false
            }
        }
    )
}

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val switchWidth = 46.dp
    val switchHeight = 24.dp
    val thumbRadius = 12.dp
    val thumbPadding = 2.dp

    val thumbPosition = animateDpAsState(
        targetValue = if (checked) switchWidth - thumbRadius * 1.8f - thumbPadding else thumbPadding,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            .size(switchWidth, switchHeight)
            .clip(RoundedCornerShape(switchHeight / 2))
            .background(if (checked) appColor else lightGrey)
            .onClick { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbRadius * 1.8f)
                .padding(thumbPadding)
                .offset(x = thumbPosition.value)
                .background(Color.White, CircleShape)
        )
    }
}
