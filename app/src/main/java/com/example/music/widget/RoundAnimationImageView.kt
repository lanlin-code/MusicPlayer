package com.example.music.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView

class RoundAnimationImageView : androidx.appcompat.widget.AppCompatImageView {

    private val paint: Paint = Paint()
    private var init = false
    var rotation = false
    private val animation: ObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)

    init {
        init = true
        if (rotation) animation.start()
    }



    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        paint.isAntiAlias = true
        animation.duration = 30000L
        animation.repeatCount = ValueAnimator.INFINITE
        animation.repeatMode = ValueAnimator.RESTART
        animation.interpolator = LinearInterpolator()
    }

   private fun startAnimation() {
       if (!init) {
           init = true
           return
       }
       if (rotation) {
           if (animation.isStarted) {
               animation.pause()
           }
           animation.start()
       }

   }

    fun pauseAnimation() {
        if (animation.isStarted) {
            animation.pause()
        }
    }

    fun resumeAnimation() {
        if (animation.isPaused) {
            animation.resume()
        }
    }



    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        startAnimation()

    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        startAnimation()

    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        startAnimation()

    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        startAnimation()

    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = width.coerceAtMost(height)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawable is BitmapDrawable && canvas != null) {
            drawRoundImage(canvas, (drawable as BitmapDrawable).bitmap)
        } else {
            super.onDraw(canvas)
        }
    }

    private fun drawRoundImage(canvas: Canvas, bitmap: Bitmap) {
        if (width <= 0 || height <= 0) return
        val b = ThumbnailUtils.extractThumbnail(bitmap, width, height)
        val bitmapShader = BitmapShader(b, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = bitmapShader
        canvas.drawCircle(width/2.0f, height/2.0f, (width / 2.0f).coerceAtMost(height / 2.0f), paint)
    }


}