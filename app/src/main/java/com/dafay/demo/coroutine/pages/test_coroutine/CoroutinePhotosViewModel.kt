package com.dafay.demo.coroutine.pages.test_coroutine

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dafay.demo.coroutine.data.http.PexelsApi
import com.dafay.demo.coroutine.data.model.Photo
import com.dafay.demo.lib.base.net.Result
import com.dafay.demo.lib.base.net.safeApiCall
import com.dafay.demo.lib.base.net.RetrofitFamily
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
                RetrofitFamily.createService(PexelsApi::class.java).queryPhotos("art", 1, 10)
            }
            if (result is Result.Success) {
                val photos = result.value
                photosLiveData.value = photos.photos
            }
        }
    }


}