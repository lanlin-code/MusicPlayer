package com.example.music

import com.example.music.entity.User

interface AutoLoginCallback {
    fun onAutoLoginSuccess(user: User)
    fun onAutoLoginFail(message: String)
}