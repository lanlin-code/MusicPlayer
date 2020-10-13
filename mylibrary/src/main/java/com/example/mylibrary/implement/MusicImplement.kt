package com.example.mylibrary.implement

import android.util.Log
import com.example.mylibrary.RetrofitUtil
import com.example.mylibrary.api.MusicAPI
import com.example.respository.bean.DailyRecommendPlayList
import com.example.respository.bean.RecommendNewSong
import com.example.respository.bean.RecommendPlayList
import com.example.respository.RequestCallBack
import com.example.respository.api.ClientMusicApi
import com.example.respository.bean.*

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MusicImplement : ClientMusicApi {

    private val musicAPI: MusicAPI = RetrofitUtil.getRetrofit().create(MusicAPI::class.java)
    private val tag: String = "MusicImplement"
    override fun getUserPlayList(uid: Long, callBack: RequestCallBack<UserPlayListJson>) {
        val o = musicAPI.getUserPlayList(uid)
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ callBack.callback(it) }, {
                Log.d(tag, it.message, it)
                it.message?.let { it1 -> callBack.error(it1) }
            })
    }

    override fun getSongListDetail(id: Long, callBack: RequestCallBack<SongIdsJson>) {
        val o = musicAPI.getSongListDetail(id)
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ callBack.callback(it) }, {
                Log.d(tag, it.message, it)
                it.message?.let { it1 -> callBack.error(it1) }
            })
    }

    override fun getSongsDetail(id: String, callBack: RequestCallBack<SongDetailJson>) {
        val o = musicAPI.getSongsDetail(id)
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            callBack.callback(it)
        }, {
            Log.d(tag, it.message, it)
            it.message?.let { it1 -> callBack.error(it1) }
        })
    }

    override fun getSongPlay(id: Long, callBack: RequestCallBack<SongPlayJson>) {
        val o = musicAPI.getSongPlay(id)
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ callBack.callback(it) }, {
            Log.d(tag, it.message, it)
            it.message?.let { m -> callBack.error(m) }
        })
    }

    override fun getSongsPlay(ids: String, callBack: RequestCallBack<SongPlayJson>) {

    }

    override fun getSongLyric(id: Long, callBack: RequestCallBack<LyricJson>) {
        val o = musicAPI.getSongLyric(id)
        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ callBack.callback(it) }, {
            Log.d(tag, it.message, it)
            it.message?.let { m -> callBack.error(m) }
        })
    }

    override fun getRecommendPlayList(limit: Int, callBack: RequestCallBack<RecommendPlayList>) {

    }

    override fun getDailyRecommendPlayList(callback: RequestCallBack<DailyRecommendPlayList>) {

    }

    override fun getRecommendNewSong(callback: RequestCallBack<RecommendNewSong>) {

    }


}