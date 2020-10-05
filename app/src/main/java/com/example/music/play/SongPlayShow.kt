package com.example.music.play

import com.example.music.ShowDataListener
import com.example.music.entity.LrcRow
import com.example.music.util.LogUtil
import com.example.music.widget.LrcView

class SongPlayShow(var view: LrcView? = null) : ShowDataListener<MutableList<LrcRow>?> {
    override fun show(data: MutableList<LrcRow>?) {
        LogUtil.debug("song show", "data = $data")
        view?.setLrc(data)
    }
}