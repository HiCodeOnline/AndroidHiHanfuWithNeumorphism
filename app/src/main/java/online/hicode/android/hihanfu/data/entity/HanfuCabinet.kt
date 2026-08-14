package online.hicode.android.hihanfu.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hanfu_cabinet")
data class HanfuCabinet (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,

)