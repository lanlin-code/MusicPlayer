package com.example.music.play

import com.example.music.ResponseCallback
import com.example.music.ShowDataListener
import com.example.music.SongsListener
import com.example.music.entity.LrcRow

class SongPlayCallback: ResponseCallback<MutableList<LrcRow>?> {
    var listener: SongsListener? = null
    var sListener: ShowDataListener<MutableList<LrcRow>?>? = null
    override fun onSuccess(data: MutableList<LrcRow>?) {
        sListener?.show(data)
    }

    override fun onError(message: String) {
        listener?.onFail(message)
    }
}