package com.example.music.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition

class MyLayout: LinearLayout {
    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle)

    val target: ViewTarget<MyLayout, Drawable> = object : ViewTarget<MyLayout, Drawable>(this) {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            getView().background = resource
        }

    }

}