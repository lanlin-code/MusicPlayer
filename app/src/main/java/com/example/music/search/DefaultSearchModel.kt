package com.example.music.search

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.SearchDefaultJson

class DefaultSearchModel {
    fun defaultWord(callBack: RequestCallBack<SearchDefaultJson>) {
        DataUtil.searchImplement.getDefaultKeywords(callBack)
    }
}