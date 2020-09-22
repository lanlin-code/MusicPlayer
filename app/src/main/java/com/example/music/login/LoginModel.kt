package com.example.music.login

import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserJson

class LoginModel {


    fun login(callback: RequestCallBack<UserJson>, username: String?, password: String?) {
        if (username == null || password == null) {
            callback.error("username or password is null")
        } else {
            ApiImplement.loginImp.login(username, password, callback)
        }
    }

    fun autoLogin(callback: RequestCallBack<UserJson>) {
        ApiImplement.loginImp.getLoginStatus(callback)
    }

}