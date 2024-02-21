package com.dafay.demo.coroutine.pages.test_coroutine

import com.dafay.demo.coroutine.data.http.PexelsApi
import com.dafay.demo.coroutine.data.model.Photo
import com.dafay.demo.lib.base.net.RetrofitFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import com.dafay.demo.lib.base.net.Result
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/21
 */
class PhotosRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun searchPhotos(): Result<List<Photo>> {
        // TODO: 异常处理可抽取
        return withContext(dispatcher) {
            try {
                Result.Success(RetrofitFamily.createService(PexelsApi::class.java).queryPhotos("art", 1, 10).photos)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> Result.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = throwable.response()?.errorBody()?.string()
                        Result.Error(code, errorResponse)
                    }

                    else -> Result.Error(null, throwable.message)
                }
            }
        }
    }
}