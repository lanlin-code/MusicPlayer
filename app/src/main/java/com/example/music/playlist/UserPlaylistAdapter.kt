package com.example.music.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.FragmentChangeListener
import com.example.music.R
import com.example.music.entity.UserPlaylist
import com.example.music.songs.SongsFragment
import com.squareup.picasso.Picasso

class UserPlaylistAdapter(private val playlists: MutableList<UserPlaylist>) : RecyclerView.Adapter<UserPlaylistAdapter.PlaylistHolder>() {

    var listener: FragmentChangeListener? = null

    class PlaylistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv: ImageView = itemView.findViewById(R.id.home_playlist_iv)
        val name: TextView = itemView.findViewById(R.id.home_playlist_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linearlayout_playlist_item, parent, false)
        return PlaylistHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        val d = playlists[position]
        holder.itemView.setOnClickListener { listener?.onFragmentChange(SongsFragment(playlists[position])) }
        Picasso.with(holder.itemView.context).load(d.imgUrl).into(holder.iv)
        holder.name.text = d.name
    }
}