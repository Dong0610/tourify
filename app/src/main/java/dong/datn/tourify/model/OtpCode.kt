package dong.datn.tourify.model

import dong.datn.tourify.utils.timeNow
import dong.duan.travelapp.model.BaseModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OtpCode : BaseModel<OtpCode> {

    var timeCreate: String = timeNow()
    var otpCode = ""

    constructor()

    constructor(timeCreate: String, otpCode: String,) {
        this.timeCreate = timeCreate
        this.otpCode = otpCode
    }

    fun isExpired(): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val createdTime = LocalDateTime.parse(timeCreate, formatter)
            val currentTime = LocalDateTime.now()
            val duration = Duration.between(createdTime, currentTime)
            return duration.toMinutes() > 3
        } else {
            val formatter = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss", java.util.Locale.getDefault())
            val createdTime = formatter.parse(timeCreate)
            val currentTime = java.util.Date()
            val duration = currentTime.time - createdTime.time
            return duration > 3 * 60 * 1000
        }
    }
}