package com.example.music.search.result

import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.SongsListener
import com.example.music.adapter.PopListAdapter
import com.example.music.entity.Song
import com.example.music.util.LogUtil
import com.example.music.widget.SongPopupWindow
import com.squareup.picasso.Picasso


class ResultAdapter(var listener: SongsListener? = null,
                    var data: MutableList<Song> = mutableListOf()) : RecyclerView.Adapter<ResultAdapter.ResultHolder>() {

    class ResultHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.result_name)
        val artist = itemView.findViewById<TextView>(R.id.result_artist)
        val more = itemView.findViewById<ImageButton>(R.id.result_more)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linearlayout_result_item, parent, false)
        return ResultHolder(view)
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.artist.text = item.appendArtists()
        holder.itemView.setOnClickListener {
            listener?.addAndPlay(item)
        }
        holder.more.setOnClickListener {
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
            val adapter = PopListAdapter(listener, item)
            val window =listener?.onObtainWindow()
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

    override fun getItemCount(): Int {
        return data.size
    }

}