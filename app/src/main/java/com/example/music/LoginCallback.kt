package com.example.music

import com.example.music.entity.User

interface LoginCallback {
    fun onLoginSuccess(data: User)
    fun onLoginFail(message: String)
}