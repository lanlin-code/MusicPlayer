package com.example.music.search.result

import android.app.Activity
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
import com.example.music.SongsListener

class SearchResultFragment(var searchText: String = "", val hint: String = "") : BaseFragment() {

    private var songsListener: SongsListener? = null
    private val limit = 15
    private var page = 1
    private var loading = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SongsListener) {
            songsListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val back = view.findViewById<ImageButton>(R.id.result_back)
        back.setOnClickListener { fragmentChangeListener?.onBackHome() }
        val editText = view.findViewById<EditText>(R.id.result_text)
        editText.hint = hint
        if (searchText.isNotEmpty()) {
            editText.setText(searchText, TextView.BufferType.EDITABLE)
        } else {
            editText.setText(hint, TextView.BufferType.EDITABLE)
        }

        val search = view.findViewById<ImageButton>(R.id.result_search)
        search.setOnClickListener {  }

    }

    override fun onDetach() {
        super.onDetach()
        songsListener = null
    }
}