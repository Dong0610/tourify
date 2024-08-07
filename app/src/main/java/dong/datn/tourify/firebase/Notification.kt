package dong.datn.tourify.firebase

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dong.datn.tourify.R
import dong.duan.travelapp.model.Tour
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class ImageDownloader(private val listener: OnImageDownloadedListener) {

    fun downloadImage(imageUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = downloadBitmap(imageUrl)
            withContext(Dispatchers.Main) {
                listener.onImageDownloaded(bitmap)
            }
        }
    }

    private suspend fun downloadBitmap(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            var bitmap: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            bitmap
        }
    }

    interface OnImageDownloadedListener {
        fun onImageDownloaded(bitmap: Bitmap?)
    }
}

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "example_channel_id"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Example Channel"
            val descriptionText = "This is an example channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun sendNotificationLogin() {
        val customView = RemoteViews(context.packageName, R.layout.layout_noti_loggin)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_app)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(customView)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else {
                notify(getRandomIntInRange(1000, 9999), builder.build())
            }

        }
    }


    @SuppressLint("RemoteViewLayout")
    fun sendNotificationBooking(tour: Tour) {
        val id= getRandomIntInRange(1, 9)
        ImageDownloader(object : ImageDownloader.OnImageDownloadedListener {
            override fun onImageDownloaded(bitmap: Bitmap?) {
                Log.d("Download", "onImageDownloaded successfully")
                val customView =
                    RemoteViews(context.packageName, R.layout.layout_noti_booking).apply {
                        setTextViewText(R.id.tourName, tour.tourName)
                        setImageViewBitmap(R.id.image_view, bitmap)
                    }

                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_logo_app)
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(customView)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    } else {
                        notify(id, builder.build())
                    }

                }
            }
        }).downloadImage(tour.tourImage[0])
    }


    fun getRandomIntInRange(start: Int, end: Int): Int {
        return Random.nextInt(start, end)
    }

    @Composable
    fun NotificationComposeContent(title: String, content: String) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = title, fontSize = 18.sp)
            Text(text = content, fontSize = 14.sp)
        }
    }
}
