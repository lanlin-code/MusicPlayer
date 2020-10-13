package com.example.music.song

import com.example.music.ResponseCallback
import com.example.music.entity.SongData
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.SongPlayJson

class SongPresenter: RequestCallBack<SongPlayJson> {

    var listener: ResponseCallback<MutableList<SongData>>? = null
    val tag = "SongPresenter"

    override fun callback(data: SongPlayJson) {
        val list = SongData.valueOfList(data)
        listener?.onSuccess(list)

    }

    override fun error(errorMsg: String) {
        LogUtil.debug(tag, errorMsg)
        listener?.onError("加载失败")
    }
}