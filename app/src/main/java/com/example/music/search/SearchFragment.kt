package com.example.music.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.BaseFragment
import com.example.music.R
import com.example.music.search.result.SearchResultFragment

class SearchFragment: BaseFragment() {

    private val defaultWordShow = DefaultWordShow()
    private val defaultSearchPresenter = DefaultSearchPresenter()
    private lateinit var recyclerView: RecyclerView
    private val hotWordShow = HotWordShow()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back = view.findViewById<ImageButton>(R.id.search_back)
        val editText = view.findViewById<EditText>(R.id.search_text)
        val search = view.findViewById<ImageButton>(R.id.search_bt)
        recyclerView = view.findViewById(R.id.search_hot_list)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val adapter = HotWordAdapter()
        adapter.listener = fragmentChangeListener
        recyclerView.adapter = adapter
        back.setOnClickListener { fragmentChangeListener?.onBackHome() }
        search.setOnClickListener {
            val text = editText.text.toString()
            val hint = editText.hint.toString()
            if (text.isNotEmpty() || hint.isNotEmpty()) {
                val fragment = SearchResultFragment(text, hint)
                fragmentChangeListener?.onFragmentChange(fragment)
            }
        }
        val defaultSearchModel = DefaultSearchModel()
        defaultWordShow.editText = editText
        defaultWordShow.recyclerView = recyclerView
        defaultSearchPresenter.sListener = defaultWordShow
        defaultSearchModel.defaultWord(defaultSearchPresenter)
        hotWordShow.recyclerView = recyclerView
        val hotWordModel = HotWordModel()
        val hotWordPresenter = HotWordPresenter()
        hotWordPresenter.sListener = hotWordShow
        hotWordModel.getHotList(hotWordPresenter)




    }

    override fun onDetach() {
        super.onDetach()
        defaultWordShow.editText = null
        defaultSearchPresenter.sListener = null
        defaultWordShow.recyclerView = null
        if (recyclerView.adapter is HotWordAdapter) {
            (recyclerView.adapter as HotWordAdapter).listener = null
        }


    }
}