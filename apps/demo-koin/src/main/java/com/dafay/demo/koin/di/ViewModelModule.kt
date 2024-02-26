package com.dafay.demo.koin.di

import com.dafay.demo.koin.ui.page.main.viewmodel.PhotosViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/26
 */

val viewModelModule = module {
    viewModel { PhotosViewModel(get()) }
}