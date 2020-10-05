package com.example.music.search

import android.widget.EditText
import com.example.music.ShowDataListener
import com.example.music.entity.DefaultSearchWord
import com.example.music.util.LogUtil

class DefaultWordShow(var editText: EditText? = null): ShowDataListener<DefaultSearchWord> {
    override fun show(data: DefaultSearchWord) {
        LogUtil.debug("show", data.toString())
        editText?.hint = data.real
    }
}