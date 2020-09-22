package com.example.music.login

import com.example.music.ResponseCallback
import com.example.music.entity.User
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserJson

class LoginPresenter : RequestCallBack<UserJson> {

    var listener: ResponseCallback<User>? = null
    private val tag = "LoginPresenter"

    override fun callback(data: UserJson) {
        val u = User.of(data)
        LogUtil.debug(tag, "$u")
        if (User.userMessageError(u)) {
            listener?.onError("Something is wrong, please try again")
        } else {
            listener?.onSuccess(u)
        }
    }

    override fun error(errorMsg: String) {
        listener?.onError(errorMsg)
    }



}