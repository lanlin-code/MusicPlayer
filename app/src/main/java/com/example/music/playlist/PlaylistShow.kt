package com.example.music.playlist

import androidx.recyclerview.widget.RecyclerView
import com.example.music.FragmentChangeListener
import com.example.music.ShowDataListener
import com.example.music.entity.UserPlaylist

class PlaylistShow(val view: RecyclerView, var listener: FragmentChangeListener?): ShowDataListener<MutableList<UserPlaylist>> {

    override fun show(data: MutableList<UserPlaylist>) {
        val adapter = UserPlaylistAdapter(data)
        adapter.listener = listener
        view.adapter = adapter
        view.adapter?.notifyDataSetChanged()
    }

}