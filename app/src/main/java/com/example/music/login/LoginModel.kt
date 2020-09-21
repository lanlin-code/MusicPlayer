package com.example.music.login

import com.example.mylibrary.ApiImplement
import com.example.mylibrary.LoginImplement

class LoginModel {


    fun login(presenter: LoginPresenter) {
        val username = presenter.getUsername()
        val password = presenter.getPassword()
        if (username == null || password == null) {
            presenter.error("username or password is null")
        } else {
            ApiImplement.loginImp.login(username, password, presenter)
        }
    }

}