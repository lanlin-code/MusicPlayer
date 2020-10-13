package com.example.music.search

import com.example.music.entity.HotWord
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.HotList

class HotWordPresenter : RequestCallBack<HotList> {
    var sListener: HotWordShow? = null
    private val tag = "HotWordPresenter"

    override fun error(errorMsg: String) {

    }

    override fun callback(data: HotList) {
        val h = HotWord.createHotList(data.data)
        LogUtil.debug(tag, h.toString())
        sListener?.show(h)
    }
}