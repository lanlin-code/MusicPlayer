package com.example.music

import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {
    protected var fragmentChangeListener: FragmentChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            fragmentChangeListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentChangeListener = null
    }
}