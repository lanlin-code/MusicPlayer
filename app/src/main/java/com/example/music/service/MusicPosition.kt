package com.example.music.service

import com.example.music.util.LogUtil

class MusicPosition {
    companion object {
        const val order = 0 // 顺序播放
        const val random = 1 // 随机播放
        const val circle = 2 // 循环播放
        const val modeSize = 3
        const val tag = "MusicPosition"
    }

    var mode = order
    var currentPosition: Int = 0
    var size: Int = 0


    fun nextPosition() {
        when(mode) {
            order -> getOrderNextPosition()
            random -> getRandomPosition()
        }
    }

    fun lastPosition() {
        when(mode) {
            order -> getOrderLastPosition()
            random -> getRandomPosition()
        }
    }

    private fun getOrderNextPosition() {
        currentPosition ++
        if (currentPosition >= size) {
            currentPosition = 0
        }
        LogUtil.debug(tag, "currentPosition = $currentPosition, size = $size")
    }

    private fun getOrderLastPosition() {
        currentPosition --
        if (currentPosition < 0) {
            currentPosition = size - 1
        }
    }

    private fun getRandomPosition() {
        currentPosition = (Math.random()*size).toInt()
    }
}