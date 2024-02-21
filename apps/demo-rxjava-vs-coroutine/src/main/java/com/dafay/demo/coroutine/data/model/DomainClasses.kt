package com.dafay.demo.coroutine.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PagePhotos(
    val page: Int,
    val pre_page: Int,
    val photos: List<Photo>,
    val total_results: Int,
    val next_page: String,
) : Parcelable


@Parcelize
data class Photo(
    val id: Int,
    val width: Int,
    val height: Int,
    val avg_color: String? = "#E0E0E0",
    val src: UrlInfo,
    val alt: String,
) : Parcelable

@Parcelize
data class UrlInfo(
    val small: String,
    val medium: String,
    val large: String,
) : Parcelable







