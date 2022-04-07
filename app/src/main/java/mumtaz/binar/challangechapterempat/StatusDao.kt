package mumtaz.binar.challangechapterempat

import androidx.room.*

@Dao
interface StatusDao {

    @Insert
    fun insertStatus(status: Status) : Long

    @Query("SELECT * FROM Status")
    fun getAllStatus(): List<Status>

    @Delete
    fun deleteStatus(status: Status) : Int

    @Update
    fun updateStatus(status: Status) : Int
}