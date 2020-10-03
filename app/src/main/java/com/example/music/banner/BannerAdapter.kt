package com.example.music.banner

import android.content.ClipData
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.music.util.LogUtil
import com.squareup.picasso.Picasso

class BannerAdapter(private var urlList: MutableList<String>): PagerAdapter() {


    private val TAG = "BannerAdapter"

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = ImageView(container.context)
        if(urlList.size == 0) return item
        val realPosition = position % urlList.size
        Picasso.with(container.context).load(urlList[realPosition]).into(item)
        container.addView(item)
        return item
    }
}