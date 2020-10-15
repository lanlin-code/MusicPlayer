package com.example.music.search.result

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.BaseFragment
import com.example.music.R
import com.example.music.SongsListener

class SearchResultFragment(var searchText: String = "", val hint: String = "") : BaseFragment() {

    private var songsListener: SongsListener? = null
    private val message = SearchMessage()
    private val presenter = ResultPresenter(searchMessage = message)
    private val model = ResultModel()
    private var sListener = ResultShow(message = message)
    private lateinit var editText: EditText




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
        editText = view.findViewById(R.id.result_text)
        editText.hint = hint
        if (searchText.isNotEmpty()) {
            editText.setText(searchText, TextView.BufferType.EDITABLE)
        } else {
            editText.setText(hint, TextView.BufferType.EDITABLE)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.result_list)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = ResultAdapter(listener = songsListener)

        sListener.songsListener = songsListener
        presenter.sListener = sListener
        presenter.listener = songsListener
        sListener.recyclerView = recyclerView
        val textView = view.findViewById<TextView>(R.id.result_loading_text)
        sListener.textView = textView
        val button = view.findViewById<Button>(R.id.result_load_more)
        sListener.resultButton = button
        button.setOnClickListener {
            if (message.page <= message.totalCount && !message.loading) {
                button.text = "正在加载..."
                button.isClickable = false
                load()
            }
        }

        val search = view.findViewById<ImageButton>(R.id.result_search)
        search.setOnClickListener {
            if (!message.loading) {
                message.reset()
                recyclerView.visibility = View.GONE
                if (recyclerView.adapter is ResultAdapter) {
                    (recyclerView.adapter as ResultAdapter).data.clear()
                }
                button.visibility = View.GONE
                textView.visibility = View.VISIBLE
                load()
            }
        }
        load()

    }

    private fun load() {
        message.loading = false
        val text = editText.text.toString()
        val type = 1
        if (text.isNotEmpty()) {
            model.loadSearchResult(text, message.limit, message.page, type, presenter)
        } else {
            model.loadSearchResult(editText.hint.toString(), message.limit, message.page, type, presenter)
        }
    }

    override fun onDetach() {
        super.onDetach()
        songsListener = null
        sListener.recyclerView = null
        sListener.resultButton = null
        sListener.textView = null
        sListener.songsListener = null
        presenter.listener = null
        presenter.sListener = null
    }


}