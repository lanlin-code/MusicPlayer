package com.example.music

interface Parser<T> {

    fun parse(resource: String) : List<T>?

}