package com.example.music.entity

import com.example.respository.bean.SearchDefaultJson

class DefaultSearchWord(var real: String = "", var show: String = "") {

    companion object {
        fun valueOf(json: SearchDefaultJson): DefaultSearchWord {
            val word = DefaultSearchWord()
            json.data?.let {
                it.realkeyword?.let {
                    r -> word.real = r
                }
                it.showKeyword?.let {
                    s -> word.show = s
                }
            }
            return word
        }
    }

    override fun toString(): String {
        return "real = $real, show = $show"
    }
}