package dong.duan.travelapp.model

import dong.datn.tourify.app.appContext
import dong.datn.tourify.model.createFamousPlaces
import dong.datn.tourify.model.getNewVehicle

data class TourTime(
    var timeID: String =  "",
    var tourTime: String =  "",
    var startTime: String =  "",
    var endTime: String =  "",
    var count: Int =  0,
)

data class Tour(
    var tourID: String =  "",
    var tourName: String =  "",
    var tourDescription: String =  "",
    var tourImage: MutableList<String> =  mutableListOf(),
    var tourPrice: Double =  0.0,
    var saleId: String =  "",
    var success: Int =  0,
    var cancel: Int =  0,
    var star: Double = 4.5,
    var countRating: Int = 100,
    var tourTime: MutableList<TourTime> =  mutableListOf(),
    var tourAddress: String =  "",
    var vehicleId: String=""
) : BaseModel<Tour>()



fun listImage(): MutableList<String> {
    return mutableListOf(
        //coto
        "https://hoanganhtour.com.vn/wp-content/uploads/2022/11/du-lich-dao-co-to-3-ngay-2-dem.jpg",
        "https://thesinhcafetourist.com.vn/wp-content/uploads/2022/06/tour-du-lich-co-to-3-ngay-2-dem-1.jpg",
        "https://dailytourviet.vn/images/gallery/tour_tour/Co_To.jpg",
        //catba
        "https://bizweb.dktcdn.net/thumb/1024x1024/100/366/808/products/maxresdefault.jpg?v=1574159824890",
        "https://sinhcafetour.vn/pic/Tour/CAT-BA_63_637915964181415712_HasThumb.png",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQD1ftmvQ5Gmq9kR-q85BZDcNmUoCb9fjQgAg&s",
        //quanlan
        "https://pttravel.vn/wp-content/uploads/2023/06/tour-quan-lan-2-ngay-1-dem.jpg",
        "https://sinhtour.vn/wp-content/uploads/2024/04/tour-quan-lan-2-ngay-1-dem-4.jpg",
        "https://dulichdaoquanlan.net/view/at_tour-du-lich-quan-lan_9c067dae06f83fd0fdbc13577c2247ed.jpg",
        //samson
        "https://bevivu.com/wp-content/uploads/2024/02/9.jpg",
        "https://songhongtourist.vn/upload/2022-11-21/tp-7-1673-5.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDHqh5Arto4oJeuk0BCgdPRplSaCfBz1vCuQ&s",
        //traco
        "https://imgcdn.tapchicongthuong.vn/tcct-media/21/6/22/mui_sa_vi.jpg",
        "https://pttravel.vn/wp-content/uploads/2023/01/du-lich-tra-co-3.-ngay-2-dem-1.jpg",
        "https://bizweb.dktcdn.net/100/006/093/files/tra-co-mong-cai-3.jpg?v=1685934517513",
        //danang
        "https://bizweb.dktcdn.net/100/115/245/themes/146434/assets/popular_brand_img_2.png?1675053095677",
        "https://maichautourist.vn/wp-content/uploads/2017/05/tour-du-lich-da-nang-hoi-an-4-ngay-3-dem.jpg",
        "https://seventeenhotels.com.vn/wp-content/uploads/2021/05/du-lich-da-nang-seventeen-01.jpg",
        //quynhon
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTrmdRxxfQrR1_4jG1pn9kjKiurqTwpSaLjWw&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRfkvxoSTr4lMHMAoDPcFmLxyiI3p5itJFsDbS886cHJZYimNrbjx07-QDUlLjf1FLpqgk&usqp=CAU",
        "https://www.vietnambooking.com/wp-content/uploads/2017/04/Tour-Ha-Noi-Quy-Nhon-4N3D.jpg",
        //phuquoc
        "https://mayvesinhmienbac.com.vn/wp-content/uploads/2023/03/du-lich-phu-quoc-4-ngay-3-dem.jpg",
        "https://ecotour.com.vn/wp-content/uploads/2020/05/2019.jpg",
        "https://bizweb.dktcdn.net/thumb/grande/100/342/038/products/tour-phu-quoc-3-ngay-2-dem-sieu-dep-tron-goi-jpg-v-1623636873881.jpg?v=1646638510610",
        //mocchau
        "https://songhongtourist.vn/upload/2022-11-16/z3885067729149_5b41cd9b57d710636516b27d604bcb07-5.jpg",
        "https://viettour3mien.vn/wp-content/uploads/2023/02/tour-Moc-Chau-2-ngay-1-dem-2.jpg",
        "https://pttravel.vn/wp-content/uploads/2023/10/Du-lich-Moc-Chau-2-ngay-1-dem-tan-huong-canh-quan.jpg",
        //sapa
        "https://www.mercitour.com/files/thumb/850/480//images/TOUR/SAPA/sapa-thang-10-15.jpg",
        "https://www.vietnambooking.com/wp-content/uploads/2022/05/tour-sapa-3-ngay-2-dem-4.jpg",
        "https://motortrip.vn/wp-content/uploads/2021/09/tour-du-lich-sapa-3-ngay-2-dem-2.jpg",

        )
}

fun dataRandom(): MutableList<Tour> {
    return mutableListOf(
        //https://tour.pro.vn/tour-co-to
        Tour(
            tourID = "tour_id_001",
            tourName = "Tour Cô Tô 3 Ngày 2 Đêm",
            tourDescription = "Tour du lịch Cô Tô Quảng Ninh Giá Rẻ. Review lịch trình, chi phí tour Cô Tô 3 ngày 2 đêm từ Hà Nội và theo yêu cầu. Phương tiện Ô tô - Tàu cao tốc ở Vân Đồn. Chia sẻ kinh nghiệm đi du lịch biển đảo Cô Tô Cập Nhật.",
            tourImage = mutableListOf(listImage().get(0), listImage().get(1), listImage().get(2)),
            tourPrice = 2450000.0,
            saleId = "sale_id_003",
            success = 120,
            cancel = 5,

            star = 4.8,
            countRating = 200,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_001_time_0","3 ngày 2 đêm","07:00 12/08/2024","17:00 15/08/2024", 10),
                TourTime( timeID = "tour_id_001_time_1","3 ngày 2 đêm", "09:00 11/08/2024","17:00 14/08/2024",10),
                TourTime(timeID = "tour_id_001_time_2","3 ngày 2 đêm", "07:00 13/08/2024", "17:00 16/08/2024", 10),
            ),
            tourAddress = "place_11",
            vehicleId = getNewVehicle(context = appContext).get(2).vhId
        ),
        //https://tour.pro.vn/tour-cat-ba
        Tour(
            tourID = "tour_id_002",
            tourName = "Tour Cát Bà 3 ngày 2 đêm",
            tourDescription = "Tour du lịch Cát Bà Vịnh Lan Hạ Giá Rẻ. Review lịch trình, chi phí tour đảo Cát Bà 3 ngày 2 đêm từ Hà Nội, theo yêu cầu. Chia sẻ kinh nghiệm đi du lịch Cát Bà - Hải Phòng Cập Nhật.",
            tourImage = mutableListOf(listImage().get(3), listImage().get(4), listImage().get(5)),
            tourPrice = 1990000.0,
            saleId = "sale_id_003",
            success = 85,
            cancel = 2,
            star = 4.9,
            countRating = 150,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_002_time_0","3 ngày 2 đêm","07:00 12/08/2024","17:00 15/08/2024", 10),
                TourTime( timeID = "tour_id_002_time_1","3 ngày 2 đêm", "09:00 11/08/2024","17:00 14/08/2024",10),
                TourTime(timeID = "tour_id_002_time_2","3 ngày 2 đêm", "07:00 13/08/2024", "17:00 16/08/2024", 10),
            ),
            tourAddress = "place_12",
            vehicleId = getNewVehicle(context = appContext).get(1).vhId
        ),
        //https://tour.pro.vn/quan-lan
        Tour(
            tourID = "tour_id_003",
            tourName = "Tour ĐẢO QUAN LẠN 2 ngày 1 đêm",
            tourDescription = "Tour du lịch đảo Quan Lạn Giá Rẻ. Giới thiệu về địa chỉ, hình ảnh, giá vé tàu cao tốc đi Quan Lạn, tham quan, ở đâu ?. Review lịch trình, chi phí tour Quan Lạn 2 ngày 1 đêm. Chia sẻ kinh nghiệm đi du lịch Quan Lạn Quảng Ninh tự túc.",
            tourImage = mutableListOf(
                listImage().get(6),
                listImage().get(7),
                listImage().get(8)
            ),
            tourPrice = 1680000.0,
            saleId = "sale_id_003",
            success = 200,
            cancel = 10,
            star = 4.7,
            countRating = 250,
            tourTime = mutableListOf(
                TourTime( timeID = "tour_id_003_time_0","2 ngày 1 đêm", "09:00 12/08/2024", "18:00 14/08/2024", 10),
                TourTime( timeID = "tour_id_003_time_1","2 ngày 1 đêm", "09:00 13/08/2024", "18:00 14/08/2024", 10),
                TourTime(timeID = "tour_id_003_time_2","2 ngày 1 đêm", "09:00 16/08/2024", "18:00 17/08/2024", 10),
            ),
            tourAddress = "place_11",
            vehicleId = getNewVehicle(context = appContext).get(2).vhId
        ),
        //https://tour.pro.vn/ha-noi-sam-son-ha-noi
        Tour(
            tourID = "tour_id_004",
            tourName = "Tour du lịch biển Sầm Sơn 2 ngày 1 đêm",
            tourDescription = "Tour du lịch Sầm Sơn - Thanh Hóa Giá Rẻ. Review lịch trình, chi phí tour Sầm Sơn 3 ngày 2 đêm và 2 ngày 1 đêm từ Hà Nội. Chia sẻ kinh nghiệm đi du lịch biển Sầm Sơn Cập Nhật.",
            tourImage = mutableListOf(
                listImage().get(9),
                listImage().get(10),
                listImage().get(11)
            ),
            tourPrice = 1150000.0,
            saleId = "sale_id_001",
            success = 100,
            cancel = 10,
            star = 4.7,
            countRating = 250,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_004_time_0", "2 ngày 1 đêm", "06:30 22/08/2024", "17:30 23/08/2024", 10),
                TourTime(timeID = "tour_id_004_time_1", "2 ngày 1 đêm", "06:30 25/08/2024", "17:30 26/08/2024", 10),
                TourTime(timeID = "tour_id_004_time_2", "2 ngày 1 đêm", "06:30 28/08/2024", "17:30 29/08/2024", 10),
            ),
            tourAddress = "place_13",
            vehicleId = getNewVehicle(context = appContext).get(1).vhId
        ),
        //https://tour.pro.vn/tra-co-mong-cai
        Tour(
            tourID = "tour_id_005",
            tourName = "Tour TRÀ CỔ MÓNG CÁI 3 ngày 2 đêm",
            tourDescription = "Tour du lịch Trà Cổ Móng Cái Giá Rẻ. Review lịch trình, chi phí tour Trà Cổ Móng Cái Đông Hưng 3 ngày 2 đêm. Chia sẻ kinh nghiệm đi Trà Cổ Móng Cái Quảng Ninh tự túc Cập Nhật.",
            tourImage = mutableListOf(
                listImage().get(12),
                listImage().get(13),
                listImage().get(14)
            ),
            tourPrice = 1400000.0,
            saleId = "sale_id_003",
            success = 120,
            cancel = 10,
            star = 4.9,
            countRating = 200,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_005_time_0", "3 ngày 2 đêm", "07:30 12/08/2024", "18:00 14/08/2024", 10),
                TourTime(timeID = "tour_id_005_time_1", "3 ngày 2 đêm", "07:30 18/08/2024", "18:00 20/08/2024", 10),
                TourTime(timeID = "tour_id_005_time_2", "3 ngày 2 đêm", "07:30 16/08/2024", "18:00 18/08/2024", 10),
            ),
            tourAddress = "place_11",
            vehicleId = getNewVehicle(context = appContext).get(1).vhId
        ),
        //https://tour.pro.vn/tour-du-lich-da-nang-tu-ha-noi
        Tour(
            tourID = "tour_id_006",
            tourName = "Tour Đà Nẵng Giá Rẻ 4 ngày 3 đêm",
            tourDescription = "Tour du lịch Đà Nẵng - Hội An - Bà Nà hills Giá Rẻ. Review lịch trình, chi phí tour Đà Nẵng 4 ngày 3 đêm. Chia sẻ kinh nghiệm đi du lịch Đà Nẵng từ Hà Nội.",
            tourImage = mutableListOf(
                listImage().get(15),
                listImage().get(16),
                listImage().get(17)
            ),
            tourPrice = 5300000.0,
            saleId = "sale_id_003",
            success = 180,
            cancel = 10,
            star = 4.7,
            countRating = 250,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_006_time_0", "4 ngày 3 đêm", "09:00 12/08/2024", "18:00 16/08/2024", 10),
                TourTime(timeID = "tour_id_006_time_1", "4 ngày 3 đêm", "09:00 13/08/2024", "18:00 17/08/2024", 10),
                TourTime(timeID = "tour_id_006_time_2", "4 ngày 3 đêm", "09:00 15/08/2024", "18:00 18/08/2024", 10),
            ),
            tourAddress = "place_04",
            vehicleId = getNewVehicle(context = appContext).get(0).vhId
        ),
        //https://tour.pro.vn/ha-noi-quy-nhon-phu-yen-ha-noi
        Tour(
            tourID = "tour_id_007",
            tourName = "Tour Quy Nhơn Giá Rẻ 4 ngày 3 đêm",
            tourDescription = "Tour du lịch Quy Nhơn - Bình Định Giá Rẻ. Review lịch trình, chi phí tour Hà Nội - Bình Định - Phú Yên. Thời gian 4 ngày 3 đêm. Chia sẻ kinh nghiệm đi Quy Nhơn - Bình Định từ Hà Nội Cập Nhật.",
            tourImage = mutableListOf(
                listImage().get(18),
                listImage().get(19),
                listImage().get(20)
            ),
            tourPrice = 6760000.0,
            saleId = "sale_id_003",
            success = 168,
            cancel = 10,
            star = 4.9,
            countRating = 220,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_007_time_0", "4 ngày 3 đêm", "04:00 08/08/2024", "16:45 11/08/2024", 10),
                TourTime(timeID = "tour_id_007_time_1", "4 ngày 3 đêm", "04:00 15/08/2024", "16:45 18/08/2024", 10),
                TourTime(timeID = "tour_id_007_time_2", "4 ngày 3 đêm", "04:00 12/08/2024", "16:45 15/08/2024", 10),
            ),
            tourAddress = "place_14",
            vehicleId = getNewVehicle(context = appContext).get(0).vhId
        ),
        //https://tour.pro.vn/tour-ha-noi-phu-quoc
        Tour(
            tourID = "tour_id_008",
            tourName = "Tour Phú Quốc Giá Rẻ 4 ngày 3 đêm",
            tourDescription = "Tour du lịch Phú Quốc - Vinpearl land Giá Rẻ. Cẩm nang lịch trình, chi phí tour Hà Nội - Phú Quốc 4 ngày 3 đêm. Chia sẻ kinh nghiệm đi đảo Phú Quốc - Kiên Giang từ Hà Nội tự túc.",
            tourImage = mutableListOf(
                listImage().get(21),
                listImage().get(22),
                listImage().get(23)
            ),
            tourPrice = 6600000.0,
            saleId = "sale_id_003",
            success = 189,
            cancel = 10,
            star = 4.8,
            countRating = 230,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_008_time_0", "4 ngày 3 đêm", "06:00 11/08/2024", "18:50 14/08/2024", 10),
                TourTime(timeID = "tour_id_008_time_1", "4 ngày 3 đêm", "06:00 12/08/2024", "18:50 15/08/2024", 10),
                TourTime(timeID = "tour_id_008_time_2", "4 ngày 3 đêm", "06:00 13/08/2024", "18:00 16/08/2024", 10),
            ),
            tourAddress = "place_15",
            vehicleId = getNewVehicle(context = appContext).get(0).vhId
        ),
        //https://tour.pro.vn/tour-moc-chau-son-la
        Tour(
            tourID = "tour_id_009",
            tourName = "Tour Mộc Châu Giá Rẻ  2 ngày 1 đêm",
            tourDescription = "Tour du lịch Mộc Châu - Sơn La - Nông Trường Chè - Bản Áng Giá Rẻ. Review lịch trình, chi phí tour Mộc Châu 2 ngày 1 đêm từ Hà Nội. Chia sẻ kinh nghiệm đi du lịch Mộc Châu tự túc Cập Nhật.",
            tourImage = mutableListOf(
                listImage().get(21),
                listImage().get(22),
                listImage().get(23)
            ),
            tourPrice = 950000.0,
            saleId = "sale_id_004",
            success = 100,
            cancel = 10,
            star = 4.7,
            countRating = 150,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_009_time_0", "2 ngày 1 đêm", "06:00 12/08/2024", "17:00 13/08/2024", 10),
                TourTime(timeID = "tour_id_009_time_1", "2 ngày 1 đêm", "06:00 18/08/2024", "17:00 19/08/2024", 10),
                TourTime(timeID = "tour_id_009_time_2", "2 ngày 1 đêm", "06:00 16/08/2024", "17:00 17/08/2024", 10),
            ),
            tourAddress = "place_16",
            vehicleId = getNewVehicle(context = appContext).get(1).vhId
        ),
        //https://tour.pro.vn/ha-noi-sapa-fansipan
        Tour(
            tourID = "tour_id_010",
            tourName = "Tour Sapa 3 ngày 2 đêm",
            tourDescription = "Hướng dẫn tour du lịch Sapa - Hàm Rồng - Fansipan legend - Bản Cát Cát Giá Rẻ. Cẩm nang lịch trình, chi phí tour SaPa 3 ngày 2 đêm. Chia sẻ kinh nghiệm đi du lịch Sapa - Lào Cai.",
            tourImage = mutableListOf(
                listImage().get(24),
                listImage().get(25),
                listImage().get(26)
            ),
            tourPrice = 2200000.0,
            saleId = "sale_id_002",
            success = 200,
            cancel = 10,
            star = 4.7,
            countRating = 250,
            tourTime = mutableListOf(
                TourTime(timeID = "tour_id_010_time_0", "3 ngày 2 đêm", "06:00 12/08/2024", "18:00 12/08/2024", 10),
                TourTime(timeID = "tour_id_010_time_1", "3 ngày 2 đêm", "06:00 15/08/2024", "18:00 17/08/2024", 10),
                TourTime(timeID = "tour_id_010_time_2", "3 ngày 2 đêm", "06:00 23/08/2024", "18:00 25/08/2024", 10),
            ),
            tourAddress = "place_17",
            vehicleId = getNewVehicle(context = appContext).get(1).vhId
        )
    )
}