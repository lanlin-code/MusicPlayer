package com.example.music

import com.example.music.entity.Song

interface SongsListener {
    fun onFail(msg: String)
    fun transmitData(songs: MutableList<Song>)
    fun playFrom(position: Int)
    fun clearData()
    fun seekTo(position: Int)
}