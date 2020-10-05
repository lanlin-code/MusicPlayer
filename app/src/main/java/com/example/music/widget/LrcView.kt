package com.example.music.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.music.entity.LrcRow
import com.example.music.util.LogUtil
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class LrcView : View {

    private var highLightRow: Int = 0 // 当前被选中的歌词位置
    private var dragRow: Int = highLightRow
    private var model: Int = MODEL_NORMAL // 状态
    private var lrcRow: MutableList<LrcRow>? = null // 全部歌词
    lateinit var dragListener: DragListener // 监听拖动
    private lateinit var paint: Paint
    private var lastY: Float = 0.0f // 上一次手指落在屏幕的纵坐标
    private var textHeight = 0.0f
    private val path = Path()
//    var drawLine = false


    constructor(context: Context) : super(context, null) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initPaint()
    }

    fun updateRow() {
        highLightRow = dragRow
        model = MODEL_NORMAL
        invalidate()
    }

    companion object {
        const val MODEL_NORMAL: Int = 0 // 正常状态
        const val MODEL_DRAG: Int = 1 // 拖动状态，moveToTime方法失效
        const val MODEL_RELEASE: Int = 2 // 释放状态,moveToTime方法失效，同时隐藏一些组件
        const val LRC_SIZE: Float = 50.0f // 歌词字体大小
    }

    interface DragListener {
        fun onMove(time: String)
        fun onRelease()
    }

    fun setMode(model: Int) {
        this.model = model
        dragRow = highLightRow
        invalidate()
    }

    private fun initPaint() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.textSize = LRC_SIZE
        paint.textAlign = Paint.Align.CENTER
        textHeight = abs(paint.ascent() + paint.descent())
    }

    fun setLrc(lrcRow: MutableList<LrcRow>?) {
        highLightRow = 0
        dragRow = 0
        this.lrcRow = lrcRow
        invalidate()
    }



    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        super.onDraw(canvas)
        val normalColor = Color.GRAY

        val count = lrcRow?.size
        // 没有歌词时显示
        if (lrcRow == null || count == null || count <= 0) {
            drawNoLrc(canvas)
            return
        }
        val currentLrc: String? = lrcRow?.get(dragRow)?.content
        if (currentLrc == null) {
            drawNoLrc(canvas)
            return
        }
        drawLine(canvas)
        // 在布局中心用高亮颜色画出当前被选中的歌词
//        val highLightColor = Color.WHITE
        val centerX = width/2.0f
        val centerY = height/2.0f + textHeight / 2.0f
//        paint.color = Color.GRAY
        if (highLightRow == dragRow) {
            paint.color = Color.WHITE
        } else {
            paint.color = Color.DKGRAY
        }

        canvas.drawText(currentLrc, centerX, centerY, paint)
        val paddingY = textHeight*3 // 每行歌词的间距
        var lastY = centerY - textHeight/2.0f - paddingY
        paint.color = normalColor
        // 画出选中歌词上面的歌词
        for (i in dragRow - 1 downTo 0) {
            if (i == highLightRow) {
                paint.color = Color.WHITE
            } else {
                paint.color = Color.GRAY
            }
            val s = lrcRow?.get(i)?.content
            if (s != null) {
                canvas.drawText(s, centerX, lastY, paint)
                lastY -= (textHeight/2.0f + paddingY)
            }
        }

        // 画出选中歌词下面的歌词
        val size = lrcRow?.size
        var nextY = centerY + textHeight/2.0f + paddingY
        if (size != null) {
            for (i in dragRow + 1 until size) {
                if (i == highLightRow) {
                    paint.color = Color.WHITE
                } else {
                    paint.color = Color.GRAY
                }
                val s = lrcRow?.get(i)?.content
                if (s != null) {
                    canvas.drawText(s, centerX, nextY, paint)
                    nextY += (textHeight/2.0f + paddingY)
                }
            }
        }


    }

    private fun drawNoLrc(canvas: Canvas) {
        paint.color = Color.GRAY
        val s = "暂无歌词"
        canvas.drawText(s, width / 2.0f, height / 2.0f + textHeight / 2.0f, paint)
    }

    private fun drawLine(canvas: Canvas) {
//        if (!drawLine) return
        if(model == MODEL_NORMAL) return
        path.moveTo(0.0f, height / 2.0f)
        path.lineTo(width.toFloat(), height / 2.0f)
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        canvas.drawPath(path, paint)
        paint.style = Paint.Style.FILL

    }


    fun getCurrentTime(): Long {
        val l = lrcRow?.get(dragRow)
        return l?.time ?: 0L
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
//                drawLine = true
                onLrcMove(event)
                dragListener.onMove(getTime())
            }
            MotionEvent.ACTION_UP -> {
                if (model == MODEL_DRAG) {
                    model = MODEL_RELEASE
                    dragListener.onRelease()
                }


            }
        }
        return true
    }

    private fun getTime(): String {
        val l = lrcRow?.get(dragRow)
        return l?.timeText ?: "00:00"
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
        val offsetRow = (offsetY / textHeight).toInt()
        if (offsetRow > 0) {
            lastY = currentY
            dragRow += offsetRow
            dragRow = max(0, dragRow)
            dragRow = min(dragRow, size)
            invalidate()
        }
    }

    fun moveToTime(time: Long) {
        val size = lrcRow?.size
        if (lrcRow == null || size == 0 || size == null || model != MODEL_NORMAL) {
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
                   dragRow = highLightRow
                   invalidate()
                   break
               }
            }
        }
    }



}