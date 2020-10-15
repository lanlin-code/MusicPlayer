package com.example.music.play

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.music.*
import com.example.music.entity.PopOnClickListener
import com.example.music.entity.Song
import com.example.music.service.MusicPosition
import com.example.music.util.LogUtil
import com.example.music.widget.LrcView
import com.example.music.widget.MyLayout
import com.example.music.widget.RoundAnimationImageView
import jp.wasabeef.glide.transformations.BlurTransformation
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class SongPlayFragment(var player: IMusicPlayer? = null): BaseFragment() {

    private val TAG = "SongPlayFragment"
    private var songsListener: SongsListener? = null
    private lateinit var layout: MyLayout
    private lateinit var name: TextView
    private lateinit var artist: TextView
    private lateinit var songImg: RoundAnimationImageView
    private lateinit var currentTime: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var duration: TextView
    private lateinit var playState: ImageButton
    private lateinit var playMode: ImageButton
    private lateinit var lrcView: LrcView
    private val presenter = SongPlayPresenter()
    private val model = SongPlayModel()
    private val playCallback = SongPlayCallback()
    private val dragListener = LrcDrag()
    private var drag = false
    private lateinit var clickListener: PopOnClickListener
    private val timerPeriod = 100L
    private var timer = Timer()
    private val timerHandler = Handler(Looper.getMainLooper())
    private val handler = Handler(Looper.getMainLooper())
    private var callback: IMusicCallback? = object : IMusicCallback.Stub() {
        override fun getCurrentSong(imgUrl: String?, name: String?) {

        }

        override fun playCallback(position: Int) {
            handler.post {
                player?.let {
                    if (it.updateLayout(position)) {
                        updateLayout()
                        clickListener.onChange(position)
                    }
                }
            }
        }

        override fun obtainLrc(sid: Long) {
            handler.post { model.lyric(sid, presenter) }

        }

        override fun closeBar() {
            handler.post { fragmentChangeListener?.onBackHome() }

        }

        override fun playStatusChange(playing: Boolean) {
            handler.post { updateButtonState(playing) }
        }


    }
    private val format = SimpleDateFormat.getDateInstance() as SimpleDateFormat
    private var currentMode = MusicPosition.order


    init {
        format.applyPattern("mm:ss")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SongsListener) {
            songsListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back = view.findViewById<ImageButton>(R.id.play_back)
        back.setOnClickListener { fragmentChangeListener?.onBackHome() }
        initLayout(view)
        presenter.responseCallback = playCallback
        playCallback.listener = songsListener
        playCallback.sListener = SongPlayShow(lrcView)
        updateLayout()
        getLrc()



    }

    private fun initLayout(view: View) {
        layout = view.findViewById(R.id.play_layout)
        name = view.findViewById(R.id.play_name)
        artist = view.findViewById(R.id.play_artist)
        songImg = view.findViewById(R.id.play_image)
        songImg.rotation = true
        player?.let {
            if (it.isPlaying) {
                songImg.resumeAnimation()
            } else {
                songImg.pauseAnimation()
            }
        }

//        songImg.start = true
        currentTime = view.findViewById(R.id.play_current_time)
        seekBar = view.findViewById(R.id.play_seek_bar)
        duration = view.findViewById(R.id.play_duration)
        lrcView = view.findViewById(R.id.play_lrc)
        clickListener  = PopOnClickListener(songsListener)
        val centerLayout = view.findViewById<RelativeLayout>(R.id.play_center_layout)
        val touchPlay = view.findViewById<ImageButton>(R.id.play_lrc_play)
        val timeText = view.findViewById<TextView>(R.id.play_lrc_time)
        centerLayout.setOnClickListener {
            if (songImg.visibility == View.VISIBLE) {
//                songImg.clearAnimation()
                songImg.pauseAnimation()
                songImg.visibility = View.GONE
                lrcView.visibility = View.VISIBLE
            } else {
//                songImg.startAnimation()
                songImg.resumeAnimation()
                songImg.visibility = View.VISIBLE
                lrcView.visibility = View.GONE
                touchPlay.visibility = View.GONE
                timeText.visibility = View.GONE

            }
        }
        touchPlay.setOnClickListener {
            val t = lrcView.getCurrentTime()
            touchPlay.visibility = View.GONE
            timeText.visibility = View.GONE
            lrcView.updateRow()
            player?.seekTo(t.toInt())
        }
        dragListener.textView = timeText
        dragListener.touchPlay = touchPlay
        lrcView.dragListener = dragListener
        dragListener.lrcView = lrcView
        initSeekBar()
        playState = view.findViewById(R.id.play_play_state)
        clickListener.playState = playState
        initButtonState()
        player?.let { updateButtonState(it.isPlaying) }
        val last = view.findViewById<ImageButton>(R.id.play_last)
        last.setOnClickListener { player?.last() }
        playMode = view.findViewById(R.id.play_mode)
        initButtonMode()
        val next = view.findViewById<ImageButton>(R.id.play_next)
        next.setOnClickListener { player?.next() }
        val popList = view.findViewById<ImageButton>(R.id.play_list)
        popList.setOnClickListener(clickListener)
        player?.registerCallback(callback)


    }

    private fun initButtonMode() {
        player?.let {
            currentMode = it.mode
            updateButtonMode()
        }

        playMode.setOnClickListener {
            currentMode = (currentMode + 1) % MusicPosition.modeSize
            updateButtonMode()
            player?.mode(currentMode)
        }
    }

    private fun initButtonState() {
        player?.let { updateButtonState(it.isPlaying) }
        playState.setOnClickListener {
            player?.let {
                val p = it.isPrepared
                if (p) {
                    val b = it.isPlaying
                    if (b) {
                        it.parse()
                    } else {
                        it.restart()
                    }
                    updateButtonState(!b)
                }

            }
        }
    }

    private fun updateButtonMode() {
        when(currentMode) {
            MusicPosition.order -> playMode.setImageResource(R.drawable.list_circle)
            MusicPosition.circle -> playMode.setImageResource(R.drawable.only_one_circle)
            MusicPosition.random -> playMode.setImageResource(R.drawable.random)
        }
    }

    private fun updateButtonState(playing: Boolean) {
        if (playing) {
            songImg.resumeAnimation()
            playState.setImageResource(R.drawable.play)
        } else {
            songImg.pauseAnimation()
            playState.setImageResource(R.drawable.parse_48)
        }
    }

    private fun initSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBar?.let {
                    currentTime.text = format.format(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                drag = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                drag = false
                seekBar?.let {
                    songsListener?.seekTo(it.progress)
                    lrcView.moveToTime(it.progress.toLong())
                }
            }
        })

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (!drag) {
                    timerHandler.post {
                        player?.let {
                            val c = it.currentPosition()
                            val s = format.format(c)
                            seekBar.progress = c
                            currentTime.text = s
                            lrcView.moveToTime(c.toLong())
                        }
                    }
                }
            }
        }, 0L, timerPeriod)
    }

    private fun getLrc() {
        val s = player?.currentPlaying()
        s?.let {
            handler.post { model.lyric(it.id, presenter) }
        }
    }

    private fun updateLayout() {
        try {
            val s = player?.currentPlaying()
            s?.let {
                Glide.with(this).load(it.albumPic).apply(RequestOptions.bitmapTransform(
                    BlurTransformation(25, 25)
                )).into(layout.target)
                Glide.with(this).load(it.albumPic).into(songImg)
                name.text = it.name
                artist.text = it.appendArtists()

            }
            player?.let {
                val d = it.duration()
                val ds = format.format(d)
                updateButtonState(it.isPlaying)
                seekBar.max = d
                currentTime.text = format.format(it.currentPosition())
                duration.text = ds
            }
        } catch (e: RemoteException) {

        }

    }

    override fun onDetach() {
        super.onDetach()
        clickListener.playState = null
        clickListener.destroy()
//        clickListener = null
        presenter.responseCallback = null
        playCallback.sListener = null
        playCallback.listener = null
        songsListener = null
        player?.unregisterCallback(callback)
        callback = null
        player = null
        songImg.pauseAnimation()
        songImg.clearAnimation()
        dragListener.clear()
        timer.cancel()
    }
}