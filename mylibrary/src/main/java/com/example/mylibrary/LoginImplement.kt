package com.example.mylibrary

import android.util.Log
import com.example.mylibrary.api.LoginAPI
import com.example.respository.bean.UserJson
import com.example.respository.RequestCallBack
import com.example.respository.api.ClientLoginApi
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.IllegalArgumentException

class LoginImplement : ClientLoginApi {
    private val loginAPI: LoginAPI? = RetrofitUtil.getLoginRetrofit()?.create(LoginAPI::class.java)
    private val tag: String = "LoginImplement"

    override fun login(username: String, password: String, callback: RequestCallBack<UserJson>) {
        check()
        val o = loginAPI?.login(username, password)
        getData(o, callback)
    }

    override fun refreshLogin() {
        val o = loginAPI?.refreshLogin()
        o?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { Log.d(tag, "refresh finish") }
    }

    private fun getData(o: Observable<UserJson>?, callback: RequestCallBack<UserJson>) {
        if (o != null) {
            Log.d(tag, "getData")
            o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(tag, "$it")
                    callback.callback(it)
                },
                    {
                        it.message?.let { it1 -> callback.error(it1) }
                    })
        } else {
            callback.error("Fail to request, please check whether username or password is null")
        }
    }

    override fun getLoginStatus(callback: RequestCallBack<UserJson>) {
        check()
        val o = loginAPI?.getLoginStatus()
        getData(o, callback)
    }

    override fun logout() {
        val o = loginAPI?.logout()
        o?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe { Log.d(tag, "logout finish") }
    }

    private fun check() {
        if (loginAPI == null) {
            throw IllegalArgumentException("LoginApi is null")
        }
    }
}