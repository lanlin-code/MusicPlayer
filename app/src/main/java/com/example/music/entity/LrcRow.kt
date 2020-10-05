package com.example.music.entity

import android.text.TextUtils

class LrcRow(val content: String?, val strTime: String?, val time: Long): Comparable<LrcRow> {

    var timeText: String = "00:00"

    init {
        strTime?.let {
            val s = it.replace(".", "]")
            val a = s.split("]")
            timeText = a[0]
        }
    }

    override fun compareTo(other: LrcRow): Int {
        return (time - other.time).toInt()
    }

    override fun toString(): String {
        return "[$strTime $content]"
    }

    companion object {
        fun createRows(standardLrc: String?) : List<LrcRow>? {
            if (standardLrc == null || standardLrc.isEmpty() ||
                standardLrc.indexOf("[") != 0 || (standardLrc.indexOf("]") != 9
                        && standardLrc.indexOf("]") != 10)) {
                return null
            }
            val lastRight = standardLrc.indexOf("]")
            val content = standardLrc.substring(lastRight + 1)
            val times = standardLrc.substring(0, lastRight + 1).
            replace("]", "-").replace("[", "-")
            val arrTime = times.split("-")
//            val list = mutableListOf<LrcRow>()
            val list = arrayListOf<LrcRow>()
            for (s in arrTime) {
                if (s.trim().isEmpty()) {
                    continue
                }
                val row = LrcRow(content, s, translateTime(s))
                list.add(row)
            }
            return list

        }

        private fun translateTime(timeString: String) : Long {
            val s = timeString.replace(".", ":")
            val arrTime = s.split(":")
            return arrTime[0].toLong()*60*1000 + arrTime[1].toLong()*1000 + arrTime[2].toLong()
        }
    }


}