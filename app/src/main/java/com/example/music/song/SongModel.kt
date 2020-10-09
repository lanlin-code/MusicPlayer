package com.example.music.song

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.SongPlayJson

class SongModel {
    fun getSongUrl(id: Long, callBack: RequestCallBack<SongPlayJson>) {
        DataUtil.musicImplement.getSongPlay(id, callBack)
    }
}