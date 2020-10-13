package com.example.mylibrary.api

import com.example.respository.bean.HotList
import com.example.respository.bean.SearchDefaultJson
import com.example.respository.bean.SearchSongJson
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface SearchAPI {
    // 搜索
    @GET("/search")
    fun getSearchSongs(@Query("keywords") keywords: String, @Query("limit") limit: Int = 30,
               @Query("offset") offset: Int = 0) : Observable<SearchSongJson>

    @GET("/search/default")
    fun getDefaultWord(): Observable<SearchDefaultJson>

    @GET("search/hot/detail")
    fun getHotList(): Observable<HotList>
}