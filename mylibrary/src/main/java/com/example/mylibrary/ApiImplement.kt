package com.example.mylibrary

import com.example.mylibrary.implement.ImageImplement
import com.example.mylibrary.implement.LoginImplement
import com.example.mylibrary.implement.MusicImplement
import com.example.respository.api.ClientImageApi
import com.example.respository.api.ClientLoginApi
import com.example.respository.api.ClientMusicApi

object ApiImplement {
    val loginImp: ClientLoginApi = LoginImplement()
    val imageApi: ClientImageApi = ImageImplement()
    val musicImplement: ClientMusicApi = MusicImplement()
}