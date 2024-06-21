package dong.datn.tourify.model

import dong.datn.tourify.R


fun getListPlaces():MutableList<Places>{
    return mutableListOf<Places>().apply {
        add(Places("places_01","Hà Nội", R.drawable.img_tour_1,10,""))
        add(Places("places_01","Ninh Bình", R.drawable.img_tour_3,10,""))
        add(Places("places_01","Lào Cai", R.drawable.img_tour_2,10,""))
    }
}