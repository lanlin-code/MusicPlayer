package com.example.music.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.Toast
import com.example.music.IMusicCallback
import com.example.music.IMusicPlayer
import com.example.music.ResponseCallback
import com.example.music.entity.Song
import com.example.music.entity.SongData
import com.example.music.song.SongModel
import com.example.music.song.SongPresenter
import com.example.music.util.LogUtil
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList

class MyService : Service() {

    private val tag = "MyService"
    private val binder = MyBinder(this)
//    private val songList = CopyOnWriteArrayList<Song>()
//    private lateinit var musicPlayer: MediaPlayer
//    private val musicPosition = MusicPosition()
//    private val presenter = SongPresenter()
//    private val model = SongModel()
//    private val callbackList = RemoteCallbackList<IMusicCallback>()
//
//    private val binder =  object : IMusicPlayer.Stub() {
//        override fun clear() {
//            musicPlayer.reset()
//            songList.clear()
//            musicPosition.size = 0
//            musicPosition.currentPosition = 0
//        }
//
//        override fun parse() {
//            if (musicPlayer.isPlaying) {
//                musicPlayer.pause()
//            }
//        }
//
//        override fun seekTo(position: Int) {
//            musicPlayer.seekTo(position)
//        }
//
//        override fun registerCallback(callback: IMusicCallback?) {
//            callback?.let { callbackList.register(callback) }
//        }
//
//        override fun unregisterCallback(callback: IMusicCallback?) {
//            callback?.let { callbackList.unregister(callback) }
//        }
//
//        override fun mode(mode: Int) {
//            musicPosition.mode = mode
//        }
//
//        override fun isPlaying(): Boolean {
//            return musicPlayer.isPlaying
//        }
//
//        override fun currentPlaying(): Song {
//            return songList[musicPosition.currentPosition]
//        }
//
//        override fun showBar(): Boolean {
//            return songList.size > 0
//        }
//
//        override fun getMode(): Int {
//            return musicPosition.mode
//        }
//
//        override fun updateLayout(position: Int): Boolean {
//            return musicPosition.currentPosition == position
//        }
//
//        override fun addSong(song: Song?) {
//           song?.let {
//               val e = exist(it)
//               if (e != -1) {
//                   if (e != musicPosition.currentPosition) {
//                       val s = songList.removeAt(e)
//                       songList.add(musicPosition.currentPosition + 1, s)
//                   }
//               } else {
//                   songList.add(it)
//                   musicPosition.size = songList.size
//               }
//           }
//        }
//
//        override fun removeSong(song: Song?) {
//            song?.let {
//                val e = exist(it)
//                if (e != -1) {
//                    songList.remove(songList[e])
//                    musicPosition.size = songList.size
//                    if (e == musicPosition.currentPosition) {
//                        if (songList.size > 0) {
//                            next()
//                        } else {
//                            musicPlayer.reset()
//                        }
//                    }
//                }
//            }
//        }
//
//        override fun addAndPlay(song: Song?) {
//            song?.let {
//                val e = exist(it)
//                if (e != -1) {
//                    playFrom(e)
//                } else {
//                    songList.add(musicPosition.currentPosition, it)
//                    playFrom(musicPosition.currentPosition)
//                }
//            }
//        }
//
//        override fun next() {
//            musicPosition.nextPosition()
//            LogUtil.debug(tag, "current = ${musicPosition.currentPosition}")
//            prepareToPlay(musicPosition.currentPosition)
//        }
//
//        override fun currentPosition(): Int {
//            return musicPlayer.currentPosition
//        }
//
//        override fun duration(): Int {
//            return musicPlayer.duration
//        }
//
//        override fun playFrom(position: Int) {
//            musicPosition.currentPosition = position
//            prepareToPlay(musicPosition.currentPosition)
//        }
//
//        override fun last() {
//            musicPosition.lastPosition()
//            prepareToPlay(musicPosition.currentPosition)
//        }
//
//        override fun receive(songs: MutableList<Song>?) {
//            if (songs != null) {
//                songList.addAll(songs)
//                musicPosition.size = songList.size
//                musicPosition.currentPosition = 0
//            }
//        }
//
//        override fun restart() {
//            musicPlayer.start()
//        }
//
//        override fun clearAllCallback() {
//            val count = callbackList.beginBroadcast()
//            for (i in 0 until count) {
//                val c = callbackList.getBroadcastItem(i)
//                callbackList.unregister(c)
//            }
//            callbackList.finishBroadcast()
//        }
//
//
//    }
//
//    private fun exist(song: Song): Int {
//        var position = -1
//        val size = songList.size
//        for (i in 0 until size) {
//            val s = songList[i]
//            if (s.id == song.id) {
//                position = i
//                break
//            }
//        }
//        return position
//    }
//
//    private fun loadUrl() {
//        model.getSongUrl(songList[musicPosition.currentPosition].id, presenter)
//    }
//
//    private fun play(position: Int) {
//        if (position != musicPosition.currentPosition) return
//        musicPlayer.reset()
//        val song = songList[musicPosition.currentPosition]
//        if (song.errorUrl()) {
//            binder.next()
//        } else {
//            notifySongChange(song)
//
//            val url = song.url
//            try {
//                musicPlayer.setDataSource(url)
//                musicPlayer.prepareAsync()
//            } catch (e: Exception) {
//                binder.next()
//            }
//
//        }
//    }
//
//    private fun prepareToPlay(position: Int) {
//        if (position != musicPosition.currentPosition) return
//        val song = songList[musicPosition.currentPosition]
//        if (song.errorUrl()) {
//            loadUrl()
//        } else {
//            play(position)
//        }
//    }
//
//    private fun notifySongChange(song: Song) {
//        val count = callbackList.beginBroadcast()
//        val position = songList.indexOf(song)
//        for (i in 0 until count) {
//            val callback = callbackList.getBroadcastItem(i)
//            callback.getCurrentSong(song.albumPic, song.name)
//            callback.playCallback(position)
//            callback.obtainLrc(song.id)
//        }
//        callbackList.finishBroadcast()
//
//    }


//    override fun onCreate() {
//        super.onCreate()
////        musicPlayer = MediaPlayer()
////        musicPlayer.setOnPreparedListener {
////            musicPlayer.start()
////        }
////        musicPlayer.setOnCompletionListener {
////            binder.next()
////        }
////        musicPlayer.setOnErrorListener { mp, what, extra ->
////            Toast.makeText(this@MyService, "当前歌曲播放失败", Toast.LENGTH_SHORT).show()
////            false
////        }
////        presenter.listener = object : ResponseCallback<MutableList<SongData>> {
////            override fun onSuccess(data: MutableList<SongData>) {
////                if (data.size <= 0) {
////                    Toast.makeText(this@MyService, "加载失败", Toast.LENGTH_SHORT).show()
////                    binder.next()
////                }
////                var position = -1
////                for (d in data) {
////                    for (i in 0 until songList.size) {
////                        val s = songList[i]
////                        if (d.id == s.id) {
////                            s.url = d.url
////                            position = i
////                            break
////                        }
////                    }
////                }
////                if (position >= 0) {
////                    play(position)
////                }
////
////            }
////
////            override fun onError(message: String) {
////                Toast.makeText(this@MyService, "加载失败", Toast.LENGTH_SHORT).show()
////            }
////
////        }
//
//    }

    override fun onDestroy() {
        super.onDestroy()
        binder.destroy()
//        if (musicPlayer.isPlaying) {
//            musicPlayer.stop()
//        }
//        musicPlayer.release()
//        presenter.listener = null
//        songList.clear()
    }

    override fun onBind(intent: Intent): IBinder {
//        return binder
        return binder
    }

}
