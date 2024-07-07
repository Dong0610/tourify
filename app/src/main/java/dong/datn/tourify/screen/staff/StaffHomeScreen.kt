package dong.datn.tourify.screen.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.ui.theme.lightBlue
import dong.datn.tourify.widget.ViewParent

@Composable
fun HomeStaffScreen(nav: NavController, viewModel: AppViewModel,opneDrawer: ()->Unit){
    val coroutineScope = rememberCoroutineScope()
    ViewParent(
        modifier = Modifier
        .fillMaxSize(1f)
        .background(lightBlue)) {

        Text(text = "HomeStaff")

    }
}






