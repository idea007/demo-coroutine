package com.dafay.demo.koin.di

import com.dafay.demo.koin.ui.page.main.viewmodel.PhotosRepository
import org.koin.dsl.module

val repositoryModule = module {
    single(createdAtStart = true) { PhotosRepository() }
}