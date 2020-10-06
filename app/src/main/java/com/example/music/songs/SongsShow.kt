package com.example.music.songs

import android.widget.LinearLayout
import com.example.music.ShowDataListener
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.util.PinYinUtil
import com.example.music.widget.OuterRecyclerview

class SongsShow(var outerRecyclerview: OuterRecyclerview? = null,
                var listener: SongsListener? = null,
                var playAll: LinearLayout? = null): ShowDataListener<MutableList<Song>> {

    override fun show(data: MutableList<Song>) {
        val map = getData(data)
        val t = mutableListOf<String>()
        t.addAll(map.keys)
        t.sort()
        outerRecyclerview?.let {
            it.title = t
            if (it.adapter is OuterAdapter) {
                (it.adapter as OuterAdapter).listener = null
            }
            it.adapter = OuterAdapter(map, listener, t)
            it.adapter?.notifyDataSetChanged()
        }
        playAll?.setOnClickListener {
            val d = getSongs(map, t)
            listener?.transmitData(d)
            listener?.playFrom(0)
        }
    }

    private fun getSongs(map: MutableMap<String, MutableList<Song>>, title: MutableList<String>): MutableList<Song> {
        val songs = mutableListOf<Song>()
        for (t in title) {
            val s = map[t]
            s?.let {
                songs.addAll(s)
            }
        }
        return songs
    }

    private fun getData(songs: MutableList<Song>): MutableMap<String, MutableList<Song>> {
        val map = mutableMapOf<String, MutableList<Song>>()
        for (s in songs) {
            val f = s.name?.let { PinYinUtil.getPinYinHeader(it) }
            f?.let {
                if (map.containsKey(f)) {
                    val list: MutableList<Song>? = map[f]
                    if (list != null) {
                        list.add(s)
                    } else {
                        val l = mutableListOf<Song>()
                        l.add(s)
                        map[f] = l
                    }
                } else {
                    val l = mutableListOf<Song>()
                    l.add(s)
                    map[f] = l
                }
            }
        }
        return map
    }


}