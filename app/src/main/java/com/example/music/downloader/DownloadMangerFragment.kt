package com.example.music.downloader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.BaseFragment
import com.example.music.R
import com.example.music.util.DownLoader

class DownloadMangerFragment : BaseFragment() {

    private val downloader = SongsDownloader()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_download_manager, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back = view.findViewById<ImageButton>(R.id.download_back)
        back.setOnClickListener { fragmentChangeListener?.onBackHome() }
        val textView = view.findViewById<TextView>(R.id.download_place_holder)
        val recyclerView = view.findViewById<RecyclerView>(R.id.download_list)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val adapter = DownloadAdapter()
        recyclerView.adapter = adapter
        if (DownLoader.hasTasks()) {
            textView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        downloader.textView = textView
        downloader.recyclerView = recyclerView
        downloader.adapter = adapter
        DownLoader.listener = downloader
    }

    override fun onDetach() {
        super.onDetach()
        downloader.adapter?.clear()
        downloader.adapter = null
        downloader.recyclerView = null
        downloader.textView = null
        DownLoader.listener = null

    }
}