package online.hicode.android.hihanfu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hanfu_image")
data class HanfuImage (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    // 名称
    val hanfuId: Long,
    // 图片路径
    val path: String,

)