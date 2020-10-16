package com.example.music.search.result

class SearchMessage {
    var limit: Int = 15 // 每次查询返回的歌曲数目
    var page: Int = 1 // 当前页数
    var loading: Boolean = false // 加载标志
    var totalCount: Int = 0 // 歌曲总数
    var totalPage: Int = 0 // 总页数

    // 重置
    fun reset() {
        page = 1
        loading = false
        totalPage = 0
        totalCount = 0
    }
}