package com.example.music.songs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.music.*
import com.example.music.entity.UserPlaylist
import com.example.music.widget.IndexView
import com.example.music.widget.OuterRecyclerview
import com.squareup.picasso.Picasso

class SongsFragment(private var playlist: UserPlaylist? = null): BaseFragment() {

    private var dataObtainListener: DataObtainListener? = null
    private var songsListener: SongsListener? = null
    private val presenter= SongsPresenter()
    private val model = SongsModel()
    private val songsCallback = SongsCallback()
    private val songShow = SongsShow()
    private var indexView: IndexView? = null
    private var outerView: OuterRecyclerview? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_songs, container, false)
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DataObtainListener) {
            dataObtainListener = context
        }
        if (context is SongsListener) {
            songsListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        presenter.listener = null
        presenter.idListener = null
        songsCallback.l = null
        songsCallback.sl = null
        songShow.listener = null
        songShow.outerRecyclerview = null
        dataObtainListener = null
        songsListener = null
        playlist = null
        outerView?.let {
            if (it.adapter is OuterAdapter) {
                (it.adapter as OuterAdapter).listener = null
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTitleBar(view)
        initPlaylistMessage(view)
        initUserMessage(view)
        val playAll = view.findViewById<LinearLayout>(R.id.songs_play_all)
        songShow.playAll = playAll
        outerView = view.findViewById(R.id.songs_outer_list)
        indexView = view.findViewById(R.id.songs_index)
        outerView?.let {
            it.layoutManager = LinearLayoutManager(view.context)
        }
        indexView?.onIndexChangeListener = outerView
        outerView?.title = indexView?.words
        val idListener = SongsIdResponse(presenter.songPresenter)
        presenter.idListener = idListener
        presenter.listener = songsCallback
        songsCallback.l = songsListener
        songShow.outerRecyclerview = outerView
        songShow.listener = songsListener
        songsCallback.sl = songShow
        playlist?.let { model.getIds(it.id, presenter.idPresenter) }
    }

    private fun initTitleBar(view: View) {
        val back = view.findViewById<ImageButton>(R.id.songs_back)
        back.setOnClickListener { fragmentChangeListener?.onBackHome() }
    }

    // 显示歌单封面图片和歌单名
    private fun initPlaylistMessage(view: View) {
        val listPic = view.findViewById<ImageView>(R.id.songs_list_pic)
        playlist?.imgUrl?.let {
            Picasso.with(view.context).load(it).placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder).into(listPic)
        }
        val listName = view.findViewById<TextView>(R.id.songs_playlist_name)
        playlist?.name?.let { listName.text = it }
    }

    // 显示用户头像和用户名
    private fun initUserMessage(view: View) {
        val avatar = view.findViewById<ImageView>(R.id.songs_user_pic)
        dataObtainListener?.obtainUserAvatar()?.let {
            Picasso.with(view.context).load(it).placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder).into(avatar)
        }

        val username = view.findViewById<TextView>(R.id.songs_user_name)
        dataObtainListener?.obtainUsername()?.let { username.text = it }
    }
}