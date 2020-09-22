package com.example.music.banner

import com.example.music.ResponseCallback
import com.example.music.entity.Banners
import com.example.respository.RequestCallBack
import com.example.respository.bean.BannerJson

class BannerPresenter: RequestCallBack<BannerJson> {

    var listener: ResponseCallback<Banners>? = null

    override fun callback(data: BannerJson) {
        val l = data.banners
        val banners = Banners()
        l?.let {
            for (b in l) {
                val s = b.pic
                s?.let { banners.urlList.add(s) }
            }
        }
        listener?.onSuccess(banners)
    }

    override fun error(errorMsg: String) {
        listener?.onError(errorMsg)
    }

}