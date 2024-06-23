package dong.datn.tourify.model

import dong.datn.tourify.R
import dong.duan.travelapp.model.BaseModel

data class Comment(
    var commentId: String = "",
    var uId: String = "",
    var content: String = "",
    var timeComment: String = "",
    var ratting:Int=0,
    var emoji: Emoji = Emoji.NONE,
    var listImage: MutableList<Any> = mutableListOf(),
    var response: MutableList<Comment> = mutableListOf()
) : BaseModel<Comment>()

enum class Emoji {
    NONE, LIKE, LOVE, DISLIKE, ANGRY
}

fun createCommentList(): List<Comment> {
    return listOf(
        Comment(
            commentId = "1",
            uId = "VFETLnkQnFZl16ebO7LpJMkR0lC3",
            content = "This is the first comment",
            timeComment = "10:00 AM",
            ratting = 5,
            emoji = Emoji.LIKE,
            listImage = mutableListOf(R.drawable.img_test_data_1,R.drawable.img_test_data_2),
            response = mutableListOf(
                Comment(
                    commentId = "1.1",
                    uId = "bbLcZjz0hKNmTp2MwTuhOZJTGH02",
                    content = "Response to the first comment",
                    timeComment = "10:15 AM",
                    ratting = 4,
                    emoji = Emoji.LOVE
                )
            )
        ),
        Comment(
            commentId = "2",
            uId = "VFETLnkQnFZl16ebO7LpJMkR0lC3",
            content = "This is the second comment",
            timeComment = "11:00 AM",
            ratting = 3,
            emoji = Emoji.ANGRY
        ),
        Comment(
            commentId = "3",
            uId = "VFETLnkQnFZl16ebO7LpJMkR0lC3",
            content = "This is the third comment",
            timeComment = "12:00 PM",
            ratting = 2,
            emoji = Emoji.DISLIKE
        ),
        Comment(
            commentId = "4",
            uId = "VFETLnkQnFZl16ebO7LpJMkR0lC3",
            content = "This is the fourth comment",
            timeComment = "01:00 PM",
            ratting = 1,
            emoji = Emoji.NONE
        )
    )
}
