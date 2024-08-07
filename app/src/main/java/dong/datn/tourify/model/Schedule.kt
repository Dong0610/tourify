package dong.duan.travelapp.model

data class Schedule(
    var Id: String =  "",
    var ListTime: MutableList<DaySchedule> =  mutableListOf()
) : BaseModel<Schedule>()

data class DaySchedule(
    var Id: String =  "",
    var Time: String =  "",
    var ListDetail: MutableList<DetailSchedule> =  mutableListOf()
) : BaseModel<DaySchedule>()

data class DetailSchedule(var Id: String =  "", var Time: String =  "", var Content: String =  "") :
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



//https://tour.pro.vn/tour-co-to
var schedule1 = Schedule(
    Id = "tour_id_001",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: HÀ NỘI - VÂN ĐỒN - DU LỊCH BIỂN ĐẢO CÔ TÔ (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tập trung tại điểm hẹn, xe và Hướng vẫn viên đón Quý khách khởi hành đi cảng quốc tế Ao Tiên - Vân Đồn. Xe dừng nghỉ Quý khách tự do nghỉ ngơi, ăn sáng tại trạm nghỉ ở Hải Dương."
                ),
                DetailSchedule(
                    "2",
                    "11h30:",
                    " Quý khách đến cảng Ao Tiên, Vân Đồn. Dùng cơm trưa và nghỉ ngơi tại nhà hàng, sau bữa trưa hướng dẫn viên đưa Quý khách lên tàu cao tốc khởi hành đi đảo Cô Tô. Trên hành trình du khách ngắm cảnh vịnh Bái Tử Long xinh đẹp."
                ),
                DetailSchedule(
                    "3",
                    "Chiều:",
                    " Tàu cập bến cảng Cô Tô, xe đưa Quý khách về khách sạn nhận phòng nghỉ trên đảo ngay tại trung tâm thị trấn Cô Tô. Quý khách nghỉ ngơi, tắm biển tại bãi tắm tình yêu, dạo chơi khu tưởng niệm Bác Hồ."
                ),
                DetailSchedule(
                    "4",
                    "Tối:",
                    "Quý khách dùng cơm tối tại nhà hàng, thưởng thức các món ăn đặc sản biển Cô Tô. Sau bữa tối Quý khách tự do dạo chơi thị trấn biển Cô Tô, Cafe, dạo biển..."
                )

            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: BÃI ĐÁ CẦU MỴ - BÃI BIỂN VÀN CHẢY - BÃI HỒNG VÀN (ĂN S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng",
                    " Quý khách tự do dậy sớm ngắm bình minh, tắm biển. Sau đó dùng bữa sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Xe điện và hướng dẫn viên bắt đầu đưa Quý khách khám phá đảo Cô Tô. Với điểm đến đầu tiên trong hành trình là bãi đá Móng Rồng, Cầu Mỵ, mũi Ông Minh, địa điểm là nơi có cảnh quan hùng vĩ đẹp bậc nhất đảo Cô Tô. Tiếp tục hành trình xe đưa Quý khách đi biển Vàn Chảy Quý khách tự do tham quan và tắm biển."
                ),
                DetailSchedule(
                    "3",
                    "11h30:",
                    "Quý khách tự do tắm biển Hải Tiến hoặc chơi các môn thể thao tại bãi biển."
                ),
                DetailSchedule(
                    "4",
                    "Chiều:",
                    " Xe và hướng dẫn viên đưa Quý khách đi tham quan và tắm biển Hồng Vàn, nằm cách thị trấn Cô Tô 5,5km về hướng Đông Bắc. Bãi biển đẹp bậc nhất Cô Tô với bãi cát trắng mịn, " +
                            "nước biển xanh ngắt thấp thoáng là các căn homestay vô cùng bắt mắt."
                ),
                DetailSchedule(
                    "5",
                    "Tối:",
                    " Quý khách dùng cơm tối tại nhà hàng, sau bữa tối Quý khách tự do nghỉ ngơi, dạo biển tại Cô Tô."
                )
            )
        ),
        DaySchedule(
            "Day3", "NGÀY 3: CÔ TÔ - CẢNG AO TIÊN - KHỞI HÀNH VỀ HÀ NỘI (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dậy sớm đón bình binh, dạo biển, tắm biển sau đó về khách sạn dùng bữa sáng."
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Quý khách tự do đi chợ Cô Tô mua sắm đặc sản làm quà cho gia đình và người thân. Hoặc tự túc tham quan đảo Cô Tô con," +
                            " trải nghiệm chinh phục khu rừng nguyên sinh tham quan hải đăng Cô Tô (Chi phí tự túc)"
                ),
                DetailSchedule(
                    "3",
                    "10h30:",
                    " Quý khách dùng cơm trưa tại nhà hàng, nghỉ ngơi sau đó trả phòng khách sạn." +
                            " Hoặc trả phòng khách sạn sau đó di chuyển ra tàu cao tốc trở lại Vân Đồn dùng cơm trưa."
                ),
                DetailSchedule(
                    "4",
                    "14h00:",
                    " Xe và hướng dẫn viên đón Quý khách khởi hành về Hà Nội. Trên đường về xe dừng nghỉ tại Hải Dương. " +
                            "Quý khách tự do nghỉ ngơi, mua sắm đặc sản địa phương."
                ),
                DetailSchedule(
                    "5",
                    "Tối:",
                    " Xe đưa Quý khách về đền điểm hẹn, kết thúc hành trình tour Cô Tô 3 ngày 2 đêm."
                )
            )
        )
    )
)
//https://tour.pro.vn/tour-cat-ba
var schedule2 = Schedule(
    Id = "tour_id_002",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: HÀ NỘI - PHÀ ĐỒNG BÀI - ĐẢO CÁT BÀ - VỊNH LAN HẠ - ĐẢO KHỈ (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "07h00:",
                    " Xe và hướng dẫn viên đón Quý khách tại điểm hẹn trong TP Hà Nội khởi hành đi Cát Hải - Hải Phòng theo đường cao tốc 5B Hà Nội - Hải Phòng. Trên đường đi xe dừng nghỉ. Quý khách tự do nghỉ ngơi, ăn sáng tại điểm nghỉ."
                ),
                DetailSchedule(
                    "2",
                    "08h30:",
                    " Đến bến Phà Đồng Bài - Cát Hải, Quý khách lên phà khởi hành đi đảo Cát Bà - Hải Phòng (Thời gian khoảng 30 - 40 phút trên phà)."
                ),
                DetailSchedule(
                    "3",
                    "09h00:",
                    " Phà cập bến xe đón Quý khách khởi hành về thị trấn Cát Bà. Trên đường di chuyển Quý khách ngắm cảnh với vẻ đẹp của biển, những dãy núi đá vôi đặc trưng của đảo."
                ),
                DetailSchedule(
                    "4",
                    "Trưa:",
                    " Quý khách dùng cơm trưa tại nhà hàng, sau bữa trưa Quý khách nhận phòng khách sạn, nghỉ ngơi."
                ),
                DetailSchedule(
                    "5",
                    "Chiều:",
                    " Xe và hướng dẫn viên đưa Quý khách ra bến Cái Bèo - Quý khách lên tàu bắt đầu hành trình du lịch Vịnh Lan Hạ, một vịnh biển đẹp được nối liền với Vịnh Hạ Long."
                ),
                DetailSchedule(
                    "6",
                    "Tối:",
                    " Quý khách dùng bữa tối tại khách sạn. Sau bữa tối Quý khách tự do dạo chơi, ngắm biển đêm tại thị trấn Cát Bà."
                )

            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: THỊ TRẤN CÁT BÀ - DI TÍCH PHÁO ĐÀI THẦN CÔNG (ĂN S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dạo chơi, tắm biển, ngắm bình minh trên đảo. Dùng bữa sáng tại khách sạn. Sau bữa sáng hướng dẫn viên đưa Quý khách đi tham quan di tích pháo đài Thần Công Cát Bà hoặc Quý khách tự do dạo chơi mua sắm."
                ),
                DetailSchedule(
                    "2",
                    "Trưa:",
                    " Quý khách dùng cơm trưa tại nhà hàng thưởng thức các món ăn mang hương vị đặc sản biển Cát Bà."
                ),
                DetailSchedule(
                    "3",
                    "Chiều:",
                    "Quý khách tự do dạo chơi, tắm biển tại các bãi Tùng Thu, Cát Cò 1, Cát Cò 2, Cát Cò 3 - trên đảo Cát Bà."
                ),
                DetailSchedule(
                    "4",
                    "19h00:",
                    " Quý khách dùng bữa tối tại nhà hàng, Sau bữa tối Quý khách tự do dạo chơi, trải nghiệm thị trấn Cát Bà về đêm."
                ),
                DetailSchedule(
                    "5",
                    "22h00:",
                    " Quý khách nghỉ đêm tại khách sạn - thị trấn Cát Bà."
                )
            )
        ),
        DaySchedule(
            "Day3", "NGÀY 3: VUI CHƠI Ở CÁT BÀ - CÁI VIỀNG - HẢI PHÒNG - HÀ NỘI (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách dùng bữa sáng tại khách sạn, Sau bữa ăn sáng Quý khách tự do tắm  biển, vui chơi mua sắm đặc sản biển về làm quà cho gia đình và người thân."
                ),
                DetailSchedule(
                    "2",
                    "11h30:",
                    " Quý khách làm thủ tục trả phòng khách sạn. Sau đó dùng cơm trưa tại nhà hàng."
                ),
                DetailSchedule(
                    "3",
                    "13h30:",
                    " Xe ô tô đón Quý khách di chuyển trở lại bến Cái Viềng. Quý khách lên chuyến phà 15h00 di chuyển trở lại TP Hải Phòng."
                ),
                DetailSchedule(
                    "4",
                    "15h30:",
                    " Xe đón Quý khách tại bến Đồng Bài - Cát Hải, đưa Quý khách về Hà Nội theo đường cao tốc Hà Nội - Hải Phòng."
                ),
                DetailSchedule(
                    "5",
                    "17h00:",
                    " Xe đưa Quý khách về đền điểm hẹn, kết thúc hành trình tour Cát Bà 3 ngày 2 đêm."
                )
            )
        )
    )
)
//https://tour.pro.vn/quan-lan
var schedule3 = Schedule(
    Id = "tour_id_003",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: HÀ NỘI - VÂN ĐỒN - DU LỊCH QUAN LẠN - QUẢNG NINH (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "07h00:",
                    " Xe và hướng dẫn viên đón quý khách tại điểm hẹn trong TP Hà Nội khởi hành đi Vân Đồn - Quảng Ninh. Trên đường đi xe dừng nghỉ tại trạm nghỉ Hải Dương. Quý khách tự do nghỉ ngơi, ăn sáng tự túc."
                ),
                DetailSchedule(
                    "2",
                    " 11h00:",
                    " Xe đưa Quý khách đến cảng quốc tế Ao Tiên - Vân Đồn, Quý khách dùng bữa trưa tại nhà hàng."
                ),
                DetailSchedule(
                    "3",
                    "13h00:",
                    " Hướng dẫn viên đưa Quý khách lên tàu cao tốc khởi hành đi Quan Lạn. Quý khách tận hưởng non nước biển trời của vịnh Bái Tử Long xinh đẹp."
                ),
                DetailSchedule(
                    "4",
                    "14h15:",
                    "Tàu cập cảng Quan Lạn, xe túc túc hoặc xe điện đón Quý khách về khách sạn tại khu trung tâm đảo Quan Lạn nhận phòng nghỉ ngơi."
                ),
                DetailSchedule(
                    "5",
                    "Chiều:",
                    "Xe và hướng dẫn viên đưa Quý khách tham quan Đền thờ Trần Khánh Dư. Tiếp tục hành trình Quý khách tham quan, chụp ảnh tại khu đồi Cát Trắng - Sơn Hào. Tiếp tục xe đưa Quý khách khởi hành đi bãi biển Minh Châu - Xã đảo Minh Châu. " +
                            "Quý khách tự do tham quan và tắm biển tại một trong những bãi biển cát trắng đẹp nhất đảo Quan Lạn."
                ),
                DetailSchedule(
                    "6",
                    "19h00:",
                    "Quý khách dùng cơm tối tại nhà hàng thưởng thức các món ăn đặc sản biển đảo Quan Lạn."
                ),
                DetailSchedule(
                    "7",
                    "22h00:",
                    "Quý khách nghỉ đêm tại khách sạn đảo Quan Lạn."
                )

            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: VUI CHƠI Ở ĐẢO QUAN LẠN - CẢNG AO TIÊN - VỀ HÀ NỘI (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng",
                    " Quý khách tự do dạo chơi, tắm biển, dùng bữa sáng tại nhà hàng."
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Quý khách tự do dạo chơi, chinh phục Eo Gió, Bãi Robinson, bãi Cồn Trụi… mua sắm đặc sản Quan Lạn."
                ),
                DetailSchedule(
                    "3",
                    "11h00:",
                    "Quý khách trả phòng khách sạn, Sau đó dùng bữa trưa tại nhà hàng."
                ),
                DetailSchedule(
                    "4",
                    "13h00:",
                    " Xe đón Quý khách ra bến cảng, Quý khách lên tàu cao tốc khởi hành về cảng Ao Tiên - Vân Đồn."
                ),
                DetailSchedule(
                    "5",
                    "13h45:",
                    " Tàu cao tốc đưa Quý khách cập cảng, xe ôtô đón Quý khách khởi hành về Hà Nội. Trên đường về xe dừng nghỉ tại Hải Dương Quý khách tự do nghỉ ngơi, mua đặc sản Hải Dương: bánh đậu xanh, bánh gai… về làm quà cho người thân và gia đình."
                ),
                DetailSchedule(
                    "6",
                    "18h00:",
                    " Về đến điểm đón trong TP Hà Nội kết thúc chương trình tour."
                )
            )
        )
    )
)
//https://tour.pro.vn/ha-noi-sam-son-ha-noi
var schedule4 = Schedule(
    Id = "tour_id_004",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: TỪ HÀ NỘI - DU LỊCH BIỂN SẦM SƠN - THANH HÓA (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "06h30:",
                    " Xe và hướng dẫn viên có mặt tại điểm hẹn đón Quý khách khởi hành đi biển Sầm Sơn - Thanh Hóa.."
                ),
                DetailSchedule(
                    "2",
                    "07h30:",
                    "  Xe dừng nghỉ tại Hà Nam Quý khách tự do nghỉ ngơi, ăn sáng. sau đó xe tiếp tục đưa Quý khách đi Sầm Sơn."
                ),
                DetailSchedule(
                    "3",
                    "Trưa:",
                    " Xe đưa Quý khách đến TP biển Sầm Sơn, Quý khách dùng bữa trưa tại nhà hàng thưởng thức các món ăn biển đặc sản địa phương. Sau bữa trưa Quý khách nhận phòng khách sạn nghỉ nghơi."
                ),
                DetailSchedule(
                    "4",
                    "Chiều:",
                    " Quý khách tham quan những địa danh nổi tiểng của thành phố biển Sầm Sơn như: Hòn Trống Mái, tham quan và lễ ở chùa Cô Tiên, đền Độc Cước."
                ),
                DetailSchedule(
                    "5",
                    "18h30:",
                    " Quý khách dùng bữa ăn tối tại nhà hàng. Sau bữa tối Quý khách tự do dạo chơi tại thành phố biển Sầm Sơn. Quý khách nghỉ đêm tại khách sạn."
                )
            )
        ),

        DaySchedule(
            "Day2", "NGÀY 2: SẦM SƠN - TỰ DO VUI CHƠI, MUA SẮM - KHỞI HÀNH VỀ HÀ NỘI ( ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách dậy sớm tắm biển, dạo chơi ở Sầm Sơn, tự do mua sắm hải sản, đặc sản địa phương về làm quà cho gia đình và người thân."
                ),
                DetailSchedule(
                    "2",
                    "11h30:",
                    " Quý khách trả phòng khách sạn, Quý khách dùng cơm trưa tại nhà hàng."
                ),
                DetailSchedule(
                    "3",
                    "13h00:",
                    " Xe đón Quý khách trở về Hà Nội. Trên đường về dừng chân tại Cầu Hàm Rồng và Thanh Liêm (Hà Nam) để quý khách mua đặc sản quê hương: Dừa, Dứa, Nem chua... "
                ),
                DetailSchedule(
                    "4",
                    "15h30:",
                    " Xe đón Quý khách tại bến Đồng Bài - Cát Hải, đưa Quý khách về Hà Nội theo đường cao tốc Hà Nội - Hải Phòng."
                ),
                DetailSchedule(
                    "5",
                    "17h30:",
                    " Về tới điểm đón kết thúc chương trình tour du lịch Sầm Sơn - Thanh Hóa"
                )
            )
        )
    )
)
//https://tour.pro.vn/tra-co-mong-cai
var schedule5 = Schedule(
    Id = "tour_id_005",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: TỪ HÀ NỘI - HẠ LONG - MÓNG CÁI - TRÀ CỔ - QUẢNG NINH (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Xe ôtô và hướng dẫn viên đón quý khách tại điểm hẹn. Đưa Quý khách lên xe khởi hành đi Trà Cổ, Móng Cái, Quảng Ninh. Trên xe di chuyển hướng dẫn viên tổ chức thuyết minh, hoạt náo vui nhộn."
                ),
                DetailSchedule(
                    "2",
                    "Trưa:",
                    " Quý khách đi ăn trưa tại nhà hàng TP. Hạ Long. Sau bữa trưa xe tiếp tục khởi hành đưa đoàn đi Trà Cổ, Móng Cái."
                ),
                DetailSchedule(
                    "3",
                    "Chiều:",
                    " Quý khách có mặt tại khu du lịch biển Trà Cổ, Quý khách nhận phòng khách sạn, tự do nghỉ ngơi và tắm biển."
                ),
                DetailSchedule(
                    "4",
                    "Tối:",
                    " Quý khách ăn tối tại nhà hàng thưởng thức những món ăn đặc sản địa phương, nghỉ đêm tại Trà Cổ."
                )

            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: BIỂN TRÀ CỔ - MÓNG CÁI - ĐÔNG HƯNG - TRUNG QUỐC (ĂN S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dậy sớm ngắm cảnh bình minh, tắm biển, ăn sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Xe và hướng dẫn viên đưa quý khách ra cửa khẩu quốc tế Móng Cái. Quý khách làm thủ tục nhập cảng vào thành phố Đông Hưng Trung Quốc \n"+
                            "Tại Đông Hưng Quý khách tham quan du lịch: Công viên hữu nghị Việt - Trung, Chùa Quan Thánh, Tháp Văn Xương, Cầu Phong Thuận, Chùa Quan Âm, chụp ảnh ngoài tòa Thị Chính Đông Hưng, nhà thuốc Đức Nhân Đường."

                ),
                DetailSchedule(
                    "3",
                    "Trưa:",
                    "Quý khách ăn trưa tại nhà hàng, thưởng thức các món ăn mang hương vị đậm đà địa phương. Sau bữa trưa Quý khách tự do tham quan, mua sắm tại Chợ Vạn Chúng, Siêu thị Bách Hội, Cửa Hàng miễn thuế, dạo phố...."
                ),
                DetailSchedule(
                    "4",
                    "15h00:",
                    " Hướng dẫn viên đưa Quý khách ra cửa khẩu, nhập cảnh trở lại Việt Nam. Quý khách tự do dạo chơi mua sắm tại chợ Asean, Móng Cái, Togi..."
                ),
                DetailSchedule(
                    "5",
                    "16h30:",
                    " Xe và hướng dẫn viên đưa Quý khách trở lại Trà Cổ, Quý khách tự do tắm biển nghỉ ngơi."
                ),
                DetailSchedule(
                    "6",
                    "Tối:",
                    " Quý khách ăn tối tại nhà hàng, sau bữa tối Quý khách tự do dạo chơi biển Trà Cổ. Nghỉ đêm tại Trà Cổ."
                )
            )
        ),
        DaySchedule(
            "Day3", "NGÀY 3: TRÀ CỔ - MŨI SA VĨ - THĂM ĐÌNH TRÀ CỔ - TRỞ VỀ HÀ NỘI (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dậy sớm ngắm cảnh bình minh, tắm biển, ăn sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "07h30:",
                    " Xe và hướng dẫn viên đưa quý khách đi tham quan mũi Sa Vĩ, Trà Cổ địa đầu tổ quốc, đã đi vào những áng thơ nổi tiếng của nhà thơ Tố Hữu “Từ Trà Cổ rừng dương tới Cà Mau rừng đước”. " +
                            "Tiếp tục du khách tham quan Đình Trà Cổ, ngôi đình nổi tiếng với tuổi lâu đời bậc nhất Việt Nam."
                ),
                DetailSchedule(
                    "3",
                    "10h30:",
                    " Quý khách trả phòng khách sạn, sau đó dùng cơm trưa tại nhà hàng."
                ),
                DetailSchedule(
                    "4",
                    "12h00:",
                    " Xe và hướng dẫn viên đón quý khách khởi hành về điểm đón ban đầu. Trên đường về xe dừng nghỉ, Quý khách tự do nghỉ ngơi, mua sắm đặc sản địa phương về làm quà cho gia đình và người thân."
                ),
                DetailSchedule(
                    "5",
                    "18h00:",
                    " Quý khách về đến điểm đón theo yêu cầu ở Hà Nội, kết thúc hành trình du lịch 3 ngày 2 đêm."
                )
            )
        )
    )
)
//https://tour.pro.vn/tour-du-lich-da-nang-tu-ha-noi
var schedule6 = Schedule(
    Id = "tour_id_006",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: TỪ HÀ NỘI - DU LỊCH ĐÀ NẴNG - BÁN ĐẢO SƠN TRÀ ( ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "07h30:",
                    " Xe đón quý khách điểm hẹn trong TP Hà Nội khởi hành đi sân bay Nội Bài. Quý khách đáp chuyến bay VJ517 lúc 10h25."
                ),
                DetailSchedule(
                    "2",
                    "11h30:",
                    " Tới Đà Nẵng xe đón Quý khách đi ăn trưa tại nhà hàng. sau đó đoàn về khách sạn nhận phòng nghỉ ngơi. "
                ),
                DetailSchedule(
                    "3",
                    "Chiều:",
                    " Quý khách tự do dạo chơi, tắm biển Mỹ Khê."
                ),
                DetailSchedule(
                    "4",
                    "17h00:",
                    " Xe đưa đoàn tham quan Bán Đảo Sơn Trà, với những ngôi chùa tuyệt đẹp và uy nghiêm, thắp hương và lễ tại Chùa Linh ứng."
                ),
                DetailSchedule(
                    "5",
                    "19h00:",
                    " Quý khách ăn tối tại nhà hàng, sau đó quý khách tự dạo chơi ngắm biển Mỹ Khê về đêm, nghỉ đêm tại khách sạn."
                )

            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: ĐÀ NẴNG - THAM QUAN TP CỔ HỘI AN - QUẢNG NAM (ĂN S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự dạo chơi, tắm biển, ăn sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Xe đón Quý khách khởi hành đi tham quan khu du lịch Non Nước - Ngũ Hành Sơn. Quý khách leo núi tham quan động Huyền Không, Chùa Tam Thai, Chùa Linh Ứng... tiếp tục tham quan Làng Đá Mỹ Nghệ Non Nước."
                ),
                DetailSchedule(
                    "3",
                    "Trưa:",
                    " Xe đưa Quý khách đi ăn trưa tại nhà hàng. Thưởng thức các món ăn đặc sản địa phương."
                ),
                DetailSchedule(
                    "4",
                    "Chiều:",
                    " Quý khách tự do nghỉ ngơi tại khách sạn, dạo chơi TP Đà Nẵng, tắm biển Mỹ Khê."
                ),
                DetailSchedule(
                    "5",
                    "Tối:",
                    " Xe đưa Quý khách đi ăn cơm tối tại nhà hàng. Nghỉ đêm tại Đà Nẵng (Khách sạn nằm trên bãi Biển Mỹ Khê)."
                )
            )
        ),
        DaySchedule(
            "Day3", "NGÀY 3: ĐÀ NẴNG - KHU DU LỊCH BÀ NÀ HILLS - TẮM BIỂN MỸ KHÊ (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dậy sớm, ngắm cảnh bình minh trên biển, tắm biển. Sau đó ăn sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "07h30:",
                    " Xe đưa Quý khách khởi hành đi tham quan khu du lịch Bà Nà Hills, Quý khách lên cáp treo dài nhất Việt Nam đi lên núi với độ cao 1487m so với mực nước biển."
                ),
                DetailSchedule(
                    "3",
                    "Trưa:",
                    " Quý khách ăn trưa tự do (chi phí tự túc ăn trưa tại Bà Nà)."
                ),
                DetailSchedule(
                    "4",
                    "15h30:",
                    " Quý khách tạm biệt Bà Nà hills khởi hành về lại Đà Nẵng."
                ),
                DetailSchedule(
                    "5",
                    "Tối:",
                    " Quý khách ăn tối tại nhà hàng, tự do dạo chơi, nghỉ tại Đà Nẵng (khách sạn nằm trên bãi Biển Mỹ Khê). "
                )
            )
        ),
        DaySchedule(
            "Day4", "NGÀY 4: ĐÀ NẴNG - VUI CHƠI, MUA SẮM - KHỞI HÀNH VỀ HÀ NỘI (ĂN S).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách ăn sáng tại khách sạn, đoàn tự do tắm biển. Dạo chơi mua sắm đặc sản địa phương về làm quà cho gia đình và người thân."
                ),
                DetailSchedule(
                    "2",
                    "10h00:",
                    " Quý khách làm thủ tục trả phòng, xe đưa quý khách ra sân bay Đà Nẵng. Quý khách đáp chuyến bay lúc 13h05 - VJ516 khởi hành về sân bay Nội Bài ."
                ),
                DetailSchedule(
                    "3",
                    "14h05:",
                    " Xe đón đoàn trở tại sân bay quốc tế Nội Bài về Hà Nội, kết thúc chương trình. Hướng dẫn viên chia tay Quý khách, cảm ơn và hẹn gặp lại!"
                )
            )
        )
    )
)
//
var schedule7 = Schedule(
    Id = "tour_id_007",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: TỪ HÀ NỘI - DU LỊCH QUY NHƠN - BÃI TẮM HOÀNG HẬU (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "04h40:",
                    " Xe và hướng dẫn viên đón đoàn tại điểm hẹn khởi hành đi sân bay Nội Bài đáp chuyến bay VJ433 lúc 6h35 khởi hành đi Quy Nhơn ."
                ),
                DetailSchedule(
                    "2",
                    "08h30:",
                    " Quý khách đến sân bay Phù Cát, Quy Nhơn, Bình Định. Xe và hướng dẫn đưa Quý khách tham quan một số địa điểm nổi tiếng tại TP. Quy Nhơn. Như Ghềnh Ráng Tiên Sa, Đồi Thi Nhân, bãi tắm Hoàng Hậu, Khu tưởng niệm và mộ Hàn Mặc Tử."
                ),
                DetailSchedule(
                    "3",
                    "11h30:",
                    " Kết thúc tham quan, đoàn trở lại nhà hàng dùng cơm trưa, thưởng thức hương vị ẩm thực Quy Nhơn. Sau bữa trưa hướng dẫn đưa Quý khách về khách sạn nhận phòng nghỉ ngơi."
                ),
                DetailSchedule(
                    "4",
                    "15h00:",
                    " Quý khách tự do dạo chơi, tắm biển tại thành phố Quy Nhơn. Cuối buổi chiều, hướng dẫn đưa du khách tham quan di tích lịch sử Tháp Đôi - Một trong những ngôi tháp điển hình của kiến trúc dân tộc Chăm."
                ),
                DetailSchedule(
                    "5",
                    "18h30:",
                    " Quý khách dùng bữa tối tại nhà hàng, sau bữa tối Quý khách tự do dạo chơi TP biển Quy Nhơn. Nghỉ đêm tại Quy Nhơn."
                )
            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: TỪ QUY NHƠN - “ VỀ THĂM MIỀN ĐẤT VÕ TÂY SƠN” (ĂN S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dậy sớm, ngắm biển, đón bình minh, tắm biển. Sau đó dùng bữa sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Xe và hướng dẫn viên đón Quý khách khởi hành đi Tây Sơn, Bình Định. Địa danh nổi tiếng gắn liền với lịch sử thời kỳ Tây Sơn."
                ),
                DetailSchedule(
                    "3",
                    "11h30:",
                    "Quý khách dùng bữa trưa tại nhà hàng, sau bữa trưa xe đưa Quý khách về lại khách sạn tại TP. Quy Nhơn nghỉ ngơi."
                ),
                DetailSchedule(
                    "4",
                    "Chiều:",
                    " Quý khách nghỉ ngơi tắm biển, tự do dạo chơi TP Quy Nhơn."
                ),
                DetailSchedule(
                    "5",
                    "Tối:",
                    " Đoàn ăn tối tại nhà hàng, sau đó nghỉ đêm tại khách sạn - TP Quy Nhơn."
                )
            )
        ),
        DaySchedule(
            "Day3", "NGÀY 3: QUY NHƠN - GHỀNH ĐÁ ĐĨA (PHÚ YÊN) - QUY NHƠN (ĂN S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dậy sớm, ngắm biển, đón bình minh, tắm biển. Sau đó dùng bữa sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "07h30:",
                    " Xe và hướng dẫn viên đón Quý khách khởi hành đi Phú Yên. Du khách tham quan và chụp hình kỷ niệm tại khu du lịch Ghềnh Đá Đĩa, Phú Yên."
                ),
                DetailSchedule(
                    "3",
                    "Trưa:",
                    " Quý khách ăn trưa tại Đầm Ô Loan - Phú Yên, Quý khách thưởng thức hương vị đặc sản Phú Yên."
                ),
                DetailSchedule(
                    "4",
                    "15h30:",
                    " Xe đón Quý khách trở về Quy Nhơn, du khách tự do dạo chơi, tắm biển, nghỉ ngơi."
                ),
                DetailSchedule(
                    "5",
                    "Tối:",
                    " Quý khách ăn tối tại nhà hàng, đoàn tự do dạo chơi thăm thành phố, ngắm biển Quy Nhơn về đêm."
                )
            )
        ),
        DaySchedule(
            "Day4", "NGÀY 4: THAM QUAN TP QUY NHƠN -  KHỞI HÀNH VỀ HÀ NỘI (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách ăn sáng, sau đó quý khách tự do tắm biển, nghỉ ngơi."
                ),
                DetailSchedule(
                    "2",
                    "09h30:",
                    " Xe đưa Quý khách ra siêu thị Hải Sản và đặc sản Phương Nghi: Đc 115 -117 -119 Đường Tây Sơn - TP Quy Nhơn. Quý khách tự do mua sắm đặc sản về làm quà cho gia đình và người thân."
                ),
                DetailSchedule(
                    "3",
                    "10h30:",
                    " Quý khách trở lại khách sạn, làm thủ tục trả phòng khách sạn. Sau đó xe đưa Quý khách đi ăn trưa tại nhà hàng."
                ),
                DetailSchedule(
                    "4",
                    "12h40:",
                    " Xe và hướng dẫn viên đón Quý khách khởi hành đi sân bay Phù Cát, đáp chuyến bay VJ 434 về Hà Nội lúc 14h45."
                ),
                DetailSchedule(
                    "5",
                    "16h45:",
                    " Quý khách có mặt tại sân bay Nội Bài, xe đón Quý khách về điểm đón ban đầu. Kết thúc chương trình với những trải nghiệm thú vị. Chia tay quý khách và hẹn gặp lại."
                )
            )
        )
    )
)
//https://tour.pro.vn/tour-ha-noi-phu-quoc
var schedul8 = Schedule(
    Id = "tour_id_008",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: KHỞI HÀNH TỪ HÀ NỘI - DU LỊCH ĐẢO PHÚ QUỐC (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "06h00:",
                    " Xe và hướng dẫn viên đón Quý khách tại điểm hẹn khởi hành ra sân bay Nội Bài, đáp chuyến bay VN 1233 đi Phú Quốc lúc 10h05."
                ),
                DetailSchedule(
                    "2",
                    "Trưa:",
                    " Máy bay hạ cánh xuống sân bay Phú Quốc, xe và hướng dẫn viên đưa đoàn đi ăn trưa tại nhà hàng, sau bữa trưa Quý khách làm thủ tục nhận phòng khách sạn."
                ),
                DetailSchedule(
                    "3",
                    "Chiều:",
                    " Xe và hướng dẫn viên đưa Quý khách khởi hành tham quan các điểm gồm: Chùa Hùng Long, KDL Suối Tranh, Dinh Cậu, nhà Tù Phú Quốc, Chùa Hộ Quốc."
                ),
                DetailSchedule(
                    "4",
                    "18h30:",
                    " Quý khách dùng bữa tối tại nhà hàng, thưởng thức các món ăn mang hương vị biển Phú Quốc. Sau bữa tối Quý khách tự do tham quan, dạo chơi."
                )
            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: THAM QUAN KHÁM PHÁ, DU LỊCH ĐẢO PHÚ QUỐC (ĂN S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách dùng bữa sáng tại khách sạn, tự do tham quan, tắm biển."
                ),
                DetailSchedule(
                    "2",
                    "08h30:",
                    " Hướng dẫn viên đưa đoàn khởi hành đi tham quan khám phá biển Tây Nam."
                ),
                DetailSchedule(
                    "3",
                    "09h30:",
                    "Quý khách lên tàu khám phá biển đảo tại Phú Quốc, tại đây quý khách sẽ được tận hưởng từng khoảnh khắc đáng nhớ. Khi chính tay mình buông câu bắt cá, tắm biển ngắm san hô..."
                ),
                DetailSchedule(
                    "4",
                    "11h30:",
                    " Quý khách dùng bữa trưa trên tàu và nghỉ ngơi, thưởng thức các món ăn và ngắm cảnh biển Phú Quốc."
                ),
                DetailSchedule(
                    "5",
                    "14h00:",
                    " Tàu cập cảng An Thới, xe đón Quý khách đi tham quan Bãi Sao, cơ sở nuôi cấy ngọc trai của Úc, " +
                            "tham quan các cơ sở chế biến nước mắm Phú Quốc, làng chài cổ Hàm Ninh, thưởng thức Rượu sim - loại rượu vang làm từ trái sim rừng chín."
                ),
                DetailSchedule(
                    "6",
                    "16h30:",
                    " Quý khách trở về khách sạn nghỉ ngơi. Tự do tắm biển, dạo chơi."
                ),
                DetailSchedule(
                    "7",
                    "18h00:",
                    " Quý khách ăn tối tại nhà hàng, thưởng thức các đặc sản món ăn mang hương vị đặc sản biển Phú Quốc."
                ),
                DetailSchedule(
                    "8",
                    "Tối:",
                    " Quý khách tự do khám phá Đảo Ngọc về đêm, tham quan chợ đêm Dinh Cậu, nghỉ đêm tại khách sạn."
                )
            )
        ),
        DaySchedule(
            "Day3", "NGÀY 3: KHÁM PHÁ, TRẢI NGHIỆM TẠI VINPEARL PHÚ QUỐC (ĂN S, T,  T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dậy sớm ngắm bình minh, dạo chơi, tắm biển. Sau đó dùng bữa sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Xe đưa đoàn khởi hành đi khu du lịch Vinpearl Phú Quốc, Quý khách tự do vui chơi, check in tại Grand World Phú Quốc hoặc vui chơi, tắm biển, tham gia vào nhiều trò chơi cảm giác mạnh, khám phá thế giới đại dương kỳ thú ở Thủy Cung Vinpearl, tắm biển, mua sắm...(chi phí vé tự túc)."
                ),
                DetailSchedule(
                    "3",
                    "Trưa:",
                    " Quý khách dùng bữa trưa ở nhà hàng, thưởng thức các món ngon mang hương vị địa phương biển đảo Phú Quốc."
                ),
                DetailSchedule(
                    "4",
                    "Chiều:",
                    " Quý khách trở về nghỉ ngơi ở khách sạn, tự do tắm biển Phú Quốc."
                ),
                DetailSchedule(
                    "5",
                    "Tối:",
                    " Quý khách dùng bữa trưa ở nhà hàng, thưởng thức hương vị ẩm thực Phú Quốc và nghỉ đêm tại khách sạn."
                )
            )
        ),
        DaySchedule(
            "Day4", "NGÀY 4: PHÚ QUỐC - TỰ DO VUI CHƠI, MUA SẮM - VỀ HÀ NỘI (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách ăn sáng tại khách sạn, tự do tắm biển. Hoặc đi mua sắm đặc sản Phú Quốc về làm quà"
                ),
                DetailSchedule(
                    "2",
                    "10h30:",
                    " Quý khách ăn trưa tại nhà hàng. Quý khách làm thủ tục trả phòng khách sạn."
                ),
                DetailSchedule(
                    "3",
                    "13h30:",
                    "Xe đưa quý khách ra sân bay Phú Quốc, đáp chuyến bay VN cất cánh lúc 13h25."
                ),
                DetailSchedule(
                    "4",
                    "18h50:",
                    " Về đến sân bay Nội Bài, Hướng dẫn viên đưa quý khách trở về điểm đón  ban đầu. Kết thúc chương trình du lịch Hà Nội - Phú Quốc, Hướng dẫn viên cảm ơn, chia tay, hẹn gặp lại quý khách."
                )
            )
        )
    )
)
// https://tour.pro.vn/tour-moc-chau-son-la
var schedule9 = Schedule(
    Id = "tour_id_009",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: TỪ HÀ NỘI - DU LỊCH MỘC CHÂU - ĐỒI CHÈ TRÁI TIM (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "06h00:",
                    " Xe và hướng dẫn viên đón Quý khách tại điểm hẹn, khởi hành đi Mộc Châu - Sơn La. Trên hành trình hướng dẫn viên tổ chức thuyết minh hoạt náo vui nhộn trên xe. "
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Xe dừng nghỉ tại đèo Thung Khe - Thung Nhuối. Quý khách tự do nghỉ ngơi, chụp ảnh, thưởng ngoạn toàn cảnh thung lũng thơ mộng và yên bình."
                ),
                DetailSchedule(
                    "3",
                    "Trưa:",
                    " Xe đưa Quý khách tới Mộc Châu, Quý khách dùng bữa trưa tại nhà hàng. Sau bữa trưa Quý khách trở lại khách sạn nhận phòng nghỉ ngơi."
                ),
                DetailSchedule(
                    "4",
                    "Chiều:",
                    " Quý khách tham quan đồi chè - đồi chè trái tim, chụp hình lưu niệm với những sóng chè xanh ngát uốn lượn, tạo hình đầy quyến rũ tại nông trường chè Mộc Châu."
                ),
                DetailSchedule(
                    "5",
                    "19h00:",
                    " Quý khách dùng bữa tối tại nhà hàng. Thưởng thức các món ăn đặc sản ở Mộc Châu như bê chao...."
                )
            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: MỘC CHÂU - RỪNG THÔNG BẢN ÁNG - THÁC DẢI YẾM - HÀ NỘI (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách dùng bữa sáng tại khách sạn. Sau bữa sáng xe và hướng dẫn viên tiếp tục đưa Quý khách tham quan và trải nghiệm."
                ),
                DetailSchedule(
                    "2",
                    "08h00:",
                    " Xe và hướng dẫn viên đưa Quý khách tham quan khu du lịch rừng thông Bản Áng - Điểm du lịch lãng mạn với những ngôi nhà sàn truyền thống, những rừng thông hút tầm mắt. dạo quanh ngắm hồ được mệnh danh là  Hồ Xuân Hương  của Tây Bắc."
                ),
                DetailSchedule(
                    "3",
                    "09h00:",
                    "Quý khách tham quan những vườn hoa cải trắng tinh khôi, nở bạt ngàn tạo nên những khung cảnh thần tiên lãng mạn ( Vào mùa hoa Cải). Quý khách tiếp tục ghé thăm Thác Dải Yếm - Nơi có dòng thác hùng vĩ, ngày đêm đổ nước trắng xóa tạo lên khung cảnh thơ mộng đến huyền ảo. (lựa chọn 2 xe đưa Quý khách tham quan Happy Land với những thảm hoa muôn sắc màu, rực rỡ)."
                ),
                DetailSchedule(
                    "4",
                    "11h30:",
                    " Quý khách trả phòng khách sạn, sau đó dùng cơm trưa và nghỉ ngơi tại nhà hàng. Sau bữa trưa xe đón Quý khách khởi hành về Hà Nội.Trên đường về, dừng chân quý khách nghỉ ngơi và mua đặc sản địa phương về làm quà cho người thân và gia đình."
                ),
                DetailSchedule(
                    "5",
                    "17h00:",
                    " Xe đưa Quý khách về đến điểm đón tại Hà Nội, kết thúc tour Mộc Châu 2 ngày 1 đêm."
                )
            )
        )
    )
)
var schedule10 = Schedule(
    Id = "tour_id_010",
    ListTime = mutableListOf(
        DaySchedule(
            "Day1", "NGÀY 1: TỪ HÀ NỘI - DU LỊCH SAPA - KHU DU LỊCH NÚI HÀM RỒNG (ĂN T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "06h00:",
                    " Xe và hướng dẫn viên đón khách tại điểm hẹn, điểm danh quân số, khởi hành đi Sapa - Lào Cai. Hướng dẫn viên thuyết minh, tổ chức hoạt náo vui nhộn trên xe."
                ),
                DetailSchedule(
                    "2",
                    "11h00:",
                    " Xe đến dừng chân tại thị trấn Sapa - Nơi gặp gỡ đất trời, quý khách làm thủ tục nhận phòng, dùng bữa trưa tại nhà hàng sau đó trở về khách sạn nghỉ ngơi."
                ),
                DetailSchedule(
                    "3",
                    "14h30:",
                    " Hướng dẫn viên đưa quý khách đi KDL núi Hàm Rồng, du khách sẽ có cơ hội ngắm nhìn toàn cảnh thị trấn Sapa cùng thung lũng Mường Hoa, bản Cát Cát, bản Tả Phìn từ trên cao, chiêm ngưỡng nhiều loài hoa độc đáo và được khám phá những nét văn hóa đặc sắc của người dân tộc vùng cao, khám phá 3 điểm nổi tiếng như: trạm viễn thông Sapa, vườn lan và sân mây.Hướng dẫn viên đưa quý khách đi KDL núi Hàm Rồng, du khách sẽ có cơ hội ngắm nhìn toàn cảnh thị trấn Sapa cùng thung lũng Mường Hoa, bản Cát Cát, bản Tả Phìn từ trên cao, chiêm ngưỡng nhiều loài hoa độc đáo và được khám phá những nét văn hóa đặc sắc của người dân tộc vùng cao, khám phá 3 điểm nổi tiếng như: trạm viễn thông Sapa, vườn lan và sân mây."
                ),
                DetailSchedule(
                    "4",
                    "19h00:",
                    " Quý khách dùng bữa tối tại nhà hàng, thưởng thức các món ăn đặc sản mang đậm hương vị vùng cao như: rau rừng, cá suối nướng, thịt lợn bản, nhâm nhi những ly rượu táo mèo."
                ),
                DetailSchedule(
                    "5",
                    "20h00:",
                    " Quý khách được tự do đi dạo thị trấn, khám phá chợ đêm Sapa, hòa mình vào không khí nhộn nhịp của tiếng khèn và những điệu múa xòe của các chàng trai, cô gái người Mông (tối thứ 7 hàng tuần). Quý khách trở về nghỉ ngơi tại khách sạn."
                )

            )
        ),
        DaySchedule(
            "Day2", "NGÀY 2: CHINH PHỤC ĐỈNH FANSIPAN - NÓC NHÀ ĐÔNG DƯƠNG (ĂN S, T, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "Sáng:",
                    " Quý khách tự do dậy sớm ngắm bình minh, dạo chơi, sau đó Quý khách dùng bữa sáng tại khách sạn."
                ),
                DetailSchedule(
                    "2",
                    "07h30:",
                    " Xe và hướng dẫn viên đưa quý khách đến ga cáp treo để bắt đầu hành trình chinh phục đỉnh núi Fansipan(3143m). Được mệnh danh là nóc nhà Đông Dương."
                ),
                DetailSchedule(
                    "3",
                    "12h00:",
                    "Quý khách dùng bữa trưa tại nhà hàng, nghỉ ngơi tại khách sạn."
                ),
                DetailSchedule(
                    "4",
                    "Chiều:",
                    " Quý khách tự do tham quan, ngắm cảnh, chụp ảnh ở một số địa điểm quen thuộc như: Nhà thờ đá, hồ Sapa, cầu Mây, Bản Hồ..."
                ),
                DetailSchedule(
                    "5",
                    "Tối:",
                    " Quý khách dùng bữa tối tại nhà hàng, thưởng thức các đặc sản mang đậm hương vị núi rừng Tây Bắc, tự do tham quan, đi dạo, cafe, ngắm thị trấn Sapa về đêm. Sau đó trở về nghỉ ngơi tại khách sạn."
                )
            )
        ),
        DaySchedule(
            "Day3", "NGÀY 3: VUI CHƠI Ở CÁT BÀ - CÁI VIỀNG - HẢI PHÒNG - HÀ NỘI (ĂN S, T).",
            mutableListOf(
                DetailSchedule(
                    "1",
                    "08h00:",
                    " Quý khách dùng bữa sáng tại khách sạn. Xe và hướng dẫn viên đưa quý khách đi khám phá bản Cát Cát - ngôi làng cổ đẹp nhất Tây Bắc. Nơi có con suối róc rách hiền hòa, có tiếng thủ thỉ thân thương, có nụ cười trong veo của em thơ bên nếp nhà mộc mạc."
                ),
                DetailSchedule(
                    "2",
                    "11h00:",
                    " Quý khách dùng bữa trưa tại nhà hàng, nghỉ ngơi, làm thủ tục trả phòng khách sạn."
                ),
                DetailSchedule(
                    "3",
                    "13h00:",
                    " Xe ô tô đón Quý khách diHướng dẫn viên điểm danh quân số, Quý khách lên xe trở về Hà Nội."
                ),
                DetailSchedule(
                    "4",
                    "18h00:",
                    " Xe đưa quý khách trở về điểm đón ban đầu, hướng dẫn viên chia tay, cảm ơn và hẹn gặp lại quý khách. Kết thúc chuyến hành trình Hà Nội - Sapa - Fansipan - Hà Nội 3 ngày 2 đêm."
                )
            )
        )
    )
)

fun listSchedule ():MutableList<Schedule>{
    return mutableListOf<Schedule>().apply {
        add(schedule1)
        add(schedule2)
        add(schedule3)
        add(schedule4)
        add(schedule5)
        add(schedule6)
         add(schedule7)
        add(schedul8)
         add(schedule9)
        add(schedule10)

    }
}
