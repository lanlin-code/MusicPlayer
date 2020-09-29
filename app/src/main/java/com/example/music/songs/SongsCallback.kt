package com.example.music.songs

import com.example.music.ResponseCallback
import com.example.music.ShowDataListener
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.util.LogUtil

class SongsCallback : ResponseCallback<MutableList<Song>> {
    var l: SongsListener? = null
    var sl: ShowDataListener<MutableList<Song>>? = null
    override fun onSuccess(data: MutableList<Song>) {
        sl?.show(data)
    }

    override fun onError(message: String) {
        l?.onFail(message)
    }
}