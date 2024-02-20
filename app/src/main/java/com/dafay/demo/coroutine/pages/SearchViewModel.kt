package com.dafay.demo.coroutine.pages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dafay.demo.coroutine.data.http.PexelsApi
import com.dafay.demo.coroutine.data.model.Photo
import com.dafay.demo.lib.base.net.rx.RetrofitManager
import com.dafay.demo.lib.base.ui.BaseViewModel
import com.dafay.demo.lib.base.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @Des
 * @Author lipengfei
 * @Date 2023/11/27 18:50
 */

class SearchViewModel : BaseViewModel() {


    private val PAGE_COUNT = 10

    val photosLiveData = MutableLiveData<List<Photo>>()


    fun refreshPhotos() {
        compositeDisposable.add(
            RetrofitManager.createService(PexelsApi::class.java)
                .searchPhotos("art", 1, PAGE_COUNT).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    LogUtils.d("success: photos.size=${it.photos.size}")
                    if (!it.photos.isEmpty()) {
                        photosLiveData.value = it.photos
                    }
                }) { onError: Throwable? ->
                    LogUtils.e("fail:", onError)
                })
    }

}

