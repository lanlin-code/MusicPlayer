package com.example.mylibrary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import okhttp3.Cookie

class CookieDBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val CREATE_TABLE = "create table cookie(name text, value text, domain text, path text, expires text, del Integer)"
        const val name = "cookieDatabase"
        const val version = 1
        private val tag = "CookieDBHelper"
    }

    fun insert(cookie: Cookie) {
        Log.d("main", "insert")
        val writer = this.writableDatabase
        writer.execSQL("insert into cookie(name, value, domain, path, expires, del) values(?, ?, ?, ?, ?, ?)"
            , arrayOf(cookie.name(), cookie.value(), cookie.domain(), cookie.path(), cookie.expiresAt(), 0))
    }

    fun getCookies(): MutableList<Cookie> {
        val reader = this.readableDatabase
        Log.d(tag, "getCookie")
        val list = mutableListOf<Cookie>()
        val c = reader.rawQuery("select * from cookie where del = ?", arrayOf("0"))
        if (c.moveToFirst()) {
            do {
                val name = c.getString(c.getColumnIndex("name"))
                val value = c.getString(c.getColumnIndex("value"))
                val domain = c.getString(c.getColumnIndex("domain"))
                val path = c.getString(c.getColumnIndex("path"))
                val expires = c.getLong(c.getColumnIndex("expires"))
                val cookie = Cookie.Builder().name(name).value(value).expiresAt(expires).domain(domain).path(path).build()
                list.add(cookie)
            } while (c.moveToNext())
        }
        Log.d(tag, "$list")
        c.close()
        return list
    }

    fun delete() {
        this.writableDatabase.execSQL("update cookie set del = ? where del = ?", arrayOf(1, 0))
    }



    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists cookie")
        onCreate(db)
    }
}