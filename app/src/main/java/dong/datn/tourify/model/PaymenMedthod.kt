package dong.duan.travelapp.model

data class PaymentMethod(var id: String = "", var name: String = "", var image: Any? = null) :
    BaseModel<PaymentMethod>()
