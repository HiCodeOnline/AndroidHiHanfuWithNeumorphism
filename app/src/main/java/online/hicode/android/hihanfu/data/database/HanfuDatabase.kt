package online.hicode.android.hihanfu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import online.hicode.android.hihanfu.data.dao.HanfuDao
import online.hicode.android.hihanfu.data.entity.Hanfu
import online.hicode.android.hihanfu.data.entity.HanfuCabinet
import online.hicode.android.hihanfu.data.entity.HanfuImage
import online.hicode.android.hihanfu.data.entity.Jewelry
import online.hicode.android.hihanfu.data.entity.JewelryCabinet
import online.hicode.android.hihanfu.data.entity.JewelryImage
import online.hicode.android.hihanfu.data.entity.Outfit
import online.hicode.android.hihanfu.data.entity.OutfitHanfuRel
import online.hicode.android.hihanfu.data.entity.OutfitImage

@Database(
    entities = [
        Hanfu::class,
        HanfuCabinet::class,
        HanfuImage::class,
        Jewelry::class,
        JewelryCabinet::class,
        JewelryImage::class,
        Outfit::class,
        OutfitImage::class,
        OutfitHanfuRel::class
    ],
    version = 1
)
abstract class HanfuDatabase : RoomDatabase() {

    abstract fun hanfuDao(): HanfuDao


}


// 💡版本升级时通过migration 声明从版本 1 到 2 的升级逻辑
//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        // 使用标准的 SQLite 语法
//        db.execSQL("ALTER TABLE hanfu ADD COLUMN size TEXT NOT NULL DEFAULT ''")
//        db.execSQL("ALTER TABLE hanfu ADD COLUMN material TEXT NOT NULL DEFAULT ''")
//
//        db.execSQL("ALTER TABLE jewelry ADD COLUMN size TEXT NOT NULL DEFAULT ''")
//        db.execSQL("ALTER TABLE jewelry ADD COLUMN material TEXT NOT NULL DEFAULT ''")
//    }
//}