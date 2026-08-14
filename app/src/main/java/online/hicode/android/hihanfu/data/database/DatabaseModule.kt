package online.hicode.android.hihanfu.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import online.hicode.android.hihanfu.data.dao.HanfuDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): HanfuDatabase {
        // Hilt 负责执行这段代码，真正的“大厨”依然是 Room 的 databaseBuilder
        return Room.databaseBuilder(
            context,
            HanfuDatabase::class.java,
            "hanfu_database"
        )
        // 版本升级使用
        // .addMigrations(MIGRATION_1_2)
        .build()
    }

    @Provides
    @Singleton
    fun provideHanfuDao(database: HanfuDatabase): HanfuDao {
        // 🌟 核心点：这里直接调用了你在 @Database 类里写的那个抽象方法
        // 因为 Room 已经在底层把这个方法实现了，所以这里能完美拿到实例并分发出去
        return database.hanfuDao()
    }

}