package online.hicode.android.hihanfu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hanfu")
data class Hanfu (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val cabinetId: Long,
    // 名称
    val name: String,
    // 价格
    val price: String,
    // 购买日期
    val date: String,

    // 尺码
    val size: String = "",

    // 材质
    val material: String = "",

    val cover: String,

)