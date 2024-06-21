package dong.datn.tourify.model

import kotlin.random.Random

data class Notification(
    var notiId: String = "",
    var senderId: String = "",
    var title: String = "",
    var content: String="",
    var time: String = "",
    var isRead: Boolean = false,
    var type: String = "",
    var image: Any? = null,
    var link: String = "",
    var route: String = "",
)
fun getRandomString(length: Int): String {
    val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}

fun getRandomBoolean(): Boolean {
    return Random.nextBoolean()
}

fun getRandomNotification(): Notification {
    return Notification(
        notiId = getRandomString(10),
        senderId = getRandomString(10),
        title = getRandomString(15),
        content = getRandomString(20),
        time = "2023-01-01T00:00:00Z", // or use a date library to get a random date
        isRead = getRandomBoolean(),
        type = getRandomString(5),
        image = null, // Assuming image is optional and can be null
        link = getRandomString(10),
        route = getRandomString(10)
    )
}

fun  getListNotification():MutableList<Notification>{
    return mutableListOf<Notification>().apply {
        add(getRandomNotification())
        add(getRandomNotification())
        add(getRandomNotification())
        add(getRandomNotification())
        add(getRandomNotification())
        add(getRandomNotification())
    }

}