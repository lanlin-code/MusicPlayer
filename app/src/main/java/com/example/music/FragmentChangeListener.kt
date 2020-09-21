package com.example.music

import androidx.fragment.app.Fragment

interface FragmentChangeListener {
    fun onFragmentChange(fragment: Fragment)
    fun onBackHome()
}