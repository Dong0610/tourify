package dong.datn.tourify.model

import dong.duan.travelapp.model.BaseModel
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
):BaseModel<Notification>()