package dong.datn.tourify.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Love")
data class LoveItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tourName: String,
    val tourId: String
)

data class LoveItemRemote(
    var id: Int = 0,
    var tourName: String = "",
    var tourId: String = ""
)
