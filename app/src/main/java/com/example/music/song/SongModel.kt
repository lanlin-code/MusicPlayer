package com.example.music.song

import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.SongPlayJson

class SongModel {
    fun getSongUrl(id: Long, callBack: RequestCallBack<SongPlayJson>) {
        ApiImplement.musicImplement.getSongPlay(id, callBack)
    }
}