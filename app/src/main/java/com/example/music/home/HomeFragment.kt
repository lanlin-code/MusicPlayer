package com.example.music.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.music.FragmentChangeListener
import com.example.music.MainActivity
import com.example.music.R
import com.example.music.ResponseCallback
import com.example.music.banner.BannerAdapter
import com.example.music.banner.BannerModel
import com.example.music.banner.BannerPresenter
import com.example.music.entity.Banners
import com.example.music.login.LoginFragment
import com.example.music.showUserStatus.ShowUserStatusFragment
import com.example.music.util.LogUtil
import com.example.music.util.ThreadAdjust
import com.example.music.widget.BannerView
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*

class HomeFragment: Fragment(), MainActivity.OnLoginSuccessListener, ResponseCallback<Banners> {
    private var listener: FragmentChangeListener? = null
    private lateinit var avatar: ImageView
    private var loginSuccess: Boolean = false
    private lateinit var timer: Timer
    private lateinit var bannerView: BannerView
    private lateinit var bannerDots: LinearLayout
    private var dotsList: MutableList<ImageView>? = null

    private val TAG = "HomeFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        }
        timer = Timer()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatar = view.findViewById(R.id.avatar_iv)
        avatar.setOnClickListener {
            if (!loginSuccess) {
                listener?.onFragmentChange(LoginFragment())
            } else {
                listener?.onFragmentChange(ShowUserStatusFragment())
            }
        }
        bannerView = view.findViewById(R.id.banner)
        bannerDots = view.findViewById(R.id.banner_dots)
        initBanner()

    }

    private fun initBanner() {
        bannerView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                LogUtil.debug(TAG, "position = $position")
                dotsList?.let {
                    val c = bannerView.currentPosition % it.size
                    dotsList?.get(c)?.let { imageView -> context?.let { Picasso.with(context)
                        .load(R.drawable.not_selected).into(imageView) } }

                }
//                val iv = dotsList?.get(bannerView.currentPosition)
//                iv?.let { context?.let { Picasso.with(context).load(R.drawable.not_selected).into(iv) } }
                bannerView.currentPosition = position
                dotsList?.let {
                    val c = bannerView.currentPosition % it.size
                    dotsList?.get(c)?.let { imageView -> context?.let { Picasso.with(context)
                        .load(R.drawable.selected).into(imageView) } }

                }
//                val v = dotsList?.get(position)
//                v?.let { context?.let { Picasso.with(context).load(R.drawable.selected).into(iv) } }
            }
        })

        val bannerPresenter = BannerPresenter()
        bannerPresenter.listener = this
        val model = BannerModel()
        val typeAndroid = 1
        model.loadBanners(typeAndroid, bannerPresenter)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        timer.cancel()
    }

    override fun onSuccess(avatarUrl: String, context: Context) {
        loginSuccess = true
        Picasso.with(context).load(avatarUrl).into(avatar)
    }

    override fun onSuccess(data: Banners) {
        val l = data.urlList
        dotsList = mutableListOf()
        LogUtil.debug(TAG, l.toString())
        context?.let {
            val width = 10
            val height = 10
            for (i in 0 until l.size) {
                val iv = ImageView(it)
                val params = LinearLayout.LayoutParams(width, height)
                if (i == 0) {
                    Picasso.with(it.applicationContext).load(R.drawable.selected).into(iv)
                } else {
                    params.leftMargin = 10
                    Picasso.with(it.applicationContext).load(R.drawable.not_selected).into(iv)
                }
                bannerDots.addView(iv, params)
                dotsList?.add(iv)

//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
//                if (i > 0) {
//                    layoutParams.leftMargin = 10;
//                }
//                dotContainer.addView(dot);
            }
        }
        bannerView.adapter = BannerAdapter(l)
//        ThreadAdjust.postDelay(Runnable {
//            bannerView.setCurrentItem(bannerView.currentPosition + 2, true)
//            bannerView.loop = true
//        }, bannerView.delay)
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (bannerView.loop) {
                    ThreadAdjust.post(Runnable {
                        bannerView.setCurrentItem(bannerView.currentPosition + 1, true)
                    })
                }
            }
        }, 5000L, bannerView.period)
    }

    override fun onError(message: String) {
        LogUtil.debug(TAG, message)
        val adapter = BannerAdapter(mutableListOf())
        bannerView.adapter = adapter
    }
}