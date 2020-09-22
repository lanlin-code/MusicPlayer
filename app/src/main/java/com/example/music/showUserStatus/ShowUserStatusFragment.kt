package com.example.music.showUserStatus

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.music.BaseFragment
import com.example.music.LoadStatusListener
import com.example.music.R

class ShowUserStatusFragment: BaseFragment() {

    private var listener: LoadStatusListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoadStatusListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_user_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.status_avatar)
        val textView = view.findViewById<TextView>(R.id.user_name)
        val back = view.findViewById<ImageButton>(R.id.status_back)
        back.setOnClickListener { fragmentChangeListener?.onBackHome() }
        val logout = view.findViewById<Button>(R.id.logout)
        listener?.onLoadStatus(imageView, textView)
    }
}