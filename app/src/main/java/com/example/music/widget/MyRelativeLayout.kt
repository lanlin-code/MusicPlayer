package com.example.music.widget

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.example.music.util.LogUtil
import kotlin.math.abs
import kotlin.math.sqrt

class MyRelativeLayout : RelativeLayout {

    private var lastPoint: PointF = PointF()
    private val condition = 1.0

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when(ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastPoint.x = it.x
                    lastPoint.y = it.y
                }
                MotionEvent.ACTION_UP -> {
                    val distance = (it.x - lastPoint.x)*(it.x - lastPoint.x) + (it.y - lastPoint.y)*(it.y - lastPoint.y)
                    if (sqrt(distance.toDouble()) <= condition) {
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}