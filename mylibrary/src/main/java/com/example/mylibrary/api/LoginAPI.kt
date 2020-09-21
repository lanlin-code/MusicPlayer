package com.example.mylibrary.api

import com.example.respository.bean.UserJson
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface LoginAPI {
    // 手机登录
    @GET("login/cellphone")
    fun login(@Query("phone") phone: String, @Query("password") password: String): Observable<UserJson>

    @GET("login/status")
    fun getLoginStatus(): Observable<UserJson>

    @GET("login/refresh")
    fun refreshLogin(): Observable<Unit>

    @GET("logout")
    fun logout(): Observable<Unit>



}