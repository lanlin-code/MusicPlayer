package com.example.mylibrary

import android.content.Context
import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

object RetrofitUtil {
    private var loginRetrofit: Retrofit? = null
    private val tag: String = "RetrofitUtil"
    private val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).client(OkHttpClient()).
        baseUrl("http://47.102.203.97:3000/").build()

    fun init(context: Context) {
        val okHttpClient = OkHttpClient.Builder().connectTimeout(20000, TimeUnit.MILLISECONDS)
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                    val helper = CookieDBHelper(context.applicationContext, CookieDBHelper.name,
                        null, CookieDBHelper.version)
                    helper.delete()
                    for (c in cookies) {
                        helper.insert(c)
                    }
                }

                override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                    val helper = CookieDBHelper(context.applicationContext, CookieDBHelper.name,
                        null, CookieDBHelper.version)
                    return helper.getCookies()
                }

            }).build()

        loginRetrofit = Retrofit.Builder().baseUrl("https://netease-api.aliyun.topviewclub.cn/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    fun getLoginRetrofit(): Retrofit? {
        if (loginRetrofit == null) {
            throw IllegalArgumentException("retrofit has not init")
        }
        Log.d(tag, "getLoginRetrofit")
        return loginRetrofit
    }

    fun getRetrofit() = retrofit
}