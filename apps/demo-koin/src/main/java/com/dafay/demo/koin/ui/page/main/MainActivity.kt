package com.dafay.demo.koin.ui.page.main

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dafay.demo.coroutine.pages.adapter.PhotoAdapter
import com.dafay.demo.koin.R
import com.dafay.demo.koin.databinding.ActivityMainBinding
import com.dafay.demo.koin.ui.page.main.viewmodel.PhotosViewModel
import com.dafay.demo.lib.base.net.Result
import com.dafay.demo.lib.base.ui.base.BaseActivity
import com.dafay.demo.lib.base.utils.dp2px
import com.dafay.demo.lib.base.utils.hideLoading
import com.dafay.demo.lib.base.utils.showLoading
import com.dafay.demo.lib.base.utils.toast
import com.example.demo.biz.base.widgets.GridMarginDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(R.layout.activity_main) {
    override val binding: ActivityMainBinding by viewBinding()
    private val viewModel: PhotosViewModel by viewModel()

    private lateinit var photoAdapter: PhotoAdapter
    override fun initViews() {
        super.initViews()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        photoAdapter = PhotoAdapter()
        binding.rvRecyclerview.addItemDecoration(GridMarginDecoration(4.dp2px, 4.dp2px, 4.dp2px, 4.dp2px))
        binding.rvRecyclerview.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        binding.rvRecyclerview.adapter = photoAdapter
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.photosLiveData.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading()
                }

                is Result.Success -> {
                    hideLoading()
                    photoAdapter.setDatas(it.value)
                }

                is Result.Error, Result.NetworkError -> {
                    hideLoading()
                    toast("error")
                }
            }
        }
    }

    override fun initializeData() {
        super.initializeData()
        viewModel.searchPhotosWithLD().observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading()
                }

                is Result.Success -> {
                    hideLoading()
                    photoAdapter.setDatas(it.value)
                }

                is Result.Error, Result.NetworkError -> {
                    hideLoading()
                    toast("error")
                }
            }
        }
    }
}