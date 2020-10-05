package com.example.music.play

import com.example.music.util.LogUtil
import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.LyricJson

class SongPlayModel {
    fun lyric(id: Long, callBack: RequestCallBack<LyricJson>) {
        ApiImplement.musicImplement.getSongLyric(id, callBack)
    }
}