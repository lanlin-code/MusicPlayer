package com.example.music.playlist

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserPlayListJson

class PlaylistModel {

    fun userPlaylists(uid: Long, callBack: RequestCallBack<UserPlayListJson>) {
        DataUtil.musicImplement.getUserPlayList(uid, callBack)
    }

}