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



//https://tour.pro.vn/tour-co-to
val services1 = Service(
    id = "tour_id_001",
    "3 ngày 2 đêm",
    "HÀ NỘI - VÂN ĐỒN - DU LỊCH ĐẢO CÔ TÔ - HÀ NỘI",
    appContext.getString(R.string.str_ct_train),
    "Tour du lịch Cô Tô - Quảng Ninh:",
    "Tour du lịch Cô Tô là hành trình du lịch lý tưởng để nghỉ " +
            "dưỡng mùa hè, thư giãn, nghỉ ngơi cùng bạn bè, người thân. " +
            "Có quãng đường di chuyển từ trung tâm TP Hà Nội khoảng 260km, bằng cả đường " +
            "bộ và đường biển. Phù hợp với những bạn muốn có chuyến đi ít ngày, kinh phí không quá cao." +
            " Nhưng vẫn có được một kỳ nghỉ hè tuyệt vời, với những bức ảnh ‘sống ảo’ cùng biển xanh," +
            " cát trắng, nắng vàng.",
    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Nghỉ 02 đêm tại khách sạn khu trung tâm đảo Cô Tô: 2 - 4 khách/phòng (Lẻ nam hoặc nữ ngủ 3 khách)."),
        transport= mutableListOf(
            Transport("0","+ Xe du lịch đời mới, máy lạnh đưa đón theo lịch trình đón tiễn ga Giáp bát và phục vụ suốt hành trình tại: Hà Nội - Cảng Ao Tiên, Vân Đồn - Hà Nội.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 05 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 02 bữa ăn phụ: 30.000 vnđ/suất/bữa. (Tiêu chuẩn 3* - Ăn sáng buffet tại khách sạn).")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống 1 chai/ khách/ngày.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Trẻ em dưới 5 tuổi: Miễn giá tour bố mẹ tự túc lo các chi phí ăn, tham quan (nếu có) cho bé."),
        NoteService("Note2","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note3","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//https://tour.pro.vn/tour-cat-ba
val services2 = Service(
    id = "tour_id_002",
    "3 ngày 2 đêm",
    "HÀ NỘI - DU LỊCH ĐẢO CÁT BÀ - VỊNH LAN HẠ - HÀ NỘI",
    appContext.getString(R.string.str_ct_car),
    "Tour du lịch Cát Bà Hải Phòng:",
    "Du lịch Đảo Cát Bà, hòn đảo lớn nhất trong quần thể Vịnh Hạ Long. " +
            "Nằm trong quản lý địa giới hành chính TP. Hải Phòng. " +
            "Là địa điểm du lịch nổi tiếng bậc nhất của Hải Phòng, với nhiều bãi tắm biển đẹp, " +
            "điểm tham quan du lịch hấp dẫn có thể kể đến như: Vịnh Lan Hạ, Rừng Quốc Gia, Đảo Khỉ, " +
            "Pháo Đài Thần Công, Động Trung Trang, hang quân y, làng cổ Việt Hải...các bãi biển đẹp Cát Cò 1, 2, 3," +
            " Tùng Thu và còn nhiều bãi tắm nhỏ nằm ven các đảo nhỏ trong vịnh...",
    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Nghỉ 02 đêm tại khách sạn khu trung tâm đảo Cát Bà: 2 - 4 khách/phòng (Lẻ nam hoặc nữ ngủ 3 khách)."),
        transport= mutableListOf(
            Transport("0","+ Xe ô tô du lịch đời mới, máy lạnh đưa đón theo lịch trình như trên: Hà Nội - Bến phà Đồng Bài - Cát Bà - Hà Nội.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 05 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 02 bữa ăn phụ: 30.000 vnđ/suất/bữa. (Tiêu chuẩn 3* - Ăn sáng buffet tại khách sạn).")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0", "+ Vé phà Đồng Bài - Cát Bà khứ hồi hai chiều."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Trẻ em dưới 5 tuổi: Miễn giá tour bố mẹ tự túc lo các chi phí ăn, tham quan (nếu có) cho bé."),
        NoteService("Note2","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note3","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//https://tour.pro.vn/quan-lan
val services3 = Service(
    id = "tour_id_003",
    "2 ngày 1 đêm",
    "HÀ NỘI - VÂN ĐỒN - ĐẢO QUAN LẠN - MINH CHÂU - HÀ NỘI",
    appContext.getString(R.string.str_ct_train),
    "Tour du lịch đảo Quan Lạn - Quảng Ninh:",
    "Du lịch ĐẢO Quan Lạn có hai bãi tắm đẹp nổi tiếng là biển Quan Lạn và Minh Châu, " +
            "trải nghiệm đi tour Quan Lạn du khách thỏa sức tận hưởng không gian biển đầy nắng," +
            " gió, mang hương vị mằn mặn của biển khơi, khám phá cảnh quan thiên nhiên hoang sơ trên đảo với những rừng thông, " +
            "rừng trâm, đồi cát trắng bạt ngàn. Những điểm tham quan chụp hình, check in vô cùng kỳ ảo.",
    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Nghỉ 01 đêm tại khách sạn khu trung tâm đảo Quan Lạn: 2 - 4 khách/phòng (Lẻ nam hoặc nữ ngủ 3 khách)."),
        transport= mutableListOf(
            Transport("0","+ Xe ô tô du lịch đời mới, máy lạnh đưa đón theo lịch trình đón tiễn ga Giáp bát và phục vụ suốt hành trình tại: Hà Nội  - Cảng Ao Tiên - Vân Đồn - Quảng Ninh - Hà Nội.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 03 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 01 bữa ăn phụ: 30.000 vnđ/suất/bữa. (Tiêu chuẩn 3* - Ăn sáng buffet tại khách sạn).")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Trẻ em dưới 5 tuổi: Miễn giá tour bố mẹ tự túc lo các chi phí ăn, tham quan (nếu có) cho bé."),
        NoteService("Note2","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note3","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//https://tour.pro.vn/ha-noi-sam-son-ha-noi
val services4 = Service(
    id = "tour_id_004",
    "2 ngày 1 đêm",
    "HÀ NỘI - DU LỊCH SẦM SƠN - THANH HÓA - HÀ NỘI",
    appContext.getString(R.string.str_ct_car),
    "Tour du lịch Sầm Sơn - Thanh Hóa:",
    "Du lịch biển Sầm Sơn, Thanh Hóa có lẽ không còn xa lạ với du khách, " +
            "mỗi năm có hàng triệu lượt du khách đến với Sầm Sơn tắm biển, nghỉ ngơi." +
            " Vùng biển còn có nhiều điểm tham quan hấp dẫn có thể kết hợp trong" +
            " chuyến đi như: Hòn Trống Mái, Chùa Cô Tiên, Đền Độc Cước, Thành Nhà Hồ...",
    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Nghỉ 01 đêm tại khách sạn khu trung tâm : 2 - 4 khách/phòng (Lẻ nam hoặc nữ ngủ 3 khách)."),
        transport= mutableListOf(
            Transport("0","+ Xe du lịch đời mới, máy lạnh đưa đón theo lịch trình tour: Hà Nội - Sầm Sơn - Thanh Hóa - Hà Nội.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 03 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 01 bữa ăn phụ: 30.000 vnđ/suất/bữa. (Tiêu chuẩn 3* - Ăn sáng buffet tại khách sạn).")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Trẻ em dưới 5 tuổi: Miễn giá tour bố mẹ tự túc lo các chi phí ăn, tham quan (nếu có) cho bé."),
        NoteService("Note2","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note3","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//https://tour.pro.vn/tra-co-mong-cai
val services5 = Service(
    id = "tour_id_005",
    "3 ngày 2 đêm",
    "HÀ NỘI - HẠ LONG - TRÀ CỔ - MÓNG CÁI - ĐÔNG HƯNG - TRUNG QUỐC - HÀ NỘI",
    appContext.getString(R.string.str_ct_car),
    "Tour du lịch Trà Cổ Móng Cái - Quảng Ninh:",
    "{Du lịch Trà Cổ Móng Cái} luôn thu hút đông đảo du khách từ khắp mọi nơi đến với mảnh đất nơi địa đầu tổ quốc. " +
            "Trà Cổ hấp dẫn du khách bởi vẻ đẹp của biển cả, nơi con nước vùng tuyến đầu, những bãi tắm biển trải dài hàng chục km." +
            " Móng cái thì thu hút khách du lịch với phố phường tấp nập, nơi giao thương biên giới sầm uất.",
    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Nghỉ 02 đêm tại khách sạn khu trung tâm : 2 - 4 khách/phòng (Lẻ nam hoặc nữ ngủ 3 khách)."),
        transport= mutableListOf(
            Transport("0","+ Xe ô tô du lịch đời mới, máy lạnh đưa đón theo lịch trình: Hà Nội - Hạ Long - Móng Cái - Trà Cổ - Đông Hưng - Trung Quốc - Hà Nội.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 05 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 02 bữa ăn phụ: Ăn sáng tại khách sạn.")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Thủ tục xuất nhập cảnh, lệ phí tham quan tại Đông Hưng, Trung Quốc."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Trẻ em dưới 5 tuổi: Miễn giá tour bố mẹ tự túc lo các chi phí ăn, tham quan (nếu có) cho bé."),
        NoteService("Note2","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note3","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//https://tour.pro.vn/tour-du-lich-da-nang-tu-ha-noi
val services6 = Service(
    id = "tour_id_006",
    "4 ngày 3 đêm",
    "HÀ NỘI - ĐÀ NẴNG - BÁN ĐẢO SƠN TRÀ - BÀ NÀ - HỘI AN - NGŨ HÀNH SƠN - HÀ NỘI",
    appContext.getString(R.string.str_ct_plane),
    "Tour Du Lịch Đà Nẵng Từ Hà Nội:",
    "{Du lịch Trà Cổ Móng Cái} luôn thu hút đông đảo du khách từ khắp mọi nơi đến với mảnh đất nơi địa đầu tổ quốc. " +
            "Trà Cổ hấp dẫn du khách bởi vẻ đẹp của biển cả, nơi con nước vùng tuyến đầu, những bãi tắm biển trải dài hàng chục km." +
            " Móng cái thì thu hút khách du lịch với phố phường tấp nập, nơi giao thương biên giới sầm uất.",
    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Khách sạn theo chương trình tiêu chuẩn 2 khách/phòng, lẻ khách nam hoặc nữ ở ghép 3. Trẻ em ngủ cùng bố mẹ."),
        transport= mutableListOf(
            Transport("0","+ Xe ô tô du lịch đời mới, máy lạnh đưa đón theo lịch trình đón tiễn sân bay Nội Bài và phục vụ suốt hành trình tại Đà Nẵng - Hội An.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 05 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 03 bữa ăn phụ: Ăn sáng tại khách sạn.")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Khi làm thủ tục đăng ký tour, Quý khách vui lòng đem theo bản chính hoặc photo các giấy tờ như sau: CCCD còn hạn 15 năm, Hộ chiếu, bằng lái xe còn hạn sử dụng. Đối với trẻ em chưa có CCCD mang theo giấy khai sinh bản gốc, bản photo công chứng, các giấy tờ chứng thực theo quy định của pháp luật."),
        NoteService("Note2","+ Ngày khởi hành phải mang đầy đủ một trong những giấy tờ như đã nêu, để làm thủ tục cho chuyến bay."),
        NoteService("Note3","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note4","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//https://tour.pro.vn/ha-noi-quy-nhon-phu-yen-ha-noi
val services7 = Service(
    id = "tour_id_007",
    "4 ngày 3 đêm",
    "HÀ NỘI - QUY NHƠN “VỀ MIỀN ĐẤT VÕ” - GHỀNH ĐÁ ĐĨA - PHÚ YÊN - HÀ NỘI",
    appContext.getString(R.string.str_ct_plane),
    "Tour du lịch Quy Nhơn Bình Định:",
    "Quy Nhơn, Bình Định những năm trở lại đây luôn là lựa chọn của đông đảo du" +
            " khách mỗi mùa du lịch đến. Mảnh đất này được du khách biết đến với biển xanh cát " +
            "trắng, nắng vàng. Kết hợp với nhiều điểm danh lam thắng cảnh, tham quan lịch sử hấp dẫn. " +
            "Hè này Quy Nhơn sẽ là một gợi ý vô cùng phù hợp cho kỳ nghỉ của bạn và gia đình.",
    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Khách sạn theo chương trình tiêu chuẩn 2 khách/phòng, lẻ khách nam hoặc nữ ở ghép 3. Trẻ em ngủ cùng bố mẹ."),
        transport= mutableListOf(
            Transport("0","+ Xe ô tô du lịch đời mới, máy lạnh đưa đón theo lịch trình: Hà Nội - Nội Bài. Phục vụ theo lịch trình tại Quy Nhơn, Bình Định và Phú Yên.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 07 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 03 bữa ăn phụ: Ăn sáng tại khách sạn.")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Khi làm thủ tục đăng ký tour, Quý khách vui lòng đem theo bản chính hoặc photo các giấy tờ như sau: CCCD còn hạn 15 năm, Hộ chiếu, bằng lái xe còn hạn sử dụng. Đối với trẻ em chưa có CCCD mang theo giấy khai sinh bản gốc, bản photo công chứng, các giấy tờ chứng thực theo quy định của pháp luật."),
        NoteService("Note2","+ Ngày khởi hành phải mang đầy đủ một trong những giấy tờ như đã nêu, để làm thủ tục cho chuyến bay."),
        NoteService("Note3","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note4","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//  https://tour.pro.vn/tour-ha-noi-phu-quoc
val services8 = Service(
    id = "tour_id_008",
    "4 ngày 3 đêm",
    "HÀ NỘI - ĐẢO NGỌC PHÚ QUỐC - VINPEARL LAND - KHÁM PHÁ BIỂN TÂY NAM - HÀ NỘI",
    appContext.getString(R.string.str_ct_plane),
    "Tour du lịch Hà Nội - Phú Quốc:",
    "Phú Quốc là nơi duy nhất ở Việt Nam mà du khách có thể ngắm mặt trời mọc" +
            " và lặn trên biển. Bên cạnh những bãi biển tuyệt đẹp thì những đặc sản nơi " +
            "đây cũng là một trong những yếu tố tạo nên nét đặc trưng riêng cho du lịch Phú Quốc." +
            "Phú Quốc là hòn đảo lớn nhất Việt Nam, nằm trong Vịnh Thái Lan, thuộc tỉnh Kiên Giang," +
            " phía Đông Bắc tiếp giáp với Cam-Pu-Chia, phía đông Bắc tiếp giáp với Rạch Giá, " +
            "phía Đông tiếp giáp với thị xã Hà Tiên. ",

    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Khách sạn theo chương trình tiêu chuẩn 2 khách/phòng, lẻ khách nam hoặc nữ ở ghép 3. Trẻ em ngủ cùng bố mẹ."),
        transport= mutableListOf(
            Transport("0","+ Xe ô tô du lịch đời mới, máy lạnh đưa đón theo lịch trình đưa đón sân bay quốc tế Nội Bài và phục vụ suốt hành trình tại Phú Quốc.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 07 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 03 bữa ăn phụ: Ăn sáng tại khách sạn.")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Khi làm thủ tục đăng ký tour, Quý khách vui lòng đem theo bản chính hoặc photo các giấy tờ như sau: CCCD còn hạn 15 năm, Hộ chiếu, bằng lái xe còn hạn sử dụng. Đối với trẻ em chưa có CCCD mang theo giấy khai sinh bản gốc, bản photo công chứng, các giấy tờ chứng thực theo quy định của pháp luật."),
        NoteService("Note2","+ Ngày khởi hành phải mang đầy đủ một trong những giấy tờ như đã nêu, để làm thủ tục cho chuyến bay."),
        NoteService("Note3","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note4","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//https://tour.pro.vn/tour-moc-chau-son-la
val services9 = Service(
    id = "tour_id_009",
    "2 ngày 1 đêm",
    "HÀ NỘI - DU LỊCH MỘC CHÂU - ĐỒI CHÈ TRÁI TIM - RỪNG THÔNG BẢN ÁNG - HÀ NỘI",
    appContext.getString(R.string.str_ct_car),
    "Tour du lịch Mộc Châu Sơn La: ",
    "Để tìm kiếm một vùng đất với thiên nhiên hùng vĩ, khí hậu mát mẻ, " +
            "văn hóa đặc sắc và đặc biệt là nhiều món ăn ngon. Thì Mộc Châu thực sự là một lựa chọn thú vị cho những chuyến du" +
            " lịch ngắn ngày. Tour du lịch Mộc Châu được ví như Đà Lạt của Tây Bắc. " +
            "Đi tour Mộc Châu vào bất kì thời điểm nào trong năm cũng đều khiến bạn ngẩn ngơ, " +
            "ngỡ ngàng trước vẻ đẹp của khung cảnh thiên nhiên hùng vĩ.",

    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Khách sạn theo chương trình tiêu chuẩn 2 khách/phòng, lẻ khách nam hoặc nữ ở ghép 3. Trẻ em ngủ cùng bố mẹ."),
        transport= mutableListOf(
            Transport("0","+ Xe du lịch đời mới, máy lạnh đưa đón theo lịch trình: Hà Nội - Mộc Châu - Sơn La - Hà Nội.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 03 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 01 bữa ăn phụ: Ăn sáng tại khách sạn.")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Trẻ em dưới 5 tuổi: Miễn phí giá tour bố mẹ tự túc lo các chi phí ăn, tham quan (nếu có) cho bé."),
        NoteService("Note2","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note3","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)
//https://tour.pro.vn/ha-noi-sapa-fansipan
val services10 = Service(
    id = "tour_id_010",
    "3 ngày 2 đêm",
    "HÀ NỘI - DU LỊCH SAPA - KHU DU LỊCH HÀM RỒNG - FANSIPAN - HÀ NỘI ",
    appContext.getString(R.string.str_ct_car),
    "Tour du lịch SaPa - Fansipan legend:",
    "Nếu như miền Trung có Đà Lạt đi hoài không chán, thì miền Bắc có Sapa đi bao " +
            "nhiêu lần cũng không thấy mỏi. Tour du lịch Sapa - Fansipan 3 ngày 2 đêm sẽ đưa quý " +
            "khách đến khám phá những bản làng xinh đẹp. Ngắm nhìn những ngọn núi cao hùng vĩ ẩn hiện trong mây mù, chiêm ngưỡng núi rừng " +
            "Sapa khoe sắc hay chụp ảnh với những thửa ruộng bậc thang vàng ruộm vào mùa lúa chín.",

    includeService= IncludeService(
        id = "1",
        accommodation = Accommodation("1", "+ Khách sạn theo chương trình tiêu chuẩn 2 khách/phòng, lẻ khách nam hoặc nữ ở ghép 3. Trẻ em ngủ cùng bố mẹ."),
        transport= mutableListOf(
            Transport("0","+ Xe ô tô du lịch đời mới, máy lạnh đưa đón theo lịch trình: Hà Nội - Sapa - Fansipan - Hà Nội.")
        ),
        food= mutableListOf(
            Food("0",1,50000,"+ Phục vụ 05 bữa ăn chính: 150.000 vnđ/suất/bữa."),
            Food("1",1,30000,"+ Phục vụ 02 bữa ăn phụ: Ăn sáng tại khách sạn.")

        ),
        otherServices= mutableListOf(
            OtherServices("0","+ Hướng dẫn viên tiếng việt theo chương trình hài hước vui nhộn và nhiệt tình."),
            OtherServices("0","+ Bảo hiểm du lịch với mức 40.000.000đ/trường hợp."),
            OtherServices("0","+ Phí môi trường và tham quan tại các điểm theo lịch trình."),
            OtherServices("0","+ Nước uống phục vụ trên xe theo chương trình.")
        )
    ),
    noteService = mutableListOf(
        NoteService("Note1","+ Trẻ em dưới 5 tuổi: Miễn phí giá tour bố mẹ tự túc lo các chi phí ăn, tham quan (nếu có) cho bé."),
        NoteService("Note2","+ Trẻ em từ 5 - dưới 9 tuổi: 50% giá tour. Bao gồm các dịch vụ ăn uống, ghế ngồi trên xe và ngủ chung với bố mẹ."),
        NoteService("Note3","+ Trẻ em từ 9 tuổi: 100% giá tour và tiêu chuẩn như người lớn.")
    )
)

fun  listService (): MutableList<Service>{
    return mutableListOf(services1, services2, services3, services4, services5, services6, services7, services8, services9, services10)
}