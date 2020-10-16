package com.example.music.entity

import android.text.TextUtils

class LrcRow(val content: String?, // 歌词内容
             val strTime: String?, // 歌词时间字符串
             val time: Long // 歌词时间
): Comparable<LrcRow> {

    var timeText: String = "00:00"

    init {
        strTime?.let {
            val s = it.replace(".", "]")
            val a = s.split("]")
            timeText = a[0]
        }
    }

    // 按时间排序
    override fun compareTo(other: LrcRow): Int {
        return (time - other.time).toInt()
    }

    override fun toString(): String {
        return "[$strTime $content]"
    }

    companion object {
        fun createRows(standardLrc: String?) : List<LrcRow>? {
            // 检查数据是否是标准lrc歌词
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

        // 将表示时间的字符串转为Long
        private fun translateTime(timeString: String) : Long {
            val s = timeString.replace(".", ":")
            val arrTime = s.split(":")
            return arrTime[0].toLong()*60*1000 + arrTime[1].toLong()*1000 + arrTime[2].toLong()
        }
    }


}