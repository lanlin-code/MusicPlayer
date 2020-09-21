package com.example.mylibrary

import com.example.mylibrary.api.LoginAPI
import com.example.respository.api.ClientLoginApi

object ApiImplement {
    val loginImp: ClientLoginApi = LoginImplement()
}