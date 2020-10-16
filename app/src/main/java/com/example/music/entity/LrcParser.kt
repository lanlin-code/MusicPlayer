package com.example.music.entity

import com.example.music.Parser
import com.example.music.util.LogUtil
import java.io.BufferedReader
import java.io.IOException
import java.io.StringReader
import java.util.*

/**
 * 歌词解析类
 */

class LrcParser : Parser<LrcRow> {
    override fun parse(resource: String): MutableList<LrcRow>? {
        if(resource.isEmpty()) {
            return null
        }
        val reader = StringReader(resource)
        val bufferReader = BufferedReader(reader)
        val list = mutableListOf<LrcRow>()
        bufferReader.forEachLine {
            val rows = LrcRow.createRows(it)
            if (rows != null) {
                list.addAll(rows)

            }
        }
        reader.close()
        if (list.size > 0) {
            list.sort()
        }
        return list

    }


}