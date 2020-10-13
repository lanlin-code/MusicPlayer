package com.example.music.search

import androidx.recyclerview.widget.RecyclerView
import com.example.music.FragmentChangeListener
import com.example.music.ShowDataListener
import com.example.music.entity.HotWord

class HotWordShow : ShowDataListener<MutableList<HotWord>> {
    var recyclerView: RecyclerView? = null
    override fun show(data: MutableList<HotWord>) {
        val a = recyclerView?.adapter
        if (a is HotWordAdapter) {
            a.hotWordList.addAll(data)
            a.notifyDataSetChanged()
        }
    }
}