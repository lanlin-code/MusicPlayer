package com.example.music.autoLogin

import com.example.mylibrary.ApiImplement
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserJson

class AutoLoginModel {

    fun autoLogin(callback: RequestCallBack<UserJson>) {
        ApiImplement.loginImp.getLoginStatus(callback)
    }

}