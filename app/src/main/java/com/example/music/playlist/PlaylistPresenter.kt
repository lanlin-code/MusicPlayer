package com.example.music.playlist

import com.example.music.ResponseCallback
import com.example.music.entity.UserPlaylist
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserPlayListJson

class PlaylistPresenter : RequestCallBack<UserPlayListJson> {
    private val tag = "PlaylistPresenter"
    var listener: ResponseCallback<MutableList<UserPlaylist>>? = null

    override fun callback(data: UserPlayListJson) {
        val l = data.playlist
        val r = mutableListOf<UserPlaylist>()
        l?.let {
            for (list in l) {
                val ul = UserPlaylist.toPlaylist(list)
                if (UserPlaylist.isDataError(ul)) {
                    listener?.onError("Something was wrong, fail to obtain user's playlists")
                } else {
                    r.add(ul)
                }
            }
            listener?.onSuccess(r)
        }
    }

    override fun error(errorMsg: String) {
        LogUtil.debug(tag, errorMsg)
        listener?.onError("歌单加载失败")
    }

}