package com.example.music.search

import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.SearchDefaultJson

class DefaultSearchModel {
    fun defaultWord(callBack: RequestCallBack<SearchDefaultJson>) {
        ApiImplement.searchImplement.getDefaultKeywords(callBack)
    }
}