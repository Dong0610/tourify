package dong.datn.tourify.screen.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dong.datn.tourify.app.AppViewModel
import dong.datn.tourify.widget.IconView2
import dong.datn.tourify.widget.ViewParentContent
import dong.datn.tourify.widget.navigationTo

@Composable
fun DetailImageScreen(nav: NavController, viewModel: AppViewModel) {
    val currentImage = remember {
        mutableStateOf(viewModel.currentImage.value)
    }
    ViewParentContent(onBack = {
        if (viewModel.prevScreen.value != "") {
            nav.navigationTo(viewModel.prevScreen.value)
        } else {
            nav.popBackStack()
        }

    }) {
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            if (currentImage.value != "") {
                ZoomableImage(data = currentImage.value,true)
            }
            IconView2(modifier = Modifier, icon = Icons.Rounded.KeyboardArrowLeft) {
                nav.popBackStack()
            }

        }
    }
}

@Composable
fun ZoomableImage(data: Any, b: Boolean=true) {

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = data)
            .apply(block = fun ImageRequest.Builder.() {
                allowHardware(false)
            }).build()
    )
    if(b){
        val scale = remember { mutableStateOf(1f) }
        val offset = remember { mutableStateOf(Offset.Zero) }

        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale.value *= zoomChange
            offset.value = Offset(
                x = offset.value.x + panChange.x,
                y = offset.value.y + panChange.y
            )
        }




        Box(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = state), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value,
                        translationX = offset.value.x,
                        translationY = offset.value.y
                    )
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
    else
    {
        val scale = remember { mutableStateOf(1f) }
        val offset = remember { mutableStateOf(Offset.Zero) }

        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale.value *= zoomChange
            offset.value = Offset(
                x = offset.value.x + panChange.x,
                y = offset.value.y + panChange.y
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = state), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }

}