package com.example.respository.api

import com.example.respository.bean.UserJson
import com.example.respository.RequestCallBack

/**
 * 暴露给用户使用的Api.
 * */
interface ClientLoginApi {
    fun login(username: String, password: String, callback : RequestCallBack<UserJson>)
    fun refreshLogin()
    fun getLoginStatus(callback : RequestCallBack<UserJson>)
    fun logout()
}