package com.example.music.search.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.SongsListener
import com.example.music.entity.Song


class ResultAdapter(var listener: SongsListener? = null,
                    val data: MutableList<Song> = mutableListOf()) : RecyclerView.Adapter<ResultAdapter.ResultHolder>() {

    class ResultHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.result_name)
        val artist = itemView.findViewById<TextView>(R.id.result_artist)
        val more = itemView.findViewById<ImageButton>(R.id.result_more)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linearlayout_songs_inner_item, parent, false)
        return ResultHolder(view)
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.artist.text = item.appendArtists()
        holder.itemView.setOnClickListener {
            listener?.addAndPlay(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}