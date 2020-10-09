package com.example.music.play

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.LyricJson

class SongPlayModel {
    fun lyric(id: Long, callBack: RequestCallBack<LyricJson>) {
        DataUtil.musicImplement.getSongLyric(id, callBack)
    }
}