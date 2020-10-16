package com.example.music.playlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.music.FragmentChangeListener
import com.example.music.ShowDataListener
import com.example.music.entity.UserPlaylist

class PlaylistShow(val view: RecyclerView, var listener: FragmentChangeListener?): ShowDataListener<MutableList<UserPlaylist>> {

    // 更新视图
    override fun show(data: MutableList<UserPlaylist>) {
        view.visibility = View.VISIBLE
        val adapter = UserPlaylistAdapter(data)
        adapter.listener = listener
        view.adapter = adapter
        view.adapter?.notifyDataSetChanged()
    }

}