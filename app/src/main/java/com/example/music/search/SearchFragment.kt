package com.example.music.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.music.BaseFragment
import com.example.music.R
import com.example.music.search.result.SearchResultFragment

class SearchFragment: BaseFragment() {

    private val defaultWordShow = DefaultWordShow()
    private val defaultSearchPresenter = DefaultSearchPresenter()

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
        defaultSearchPresenter.sListener = defaultWordShow
        defaultSearchModel.defaultWord(defaultSearchPresenter)


    }

    override fun onDetach() {
        defaultWordShow.editText = null
        defaultSearchPresenter.sListener = null
        super.onDetach()
    }
}