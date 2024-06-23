package dong.datn.tourify.model

import dong.duan.travelapp.model.BaseModel

data class Places(
    var placeID: String = "",
    var placeName: String = "",
    var description: String = "",
    var listTour: MutableList<String> = mutableListOf(),
    var Image: MutableList<String>? = null,
    var tourCount: Int = 0,
    var createdTime:String=""
) : BaseModel<Places>()


fun createFamousPlaces(): MutableList<Places> {
    return mutableListOf(
        Places(
            placeID = "place_01",
            placeName = "Hạ Long Bay",
            description = "Vịnh Hạ Long là một trong những kỳ quan thiên nhiên thế giới với hàng ngàn đảo đá vôi lớn nhỏ.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://booking.pystravel.vn/uploads/posts/albums/6224/27866de7412bab2c732246925db7e1bf.jpg",
                "https://vcdn1-dulich.vnecdn.net/2023/07/12/HL1-9875-1689130357.jpg?w=0&h=0&q=100&dpr=1&fit=crop&s=mN2YMaNq5Vgj_oi6A_s91Q"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_02",
            placeName = "Hà Nội",
            description = "Thủ đô nghìn năm văn hiến với nhiều di tích lịch sử và danh lam thắng cảnh.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://ik.imagekit.io/tvlk/blog/2017/06/kham-pha-cac-dia-diem-du-lich-o-ha-noi-ma-ban-khong-the-bo-qua-3.jpg?tr=dpr-2,w-675",
                "https://res.klook.com/image/upload/fl_lossy.progressive,q_85/c_fill,w_680/v1635409488/blog/t8ge6e8amk8cluhdswy2.jpg"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_03",
            placeName = "Huế",
            description = "Thành phố cổ kính với quần thể di tích Cố đô Huế được UNESCO công nhận là di sản thế giới.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://ik.imagekit.io/tvlk/blog/2023/12/Dia-diem-Truong-Quoc-Hoc.jpg?tr=dpr-2,w-675",
                "https://khamphahue.com.vn/Portals/0/KhamPha/DiTich-DiSan/DanhLamThangCanh/LangTam/QuanTheDiTichCoDoHue/Khamphahue_VanThanh_Quan-the-di-tich-co-do-hue.jpg"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_04",
            placeName = "Đà Nẵng",
            description = "Thành phố biển xinh đẹp nổi tiếng với bãi biển Mỹ Khê và cầu Rồng.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://divui.com/blog/wp-content/uploads/2018/10/111111.jpg",
                "https://divui.com/blog/wp-content/uploads/2018/05/Thue-xe-tu-Da-Nang-di-Ban-Dao-Son-Tra-Ngu-Hanh-Son-Hoi-An-6-1024x576.jpg"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_05",
            placeName = "Hội An",
            description = "Phố cổ Hội An với những ngôi nhà cổ kính, lung linh ánh đèn lồng.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxr8f-UvaxOCP-mkO6f7iEdYL8jpz_Ewde3w&s",
                "https://owa.bestprice.vn/images/articles/uploads/top-15-cac-dia-diem-du-lich-da-nang-hot-nhat-ban-khong-the-bo-qua-5ed9cb93af7eb.jpg"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_06",
            placeName = "Nha Trang",
            description = "Thành phố biển nổi tiếng với bãi biển dài, đẹp và các khu nghỉ dưỡng cao cấp.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://res.klook.com/image/upload/fl_lossy.progressive,q_85/c_fill,w_680/v1601372554/blog/iextvtagqbzfhp55jtwe.jpg",
                "https://static.vinwonders.com/2022/02/cVFLn3pT-khu-du-lich-nha-trang-1.jpg"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_07",
            placeName = "Đà Lạt",
            description = "Thành phố ngàn hoa với khí hậu mát mẻ quanh năm và nhiều thắng cảnh thơ mộng.",
            listTour = mutableListOf("Tour 13", "Tour 14"),
            Image = mutableListOf(
                "https://dulichlive.com/nha-trang/wp-content/uploads/sites/3/2019/05/15-di-tich-lich-su-o-Nha-Trang.jpg",
                "https://sanvemaybay.com.vn/assets/uploads/2017/11/vinpearl-land-nha-trang.jpg"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_08",
            placeName = "Phú Quốc",
            description = "Hòn đảo ngọc với những bãi biển trong xanh và hệ sinh thái biển phong phú.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://cdn.tgdd.vn/Files/2021/06/18/1361250/top-10-dia-diem-du-lich-phu-quoc-gan-trung-tam-ban-khong-the-bo-lo-202201101615318029.jpg",
                "https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2018/01/4-20.png"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_09",
            placeName = "Sapa",
            description = "Thị trấn nhỏ bé trên núi cao, nổi tiếng với cảnh quan hùng vĩ và các bản làng dân tộc.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://owa.bestprice.vn/images/articles/uploads/top-15-dia-diem-nen-di-o-sapa-duoc-yeu-thich-nhat-5f1e52b0b2823.jpg",
                "https://cellphones.com.vn/sforum/wp-content/uploads/2024/03/dia-diem-du-lich-sapa-6.jpg",
                "https://cdn.tgdd.vn/Files/2021/11/03/1395385/10-dia-diem-du-lich-dep-o-sapa-noi-tieng-voi-nhung-net-doc-dao-va-hap-dan-202202161001113514.jpg"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        ),
        Places(
            placeID = "place_10",
            placeName = "Cần Thơ",
            description = "Thành phố lớn nhất miền Tây Nam Bộ, nổi tiếng với chợ nổi Cái Răng và vườn cây ăn trái.",
            listTour = mutableListOf("tour_id_001", "tour_id_002", "tour_id_003"),
            Image = mutableListOf(
                "https://images.baodantoc.vn/uploads/2023/Th%C3%A1ng%202/Ng%C3%A0y_13/TRUNG/C%E1%BA%A7n%20Th%C6%A1/DTVH1.jpg",
                "https://ik.imagekit.io/tvlk/blog/2021/11/dia-diem-du-lich-can-tho-cover.jpg?tr=dpr-2.625,h-320,q-25,w-320"
            ),
            tourCount = 2,
            createdTime = "2023-06-23"
        )
    )
}