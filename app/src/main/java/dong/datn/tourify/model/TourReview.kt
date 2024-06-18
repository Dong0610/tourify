package dong.duan.travelapp.model

data class TourReview(
    var ReviewId: String? = "",
    var UsersId: String? = "",
    var Stars: Float? = -1f,
    var Comment: String? = "",
    var ReviewImg: MutableList<String>? = mutableListOf(),
    var ReviewTime: String? = ""
):BaseModel<TourReview>()