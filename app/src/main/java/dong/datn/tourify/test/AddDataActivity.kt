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
import dong.datn.tourify.app.viewModels
import dong.datn.tourify.model.createFamousPlaces
import dong.datn.tourify.model.generateSales
import dong.datn.tourify.ui.theme.TourifyTheme
import dong.datn.tourify.utils.SALES
import dong.datn.tourify.utils.SpaceH
import dong.datn.tourify.utils.TOUR
import dong.datn.tourify.widget.AppButton
import dong.datn.tourify.widget.VerScrollView
import dong.duan.ecommerce.library.showToast
import dong.duan.travelapp.model.RandomTour

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
            AppButton(text = "Places", modifier = Modifier, isEnable = true) {
                putPlaceData {
                    Log.d("Put", "put: ${it}")
                }
            }
            SpaceH(h = 12)
            AppButton(text = "Tour", modifier = Modifier, isEnable = true) {
             
                putTourData {
                    Log.d("Put", "put: ${it}")
                }
            }
            SpaceH(h = 12)
            AppButton(text = "Sale", modifier = Modifier, isEnable = true) {
             
                putSaleData {
                    Log.d("Put", "put: ${it}")
                }
            }
        }
    }
}

fun putSaleData(function: (String) -> Int) {
    generateSales().forEach { place ->
        viewModels.firestore.collection("${SALES}")
            .document("${place.saleId}")
            .set(place).addOnSuccessListener {
                function.invoke(place.saleId)
            }.addOnFailureListener {
                Log.d("Put", "error: $it")
            }
    }
}

fun putTourData(function: (String) -> Unit) {

    RandomTour.dataRandom().forEach { place ->
        viewModels.firestore.collection("${TOUR}")
            .document("${place.tourID}")
            .set(place).addOnSuccessListener {
                function.invoke(place.tourID)
            }.addOnFailureListener {
                Log.d("Put", "error: $it")
            }
    }
}

fun putPlaceData(function: (String) -> Unit) {
    createFamousPlaces().forEach { place ->
        viewModels.firestore.collection("PLACES")
            .document("${place.placeID}")
            .set(place).addOnSuccessListener {
                function.invoke(place.placeID)
            }.addOnFailureListener {
                Log.d("Put", "error: $it")
            }
    }
}
