package com.example.music.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.util.LogUtil

class OuterAdapter(var data: MutableMap<String, MutableList<Song>>? = null, var listener: SongsListener? = null,
                   var title: MutableList<String>? = null): RecyclerView.Adapter<OuterAdapter.OuterHolder>() {

    private val innerListener = object : InnerListener {
        override fun callback(listPosition: Int, songPosition: Int) {
            if (listPosition < 0 ) return
            val songs = getSongs()
            if (songs.isEmpty()) return
            listener?.transmitData(songs)
            val position = getClickPosition(listPosition, songPosition)
            LogUtil.debug("OuterAdapter", "position = $position")
            listener?.playFrom(position)

        }

        override fun getSongsListener(): SongsListener? {
            return listener
        }

    }

    class OuterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.songs_inner_title)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.songs_inner_list)
        init {
            recyclerView.layoutManager = LinearLayoutManager(itemView.context)
        }
    }


    interface InnerListener {
        fun callback(listPosition: Int, songPosition: Int)
        fun getSongsListener(): SongsListener?
    }

    private fun getClickPosition(listPosition: Int, songPosition: Int): Int {
        var position: Int = songPosition
        for (i in 0 until listPosition) {
            val t = title?.get(i)
            t?.let {
                val s = data?.get(t)
                s?.let { position += s.size }
            }
        }
        return position
    }

    private fun getSongs(): MutableList<Song> {
        val count = title?.size
        val songs = mutableListOf<Song>()
        count?.let {
            for (i in 0 until it) {
                val t = title?.get(i)
                t?.let {
                    val s = data?.get(t)
                    s?.let { it1 -> songs.addAll(it1) }
                }
            }
        }
        return songs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linearlayout_songs_outer_item, parent, false)
        return OuterHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.keys?.size ?: 0
    }

    override fun onBindViewHolder(holder: OuterHolder, position: Int) {
        holder.titleText.text = title?.get(position) ?: "#"
        title?.get(position)?.let {
            data?.get(it)?.let {
                    it1 -> holder.recyclerView.adapter = InnerAdapter(it1, position, innerListener)
            }
        }

    }
}