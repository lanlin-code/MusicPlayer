package com.example.respository.bean

/**
 * 获取用户的歌单时返回的Gson.
 * 接口地址：baseUrl/user/playlist?uid=1333207903
 * */
class UserPlayListJson{
    var playlist : MutableList<PlayList>? = null
    
    class PlayList {
        var id : Long? = null
        var name : String? = null
        var coverImgUrl : String? = null

        override fun toString(): String {
            return "[Playlist id = $id, name = $name, coverImgUrl = $coverImgUrl]"
        }
    }

    override fun toString(): String {
        return "$playlist"
    }
}