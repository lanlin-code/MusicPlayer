package com.example.music.entity

import android.os.Parcel
import android.os.Parcelable
import com.example.respository.bean.UserPlayListJson

class UserPlaylist(var id: Long = errorId, var name: String = emptyMessage, var imgUrl: String = emptyMessage) {
    companion object {
        private const val errorId: Long = -1
        private const val emptyMessage: String = ""
        fun toPlaylist(list: UserPlayListJson.PlayList?): UserPlaylist {
            val l = UserPlaylist()
            list?.let {
                l.id = it.id ?: errorId
                l.name = it.name ?: emptyMessage
                l.imgUrl = it.coverImgUrl ?: emptyMessage
            }
            return l
        }

        fun isDataError(playlist: UserPlaylist): Boolean = playlist.id == errorId ||
                playlist.imgUrl == emptyMessage || playlist.name == emptyMessage
    }


    override fun toString(): String {
        return "[UserPlaylist id = $id, name = $name, imgUrl = $imgUrl]"
    }


}