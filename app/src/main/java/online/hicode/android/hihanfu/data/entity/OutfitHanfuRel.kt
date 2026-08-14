package online.hicode.android.hihanfu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outfit_hanfu_rel")
data class OutfitHanfuRel (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    // 名称
    val outfitId: Long,

    val type: String,

    // 关联ID
    val relId: Long,

)