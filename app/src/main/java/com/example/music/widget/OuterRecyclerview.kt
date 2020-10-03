package com.example.music.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OuterRecyclerview: RecyclerView, IndexView.OnIndexChangeListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle)

    var title: List<String>? = null

    override fun onChange(string: String) {
        val position = title?.indexOf(string) ?: -1
        if (position != -1) {
            if (layoutManager is LinearLayoutManager) {
                val l: LinearLayoutManager = layoutManager as LinearLayoutManager
                l.scrollToPositionWithOffset(position, 0)
            }
        }

    }


}