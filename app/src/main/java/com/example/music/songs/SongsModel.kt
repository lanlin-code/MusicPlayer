package com.example.music.songs

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.SongDetailJson
import com.example.respository.bean.SongIdsJson

class SongsModel {

    fun getIds(lid: Long, callBack: RequestCallBack<SongIdsJson>) {
        DataUtil.musicImplement.getSongListDetail(lid, callBack)
    }

    fun getSongs(ids: String, callBack: RequestCallBack<SongDetailJson>) {
        DataUtil.musicImplement.getSongsDetail(ids, callBack)
    }

}