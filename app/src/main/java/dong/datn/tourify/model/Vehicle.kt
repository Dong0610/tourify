package dong.datn.tourify.model

import android.content.Context
import dong.datn.tourify.R
import dong.duan.travelapp.model.BaseModel

data class Vehicle(var vhId:String="",var vhName:String="",var image:Any?=null):BaseModel<Vehicle>()

fun getNewVehicle(context: Context):MutableList<Vehicle>{
    return mutableListOf<Vehicle>().apply {
        add(Vehicle("vehicle_id_001",context.getString(R.string.str_ct_plane), R.drawable.ic_round_airplane))
        add(Vehicle("vehicle_id_002",context.getString(R.string.str_ct_car), R.drawable.ic_round_directions_car))
        add(Vehicle("vehicle_id_003",context.getString(R.string.str_ct_train), R.drawable.ic_directions_transit))
    }
}








































