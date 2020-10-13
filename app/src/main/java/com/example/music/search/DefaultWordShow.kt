package com.example.music.search

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.music.ShowDataListener
import com.example.music.entity.DefaultSearchWord
import com.example.music.util.LogUtil

class DefaultWordShow(var editText: EditText? = null,
                      var recyclerView: RecyclerView? = null): ShowDataListener<DefaultSearchWord> {
    override fun show(data: DefaultSearchWord) {
        editText?.hint = data.real
        val adapter = recyclerView?.adapter
        if (adapter is HotWordAdapter) {
            adapter.hint = data.real
        }
    }
}