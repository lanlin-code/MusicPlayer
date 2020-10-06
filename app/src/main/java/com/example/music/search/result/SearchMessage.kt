package com.example.music.search.result

class SearchMessage {
    var limit: Int = 15
    var page: Int = 1
    var loading: Boolean = false
    var totalCount: Int = 0
    var totalPage: Int = 0

    fun reset() {
        page = 1
        loading = false
        totalPage = 0
        totalCount = 0
    }
}