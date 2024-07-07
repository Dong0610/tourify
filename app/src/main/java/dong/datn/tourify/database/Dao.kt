package dong.datn.tourify.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoveDao {
    @Query("SELECT * FROM Love")
    fun getAllItems(): List<LoveItem>

    @Insert
    suspend fun insertItem(item: LoveItem)

    @Query("DELETE FROM Love")
    suspend fun deleteAllItems()

    @Query("SELECT EXISTS(SELECT 1 FROM Love WHERE tourId = :idTour)")
    suspend fun doesItemExist(idTour: String): Boolean
    @Query("DELETE FROM Love WHERE tourId = :tourID")
    suspend fun deleteItem(tourID: String)

}