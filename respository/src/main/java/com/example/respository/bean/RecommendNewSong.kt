package com.example.respository.bean

/**
 * 推荐新音乐.
 * */
class RecommendNewSong {
    var result : MutableList<Result>? = null
    
    class Result{
        var id : Long? = null
        var name : String? = null
        var picUrl : String? = null
        var song : Song? = null
        
        class Song{
            
            var artists : MutableList<Artist>? = null
            
            class Artist{
                var name : String? = null
                var id : Long? = null
            }
        }
    }
}