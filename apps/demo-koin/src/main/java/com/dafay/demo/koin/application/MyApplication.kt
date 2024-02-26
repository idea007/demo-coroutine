package com.dafay.demo.coroutine.application

import android.app.Application
import com.dafay.demo.coroutine.data.ConfigC
import com.dafay.demo.koin.data.http.CommonInterceptor
import com.dafay.demo.koin.di.appModules
import com.dafay.demo.koin.ui.page.main.viewmodel.PhotosRepository
import com.dafay.demo.koin.ui.page.main.viewmodel.PhotosViewModel
import com.example.demo.lib.net.HttpConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/20
 */
class MyApplication : Application() {

//    迁移到 di 目录
//    val appModules = module {
//        single { PhotosRepository(get()) }
//        viewModel { PhotosViewModel(get()) }
//    }

    override fun onCreate() {
        super.onCreate()
        initHttpConfig()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModules)
        }
    }

    private fun initHttpConfig() {
        val config = HttpConfig.Config.build {
            this.baseUrl = ConfigC.PEXELS_BASE_URL
            this.addInterceptor(CommonInterceptor())
        }
        HttpConfig.initConfig(config)
    }

    override fun onTerminate() {
        super.onTerminate()
        // 清理 Koin
        stopKoin()
    }
}