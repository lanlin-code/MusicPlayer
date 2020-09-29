package com.example.music.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.music.*
import com.example.music.banner.BannerAdapter
import com.example.music.banner.BannerModel
import com.example.music.banner.BannerPresenter
import com.example.music.entity.Banners
import com.example.music.login.LoginFragment
import com.example.music.playlist.PlaylistCallback
import com.example.music.playlist.PlaylistModel
import com.example.music.playlist.PlaylistPresenter
import com.example.music.playlist.PlaylistShow
import com.example.music.showUserStatus.ShowUserStatusFragment
import com.example.music.util.LogUtil
import com.example.music.util.ThreadAdjust
import com.example.music.widget.BannerView
import com.squareup.picasso.Picasso
import java.util.*

class HomeFragment: Fragment(), MainActivity.OnLoginSuccessListener, ResponseCallback<Banners> {
    private var listener: FragmentChangeListener? = null
    private lateinit var avatar: ImageView
    private var loginSuccess: Boolean = false
    private lateinit var timer: Timer
    private lateinit var bannerView: BannerView
    private lateinit var bannerDots: LinearLayout
    private var dotsList: MutableList<ImageView>? = null
    private lateinit var recyclerView: RecyclerView
    private val TAG = "HomeFragment"
    private var dataObtainListener: DataObtainListener? = null
    private val playlistCallback = PlaylistCallback()
    private val playlistModel = PlaylistModel()
    private val playlistPresenter: PlaylistPresenter = PlaylistPresenter()
    private var playShow: PlaylistShow? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        }
        if (context is LoadPlaylistListener) {
            playlistCallback.listener = context
        }
        if (context is DataObtainListener) {
            dataObtainListener = context
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
        recyclerView = view.findViewById(R.id.all_playlists)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        initBanner()
        playlistPresenter.listener = playlistCallback
        playShow = PlaylistShow(recyclerView, listener)
        playlistCallback.sListener = playShow

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

                dotsList?.let {
                    val c = bannerView.currentPosition % it.size
                    dotsList?.get(c)?.let { it1 -> Picasso.with(it1.context).load(R.drawable.not_selected).into(it1)}

                }
                bannerView.currentPosition = position
                dotsList?.let {
                    val c = bannerView.currentPosition % it.size
                    dotsList?.get(c)?.let { imageView -> context?.let { Picasso.with(context)
                        .load(R.drawable.selected).into(imageView) } }

                }

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
        playShow?.listener = null
        playShow = null
        timer.cancel()
    }

    override fun onSuccess(avatarUrl: String, context: Context) {
        loginSuccess = true
        Picasso.with(context).load(avatarUrl).into(avatar)
        val uid = dataObtainListener?.obtainUserId()
        uid?.let {
            playlistModel.userPlaylists(uid, playlistPresenter)
        }
    }

    override fun onSuccess(data: Banners) {
        val l = data.urlList
        dotsList = mutableListOf()
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

            }
        }
        bannerView.adapter = BannerAdapter(l)
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