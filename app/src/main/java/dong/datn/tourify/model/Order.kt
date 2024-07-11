package dong.datn.tourify.model

import dong.duan.travelapp.model.BaseModel
import dong.duan.travelapp.model.PaymentMethod
import dong.duan.travelapp.model.TourTime

data class PaymentMethod(
    val id: String,
    val name: String,
    val image: Any
)

enum class OrderStatus {
    PENDING,
    PAID,
    CANCELED, FINISH,
}

class Order(
    var orderID: String =  "",
    var userOrderId: String = "",
    var tourName: String =  "",
    var tourTime: TourTime= TourTime(),
    var tourID: String =  "",
    var orderDate: String =  "",
    var price: Double =  0.0,
    var saleId: String = "",
    var staffConfirmId: String = "",
    var note: String =  "",
    var adultCount: Int = 0,
    var childCount: Int = 0,
    var paymentMethod: PaymentMethod =  PaymentMethod(),
    var orderStatus: OrderStatus =  OrderStatus.PENDING,
    var cancelReason: String =  "",
    var cancelDate: String =  "",
    var cancelBefore: String =  "",
): BaseModel<Order>()