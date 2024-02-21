package com.dafay.demo.coroutine.pages

import android.content.Intent
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dafay.demo.coroutine.R
import com.dafay.demo.coroutine.databinding.ActivityMainBinding
import com.dafay.demo.coroutine.pages.test_coroutine.CoroutineRequestActivity
import com.dafay.demo.coroutine.pages.test_rxjava.RxJavaRequestActivity
import com.dafay.demo.lib.base.ui.base.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    override val binding: ActivityMainBinding by viewBinding()

    override fun initViews() {
        super.initViews()

        binding.mbRxjava.setOnClickListener {
            startActivity(Intent(this, RxJavaRequestActivity::class.java))
        }
        binding.mbCoroutine.setOnClickListener {
            startActivity(Intent(this, CoroutineRequestActivity::class.java))
        }
    }

}