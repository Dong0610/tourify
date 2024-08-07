package dong.datn.tourify.service


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dong.datn.tourify.R
import dong.datn.tourify.utils.ORDER

class TourTimeService : Service() {

    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove Firestore listener when service is destroyed
        listenerRegistration?.remove()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
