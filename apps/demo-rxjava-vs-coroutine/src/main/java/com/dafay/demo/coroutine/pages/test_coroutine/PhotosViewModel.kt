package com.dafay.demo.coroutine.pages.test_coroutine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dafay.demo.coroutine.data.model.Photo
import kotlinx.coroutines.launch
import com.dafay.demo.lib.base.net.Result

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/20
 */
class PhotosViewModel(private val photosRepository: PhotosRepository) : ViewModel() {

    val photosLiveData = MutableLiveData<Result<List<Photo>>>()

    fun searchPhotos() {
        photosLiveData.postValue(Result.Loading)
        viewModelScope.launch {
            photosLiveData.postValue(photosRepository.searchPhotos())
        }
    }

    /**
     * 使用 livedata-ktx
     */
    fun searchPhotosWithLD() = liveData(viewModelScope.coroutineContext) {
        emit(Result.Loading)
        val accessTokenResult = photosRepository.searchPhotos()
        emit(accessTokenResult)
    }


}