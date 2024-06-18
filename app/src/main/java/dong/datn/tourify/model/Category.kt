package dong.duan.travelapp.model

data class Category(
    var Id: String? = null,
    var Name: String? = null,
    var Image: Any? = null
) : BaseModel<Category>()