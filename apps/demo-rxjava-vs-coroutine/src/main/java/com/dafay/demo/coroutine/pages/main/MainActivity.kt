package com.dafay.demo.coroutine.pages.main

import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dafay.demo.coroutine.R
import com.dafay.demo.coroutine.databinding.ActivityMainBinding
import com.dafay.demo.coroutine.pages.test_coroutine.CoroutineRequestActivity
import com.dafay.demo.coroutine.pages.test_rxjava.RxJavaRequestActivity
import com.dafay.demo.lib.base.ui.base.BaseActivity
import com.dafay.demo.lib.base.utils.println
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.ContinuationInterceptor

class MainActivity : BaseActivity(R.layout.activity_main) {

    override val binding: ActivityMainBinding by viewBinding()


    override fun bindListener() {
        super.bindListener()
        binding.mbRxjava.setOnClickListener {
            startActivity(Intent(this, RxJavaRequestActivity::class.java))
        }
        binding.mbCoroutine.setOnClickListener {
            startActivity(Intent(this, CoroutineRequestActivity::class.java))
        }
        binding.mbTest.setOnClickListener {
            MainScope().launch {
                val scope = CoroutineScope(Job() + Dispatchers.Main + CoroutineName("parent"))
                println("-ContinuationInterceptor:" + scope.coroutineContext[ContinuationInterceptor])
                println("-Job:" + scope.coroutineContext[Job])
                println("-CoroutineName:" + scope.coroutineContext[CoroutineName])
                launch {
                    println("Job:" + this.coroutineContext[Job])
                }
                val job = scope.launch(Dispatchers.IO) {
                    println("ContinuationInterceptor:" + this.coroutineContext[ContinuationInterceptor])
                    println("Job:" + this.coroutineContext[Job])
                    println("CoroutineName:" + this.coroutineContext[CoroutineName])
                    delay(1000) //
                    println("I'm sleeping  ...")
                }
                println("job:" + job)
                job.join()
                val jobOther = scope.launch {
                    println("ContinuationInterceptor:" + this.coroutineContext[ContinuationInterceptor])
                    println("Job:" + this.coroutineContext[Job])
                    println("CoroutineName:" + this.coroutineContext[CoroutineName])
                    delay(1000) //
                    println("I'm sleeping  ...")
                }
                println("jobOther:" + jobOther)
                jobOther.join()
                println("-ContinuationInterceptor:" + scope.coroutineContext[ContinuationInterceptor])
                println("-Job:" + scope.coroutineContext[Job])
                println("-CoroutineName:" + scope.coroutineContext[CoroutineName])
            }
        }
    }


}