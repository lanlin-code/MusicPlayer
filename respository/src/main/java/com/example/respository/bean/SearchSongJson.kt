package com.example.respository.bean

/**
 * baseUrl/search?keywords= 富士山下
 * 接口：获取搜索的关键词的信息.
 * */
class SearchSongJson {
    var result: Result? = null

    class Result {
        var songs: MutableList<Song>? = null

        class Song {
            var id: Long? = null
            var name: String? = null
            var artists: MutableList<Artist>? = null

            class Artist {
                var id: Long? = null
                var name: String? = null

                override fun toString(): String {
                    return "[Artist id = $id, name = $name]"
                }
            }

            override fun toString(): String {
                return "[song id = $id, name = $name, artists = $artists]"
            }
        }
        var songCount: Int? = null

        override fun toString(): String {
            return "[Result songs = $songs, songCount = $songCount]"
        }
    }

    override fun toString(): String {
        return "[SearchSongJson result = $result]"
    }
}