package com.example.music.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class IndexView: View {
    private var itemWidth: Int = 0
    private var itemHeight: Int = 0
    private val paint: Paint = Paint()
    private val rect: Rect = Rect()
    val words: List<String> = listOf("A", "B", "C", "D", "E", "F",
        "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z")
    private var touchIndex: Int = -1
    var onIndexChangeListener: OnIndexChangeListener? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, def: Int): super(context, attrs, def)



    init {
        paint.isAntiAlias = true
        paint.textSize = 40f
        paint.color = Color.BLACK
        paint.typeface = Typeface.DEFAULT_BOLD
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthModel: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightModel: Int = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val height: Int
        var width = 0
        if (widthModel == MeasureSpec.EXACTLY) {
            width = widthSize
        }
        height = if (heightModel == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            (paddingTop + paddingBottom + words.size*paint.textSize).toInt()
        }
        itemWidth = width
        itemHeight = height/words.size
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        for (i in words.indices) {
            if (touchIndex == i) {
                paint.color = Color.BLACK
            } else {
                paint.color = Color.GRAY
            }
            val s = words[i]
            paint.getTextBounds(s, 0, s.length, rect)
            val wordWidth = rect.width()
            val wordHeight = rect.height()
            val x: Float = (itemWidth - wordWidth)/2.0f
            val y: Float = wordHeight/2.0f + itemHeight/2.0f + i*itemHeight
            canvas.drawText(s, x, y, paint)

            val lineHeight = itemHeight*(i + 1).toFloat()
            paint.color = Color.BLACK
            canvas.drawLine(0f, lineHeight, itemWidth.toFloat(), lineHeight, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val index = (event.y/itemHeight).toInt()
                if (index in words.indices) {
                    touchIndex = (event.y/itemHeight).toInt()
                    onIndexChangeListener?.onChange(words[touchIndex])
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val index = (event.y/itemHeight).toInt()
                if (touchIndex != index && index in words.indices) {
                    touchIndex = index
                    onIndexChangeListener?.onChange(words[touchIndex])
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                touchIndex = -1
                invalidate()
            }
        }
        return true
    }

    interface OnIndexChangeListener {
        fun onChange(string: String)
    }
}