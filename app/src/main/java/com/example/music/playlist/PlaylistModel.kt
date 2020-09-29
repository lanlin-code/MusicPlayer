package com.example.music.playlist

import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserPlayListJson

class PlaylistModel {

    fun userPlaylists(uid: Long, callBack: RequestCallBack<UserPlayListJson>) {
        ApiImplement.musicImplement.getUserPlayList(uid, callBack)
    }

}