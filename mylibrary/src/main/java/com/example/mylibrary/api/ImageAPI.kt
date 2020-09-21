package com.example.mylibrary.api

import com.example.respository.bean.BannerJson
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface ImageAPI {
    // 轮播图
    @GET("banner")
    fun getBanners(@Query("type") type: Int): Observable<BannerJson>
}