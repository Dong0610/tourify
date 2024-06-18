package dong.datn.tourify.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseMessageService :FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "From: ${token}")
    }
}