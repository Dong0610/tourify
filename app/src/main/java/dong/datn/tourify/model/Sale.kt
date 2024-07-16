package dong.datn.tourify.model

import dong.duan.travelapp.model.BaseModel

enum class SaleType {
    RUNNING,     // Đang diễn ra
    EXPIRED,     // Đã hết hạn
    CLOSED,      // Đã đóng
    CANCELLED,   // Đã hủy
    PAUSED,      // Tạm dừng
}

data class Sale(
    var saleId: String = "",
    var saleName: String = "",
    var saleImage: String = "",
    var percent: Float = 0f,
    var startDate: String = "",
    var endDate: String = "",
    var status: SaleType = SaleType.RUNNING,
    var description: String = "",
) : BaseModel<Sale>()

fun generateSales(): List<Sale> {
    val sales = mutableListOf<Sale>()

    // Sale 1
    sales.add(
        Sale(
            saleId = "sale_id_001",
            saleName = "Khuyến mãi mùa hè",
            saleImage = "https://firebasestorage.googleapis.com/v0/b/travelapp-datn.appspot.com/o/SALE%2Fimg_sale_1.jpg?alt=media&token=312db878-cebf-4a8f-8e4f-5a4763d7493a",
            percent = 20f,
            startDate = "01/07/2024",
            endDate = "31/07/2024",
            status = SaleType.RUNNING,
            description = "Chương trình giảm giá dành cho mùa hè, áp dụng từ ngày 1/7 đến ngày 31/7."
        )
    )

    // Sale 2
    sales.add(
        Sale(
            saleId = "sale_id_002",
            saleName = "Giảm giá cuối năm",
            saleImage = "https://firebasestorage.googleapis.com/v0/b/travelapp-datn.appspot.com/o/SALE%2Fimg_sale_2.jpg?alt=media&token=36ea26c7-b921-4ed0-a150-10602be43636",
            percent = 30f,
            startDate = "01/12/2024",
            endDate = "31/12/2024",
            status = SaleType.RUNNING,
            description = "Chương trình giảm giá cuối năm đã hết hạn."
        )
    )

    // Sale 3
    sales.add(
        Sale(
            saleId = "sale_id_003",
            saleName = "Khuyến mãi đặc biệt",
            saleImage = "https://firebasestorage.googleapis.com/v0/b/travelapp-datn.appspot.com/o/SALE%2Fimg_sale_3.png?alt=media&token=0ed468fa-1fcf-401e-9dad-ca5523ae695f",
            percent = 15f,
            startDate = "15/08/2024",
            endDate = "30/08/2024",
            status = SaleType.RUNNING,
            description = "Chương trình khuyến mãi đặc biệt đã đóng."
        )
    )

    // Sale 4
    sales.add(
        Sale(
            saleId = "sale_id_004",
            saleName = "Sale giữa năm",
            saleImage = "https://firebasestorage.googleapis.com/v0/b/travelapp-datn.appspot.com/o/SALE%2Fimg_sale_4.png?alt=media&token=80ce7aa8-6272-4316-8052-19d9065480d7",
            percent = 25f,
            startDate = "01/06/2024",
            endDate = "30/06/2024",
            status = SaleType.CANCELLED,
            description = "Chương trình giảm giá giữa năm đã bị hủy."
        )
    )

    // Sale 5
    sales.add(
        Sale(
            saleId = "sale_id_005",
            saleName = "Khuyến mãi nghỉ lễ",
            saleImage = "https://firebasestorage.googleapis.com/v0/b/travelapp-datn.appspot.com/o/SALE%2Fimg_sale_5.jpg?alt=media&token=5b3f983f-6291-432d-b183-b3b9c0749e63",
            percent = 10f,
            startDate = "01/05/2024",
            endDate = "03/05/2024",
            status = SaleType.RUNNING,
            description = "Chương trình giảm giá nghỉ lễ tạm dừng."
        )
    )

    return sales
}