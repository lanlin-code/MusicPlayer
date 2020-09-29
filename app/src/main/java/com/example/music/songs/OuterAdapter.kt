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

class OuterAdapter(var data: MutableMap<String, MutableList<Song>>? = null, var listener: SongsListener? = null,
                   var title: MutableList<String>? = null): RecyclerView.Adapter<OuterAdapter.OuterHolder>() {

    class OuterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.songs_inner_title)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.songs_inner_list)
        init {
            recyclerView.layoutManager = LinearLayoutManager(itemView.context)
        }
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
                    it1 -> holder.recyclerView.adapter = InnerAdapter(it1)
            }
        }

    }
}