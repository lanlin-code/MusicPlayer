package com.example.music.widget

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
    var start = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        paint.isAntiAlias = true
        if (start) {
            startAnimation()
        }
    }

    private fun startAnimation() {
        clearAnimation()
        val duration = 30000L
        val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = duration
        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.repeatMode = Animation.RESTART
        startAnimation(rotateAnimation)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (start) {
            startAnimation()
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (start) {
            startAnimation()
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (start) {
            startAnimation()
        }
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        if (start) {
            startAnimation()
        }
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
        val b = ThumbnailUtils.extractThumbnail(bitmap, width, height)
        val bitmapShader = BitmapShader(b, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = bitmapShader
        canvas.drawCircle(width/2.0f, height/2.0f, (width / 2.0f).coerceAtMost(height / 2.0f), paint)
    }


}