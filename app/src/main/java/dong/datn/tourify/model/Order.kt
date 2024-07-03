package dong.datn.tourify.model

import dong.duan.travelapp.model.BaseModel
import dong.duan.travelapp.model.PaymentMethod
import dong.duan.travelapp.model.TourTime

data class PaymentMethod(
    val id: String,
    val name: String,
    val image: Any
)

data class UserPayment(var count: Int = 0, var Price: Double = 0.0)

enum class OrderStatus {
    PENDING,
    PAID,
    CANCELED, FINISH,
}

class Order(
    var orderID: String =  "",
    var userOrder: String =  "",
    var tourName: String =  "",
    var tourTime: TourTime= TourTime(),
    var tourID: String =  "",
    var orderDate: String =  "",
    var totalPrice: Double =  0.0,
    var note: String =  "",
    var adultPayment: UserPayment =  UserPayment(),
    var childPayment: UserPayment =  UserPayment(),
    var paymentMethod: PaymentMethod =  PaymentMethod(),
    var orderStatus: OrderStatus =  OrderStatus.PENDING,
    var cancelReason: String =  "",
    var cancelDate: String =  "",
    var cancelBefore: String =  "",
): BaseModel<Order>()