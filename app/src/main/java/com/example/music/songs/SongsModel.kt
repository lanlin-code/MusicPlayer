package com.example.music.songs

import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.SongDetailJson
import com.example.respository.bean.SongIdsJson

class SongsModel {

    fun getIds(lid: Long, callBack: RequestCallBack<SongIdsJson>) {
        ApiImplement.musicImplement.getSongListDetail(lid, callBack)
    }

    fun getSongs(ids: String, callBack: RequestCallBack<SongDetailJson>) {
        ApiImplement.musicImplement.getSongsDetail(ids, callBack)
    }

}