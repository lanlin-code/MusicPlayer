package com.example.music

import android.app.Application
import com.example.mylibrary.RetrofitUtil

class MusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RetrofitUtil.init(applicationContext)
    }
}