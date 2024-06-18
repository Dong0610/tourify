package dong.duan.travelapp.model

import com.google.gson.Gson
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


open class BaseModel<T> : Serializable {

    fun toJson(): String {
        val gson = Gson()
        return if (this == null) "null" else gson.toJson(this)
    }

    fun fromJson(jsonString: String?): T {
        val gson = Gson()
        return gson.fromJson(jsonString, this.javaClass) as T
    }

    fun formatTimes(timestamp: Any?): String {
        if (timestamp is Long) {
            val date = Date(timestamp)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            return sdf.format(date)
        }
        return ""
    }

    val timeNow: String
        get() {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            return sdf.format(Date())
        }
}
