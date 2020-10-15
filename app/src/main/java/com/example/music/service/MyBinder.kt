package com.example.music.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.music.IMusicCallback
import com.example.music.IMusicPlayer
import com.example.music.R
import com.example.music.ResponseCallback
import com.example.music.entity.Song
import com.example.music.entity.SongData
import com.example.music.song.SongModel
import com.example.music.song.SongPresenter
import com.example.music.util.LogUtil
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList

class MyBinder(val context: Context,
               var musicNotification: MusicNotification? = null) : IMusicPlayer.Stub() {
    private val tag = "MyBinder"
    private val presenter = SongPresenter()
    private val model = SongModel()
    private val songList = CopyOnWriteArrayList<Song>()
    private var musicPlayer = MediaPlayer()
    private val musicPosition = MusicPosition()
    private val callbackList = RemoteCallbackList<IMusicCallback>()
    private var prepared = false


    init {
        musicPlayer.setOnPreparedListener {
            musicPlayer.start()
            prepared = true
            val song = songList[musicPosition.currentPosition]
            notifySongChange(song)
        }
        musicPlayer.setOnCompletionListener {
            next()
        }
        musicPlayer.setOnErrorListener { mp, what, extra ->
            Toast.makeText(context.applicationContext, "当前歌曲播放失败", Toast.LENGTH_SHORT).show()
            true
        }
        presenter.listener = object : ResponseCallback<MutableList<SongData>> {
            override fun onSuccess(data: MutableList<SongData>) {
                if (data.size <= 0) {
                    Toast.makeText(context.applicationContext, "加载失败", Toast.LENGTH_SHORT).show()
//                    next()
                }
                var position = -1
                for (d in data) {
                    for (i in 0 until songList.size) {
                        val s = songList[i]
                        if (d.id == s.id) {
                            s.url = d.url
                            position = i
                            break
                        }
                    }
                }
//                LogUtil.debug(tag, "currentPosition = ${musicPosition.currentPosition}, position = $position")
                if (position >= 0) {
                    play(position)

                }

            }

            override fun onError(message: String) {
                Toast.makeText(context.applicationContext, "加载失败", Toast.LENGTH_SHORT).show()
            }

        }
    }


    override fun clear() {
        musicPlayer.reset()
        songList.clear()
        musicPosition.size = 0
        musicPosition.currentPosition = 0

        notifySongsNull()
    }

    override fun parse() {
        if (musicPlayer.isPlaying && prepared) {
            musicPlayer.pause()
            musicNotification?.updateState(false)
            notifyPlayStatusChange(false)
        }
    }

    override fun seekTo(position: Int) {
        musicPlayer.seekTo(position)
    }

    override fun registerCallback(callback: IMusicCallback?) {
        callback?.let { callbackList.register(callback) }
    }

    override fun unregisterCallback(callback: IMusicCallback?) {
        callback?.let { callbackList.unregister(callback) }
    }

    override fun mode(mode: Int) {
        musicPosition.mode = mode
    }

    override fun isPlaying(): Boolean {
        return musicPlayer.isPlaying
    }

    override fun currentPlaying(): Song {
        return songList[musicPosition.currentPosition]
    }

    override fun showBar(): Boolean {
        return songList.size > 0
    }

    override fun getMode(): Int {
        return musicPosition.mode
    }

    override fun updateLayout(position: Int): Boolean {
        return musicPosition.currentPosition == position
    }

    override fun addSong(song: Song?) {
        song?.let {
            val e = exist(it)
            if (e != -1) {
                if (e != musicPosition.currentPosition) {
                    val s = songList.removeAt(e)
                    songList.add(musicPosition.currentPosition + 1, s)
                }
            } else {
                songList.add(it)
                musicPosition.size = songList.size
            }
            if (songList.size == 1) {
                playFrom(musicPosition.currentPosition)
            }

        }
    }

    override fun removeSong(song: Song?) {
        song?.let {
            val e = exist(it)
            if (e != -1) {
                songList.remove(songList[e])
                musicPosition.size = songList.size
                if (e == musicPosition.currentPosition) {
                    if (songList.size > 0) {
                        next()
                    } else {
                        musicPlayer.reset()
                    }
                }
                if (songList.size <= 0) {
                    notifySongsNull()
                }
            }
        }
    }

    override fun addAndPlay(song: Song?) {
        song?.let {
            val e = exist(it)
            if (e != -1) {
                playFrom(e)
            } else {
                songList.add(musicPosition.currentPosition, it)
                playFrom(musicPosition.currentPosition)
            }
        }
    }

    override fun obtainData(): MutableList<Song> {
        return songList
    }

    override fun isPrepared(): Boolean {
        return prepared
    }

    override fun next() {
        musicPosition.nextPosition()
        prepareToPlay(musicPosition.currentPosition)
    }

    override fun currentPosition(): Int {
        return if (prepared) {
            musicPlayer.currentPosition
        } else {
            0
        }
    }

    override fun duration(): Int {

        return if (prepared) {
            musicPlayer.duration
        } else {
            0
        }

    }

    override fun playFrom(position: Int) {
        musicPosition.currentPosition = position
        prepareToPlay(musicPosition.currentPosition)
    }

    override fun last() {
        musicPosition.lastPosition()
        prepareToPlay(musicPosition.currentPosition)
    }

    override fun receive(songs: MutableList<Song>?) {
        musicPlayer.reset()
        songList.clear()
        musicPosition.size = 0
        musicPosition.currentPosition = 0
        if (songs != null) {
            songList.addAll(songs)
            musicPosition.size = songList.size
            musicPosition.currentPosition = 0
        }
    }

    override fun restart() {
        if (!musicPlayer.isPlaying && prepared) {
            musicPlayer.start()
            musicNotification?.updateState(true)
            notifyPlayStatusChange(true)
        }

    }

    override fun clearAllCallback() {
        val count = callbackList.beginBroadcast()
        for (i in 0 until count) {
            val c = callbackList.getBroadcastItem(i)
            callbackList.unregister(c)
        }
        callbackList.finishBroadcast()
    }

    private fun exist(song: Song): Int {
        var position = -1
        val size = songList.size
        for (i in 0 until size) {
            val s = songList[i]
            if (s.id == song.id) {
                position = i
                break
            }
        }
        return position
    }

    private fun prepareToPlay(position: Int) {
        if (position != musicPosition.currentPosition) return
        val song = songList[musicPosition.currentPosition]

        if (song.errorUrl()) {
            loadUrl()
        } else {
            play(position)
        }
    }

    private fun play(position: Int) {

        if (position != musicPosition.currentPosition) return
        prepared = false
        val song = songList[musicPosition.currentPosition]
//        LogUtil.debug(tag, "name = ${song.name}, url = ${song.url}, id = ${song.id}")

        if (song.errorUrl()) {
            LogUtil.error(tag, "error")
            next()
        } else {

            val url = song.url
            try {
                musicPlayer.reset()
                musicPlayer.setDataSource(url)
                musicPlayer.prepareAsync()
                musicNotification?.onUpdate(song, true)

            } catch (e: Exception) {
                LogUtil.error(tag, "Music Exception")
                Log.d(tag, e.message, e)
                next()
            }

        }

    }

    private fun notifySongsNull() {
        musicNotification?.disappear()
        val count = callbackList.beginBroadcast()
        for (i in 0 until count) {
            callbackList.getBroadcastItem(i).closeBar()
        }
        callbackList.finishBroadcast()
    }

    private fun notifySongChange(song: Song) {
        val count = callbackList.beginBroadcast()
        val position = songList.indexOf(song)
        for (i in 0 until count) {
            val callback = callbackList.getBroadcastItem(i)
            callback.getCurrentSong(song.albumPic, song.name)
            callback.playCallback(position)
            callback.obtainLrc(song.id)
        }
        callbackList.finishBroadcast()

    }



    private fun notifyPlayStatusChange(playing: Boolean) {

        val c = callbackList.beginBroadcast()
        for (i in 0 until c) {
            val callback = callbackList.getBroadcastItem(i)
            callback.playStatusChange(playing)
        }
        callbackList.finishBroadcast()
    }


    private fun loadUrl() {
        model.getSongUrl(songList[musicPosition.currentPosition].id, presenter)
    }

    fun destroy() {
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
        }
        musicNotification?.destroy()
        musicNotification = null
        musicPlayer.release()
        presenter.listener = null
        clearAllCallback()
    }

}