package dong.datn.tourify.model

import android.os.Build
import androidx.annotation.RequiresApi
import dong.duan.travelapp.model.BaseModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

data class ConversionChat(
    var converIs: String = "",
    var tourName: String = "",
    var clientId: String = "",
    var staffId: String = "",
    var clientName: String = "",
    var staffName: String = "",
    var tourImage: String = "",
    var lastMessageSender: String = "",
    var lastMessageTime: String = "",
    var lastMessageClientRead: Boolean = false,
    var lastMessageStaffRead: Boolean = false,
    ): BaseModel<ConversionChat>()

fun generateRandomConversionChats(size: Int): List<ConversionChat> {
    val randomChats = mutableListOf<ConversionChat>()
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    for (i in 1..size) {
        randomChats.add(
            ConversionChat(
                converIs = "Conver_${Random.nextInt(1000, 9999)}",
                tourName = "Tour_${Random.nextInt(1000, 9999)}",
                clientId = "Client_${Random.nextInt(1000, 9999)}",
                staffId = "Staff_${Random.nextInt(1000, 9999)}",
                clientName = "ClientName_${Random.nextInt(1000, 9999)}",
                staffName = "StaffName_${Random.nextInt(1000, 9999)}",
                tourImage = "Image_${Random.nextInt(1000, 9999)}.jpg",
                lastMessageSender = if (Random.nextBoolean()) "client" else "staff",
                lastMessageTime = LocalDateTime.now().minusDays(Random.nextLong(1, 30))
                    .format(dateTimeFormatter),
                lastMessageClientRead = Random.nextBoolean(),
                lastMessageStaffRead = Random.nextBoolean()
            )
        )
    }

    return randomChats
}