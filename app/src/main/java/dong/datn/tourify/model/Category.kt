package dong.duan.travelapp.model

data class Category(
    var Id: String =  "",
    var Name: String =  "",
    var Image: Any =  ""
) : BaseModel<Category>()