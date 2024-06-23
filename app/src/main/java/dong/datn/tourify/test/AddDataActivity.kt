package dong.datn.tourify.test

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dong.datn.tourify.firebase.Firestore
import dong.datn.tourify.model.Places
import dong.datn.tourify.model.createFamousPlaces
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.VerScrollView

class AddDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourifyTheme {
                Scaffold(Modifier.fillMaxSize()) { padding ->
                    DefaultPreview(padding)
                }
            }
        }
    }
}


@Composable
fun DefaultPreview(padding: PaddingValues) {
    VerScrollView(Modifier.padding(padding)) {
        Column {
            AppButton(text = "Places", modifier = Modifier) {
                putPlaceData {
                    Log.d("Put", "put: ${it}")
                }
            }
        }
    }
}

fun putPlaceData(callback: (String) -> Unit) {
    createFamousPlaces().forEach { place ->
        Firestore.pushAsync<Places>("PLACES")
            .withId { place.placeID = it }
            .set(place)
            .finish {
                callback.invoke(place.placeID)
            }
            .error {
                Log.d("Put", "error: $it")
            }
            .execute()
    }
}
