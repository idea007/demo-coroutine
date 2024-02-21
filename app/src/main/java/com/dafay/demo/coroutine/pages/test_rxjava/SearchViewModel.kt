package com.dafay.demo.coroutine.pages.test_rxjava

import androidx.lifecycle.MutableLiveData
import com.dafay.demo.coroutine.data.ConstC
import com.dafay.demo.coroutine.data.http.PexelsApi
import com.dafay.demo.coroutine.data.model.Photo
import com.dafay.demo.lib.base.net.Result
import com.dafay.demo.lib.base.net.RetrofitFamily
import com.dafay.demo.lib.base.ui.BaseViewModel
import com.dafay.demo.lib.base.utils.debug
import com.dafay.demo.lib.base.utils.error
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @Des
 * @Author lipengfei
 * @Date 2023/11/27 18:50
 */
class SearchViewModel : BaseViewModel() {
    val photosLiveData = MutableLiveData<Result<List<Photo>>>()
    fun refreshPhotos() {
        if (photosLiveData.value is Result.Loading) {
            return
        }
        photosLiveData.value = Result.Loading
        compositeDisposable.add(
            RetrofitFamily.createService(PexelsApi::class.java)
                .searchPhotos("art", 1, ConstC.PAGE_COUNT_TEN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    debug("success: photos.size=${it.photos.size}")
                    photosLiveData.value = Result.Success(it.photos)
                }) { onError: Throwable? ->
                    error("fail:", onError)
                    photosLiveData.value = Result.Error(null, onError?.message)
                })
    }
}

