package com.example.music

import android.widget.ImageView
import android.widget.TextView

interface LoadStatusListener {
    fun onLoadStatus(imageView: ImageView, textView: TextView)
}