package online.hicode.android.hihanfu

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiHanfuApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 这里可以初始化其他三方库（如物理基础配置），Hilt 的初始化是全自动的
    }

}