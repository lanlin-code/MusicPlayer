package com.example.music.search.result

import com.example.music.ShowDataListener
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.songs.SongsModel
import com.example.music.util.LogUtil
import com.example.respository.RequestCallBack
import com.example.respository.bean.SearchSongJson
import com.example.respository.bean.SongDetailJson
import java.lang.StringBuilder

class ResultPresenter(private val searchMessage: SearchMessage) : RequestCallBack<SearchSongJson> {
    var sListener: ShowDataListener<MutableList<Song>>? = null
    var listener: SongsListener? = null
    private val presenter = ResultSongPresenter()
    private val model = SongsModel()
    private val tag = "ResultPresenter"
    override fun callback(data: SearchSongJson) {
        data.result?.let {
            it.songCount?.let {
                c ->
                if (c == 0) {
                    error("再怎么找也找不到了")
                } else {
                    searchMessage.page ++
                    searchMessage.loading = false
                    searchMessage.totalCount = c
                    searchMessage.totalPage = searchMessage.totalCount / searchMessage.limit
                }
            }
        }

        model.getSongs(buildIds(data), presenter)
//        sListener?.show(Song.valueOfResult(data))
    }

    private fun buildIds(json: SearchSongJson): String {
        val builder = StringBuilder()
        json.result?.songs?.let {
            for(s in 0 until it.size) {
                val i = it[s].id
                if (i != null) {
                    builder.append(i)
                    if (s != it.size - 1) {
                        builder.append(",")
                    }
                }
            }
        }
        return builder.toString()
    }

    override fun error(errorMsg: String) {
        listener?.onFail(errorMsg)
    }

    inner class ResultSongPresenter: RequestCallBack<SongDetailJson> {

        override fun error(errorMsg: String) {
            listener?.onFail(errorMsg)
        }

        override fun callback(data: SongDetailJson) {
            sListener?.show(Song.valueOfList(data))
        }

    }
}