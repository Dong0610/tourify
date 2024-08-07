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
    RUNNING,
    CANCELED, FINISH,
    WAITING
}
enum class PaymentStatus {
    PREPAYMENT,
    FINISHED,
    REFUND
}

class Order(
    var orderID: String =  "",
    var userOrderId: String = "",
    var tourName: String =  "",
    var tourTime: TourTime= TourTime(),
    var tourID: String =  "",
    var orderDate: String =  "",
    var saleId: String = "",
    var tourPrice: Double = 0.0,
    var prePayment: Double = 0.0,
    var totalPrice: Double = 0.0,
    var refund: Double = 0.0,
    var paymentStatus: PaymentStatus = PaymentStatus.PREPAYMENT,
    var staffConfirmId: String = "",
    var note: String = "",
    var adultCount: Int = 0,
    var childCount: Int = 0,
    var tollder: Int = 0,
    var invoiceUrl: String = "",
    var paymentMethod: PaymentMethod =  PaymentMethod(),
    var orderStatus: OrderStatus =  OrderStatus.PENDING,
    var cancelReason: String =  "",
    var cancelDate: String =  "",
    var cancelBefore: String =  "",
): BaseModel<Order>()