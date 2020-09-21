package com.example.respository.api

import com.example.respository.bean.BannerJson
import com.example.respository.RequestCallBack

interface ClientImageApi {
    /**
     * 获取轮播图.
     * */
    fun getBanners(type : Int ,callBack: RequestCallBack<BannerJson>)
}