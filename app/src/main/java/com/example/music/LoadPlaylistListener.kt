package com.example.music

import com.example.music.entity.UserPlaylist

interface LoadPlaylistListener {
    fun onLoadPlaylistSuccess(playlists: MutableList<UserPlaylist>)
    fun onLoadPlaylistFail(message: String)
}