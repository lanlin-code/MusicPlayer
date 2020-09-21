package com.example.music.login

import com.example.music.ResponseCallback
import com.example.music.entity.User
import com.example.respository.RequestCallBack
import com.example.respository.bean.UserJson

class LoginPresenter : RequestCallBack<UserJson> {

    var listener: LoginListener? = null

    override fun callback(data: UserJson) {
        val u = User.of(data)
        if (User.userMessageError(u)) {
            listener?.onError("Something is wrong, please try again")
        } else {
            listener?.onSuccess(u)
        }
    }

    override fun error(errorMsg: String) {
        listener?.onError(errorMsg)
    }


    fun getUsername(): String? {
        return listener?.getUsername()
    }

    fun getPassword(): String? {
        return listener?.getPassword()
    }

    interface LoginListener : ResponseCallback<User> {
        fun getUsername(): String?
        fun getPassword(): String?
    }
}