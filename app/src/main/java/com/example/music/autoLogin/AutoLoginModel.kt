package com.example.music.autoLogin

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserJson

class AutoLoginModel {

    fun autoLogin(callback: RequestCallBack<UserJson>) {
        DataUtil.loginImp.getLoginStatus(callback)
    }

}