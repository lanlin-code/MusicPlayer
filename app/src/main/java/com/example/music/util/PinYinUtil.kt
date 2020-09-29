package com.example.music.util

import net.sourceforge.pinyin4j.PinyinHelper

class PinYinUtil {

    companion object {
        private val builder = StringBuilder()
        fun getPinYinHeader(source: String): String? {
            builder.setLength(0)
            val c = source[0]
            val p = PinyinHelper.toHanyuPinyinStringArray(c)
            if (p != null) {
                builder.append(p[0][0])
            } else {
                builder.append(c)
            }
            return builder.toString()
        }
    }


}