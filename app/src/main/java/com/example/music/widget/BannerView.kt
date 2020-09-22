package com.example.music.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class BannerView: ViewPager {

    var loop: Boolean = true
    val period: Long = 5000
    val delay: Long = period + 100
    var currentPosition = 0

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> loop = false
            MotionEvent.ACTION_UP -> loop = true
        }
        return super.onTouchEvent(ev)
    }
}