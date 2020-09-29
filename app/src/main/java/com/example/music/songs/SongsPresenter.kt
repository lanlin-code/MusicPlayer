package com.example.music.songs

import com.example.music.ResponseCallback
import com.example.music.entity.Song
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.SongDetailJson
import com.example.respository.bean.SongIdsJson
import java.lang.StringBuilder

class SongsPresenter {
    var idListener: ResponseCallback<String>? = null
    var listener: ResponseCallback<MutableList<Song>>? = null
    val idPresenter = SongsIdPresenter()
    val songPresenter = SongDetailPresenter()


    inner class SongsIdPresenter: RequestCallBack<SongIdsJson> {
        var ids: String = ""
        override fun callback(data: SongIdsJson) {
            data.playlist?.trackIds?.let {
                val list = mutableListOf<Long>()
                for (t in it) {
                    t.id?.let { it1 -> list.add(it1) }
                }
                buildIds(list)
                if (ids.isNotEmpty()) {
                    idListener?.onSuccess(ids)
                }
            }

        }

        private fun buildIds(trackIds: MutableList<Long>) {
            val builder = StringBuilder()
            for (i in 0 until trackIds.size) {
                builder.append(trackIds[i])
                if (i != trackIds.size - 1) {
                    builder.append(",")
                }
            }
            ids = builder.toString()
            LogUtil.debug("SongPresenter", ids)
         }

        override fun error(errorMsg: String) {
            listener?.onError(errorMsg)
        }

    }


    inner class SongDetailPresenter: RequestCallBack<SongDetailJson> {
        override fun callback(data: SongDetailJson) {
            LogUtil.debug("SongDetailPresenter", "callback")
            listener?.onSuccess(Song.valueOfList(data))
        }

        override fun error(errorMsg: String) {
            listener?.onError(errorMsg)
        }

    }
}