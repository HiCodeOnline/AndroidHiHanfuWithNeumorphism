package online.hicode.android.hihanfu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jewelry_image")
data class JewelryImage (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    // 名称
    val jewelryId: Long,
    // 图片路径
    val path: String,

)