package com.dafay.demo.coroutine.app

import android.app.Application
import com.dafay.demo.coroutine.data.Constants
import com.dafay.demo.coroutine.data.http.CommonInterceptor
import com.example.demo.lib.net.HttpConfigManager

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/20
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initHttpConfig()
    }

    private fun initHttpConfig() {
        val config = HttpConfigManager.Config.build {
            this.baseUrl = Constants.PEXELS_BASE_URL
            this.addInterceptor(CommonInterceptor())
        }
        HttpConfigManager.initConfig(config)
    }
}