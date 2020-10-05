package com.example.music.search

import com.example.music.ShowDataListener
import com.example.music.entity.DefaultSearchWord
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.SearchDefaultJson

class DefaultSearchPresenter: RequestCallBack<SearchDefaultJson> {
    var sListener: ShowDataListener<DefaultSearchWord>? = null
    override fun callback(data: SearchDefaultJson) {
        sListener?.show(DefaultSearchWord.valueOf(data))
    }

    override fun error(errorMsg: String) {

    }
}