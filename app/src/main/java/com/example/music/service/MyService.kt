package com.example.music.service

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.RemoteViews
import com.example.music.R

class MyService : Service() {

    private val tag = "MyService"
    private val binder = MyBinder(this)
    private val broadcastReceiver = MusicBroadcastReceiver()

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val remoteView = RemoteViews(packageName, R.layout.linearlayout_notification)
        val musicNotification = MusicNotification(this, manager, remoteView)
        binder.musicNotification = musicNotification
        broadcastReceiver.binder = binder
        val filter = IntentFilter()
        filter.addAction(MusicBroadcastReceiver.ACTION_NEXT)
        filter.addAction(MusicBroadcastReceiver.ACTION_LAST)
        filter.addAction(MusicBroadcastReceiver.ACTION_STATE)
        registerReceiver(broadcastReceiver, filter)
        remoteView.setOnClickPendingIntent(R.id.notification_play_status, buildPendingIntent(MusicBroadcastReceiver.ACTION_STATE))
        remoteView.setOnClickPendingIntent(R.id.notification_last, buildPendingIntent(MusicBroadcastReceiver.ACTION_LAST))
        remoteView.setOnClickPendingIntent(R.id.notification_next, buildPendingIntent(MusicBroadcastReceiver.ACTION_NEXT))

    }

    private fun buildPendingIntent(action: String): PendingIntent {
        val intent = Intent(action)
        return PendingIntent.getBroadcast(this, 0, intent, 0)
    }






    override fun onDestroy() {
        super.onDestroy()
        binder.musicNotification?.disappear()
        unregisterReceiver(broadcastReceiver)
        binder.destroy()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

}
