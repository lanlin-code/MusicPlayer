package com.example.mylibrary.api

import com.example.respository.bean.SongDetailJson
import com.example.respository.bean.SongPlayJson
import com.example.respository.bean.UserPlayListJson
import com.example.respository.bean.LyricJson
import com.example.respository.bean.SongIdsJson
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface MusicAPI {

    // 获取用户的歌单的详细信息，传入用户id.
    @GET("user/playlist")
    fun getUserPlayList(@Query("uid") uid: Long): Observable<UserPlayListJson>

    // 传入歌单id，获取歌单包含的歌曲.
    @GET("playlist/detail")
    fun getSongListDetail(@Query("id") id: Long): Observable<SongIdsJson>

    // 传入歌曲的id，获取歌曲详情.
    @GET("song/detail")
    fun getSongsDetail(@Query("ids") ids: String): Observable<SongDetailJson>

    // 获取歌曲url、码率等信息，传入歌曲的多个id
    @GET("song/url")
    fun getSongPlay(@Query("id") id: Long): Observable<SongPlayJson>

    // 获取歌曲的歌词.
    @GET("lyric")
    fun getSongLyric(@Query("id") id: Long): Observable<LyricJson>


}