package com.example.respository.bean

/**
 * 推荐歌单的GsonBean.
 * */
class RecommendPlayList {
    
    var result : MutableList<Result>? = null
    
    class Result{
        var id : Long? = null
        var name : String? = null
        var copywriter : String? = null
        var picUrl : String? = null
    }
    
}