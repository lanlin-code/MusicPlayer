package com.example.mylibrary.implement

import com.example.mylibrary.RetrofitUtil
import com.example.mylibrary.api.SearchAPI
import com.example.respository.RequestCallBack
import com.example.respository.api.ClientSearchApi
import com.example.respository.bean.SearchDefaultJson
import com.example.respository.bean.SearchSongJson
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SearchImplement : ClientSearchApi {
    private val searchApi = RetrofitUtil.getRetrofit().create(SearchAPI::class.java)
    override fun getSearchSongs(
        limit: Int,
        offset: Int,
        type: Int,
        keyword: String,
        callBack: RequestCallBack<SearchSongJson>
    ) {

        val o = searchApi.getSearchSongs(keyword, limit, offset)
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ callBack.callback(it) }, { it.message?.let { m -> callBack.error(m) } })

    }

    override fun getDefaultKeywords(callBack: RequestCallBack<SearchDefaultJson>) {
        val o = searchApi.getDefaultWord()
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ callBack.callback(it) }, { it.message?.let { m -> callBack.error(m) } })
    }
}