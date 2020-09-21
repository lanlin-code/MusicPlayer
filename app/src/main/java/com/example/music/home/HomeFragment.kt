package com.example.music.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.music.FragmentChangeListener
import com.example.music.R
import com.example.music.login.LoginFragment

class HomeFragment: Fragment() {
    private var listener: FragmentChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val avatar = view.findViewById<ImageView>(R.id.avatar_iv)
        avatar.setOnClickListener {
            listener?.onFragmentChange(LoginFragment())
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}