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
    private val callbackList = RemoteCallbackList<IMusicCallback>() // 跨进程回调
    private var prepared = false // MediaPlayer是否准备的标志


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
                    if (songList.size > 1) next()
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
                if (position >= 0) {
                    play(position)

                }

            }

            override fun onError(message: String) {
                Toast.makeText(context.applicationContext, "加载失败", Toast.LENGTH_SHORT).show()
            }

        }
    }


    // 清空播放列表
    override fun clear() {
        musicPlayer.reset()
        songList.clear()
        musicPosition.size = 0
        musicPosition.currentPosition = 0
        notifySongsNull()
    }

    // 暂停播放
    override fun parse() {
        if (musicPlayer.isPlaying && prepared) {
            musicPlayer.pause()
            musicNotification?.updateState(false)
            notifyPlayStatusChange(false)
        }
    }

    // 跳转到选择位置播放
    override fun seekTo(position: Int) {
        musicPlayer.seekTo(position)
    }

    // 设置回调
    override fun registerCallback(callback: IMusicCallback?) {
        callback?.let { callbackList.register(callback) }
    }

    // 取消回调
    override fun unregisterCallback(callback: IMusicCallback?) {
        callback?.let { callbackList.unregister(callback) }
    }

    // 更改播放模式
    override fun mode(mode: Int) {
        musicPosition.mode = mode
    }

    // 是否正在播放
    override fun isPlaying(): Boolean {
        return musicPlayer.isPlaying
    }

    // 获取当前播放歌曲
    override fun currentPlaying(): Song {
        return songList[musicPosition.currentPosition]
    }

    // 是否应该显示播放栏
    override fun showBar(): Boolean {
        return songList.size > 0
    }

    // 获取播放模式
    override fun getMode(): Int {
        return musicPosition.mode
    }

    // 是否更新视图
    override fun updateLayout(position: Int): Boolean {
        return musicPosition.currentPosition == position
    }

    // 添加到下一首播放
    override fun addSong(song: Song?) {
        song?.let {
            val e = exist(it)
            if (e != -1) {
                if (e != musicPosition.currentPosition) {
                    val s = songList.removeAt(e)
                    songList.add(musicPosition.currentPosition + 1, s)
                }
            } else {
                songList.add(musicPosition.currentPosition + 1, it)
                musicPosition.size = songList.size
            }
            if (songList.size == 1) {
                playFrom(musicPosition.currentPosition)
            }

        }
    }

    // 移除歌曲
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

    // 添加并播放歌曲
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

    // 获取播放列表
    override fun obtainData(): MutableList<Song> {
        return songList
    }

    // 播放器是否准备
    override fun isPrepared(): Boolean {
        return prepared
    }

    // 下一曲
    override fun next() {
        musicPosition.nextPosition()
        prepareToPlay(musicPosition.currentPosition)
    }

    // 当前播放时间
    override fun currentPosition(): Int {
        return if (prepared) {
            musicPlayer.currentPosition
        } else {
            0
        }
    }

    // 总时长
    override fun duration(): Int {

        return if (prepared) {
            musicPlayer.duration
        } else {
            0
        }

    }

    // 从某一项开始播放
    override fun playFrom(position: Int) {
        musicPosition.currentPosition = position
        prepareToPlay(musicPosition.currentPosition)
    }

    // 上一曲
    override fun last() {
        musicPosition.lastPosition()
        prepareToPlay(musicPosition.currentPosition)
    }

    // 接收播放列表
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

    // 恢复播放
    override fun restart() {
        if (!musicPlayer.isPlaying && prepared) {
            musicPlayer.start()
            musicNotification?.updateState(true)
            notifyPlayStatusChange(true)
        }

    }

    // 清除所有回调
    override fun clearAllCallback() {
        val count = callbackList.beginBroadcast()
        for (i in 0 until count) {
            val c = callbackList.getBroadcastItem(i)
            callbackList.unregister(c)
        }
        callbackList.finishBroadcast()
    }

    // 检查播放列表是否存在该歌曲
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

    // 检查歌曲url，为空则加载，否则播放
    private fun prepareToPlay(position: Int) {
        if (position != musicPosition.currentPosition) return
        val song = songList[musicPosition.currentPosition]

        if (song.errorUrl()) {
            loadUrl()
        } else {
            play(position)
        }
    }

    // 播放歌曲
    private fun play(position: Int) {

        if (position != musicPosition.currentPosition) return
        prepared = false
        val song = songList[musicPosition.currentPosition]
//        LogUtil.debug(tag, "name = ${song.name}, url = ${song.url}, id = ${song.id}")

        if (song.errorUrl()) {
            next()
        } else {

            val url = song.url
            try {
                musicPlayer.reset()
                musicPlayer.setDataSource(url)
                musicPlayer.prepareAsync()
                musicNotification?.onUpdate(song, true)

            } catch (e: Exception) {
                next()
            }

        }

    }

    // 播放列表空时调用
    private fun notifySongsNull() {
        musicNotification?.disappear()
        val count = callbackList.beginBroadcast()
        for (i in 0 until count) {
            callbackList.getBroadcastItem(i).closeBar()
        }
        callbackList.finishBroadcast()
    }

    // 播放歌曲变化时调用
    private fun notifySongChange(song: Song) {
        val count = callbackList.beginBroadcast()
        val position = songList.indexOf(song)
        for (i in 0 until count) {
            val callback = callbackList.getBroadcastItem(i)
            callback.getCurrentSong(song.albumPic, song.name)
            callback.playCallback(position)
            callback.obtainLrc(song.id)
            callback.playStatusChange(true)
        }
        callbackList.finishBroadcast()

    }

    // 播放状态变化时调用
    private fun notifyPlayStatusChange(playing: Boolean) {

        val c = callbackList.beginBroadcast()
        for (i in 0 until c) {
            val callback = callbackList.getBroadcastItem(i)
            callback.playStatusChange(playing)
        }
        callbackList.finishBroadcast()
    }

    // 加载url
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