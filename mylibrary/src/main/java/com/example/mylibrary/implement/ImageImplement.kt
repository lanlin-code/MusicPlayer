package com.example.mylibrary.implement

import android.util.Log
import com.example.mylibrary.RetrofitUtil
import com.example.mylibrary.api.ImageAPI
import com.example.respository.RequestCallBack
import com.example.respository.api.ClientImageApi
import com.example.respository.bean.BannerJson
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ImageImplement: ClientImageApi {
    private val imageApi: ImageAPI = RetrofitUtil.getRetrofit().create(ImageAPI::class.java)
    private val tag = "ImageImplement"

    override fun getBanners(type: Int, callBack: RequestCallBack<BannerJson>) {
        val o = imageApi.getBanners(type)
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ callBack.callback(it) },
            {
                Log.d(tag, it.message, it)
                val s = it.message
                s?.let { callBack.error(s) }
            })
    }

}