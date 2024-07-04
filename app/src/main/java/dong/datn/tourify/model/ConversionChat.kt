package dong.datn.tourify.model

import android.os.Build
import androidx.annotation.RequiresApi
import dong.duan.travelapp.model.BaseModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

data class ConversionChat(
    var converId: String = "",
    var clientName: String = "",
    var clientId: String = "",
    var clientImage: String = "",
    var lastSenderId:String="",
    var lastMessageSender: String = "",
    var lastMessageTime: String = "",
    var lastMessageClientRead: Boolean = false,
    var lastMessageStaffRead: Boolean = false,
    ): BaseModel<ConversionChat>()
