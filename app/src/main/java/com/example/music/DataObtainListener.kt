package com.example.music

interface DataObtainListener {
    fun obtainUserId(): Long
    fun obtainUserAvatar(): String
    fun obtainUsername(): String
}