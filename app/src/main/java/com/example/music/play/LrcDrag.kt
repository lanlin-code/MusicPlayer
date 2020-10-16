package com.example.music.play

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.music.util.LogUtil
import com.example.music.widget.LrcView

class LrcDrag(var touchPlay: ImageButton? = null, // 拖动时出现的播放按钮
              var textView: TextView? = null, // 显示时间
              var lrcView: LrcView? = null // 显示歌词的view
): LrcView.DragListener {

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
    // 歌词拖动时按钮和显示时间的textView可见
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