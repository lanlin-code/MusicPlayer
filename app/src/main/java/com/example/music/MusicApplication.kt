package com.example.music

import android.app.Application
import android.content.Context
import com.example.music.util.DownLoader
import com.example.mylibrary.RetrofitUtil

class MusicApplication : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        RetrofitUtil.init(applicationContext)
        context = applicationContext
        DownLoader.cacheDir = applicationContext.getExternalFilesDir("song")
    }
}