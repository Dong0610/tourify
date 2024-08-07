package dong.datn.tourify.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OrderTime")
data class OrderTime(
    @PrimaryKey(autoGenerate = true) val timeId: Int = 0,
    var tourId: String,
    var startTime: String,
    var endTime: String,
    var orderId: String, var status: String
)