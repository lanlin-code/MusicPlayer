package com.example.music.entity

import com.example.music.Parser
import java.io.BufferedReader
import java.io.IOException
import java.io.StringReader
import java.util.*

class LrcParser : Parser<LrcRow> {
    override fun parse(resource: String): List<LrcRow>? {
        if(resource.isEmpty()) {
            return null
        }
        val reader = StringReader(resource)
        val bufferReader = BufferedReader(reader)
        val list = arrayListOf<LrcRow>()
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