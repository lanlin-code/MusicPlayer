package com.example.mylibrary

import com.example.mylibrary.implement.ImageImplement
import com.example.mylibrary.implement.LoginImplement
import com.example.mylibrary.implement.MusicImplement
import com.example.mylibrary.implement.SearchImplement
import com.example.respository.RequestCallBack
import com.example.respository.api.ClientImageApi
import com.example.respository.api.ClientLoginApi
import com.example.respository.api.ClientMusicApi
import com.example.respository.api.ClientSearchApi

object DataUtil {
    val loginImp: ClientLoginApi = LoginImplement()
    val imageApi: ClientImageApi = ImageImplement()
    val musicImplement: ClientMusicApi = MusicImplement()
    val searchImplement: ClientSearchApi = SearchImplement()

    fun getJsonData(url : String,callback : RequestCallBack<String>){

    }
}