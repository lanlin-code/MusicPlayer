package com.example.music.play

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.music.BaseFragment
import com.example.music.IMusicPlayer
import com.example.music.R
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.service.MusicPosition
import com.example.music.util.LogUtil
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
    private var drag = false
    private var timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
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
        updateLayout()

    }

    private fun initLayout(view: View) {
        layout = view.findViewById(R.id.play_layout)
        name = view.findViewById(R.id.play_name)
        artist = view.findViewById(R.id.play_artist)
        songImg = view.findViewById(R.id.play_image)
        songImg.start = true
        currentTime = view.findViewById(R.id.play_current_time)
        seekBar = view.findViewById(R.id.play_seek_bar)
        duration = view.findViewById(R.id.play_duration)
        initSeekBar()
        playState = view.findViewById(R.id.play_play_state)
        initButtonState()
        player?.let { updateButtonState(it.isPlaying) }
        val last = view.findViewById<ImageButton>(R.id.play_last)
        last.setOnClickListener { player?.last() }
        playMode = view.findViewById(R.id.play_mode)
        initButtonMode()
        val next = view.findViewById<ImageButton>(R.id.play_next)
        next.setOnClickListener { player?.next() }

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

    private fun updateButtonMode() {
        when(currentMode) {
            MusicPosition.order -> playMode.setImageResource(R.drawable.list_circle)
            MusicPosition.circle -> playMode.setImageResource(R.drawable.only_one_circle)
            MusicPosition.random -> playMode.setImageResource(R.drawable.random)
        }
    }

    private fun updateButtonState(playing: Boolean) {
        if (playing) {
            playState.setImageResource(R.drawable.play)
        } else {
            playState.setImageResource(R.drawable.parse)
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
                }
            }
        })

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (!drag) {
                    handler.post {
                        player?.let {
                            val c = it.currentPosition()
                            val s = format.format(c)
                            seekBar.progress = c
                            LogUtil.debug(TAG, "currentTime = $s")
                            currentTime.text = s
                        }
                    }
                }
            }
        }, 0L, 100L)
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
                val builder = StringBuilder()
                val size = it.artists.size
                for (i in 0 until size) {
                    builder.append(it.artists[i].name)
                    if (i != size - 1) {
                        builder.append(" ")
                    }
                }
                artist.text = builder.toString()
            }
            player?.let {
                val d = it.duration()
                val ds = format.format(d)
                LogUtil.debug(TAG, "duration = $ds")
                seekBar.max = d
                currentTime.text = format.format(it.currentPosition())
                duration.text = ds
            }
        } catch (e: RemoteException) {

        }

    }

    override fun onDetach() {
        super.onDetach()
        songsListener = null
        player = null
        songImg.clearAnimation()
        timer.cancel()
    }
}