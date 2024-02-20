package com.dafay.demo.coroutine.pages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dafay.demo.coroutine.data.http.PexelsApi
import com.dafay.demo.coroutine.data.model.Photo
import com.dafay.demo.lib.base.net.rx.RetrofitManager
import com.dafay.demo.lib.base.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/20
 */
class CoroutinePhotosViewModel : ViewModel() {

    val photosLiveData = MutableLiveData<List<Photo>>()

    fun queryPhotos() {
        viewModelScope.launch {
            val result = safeApiCall(Dispatchers.IO) {
                RetrofitManager.createService(PexelsApi::class.java).queryPhotos("art", 1, 10)
            }
            if (result is Result.Success) {
                val photos = result.value
                photosLiveData.value = photos.photos
            }
        }
    }


}