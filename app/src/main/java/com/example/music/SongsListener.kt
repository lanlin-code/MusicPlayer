package com.example.music

import android.view.Window
import com.example.music.entity.Song

interface SongsListener {
    fun onFail(msg: String)
    fun onObtainWindow(): Window
    fun transmitData(songs: MutableList<Song>)
    fun playFrom(position: Int)
    fun clearData()
    fun seekTo(position: Int)
    fun addToNext(song: Song)
    fun addAndPlay(song: Song)
    fun obtainData(): MutableList<Song>?
    fun transferMode(mode: Int)
    fun mode(): Int?
    fun obtainCurrentPlaying(): Song?
    fun removeItem(song: Song)
}