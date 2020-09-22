package com.example.music.banner

import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.BannerJson

class BannerModel {
    fun loadBanners(type: Int, callback: RequestCallBack<BannerJson>) {
        ApiImplement.imageApi.getBanners(type, callback)
    }
}