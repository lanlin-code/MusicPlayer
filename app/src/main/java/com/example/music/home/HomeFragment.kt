package com.example.music.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.music.FragmentChangeListener
import com.example.music.MainActivity
import com.example.music.R
import com.example.music.login.LoginFragment
import com.example.music.showUserStatus.ShowUserStatusFragment
import com.squareup.picasso.Picasso

class HomeFragment: Fragment(), MainActivity.OnLoginSuccessListener {
    private var listener: FragmentChangeListener? = null
    private lateinit var avatar: ImageView
    private var loginSuccess: Boolean = false

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
        avatar = view.findViewById(R.id.avatar_iv)
        avatar.setOnClickListener {
            if (!loginSuccess) {
                listener?.onFragmentChange(LoginFragment())
            } else {
                listener?.onFragmentChange(ShowUserStatusFragment())
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onSuccess(avatarUrl: String, context: Context) {
        loginSuccess = true
        Picasso.with(context).load(avatarUrl).into(avatar)
    }
}