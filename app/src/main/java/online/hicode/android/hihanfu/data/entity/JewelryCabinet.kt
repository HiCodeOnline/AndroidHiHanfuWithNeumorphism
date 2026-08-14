package online.hicode.android.hihanfu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jewelry_cabinet")
data class JewelryCabinet (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,

)