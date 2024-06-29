package dong.datn.tourify.model

import dong.duan.travelapp.model.BaseModel

data class Chat(
    val idChat: String,
    var content: String,
    var staffId: String = "",
    var clientId: String = "",
    var staffImage: String = "",
    var clientImage: String = "",
    var time: String = "",
    var chatType=
    var listImages: MutableList<String> = mutableListOf()
):BaseModel<Chat>()