package com.example.respository.bean

/**
 * 获取歌曲详情，包括专辑图片等(无音乐url)：baseUrl/song/detail?ids=191528,191527
 * Gson解析的bean类.
 * */
class SongDetailJson {
    var songs: MutableList<Song>? = null

    override fun toString(): String {
        return "songs is $songs"
    }

    class Song {
        var name: String? = null
        var id: Long? = null

        /**
         * 专辑.
         * */
        var al: Album? = null

        /**
         * 艺术家，歌手.
         * */
        var ar: MutableList<Artist>? = null

        override fun toString(): String {
            return "name is $name, id is $id, al is $al, ar is $ar"
        }

        /**
         * 专辑的信息.
         * */
        class Album {
            var id: Long? = null
            var name: String? = null
            var picUrl: String? = null

            override fun toString(): String {
                return "id is $id, name is $name, picUrl is $picUrl"
            }
        }

        /**
         * 对应字段ar，艺术家/歌手.
         * */
        class Artist {
            var id: Long? = null
            var name: String? = null
            override fun toString(): String {
                return "id is $id, name is $name"
            }
        }
    }
}