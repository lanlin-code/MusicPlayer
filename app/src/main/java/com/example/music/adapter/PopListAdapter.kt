package com.example.music.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.R
import com.example.music.SongsListener
import com.example.music.entity.Song


class PopListAdapter(var songsListener: SongsListener? = null, val song: Song) : RecyclerView.Adapter<PopListAdapter.PopListHolder>() {

    private val imageSource = arrayOf(R.drawable.add_to_next)
    private val textSource = arrayOf(R.string.next_to_play)
    var listener: PopListDismissListener? = null

    class PopListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.button_list_iv)
        val textView = itemView.findViewById<TextView>(R.id.button_list_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linearlayout_pop_button_list_item, parent, false)
        return PopListHolder(view)
    }

    override fun onBindViewHolder(holder: PopListHolder, position: Int) {
        when(position) {
            0 -> holder.itemView.setOnClickListener {
                songsListener?.addToNext(song)
                listener?.onDismiss()
            }
        }
        holder.imageView.setImageResource(imageSource[position])
        holder.textView.setText(textSource[position])
    }

    override fun getItemCount(): Int {
        return imageSource.size
    }

    interface PopListDismissListener {
        fun onDismiss()
    }

}