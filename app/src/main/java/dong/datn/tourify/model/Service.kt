package dong.duan.travelapp.model

import dong.datn.tourify.R
import dong.datn.tourify.app.ContextProvider.Companion.appContext


data class Transport(
    var id: String =  "",
    val description: String =  "",
) : BaseModel<Transport>()


data class OtherServices(
    var id: String =  "",
    val description: String =  "",
) : BaseModel<OtherServices>()

data class Food(
    val id: String =  "",
    val count: Int =  -1,
    val price: Int =  0,
    val description: String =  ""
) : BaseModel<Food>()

data class Accommodation(
    var id: String =  "",
    val description: String =  ""
) : BaseModel<Accommodation>()

data class IncludeService(
    var id: String =  "",
    var accommodation: Accommodation =  Accommodation(),
    var transport: MutableList<Transport> =  mutableListOf(),
    var food: MutableList<Food> =  mutableListOf(),
    var otherServices: MutableList<OtherServices> =  mutableListOf()
) : BaseModel<IncludeService>()

data class NoteService(var id: String =  "", var content: String =  "") : BaseModel<NoteService>()
data class Service(
    var id: String =  "",
    var time: String =  "",
    var road: String =  "",
    var vehicle: String =  "",
    var name: String =  "",
    var introduce: String =  "",
    var includeService: IncludeService =  IncludeService(),
    var noteService: MutableList<NoteService> =  mutableListOf()
) : BaseModel<Service>()


val services = Service(
    id = "1",
    "3 Ngày hai đêm",
    "HÀ NỘI - DU LỊCH HẢI TIẾN - THANH HÓA - HÀ NỘI",
    appContext.getString(R.string.str_ct_plane),
    "Tour du lịch Hải Tiến Thanh Hóa:",
    "Bất cứ du khách nào đã từng đến du lịch Hải Tiến" +
            " - Thanh Hóa. Cũng đều cảm nhận được sự hấp dẫn của vùng" +
            " biển Hải Tiến, chính là vẻ đẹp hoang sơ mà ít nơi có được" +
            ". Khu du lịch Hải Tiến được nhiều du khách ví như Trà Cổ thứ hai của Việt " +
            "Nam. Biển Hải Tiến như một nét chấm phá trong bức tranh sơn thủy hữu tình của mảnh đất xứ Thanh.",
    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Nghỉ 02 đêm tại khách sạn khu trung tâm: 2 - 4 khách/phòng (Lẻ nam hoặc nữ ngủ 3 khách)."),
        transport= mutableListOf(
            Transport("0","+ Xe ô tô du lịch đời mới, máy lạnh đưa đón theo lịch trình Hà Nội - Du Lịch Hải Tiến - Thanh Hóa - Hà Nội.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Các bữa ăn chính theo hành trình: 05 bữa chính 150.000 đ/bữa/khách."),
            Food("1",1,30000,"+ Các bữa ăn phụ: 02 bữa phụ ăn buffet tại khách sạn hoặc 30.000đ/khách")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Trẻ em dưới 5 tuổi: Miễn giá tour bố mẹ tự túc lo các chi phí ăn, tham quan (nếu có) cho bé."),
        NoteService("Note2","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note3","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)

