package com.example.music.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyService : Service() {

    private val tag = "MyService"
    private val binder = MyBinder(this)

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelId = "music"
            val channelName = "music_channel"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(this, channelId)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binder.destroy()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

}
