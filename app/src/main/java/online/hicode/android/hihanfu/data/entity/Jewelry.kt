package online.hicode.android.hihanfu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jewelry")
data class Jewelry (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val cabinetId: Long,

    val name: String,

    val cover: String,

    val price: String,

    val date: String,

    // 尺码
    val size: String = "",

    // 材质
    val material: String = "",

)