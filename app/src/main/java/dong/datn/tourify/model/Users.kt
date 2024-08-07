package dong.duan.travelapp.model

data class Users(
    var UId: String = "",
    var Name: String = "",
    var Address: String = "",
    var Password: String = "",
    var Email: String = "",
    var PhoneNumber: String = "",
    var Birthday: String = "",
    var CreateTime: String = "",
    var Image: String = "",
    var Gender:String= "",
    var Token: String = "",
    var Role: String = "",
    var LoginType: String = "Account",
    var ConversionChatId: String = "",
    var History: ArrayList<String> = arrayListOf()
) : BaseModel<Users>()

