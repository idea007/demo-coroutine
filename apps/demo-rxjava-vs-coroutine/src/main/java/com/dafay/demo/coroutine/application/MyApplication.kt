package com.dafay.demo.coroutine.application

import android.app.Application
import com.dafay.demo.coroutine.data.ConfigC
import com.dafay.demo.coroutine.data.http.CommonInterceptor
import com.example.demo.lib.net.HttpConfig

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
        val config = HttpConfig.Config.build {
            this.baseUrl = ConfigC.PEXELS_BASE_URL
            this.addInterceptor(CommonInterceptor())
        }
        HttpConfig.initConfig(config)
    }
}