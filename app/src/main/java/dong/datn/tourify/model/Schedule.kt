package dong.duan.travelapp.model

data class Schedule(
    var Id: String? = "",
    var ListTime: MutableList<DaySchedule>? = mutableListOf()
) : BaseModel<Schedule>()

data class DaySchedule(
    var Id: String? = "",
    var Time: String? = "",
    var ListDetail: MutableList<DetailSchedule>? = mutableListOf()
) : BaseModel<DaySchedule>()

data class DetailSchedule(var Id: String? = "", var Time: String? = "", var Content: String? = "") :
    BaseModel<DetailSchedule>()


var schedule = Schedule(
    Id = "1",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "Ngày 1: Hà Nội - Du Lịch Biển Hải Tiến - Team Building (Ăn T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    " 06h30",
                    " Xe và hướng dẫn viên Tour đón quý khách tại điểm hẹn khởi hành chương trình tour Hải Tiến - Thanh Hóa. Trên xe, hướng dẫn viên tổ chức thuyết minh, hoạt náo nhiệt tình, vui nhộn."
                ),
                DetailSchedule(
                    "2",
                    " 07h30",
                    " Xe dừng tại Hà Nam, quý khách tự do ăn sáng (chi phí tự túc"
                ),
                DetailSchedule(
                    "3",
                    "11h30",
                    "Xe đưa Quý khách đến khu du lịch Hải Tiến, Quý khách dùng bữa trưa tại nhà hàng. Sau bữa trưa Quý khách nhận phòng, nghỉ ngơi tại khách sạn."
                ),
                DetailSchedule(
                    "4",
                    "13h30",
                    "Quý khách tập trung tại bãi biển theo sự sắp xếp của ban tổ chức và hướng dẫn viên để tham gia chương trình Team Building bãi biển." +
                            " Với những trò chơi vui nhộn: Rồng hội Thăng Long, hành quân bom, tát cạn biển đông và đua thuyền trên cạn, Vượt Đại Dương Xanh (Áp dụng cho đoàn 40 khách trở lên, " +
                            "có kịch bản đính kèm)."
                ),
                DetailSchedule(
                    "5",
                    "19h30",
                    "Quý khách dùng bữa tối tại nhà hàng. Sau bữa tối, quý khách tự do đi dạo, ngắm biển Hải Tiến về đêm. Nghỉ đêm tại khách sạn."
                )

            )
        ),
        DaySchedule(
            "Day2", "Ngày 2: Biển Hải Tiến - Tổ Chức Gala Dinner (Ăn S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng",
                    "Quý khách dùng bữa sáng tại khách sạn. Sau bữa sáng, Quý khách tự do dạo chơi, ngắm cảnh, chụp ảnh, check in..."
                ),
                DetailSchedule(
                    "2",
                    " Trưa",
                    "Quý khách dùng bữa trưa tại nhà hàng, thưởng thức các món ăn mang hương vị xứ Thanh."
                ),
                DetailSchedule(
                    "3",
                    "Chiều",
                    "Quý khách tự do tắm biển Hải Tiến hoặc chơi các môn thể thao tại bãi biển."
                ), DetailSchedule(
                    "4",
                    "Tối",
                    "Quý khách dùng bữa tối tại nhà hàng, sau bữa tối quý khách tham gia chương trình \"Gala Dinner\" kết" +
                            " hợp với các trò vui nhộn (Áp dụng cho đoàn 40 khách người lớn trở lên). Kết thúc chương trình quý khách " +
                            "tự do dạo chơi trên bãi biển. Nghỉ đêm tại khách sạn."
                )
            )
        ),
        DaySchedule(
            "Day3", "Ngày 3: Hải Tiến - Thanh Hóa - Hà Nam - Hà Nội (Ăn S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng",
                    "Quý khách tự do dạo chơi, ngắm bình minh biển, tự do tắm biển. Sau đó dùng bữa sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    " Trưa",
                    "Quý khách làm thủ tục trả phòng khách sạn, dùng bữa trưa tại nhà hàng."
                ),
                DetailSchedule(
                    "3",
                    "13h30",
                    "Xe đón Quý khách lên xe trở về Hà Nội. Trên đường về quý khách dừng chân mua đặc sản Thanh Hóa về làm quà cho gia đình và bạn bè."
                ),
                DetailSchedule(
                    "4",
                    "17h30",
                    "Về đến Hà Nội, xe đưa quý khách trở về điểm đón ban đầu. Hướng dẫn viên Tour Pro cảm ơn, chia tay, hẹn gặp lại quý khách. Kết thúc chuyến hành trình tour du lịch biển Hải Tiến."
                )
            )
        )
    )
)