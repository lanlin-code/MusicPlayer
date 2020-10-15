package com.example.music.entity

import com.example.respository.bean.HotList

/**
 * 搜索热词
 */

class HotWord(var searchWord: String = ERROR_STRING,
              var score: Long = ERROR_LONG,
              var content: String = ERROR_STRING,
              var iconUrl: String = ERROR_STRING) {



    companion object {
        const val ERROR_STRING = ""
        const val ERROR_LONG = -1L
        fun createHotList(json: MutableList<HotList.Data>?): MutableList<HotWord> {
            val list = mutableListOf<HotWord>()
            json?.let {
                for (d in json) {
                    val h = valueOf(d)
                    if (!h.error()) {
                        list.add(h)
                    }
                }
            }
            return list

        }

        private fun valueOf(data: HotList.Data): HotWord {
            val h = HotWord()
            data.content?.let { h.content = it }
            data.iconUrl?.let { h.iconUrl = it }
            data.score?.let { h.score = it }
            data.searchWord?.let { h.searchWord = it }
            return h
        }
    }

    private fun error() = content == ERROR_STRING ||
            score == ERROR_LONG || searchWord == ERROR_STRING

    override fun toString(): String {
        return "searchWord = $searchWord, content = $content, score = $score"
    }
}