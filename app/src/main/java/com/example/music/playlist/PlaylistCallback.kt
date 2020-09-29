package com.example.music.playlist

import com.example.music.LoadPlaylistListener
import com.example.music.ResponseCallback
import com.example.music.ShowDataListener
import com.example.music.entity.UserPlaylist
import com.example.music.util.LogUtil

class PlaylistCallback: ResponseCallback<MutableList<UserPlaylist>> {
    private val tag = "PlaylistCallback"
    var listener: LoadPlaylistListener? = null
    var sListener: ShowDataListener<MutableList<UserPlaylist>>? = null

    override fun onError(message: String) {
        listener?.onLoadPlaylistFail(message)
    }

    override fun onSuccess(data: MutableList<UserPlaylist>) {
        listener?.onLoadPlaylistSuccess(data)
        sListener?.show(data)
    }
}