package com.example.music.adapter

import android.graphics.Color

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.widget.SongPopupWindow

class ButtonPopAdapter(var data: MutableList<Song> = mutableListOf(),
                       var window: SongPopupWindow? = null,
                       var listener: SongsListener? = null,
                       var count: TextView? = null,
                       var currentPosition: Int = 0) : RecyclerView.Adapter<ButtonPopAdapter.ButtonPopHolder>() {

    private var last: TextView? = null // 记录上一次选中的TextView

    class ButtonPopHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.bt_item_tv) // 显示歌曲名和歌手
        val imageButton = itemView.findViewById<ImageButton>(R.id.bt_item_clear_bt) // 清除该子View的按钮
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonPopHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linearlayout_bt_window_item,
            parent, false)
        return ButtonPopHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonPopHolder, position: Int) {

        val item = data[position]
        val text = "${item.name} - ${item.appendArtists()}"
        // 如果当前视图显示的是播放歌曲，则用红色的字体显示
        if (position == currentPosition) {
            last = holder.textView
            holder.textView.setTextColor(Color.RED)
        } else {
            holder.textView.setTextColor(Color.BLACK)
        }
        holder.textView.text = text

        holder.itemView.setOnClickListener {
            listener?.playFrom(position)
            currentPosition = position
            last?.setTextColor(Color.BLACK)
            holder.textView.setTextColor(Color.RED)
            last = holder.textView
        }
        holder.imageButton.setOnClickListener {
            listener?.removeItem(item)
            // 如果播放列表空，则使整个视图消失
            if (data.size <= 1) {
                data.remove(item)
                last = null
                count = null
                listener = null
                window?.dismiss()
                window = null
            } else {
                data.remove(item)
                val s = "(${data.size})"
                count?.text = s
                // 如果删除的项是播放歌曲重绘视图
                if (position == currentPosition) {
                    notifyDataSetChanged()
                } else {
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }




}