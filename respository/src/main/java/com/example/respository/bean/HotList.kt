package com.example.respository.bean

/**
 * 热搜列表.
 * */
class HotList {
    var data : MutableList<Data>? = null
    
    class Data {
        var searchWord : String? = null
        var score : Long? = null
        var content : String? = null
        var iconUrl : String? = null
    }
}