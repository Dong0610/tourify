package dong.datn.tourify.model

import dong.datn.tourify.app.authSignIn
import dong.duan.travelapp.model.BaseModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class ChatType {
    IMAGE,
    MESSAGE
}

data class Chat(
    val idChat: String,
    var content: String,
    var staffId: String = "",
    var clientId: String = "",
    var staffImage: String = "",
    var senderId: String = "",
    var clientImage: String = "",
    var time: String = "",
    var chatType: ChatType = ChatType.MESSAGE,
    var listImages: MutableList<String> = mutableListOf()
) : BaseModel<Chat>()


fun generateChatData(count: Int): List<Chat> {
    val chatList = mutableListOf<Chat>()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    for (i in 1..count) {
        val chat = (if (i % 2 == 0) authSignIn!!.UId else "")?.let {
            Chat(
                idChat = "chat_$i",
                content = "Sample content for chat $i",
                staffId = "staff_$i",
                clientId = "client_$i",
                senderId = it,
                staffImage = "http://example.com/staff_$i.png",
                clientImage = "http://example.com/client_$i.png",
                time = LocalDateTime.now().format(formatter),
                chatType = if (i % 2 == 0) ChatType.IMAGE else ChatType.MESSAGE,
                listImages = if (i % 2 == 0) mutableListOf("http://example.com/image_$i.png") else mutableListOf()
            )
        }
        if (chat != null) {
            chatList.add(chat)
        }
    }
    return chatList
}