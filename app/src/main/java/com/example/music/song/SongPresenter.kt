package com.example.music.song

import com.example.music.ResponseCallback
import com.example.music.entity.SongData
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.SongPlayJson

class SongPresenter: RequestCallBack<SongPlayJson> {

    var listener: ResponseCallback<MutableList<SongData>>? = null

    override fun callback(data: SongPlayJson) {
        LogUtil.debug("SongPresenter", Thread.currentThread().name)
        val list = SongData.valueOfList(data)
        listener?.onSuccess(list)

    }

    override fun error(errorMsg: String) {
        listener?.onError(errorMsg)
    }
}