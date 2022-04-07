package mumtaz.binar.challangechapterempat

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Status(
    @PrimaryKey(autoGenerate = true)
    var id : Int?,

    @ColumnInfo(name = "nama")
    var nama : String,

    @ColumnInfo(name = "status")
    var status:String

) : Parcelable
