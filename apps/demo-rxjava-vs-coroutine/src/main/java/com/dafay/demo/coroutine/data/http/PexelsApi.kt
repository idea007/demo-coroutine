package com.dafay.demo.coroutine.data.http

import com.dafay.demo.coroutine.data.model.PagePhotos
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/20
 */
interface PexelsApi {

    @GET("/v1/search")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
    ): Observable<PagePhotos>


    @GET("/v1/search")
    suspend fun queryPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
    ): PagePhotos


    @GET("/v1/xxxxxx")
    suspend fun queryPhotosError(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
    ): PagePhotos

}