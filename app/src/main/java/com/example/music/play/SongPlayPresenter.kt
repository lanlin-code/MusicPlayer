package com.example.music.play

import com.example.music.ResponseCallback
import com.example.music.entity.LrcParser
import com.example.music.entity.LrcRow
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.LyricJson

class SongPlayPresenter : RequestCallBack<LyricJson> {
    var responseCallback: ResponseCallback<MutableList<LrcRow>?>? = null
    val tag = "SongPlayPresenter"
    private val parser = LrcParser()
    override fun callback(data: LyricJson) {
        data.lrc?.let {
            it.lyric?.let {
                l -> responseCallback?.onSuccess(parser.parse(l))
            }
        }
    }

    override fun error(errorMsg: String) {
        LogUtil.debug(tag, errorMsg)
        responseCallback?.onError("歌词加载失败")
    }

}