package com.example.music.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.music.entity.LrcRow
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class LrcView : View {

    var highLightRow: Int = 0 // 当前被选中的歌词位置
    var model: Int = MODEL_NORMAL // 状态
    var lrcRow: List<LrcRow>? = null // 全部歌词
    lateinit var dragListener: DragListener // 监听拖动
    lateinit var paint: Paint
    var lastY: Float = 0.0f // 上一次手指落在屏幕的纵坐标


    constructor(context: Context) : super(context, null) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    companion object {
        const val MODEL_NORMAL: Int = 0 // 正常状态
        const val MODEL_DRAG: Int = 1 // 拖动状态，moveToTime方法失效
        const val MODEL_RELEASE: Int = 2 // 释放状态,moveToTime方法失效，同时隐藏一些组件
        const val LRC_SIZE: Float = 50.0f // 歌词字体大小
    }

    interface DragListener {
        fun onMove()
        fun onRelease()
    }

    private fun initPaint() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.textSize = LRC_SIZE
        paint.textAlign = Paint.Align.CENTER
    }

    fun setLrc(lrcRow: List<LrcRow>? ) {
        this.lrcRow = lrcRow
        invalidate()
    }



    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        super.onDraw(canvas)
        val normalColor = Color.GRAY
        val currentLrc: String? = lrcRow?.get(highLightRow)?.content
        // 没有歌词时显示
        if (lrcRow == null || currentLrc == null) {
            paint.color = normalColor
            val s = "暂无歌词"
            canvas.drawText(s, width/2.0f, height/2.0f - LRC_SIZE, paint)
            return
        }
        // 在布局中心用高亮颜色画出当前被选中的歌词
        val highLightColor = Color.WHITE
        val centerX = width/2.0f
        val centerY = height/2.0f - LRC_SIZE/2.0f
        paint.color = highLightColor
        canvas.drawText(currentLrc, centerX, centerY, paint)

        val paddingY = 40 // 每行歌词的间距
        var lastY = centerY - LRC_SIZE/2.0f - paddingY
        paint.color = normalColor
        // 画出选中歌词上面的歌词
        for (i in highLightRow - 1 downTo 0) {
            val s = lrcRow?.get(i)?.content
            if (s != null) {
                canvas.drawText(s, centerX, lastY, paint)
                lastY -= (LRC_SIZE/2.0f + paddingY)
            }
        }

        // 画出选中歌词下面的歌词
        val size = lrcRow?.size
        var nextY = centerY + LRC_SIZE/2.0f + paddingY
        if (size != null) {
            for (i in highLightRow + 1 until size) {
                val s = lrcRow?.get(i)?.content
                if (s != null) {
                    canvas.drawText(s, centerX, nextY, paint)
                    nextY += (LRC_SIZE/2.0f + paddingY)
                }
            }
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val size = lrcRow?.size
        if (event == null || lrcRow == null || (size == null || size == 0)) {
            return super.onTouchEvent(event)
        }
        when(event.action) {
            MotionEvent.ACTION_DOWN -> lastY = event.y
            MotionEvent.ACTION_MOVE -> {
                model = MODEL_DRAG
                onLrcMove(event)
                dragListener.onMove()
            }
            MotionEvent.ACTION_UP -> {
                if (model == MODEL_DRAG) {
                    dragListener.onRelease()
                }
                model = MODEL_RELEASE
            }
        }
        return true
    }

    /**
     * 计算手指的纵偏移量，根据偏移量来寻找当前被选中的歌词。
     * @param event 拖动事件
     */
    fun onLrcMove(event: MotionEvent) {
        val size = lrcRow?.size
        if (size == null || size == 0) {
            return
        }
        val currentY = event.y
        val offsetY = currentY - lastY
        val minOffset = 10f
        if (abs(offsetY) < minOffset) {
            return
        }
        val offsetRow = (offsetY / LRC_SIZE).toInt()
        if (offsetRow > 0) {
            lastY = currentY
            highLightRow += offsetRow
            highLightRow = max(0, highLightRow)
            highLightRow = min(highLightRow, size)
            invalidate()
        }
    }

    fun moveToTime(time: Long) {
        val size = lrcRow?.size
        if (lrcRow == null || size == 0 || size == null) {
            return
        }
        for (i in 0 until size) {
            val current = lrcRow?.get(i)
            val next = if (i == size - 1) {
                null
            } else {
                lrcRow?.get(i + 1)
            }
            val currentTime = current?.time
            if (currentTime != null) {
               if (time >= currentTime && (next == null || time < next.time)) {
                    highLightRow = i
                   invalidate()
                   break
               }
            }
        }
    }



}