package com.example.music.search

import com.example.mylibrary.DataUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.HotList

class HotWordModel {
    fun getHotList(callback: RequestCallBack<HotList>) {
        DataUtil.searchImplement.getHotList(callback)
    }
}