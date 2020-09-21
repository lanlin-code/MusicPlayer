package com.example.music

interface ResponseCallback<T> {
    fun onSuccess(data: T)
    fun onError(message: String)
}