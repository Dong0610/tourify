package dong.datn.tourify.screen.client

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.widget.ViewParent

@Composable
fun WishListScreen(navController: NavHostController, viewModels: AppViewModel) {
    ViewParent {
        Text(text = "WishListScreen")
    }
}