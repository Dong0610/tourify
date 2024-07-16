package dong.datn.tourify.model

import dong.datn.tourify.R
import dong.duan.travelapp.model.BaseModel

data class Comment(
    var commentId: String = "",
    var uId: String = "",
    var content: String = "",
    var timeComment: String = "",
    var tourId: String = "",
    var ratting:Float=0f,
    var emoji: Emoji = Emoji.NONE,
    var commentType: CommentType= CommentType.COMMENT,
    var listImage: MutableList<Any> = mutableListOf(),
    var response: MutableList<Reply> = mutableListOf()
) : BaseModel<Comment>()
data class Reply(
    var commentId: String = "",
    var uId: String = "",
    var content: String = "",
    var parentId: String = "",
    var timeComment: String = "",
    var tourId: String = "",
    var emoji: Emoji = Emoji.NONE,
) : BaseModel<Comment>()
enum class Emoji {
    NONE, LIKE, LOVE, DISLIKE, ANGRY
}
enum class CommentType {
    COMMENT, RESPONSE
}

fun createCommentList(): List<Comment> {
    return listOf(
        Comment(
            commentId = "1",
            uId = "VFETLnkQnFZl16ebO7LpJMkR0lC3",
            content = "This is the first comment",
            timeComment = "10:00 AM",
            ratting = 5f,
            emoji = Emoji.LIKE,
            listImage = mutableListOf(R.drawable.img_test_data_1,R.drawable.img_test_data_2),

        ),
        Comment(
            commentId = "2",
            uId = "VFETLnkQnFZl16ebO7LpJMkR0lC3",
            content = "This is the second comment",
            timeComment = "11:00 AM",
            ratting = 3f,
            emoji = Emoji.ANGRY
        ),
        Comment(
            commentId = "3",
            uId = "VFETLnkQnFZl16ebO7LpJMkR0lC3",
            content = "This is the third comment",
            timeComment = "12:00 PM",
            ratting = 2f,
            emoji = Emoji.DISLIKE
        ),
        Comment(
            commentId = "4",
            uId = "VFETLnkQnFZl16ebO7LpJMkR0lC3",
            content = "This is the fourth comment",
            timeComment = "01:00 PM",
            ratting = 1f,
            emoji = Emoji.NONE
        )
    )
}
