package com.example.music.songs

import com.example.music.ResponseCallback

class SongsIdResponse(private val songPresenter: SongsPresenter.SongDetailPresenter) : ResponseCallback<String> {
    override fun onSuccess(data: String) {
        val model = SongsModel()
        model.getSongs(data, songPresenter)
    }

    override fun onError(message: String) {

    }
}