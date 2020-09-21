package com.example.music.util

import android.util.Log

class LogUtil {
    companion object {
        var now: Int = 2
        private const val verbose: Int = 1
        private const val debug: Int = 2
        private const val info: Int = 3
        private const val warn: Int = 4
        private const val error: Int = 5

        fun verbose(tag: String, message: String) {
            if (now >= verbose) {
                Log.v(tag, message)
            }
        }

        fun debug(tag: String, message: String) {
            if (now >= debug) {
                Log.d(tag, message)
            }
        }

        fun info(tag: String, message: String) {
            if (now >= info) {
                Log.i(tag, message)
            }
        }

        fun warn(tag: String, message: String) {
            if (now >= warn) {
                Log.w(tag, message)
            }
        }

        fun error(tag: String, message: String) {
            if (now >= error) {
                Log.e(tag, message)
            }
        }
    }
}