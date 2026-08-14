package online.hicode.android.hihanfu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outfit_image")
data class OutfitImage (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    // 名称
    val outfitId: Long,
    // 图片路径
    val path: String,

)