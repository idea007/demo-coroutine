package com.dafay.demo.coroutine.test

import android.annotation.SuppressLint
import com.dafay.demo.lib.base.utils.error
import io.reactivex.Observable
import io.reactivex.plugins.RxJavaPlugins

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/23
 */
object TestRxJava {

    init {
        RxJavaPlugins.setErrorHandler({
            error("RxJavaPlugins errorHandler", it)
        })
    }

    @SuppressLint("CheckResult")
    fun test() {
        Observable.create<String?> {
            it.onNext(TestDataSource.instance.nullStr)
            it.onComplete()
        }.subscribe({},{
            error("error", it)
        })
    }
}