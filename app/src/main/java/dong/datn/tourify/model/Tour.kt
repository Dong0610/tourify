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
    var countTour: Int = 10,
    var star: Double = 4.5,
    var countRating: Int = 100,
    var tourTime: MutableList<TourTime> =  mutableListOf(),
    var tourAddress: String =  "",
    var vehicleId: String=""
) : BaseModel<Tour>()

fun listImage(): MutableList<String> {
    return mutableListOf(
        "https://www.anlamtravel.vn/storage/pagedata/100921/img/images/menu/2_1.jpg",
        "https://www.cathaypacific.com/content/dam/focal-point/cx/inspiration/2024/03/" +
                "The_best_destinations_for_solo_female_travel_in_2024-National_Park_Surat_Thani" +
                "_Thailand-Peera_Sathawirawong-Gettyimages-HERO.renditionimage.900.600.jpg",
        "https://burst.shopifycdn.com/photos/calm-water-in-european-city.jpg?width=1000&format=pjpg&exif=0&iptc=0",
        "https://cdn.pixabay.com/photo/2016/11/29/10/16/buildings-1868962_640.jpg",
        "https://cdn.pixabay.com/photo/2016/10/18/08/13/travel-1749508_640.jpg",
        "https://www.shutterstock.com/image-photo/pumped-storage-hydro-plant-dlouhe-260nw-1958569861.jpg",
        "https://bekatravel.com/files/thumb/480/320/uploads/Slide/cau-vang.png"
    )
}

object RandomTour {
    fun dataRandom(): MutableList<Tour> {
        return mutableListOf(
            Tour(
                tourID = "tour_id_001",
                tourName = "Discover Paris",
                tourDescription = "Explore the iconic landmarks of Paris including the Eiffel Tower, Louvre Museum, and more.",
                tourImage = mutableListOf(listImage().get(0), listImage().get(1)),
                tourPrice = 1000000.0,
                saleId="sale_id_003",
                success = 120,
                cancel = 5,
                countTour = 10,
                star = 4.8,
                countRating = 200,
                tourTime = mutableListOf(
                    TourTime(
                        timeID = "tour_id_001_time_0",
                        "3 ngày hai đêm",
                        "09:00 02/06/2024",
                        "12:00 05/06/2024",
                        10
                    ),
                    TourTime(
                        timeID = "tour_id_001_time_1",
                        "3 ngày hai đêm",
                        "09:00 04/06/2024",
                        "12:00 07/06/2024",
                        10
                    ),
                    TourTime(
                        timeID = "tour_id_001_time_2",
                        "3 ngày hai đêm", "09:00 08/06/2024", "12:00 11/06/2024", 10
                    ),
                ),
                tourAddress =createFamousPlaces().get(0).placeID,
                vehicleId = getNewVehicle(context = appContext).get(0).vhId
            ), Tour(
                tourID = "tour_id_002",
                tourName = "Safari Adventure",
                tourDescription = "Experience the thrill of a safari in the Serengeti with guided tours and luxury accommodation.",
                tourImage = mutableListOf(listImage().get(2), listImage().get(3)),
                tourPrice = 1000000.0,
                saleId="sale_id_003",
                success = 85,
                cancel = 2,
                countTour = 10,
                star = 4.9,
                countRating = 150,
                tourTime = mutableListOf(
                    TourTime(
                        timeID = "tour_id_002_time_0",
                        "3 ngày hai đêm", "09:00 02/06/2024", "12:00 05/06/2024", 10
                    ),
                    TourTime(
                        timeID = "tour_id_002_time_1",
                        "3 ngày hai đêm", "09:00 04/06/2024", "12:00 07/06/2024", 10
                    ),
                    TourTime(
                        timeID = "tour_id_002_time_2",
                        "3 ngày hai đêm", "09:00 08/06/2024", "12:00 11/06/2024", 10
                    ),
                ),
                tourAddress = createFamousPlaces().get(0).placeID,
                vehicleId = getNewVehicle(context = appContext).get(1).vhId
            ), Tour(
                tourID = "tour_id_003",
                tourName = "Tokyo Delights",
                tourDescription = "Immerse yourself in the vibrant culture of Tokyo, from sushi making to exploring historic temples.",
                tourImage = mutableListOf(
                    listImage().get(4),
                    listImage().get(5),
                    listImage().get(6)
                ),
                tourPrice = 1000000.0,
                saleId="sale_id_003",
                success = 200,
                cancel = 10,
                countTour = 10,
                star = 4.7,
                countRating = 250,
                tourTime = mutableListOf(
                    TourTime(
                        timeID = "tour_id_003_time_0",
                        "3 ngày hai đêm", "09:00 02/06/2024", "12:00 05/06/2024", 10
                    ),
                    TourTime(
                        timeID = "tour_id_003_time_1",
                        "3 ngày hai đêm", "09:00 04/06/2024", "12:00 07/06/2024", 10
                    ),
                    TourTime(
                        timeID = "tour_id_003_time_2",
                        "3 ngày hai đêm", "09:00 08/06/2024", "12:00 11/06/2024", 10
                    ),
                ),
                tourAddress = createFamousPlaces().get(3).placeID,
                vehicleId = getNewVehicle(context = appContext).get(2).vhId
            )
        )
    }

}