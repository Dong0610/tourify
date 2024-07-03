package dong.datn.tourify.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dong.datn.tourify.app.authSignIn
import dong.datn.tourify.app.currentTheme
import dong.datn.tourify.ui.theme.findActivity

@Composable
fun getScreenWidth(): Int {
    val context = LocalContext.current
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

@Composable
fun getScreenHeight(): Int {
    val context = LocalContext.current
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}
@Suppress("DEPRECATION")
fun noTitlebar(activity: Activity) {
    activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    activity.window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
}


fun checkEmail(email: String): Boolean {
    val regexPattern = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return regexPattern.matches(email)
}






fun checkPassword(password: String, newPassword: String, reNewPassword: String): Boolean {
    val currentPass = authSignIn?.Password ?: return false

    return when {
        password != currentPass -> false
        newPassword.length < 6 -> false
        newPassword != reNewPassword -> false
        else -> true
    }
}


@Composable
fun widthPercent(percent: Float): Int {
    return ((getScreenWidth() / 100) * percent).toInt()
}

@Composable
fun heightPercent(percent: Float): Int {
    return ((getScreenHeight() / 100) * percent).toInt()
}


@Composable
fun Int.toDp(): Dp {
    return (this / LocalContext.current.resources.displayMetrics.density).dp
}

@Composable
fun Float.toDp(): Dp {
    return (this / LocalContext.current.resources.displayMetrics.density).dp
}
@Composable
fun dpToInt(dp: Dp): Int {
    val density = LocalDensity.current
    return with(density) { dp.toPx().toInt() }
}

fun Modifier.heightPercent(percent: Float): Modifier = composed {
    val screenHeight = getScreenHeight()
    val heightInDp = (screenHeight * percent / 100).toDp()
    this.then(Modifier.height(heightInDp))
}

fun Modifier.widthPercent(percent: Float): Modifier = composed {
    val screenWidth = getScreenWidth()
    val widthInDp = (screenWidth * percent / 100).toDp()
    this.then(Modifier.width(widthInDp))
}
fun changeTheme(themeId: Int,context: Context) {
    val nightMode = when (themeId) {
        themeId -> AppCompatDelegate.MODE_NIGHT_NO
        themeId -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    AppCompatDelegate.setDefaultNightMode(nightMode)
    context.findActivity()?.recreate()
    currentTheme = themeId
}
