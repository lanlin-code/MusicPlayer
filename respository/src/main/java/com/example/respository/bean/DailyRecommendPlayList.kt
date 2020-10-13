package com.example.respository.bean

/**
 * 获取每日推荐歌单的GsonBean.
 * */
class DailyRecommendPlayList {
    var recommend : MutableList<Recommend>? = null

    class Recommend{
        var id : Long? = null
        var name : String? = null
        var copywriter : String? = null
        var picUrl : String? = null
    }
}