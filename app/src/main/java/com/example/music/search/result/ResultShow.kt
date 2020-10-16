package com.example.music.search.result

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.ShowDataListener
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.util.LogUtil

class ResultShow(var recyclerView: RecyclerView? = null,
                 val message: SearchMessage,
                 var resultButton: Button? = null, // 加载按钮
                 var textView: TextView? = null, // 占位图
                 var songsListener: SongsListener? = null) : ShowDataListener<MutableList<Song>> {
    override fun show(data: MutableList<Song>) {
        textView?.visibility = View.GONE
        if (message.page >= message.totalPage) {
            resultButton?.text = "再怎么找也找不到了"
            resultButton?.isClickable = false
        } else {
            resultButton?.text = "加载更多"
            resultButton?.isClickable = true
        }
        recyclerView?.let {
            if (it.adapter is ResultAdapter) {
                (it.adapter as ResultAdapter).data.addAll(data)
                (it.adapter as ResultAdapter).notifyDataSetChanged()
            }
        }
        recyclerView?.visibility = View.VISIBLE
        resultButton?.visibility = View.VISIBLE
    }
}