package com.example.music.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.music.R
import com.example.music.entity.Song
import com.example.music.util.LogUtil
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.IOException

class MusicNotification(val context: Context,
                        var manager: NotificationManager? = null,
                        var remoteViews: RemoteViews? = null) {

    private val nid = 1
    private var disappear = true
    private val tag = "MusicNotification"
    private val builder: NotificationCompat.Builder
    private val notification: Notification
    private val handler = Handler(Looper.getMainLooper())




    init {
        builder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelId = "music"
            val channelName = "music_channel"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)

            manager?.createNotificationChannel(channel)
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context)
        }
        notification = builder.setSmallIcon(R.drawable.ic_launcher_foreground).setOngoing(false)
            .setCustomContentView(remoteViews).setPriority(NotificationCompat.PRIORITY_MAX).build()
        notification.flags = Notification.FLAG_ONGOING_EVENT
    }

    fun isShow() = !disappear

    // 更新视图
    fun onUpdate(song: Song, isPlaying: Boolean) {
        handler.post {
            remoteViews?.setTextViewText(R.id.notification_name, song.name)
            remoteViews?.setTextViewText(R.id.notification_artist, song.appendArtists())
            Picasso.with(context).load(song.albumPic).into(remoteViews, R.id.notification_image, 1, notification)
            updateState(isPlaying)
        }


    }

    // 更新播放图片
    fun updateState(isPlaying: Boolean) {
        if (isPlaying) {
            remoteViews?.setImageViewResource(R.id.notification_play_status, R.drawable.play_32)
        } else {
            remoteViews?.setImageViewResource(R.id.notification_play_status, R.drawable.parse_32)
        }
       sendNotification()
    }


    fun sendNotification() {
        LogUtil.debug(tag, "send message")
        LogUtil.debug(tag, "manager is null? ${manager == null}")
        disappear = false

        manager?.notify(nid, notification)

    }


    fun disappear() {
        disappear = true
        manager?.cancel(nid)
    }

    fun destroy() {
        manager = null
        remoteViews = null
    }

}