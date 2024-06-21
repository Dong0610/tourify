package dong.datn.tourify.model

import dong.duan.travelapp.model.BaseModel

data class Places(
    var placeID: String = "",
    var placeName: String = "",
    var Image: Any? = null,
    var tourCount: Int = 0,
    var createdTime:String=""
) : BaseModel<Places>()

