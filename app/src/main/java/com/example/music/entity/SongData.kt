package com.example.music.entity

import com.example.respository.bean.SongPlayJson

class SongData(var id: Long = errorLong,
               var url: String = "",
               var size: Long = errorLong,
               var type: String = errorMessage,
               var br: Long = errorLong) {


    companion object {
        const val errorMessage = ""
        const val errorLong = -1L

        fun valueOfList(json: SongPlayJson): MutableList<SongData> {
            val l = json.data
            val r = mutableListOf<SongData>()
            l?.let {
                for (d in l) {
                    val songData = valueOf(d)
                    if (songData.error()) {
                        continue
                    }
                    r.add(songData)
                }
            }
            return r
        }

        private fun valueOf(data: SongPlayJson.Data): SongData {
            val songData = SongData()
            data.url?.let { songData.url = it }
            data.id?.let { songData.id = it }
            data.br?.let { songData.br = it }
            data.size?.let { songData.size = it }
            data.type?.let { songData.type = it }
            return songData
        }
    }

    fun error() = id == errorLong || url == errorMessage

    override fun toString(): String {
        return "id = $id, url = $url"
    }
}