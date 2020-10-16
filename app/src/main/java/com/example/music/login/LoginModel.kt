package com.example.music.login

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserJson

class LoginModel {

    // 登录
    fun login(callback: RequestCallBack<UserJson>, username: String?, password: String?) {
        if (username == null || password == null) {
            callback.error("username or password is null")
        } else {
            DataUtil.loginImp.login(username, password, callback)
        }
    }

}