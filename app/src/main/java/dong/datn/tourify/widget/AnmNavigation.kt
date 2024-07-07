package dong.datn.tourify.widget

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dong.datn.tourify.app.viewModels
import java.io.Serializable


@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.animComposable(
    route: String,
    delay: Int = 700,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {


    composable(route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(delay)
            )

        },
        exitTransition = {

            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(delay)
            )

        },
        popEnterTransition = {

            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(delay)
            )


        },
        popExitTransition = {

            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(delay)
            )


        }) {
        BackHandler(true) {

        }
        content(it)
    }

}


fun NavController.navigationTo(route: String) {
    this.navigate(route) {
        graph.startDestinationRoute?.let { route ->
            popUpTo(route) { saveState = true }
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.navigationTo(route: String,backScreen:String?=null) {
    this.navigate(route) {
        if(backScreen != null){
            viewModels?.prevScreen!!.value = backScreen
        }
        graph.startDestinationRoute?.let { route ->
            popUpTo(route) { saveState = true }
        }
        launchSingleTop = true
        restoreState = true
    }
}
