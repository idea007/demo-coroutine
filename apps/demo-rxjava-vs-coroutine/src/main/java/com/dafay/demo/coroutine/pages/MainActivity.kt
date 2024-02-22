package com.dafay.demo.coroutine.pages

import android.content.Intent
import androidx.annotation.WorkerThread
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dafay.demo.coroutine.R
import com.dafay.demo.coroutine.databinding.ActivityMainBinding
import com.dafay.demo.coroutine.pages.test_coroutine.CoroutineRequestActivity
import com.dafay.demo.coroutine.pages.test_rxjava.RxJavaRequestActivity
import com.dafay.demo.coroutine.test.TestCoroutine
import com.dafay.demo.lib.base.ui.base.BaseActivity
import com.dafay.demo.lib.base.utils.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

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

        binding.mbTestCoroutine.setOnClickListener {
            TestCoroutine.testLaunch()
        }
    }




}