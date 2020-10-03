package com.example.music.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.util.LogUtil
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

class InnerAdapter(var data: MutableList<Song>? = null,
                   var myPosition: Int = -1,
                   var innerListener: OuterAdapter.InnerListener? = null): RecyclerView.Adapter<InnerAdapter.InnerHolder>() {

    class InnerHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.song_inner_iv)
        val artist = itemView.findViewById<TextView>(R.id.song_inner_artist)
        val songName = itemView.findViewById<TextView>(R.id.song_inner_name)
        val button = itemView.findViewById<ImageButton>(R.id.song_inner_bt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linearlayout_songs_inner_item, parent, false)
        return InnerHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemView.setOnClickListener {
            data?.let {
                innerListener?.callback(myPosition, position)
            }
        }
        val item = data?.get(position)
        item?.let {
            val iv = holder.imageView
            Picasso.with(iv.context).load(it.albumPic).placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder).into(iv)
            val builder = StringBuilder()
            for (a in it.artists) {
                builder.append(a.name).append(" ")
            }
            builder.append("-").append(it.albumName)
            holder.artist.text = builder.toString()
            builder.clear()
            holder.songName.text = it.name
        }
    }

}