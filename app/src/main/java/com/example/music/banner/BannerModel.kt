package com.example.music.banner

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.BannerJson

class BannerModel {
    fun loadBanners(type: Int, callback: RequestCallBack<BannerJson>) {
        DataUtil.imageApi.getBanners(type, callback)
    }
}