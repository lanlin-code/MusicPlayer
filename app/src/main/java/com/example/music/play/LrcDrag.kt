package com.example.music.play

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.music.util.LogUtil
import com.example.music.widget.LrcView

class LrcDrag(var touchPlay: ImageButton? = null,
              var textView: TextView? = null,
              var lrcView: LrcView? = null): LrcView.DragListener {
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val t: Long = msg.obj as Long
            if (t == sendTime) {
                LogUtil.debug("drag", "handle")
                touchPlay?.visibility = View.GONE
                textView?.visibility = View.GONE
                lrcView?.setMode(LrcView.MODEL_NORMAL)
            }
        }
    }
    private val delayTime = 2000L
    private var sendTime: Long = 0L
    override fun onMove(time: String) {
        touchPlay?.visibility = View.VISIBLE
        textView?.text = time
        textView?.visibility = View.VISIBLE
    }


    override fun onRelease() {
        LogUtil.debug("drag", "onRelease")
        val message = handler.obtainMessage()
        sendTime = System.currentTimeMillis()
        message.obj = sendTime
        handler.sendMessageDelayed(message, delayTime)
    }

    fun clear() {
        handler.removeCallbacksAndMessages(null)
        touchPlay = null
        textView = null
        lrcView = null
    }
}