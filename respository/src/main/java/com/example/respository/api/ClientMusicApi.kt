package com.example.respository.api

import com.example.respository.bean.DailyRecommendPlayList
import com.example.respository.bean.RecommendNewSong
import com.example.respository.bean.RecommendPlayList
import com.example.respository.bean.SongDetailJson
import com.example.respository.bean.SongPlayJson
import com.example.respository.bean.UserPlayListJson
import com.example.respository.bean.LyricJson
import com.example.respository.bean.SongIdsJson
import com.example.respository.RequestCallBack

/**
 * 暴露给用户使用的，与音乐有关的Api.
 * */
interface ClientMusicApi {
    /**
     * 获取用户的歌单的详细信息，传入用户id.
     * */
    fun getUserPlayList(uid: Long, callBack: RequestCallBack<UserPlayListJson>)

    /**
     * 传入歌单id，获取歌单包含的歌曲.
     * */
    fun getSongListDetail(id: Long, callBack: RequestCallBack<SongIdsJson>)

    /**
     * 传入歌曲的id，获取歌曲详情.
     * */
    fun getSongsDetail(id: String, callBack: RequestCallBack<SongDetailJson>)

    /**
     * 获取歌曲url、码率等信息，传入歌曲的单个id
     * */
    fun getSongPlay(id: Long, callBack: RequestCallBack<SongPlayJson>)

    /**
     * 获取歌曲url、码率等信息，传入歌曲的多个id
     * */
    fun getSongsPlay(ids: String, callBack: RequestCallBack<SongPlayJson>)

    /**
     * 获取歌曲的歌词.
     * */
    fun getSongLyric(id: Long, callBack: RequestCallBack<LyricJson>)

    /**
     * 获取推荐歌单，无需登录.
     * url：baseUrl/personalized?limit=...
     * */
    fun getRecommendPlayList(limit : Int,callBack: RequestCallBack<RecommendPlayList>)

    /**
     * 获取每日推荐歌单，需要登录.
     * url：baseUrl/recommend/resource
     * */
    fun getDailyRecommendPlayList(callback : RequestCallBack<DailyRecommendPlayList>)

    /**
     * 获取推荐的新音乐.
     * url：baseUrl/personalized/newsong
     * */
    fun getRecommendNewSong(callback : RequestCallBack<RecommendNewSong>)

}