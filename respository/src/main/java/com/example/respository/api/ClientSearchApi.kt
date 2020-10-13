package com.example.respository.api

import com.example.respository.bean.HotList
import com.example.respository.bean.SearchDefaultJson
import com.example.respository.bean.SearchSongJson
import com.example.respository.RequestCallBack

interface ClientSearchApi {
    fun getSearchSongs(
        limit: Int,
        offset: Int,
        type: Int,
        keyword: String,
        callBack: RequestCallBack<SearchSongJson>
    )
    
    fun getDefaultKeywords(callBack: RequestCallBack<SearchDefaultJson>)

    /**
     * 获取热搜列表.
     * url：baseUrl/search/hot/detail
     * */
    fun getHotList(callBack: RequestCallBack<HotList>)
}