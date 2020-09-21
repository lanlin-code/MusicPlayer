package com.example.respository

interface RequestCallBack<T>{
    fun callback(data : T)
    
    fun error(errorMsg : String)
}