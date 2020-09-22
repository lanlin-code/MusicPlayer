package com.example.mylibrary

import com.example.mylibrary.implement.ImageImplement
import com.example.mylibrary.implement.LoginImplement
import com.example.respository.api.ClientImageApi
import com.example.respository.api.ClientLoginApi

object ApiImplement {
    val loginImp: ClientLoginApi = LoginImplement()
    val imageApi: ClientImageApi = ImageImplement()
}