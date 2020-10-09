package com.example.music.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow

class SongPopupWindow(context: Context, contentView: View, halfHeight: Boolean = true) : PopupWindow(context) {

    init {
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels
        height = if (halfHeight) {
            metrics.heightPixels / 2
        } else {
            WindowManager.LayoutParams.WRAP_CONTENT
        }
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isOutsideTouchable = true
        isFocusable = true
        setContentView(contentView)

    }

}