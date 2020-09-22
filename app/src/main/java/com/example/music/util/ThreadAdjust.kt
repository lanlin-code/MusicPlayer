package com.example.music.util

import android.os.Handler
import android.os.Looper

object ThreadAdjust {
    private val handler = Handler(Looper.getMainLooper())

    fun post(r: Runnable) {
        handler.post(r)
    }

    fun postDelay(r: Runnable, delay: Long) {
        handler.postDelayed(r, delay)
    }
}