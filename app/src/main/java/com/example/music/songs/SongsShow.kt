package com.example.music.songs

import com.example.music.FragmentChangeListener
import com.example.music.ShowDataListener
import com.example.music.SongsListener
import com.example.music.entity.Song
import com.example.music.util.LogUtil
import com.example.music.util.PinYinUtil
import com.example.music.widget.OuterRecyclerview

class SongsShow(var outerRecyclerview: OuterRecyclerview? = null,
                var listener: SongsListener? = null): ShowDataListener<MutableList<Song>> {

    override fun show(data: MutableList<Song>) {
        outerRecyclerview?.let {
            val map = getData(data)

            val t = mutableListOf<String>()
            t.addAll(map.keys)
            t.sort()
            it.adapter = OuterAdapter(map, listener, t)
            it.adapter?.notifyDataSetChanged()
        }
    }

    private fun getData(songs: MutableList<Song>): MutableMap<String, MutableList<Song>> {
        val map = mutableMapOf<String, MutableList<Song>>()
        for (s in songs) {
            val f = PinYinUtil.getPinYinHeader(s.name)
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