package com.example.music.songs

import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.adapter.PopListAdapter
import com.example.music.entity.Song
import com.example.music.widget.SongPopupWindow
import com.squareup.picasso.Picasso
import java.lang.StringBuilder

class InnerAdapter(var data: MutableList<Song>? = null,
                   var myPosition: Int = -1,
                   var innerListener: OuterAdapter.InnerListener? = null): RecyclerView.Adapter<InnerAdapter.InnerHolder>() {

    class InnerHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.song_inner_iv)
        val artist: TextView = itemView.findViewById(R.id.song_inner_artist)
        val songName: TextView = itemView.findViewById(R.id.song_inner_name)
        val button: ImageButton = itemView.findViewById(R.id.song_inner_bt)
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
            holder.button.setOnClickListener {
                val context = holder.itemView.context
                val contentView = LayoutInflater.from(context).inflate(R.layout.cardview_list_window, null)
                val imageView = contentView.findViewById<ImageView>(R.id.window_list_image)
                Picasso.with(context).load(item.albumPic).placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder).into(imageView)
                val name = contentView.findViewById<TextView>(R.id.window_list_name)
                name.text = item.name
                val artist = contentView.findViewById<TextView>(R.id.window_list_artist)
                artist.text = item.appendArtists()
                val recyclerView = contentView.findViewById<RecyclerView>(R.id.window_list)
                val popupWindow = SongPopupWindow(context, contentView, false)
                recyclerView.layoutManager = LinearLayoutManager(context)
                val adapter = PopListAdapter(innerListener?.getSongsListener(), item)
                val window = innerListener?.getSongsListener()?.onObtainWindow()
                window?.let {
                    w -> w.attributes.alpha = 0.5f
                    w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                }
                popupWindow.setOnDismissListener {
                    window?.let {
                        w -> w.attributes.alpha = 1.0f
                        w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    }
                }
                adapter.listener = object : PopListAdapter.PopListDismissListener {
                    override fun onDismiss() {
                        adapter.songsListener = null
                        popupWindow.dismiss()
                    }

                }
                recyclerView.adapter = adapter
                popupWindow.showAtLocation(holder.itemView, Gravity.BOTTOM, 0, 0)

            }
        }
    }



}