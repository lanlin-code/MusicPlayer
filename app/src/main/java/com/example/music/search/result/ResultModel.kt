package com.example.music.search.result

import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.SearchSongJson

class ResultModel {
    fun loadSearchResult(keyword: String, limit: Int, page: Int, type: Int, callBack: RequestCallBack<SearchSongJson>) {
        val offset = (page - 1)*limit
        ApiImplement.searchImplement.getSearchSongs(limit, offset, type, keyword, callBack)
    }
}