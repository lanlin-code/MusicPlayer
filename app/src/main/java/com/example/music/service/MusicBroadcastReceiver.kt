package com.example.music.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.music.util.LogUtil

/**
 * 通知视图注册的广播
 */

class MusicBroadcastReceiver(var binder: MyBinder? = null) : BroadcastReceiver() {

    companion object {
        const val ACTION_LAST = "com.example.music.LAST"
        const val ACTION_STATE = "com.example.music.STATE"
        const val ACTION_NEXT = "com.example.music.NEXT"

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        LogUtil.debug("MusicBroadcastReceiver", "action = ${intent?.action}")
        when(intent?.action) {
            ACTION_LAST -> binder?.last()
            ACTION_NEXT -> binder?.next()
            ACTION_STATE -> {
                val state = binder?.isPlaying
                state?.let {
                    if (it) {
                        binder?.parse()
                    } else {
                        binder?.restart()
                    }
                }
            }
            else -> {
                LogUtil.debug("Receiver", "other action")
            }
        }
    }
}