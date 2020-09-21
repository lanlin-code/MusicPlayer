package com.example.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.music.entity.User
import com.example.music.home.HomeFragment
import com.example.music.login.LoginFragment
import com.example.music.util.LogUtil

class MainActivity : AppCompatActivity(), LoginFragment.LoginCallback, FragmentChangeListener {

    private lateinit var currentFragment: Fragment
    private var user: User = User()
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentFragment = HomeFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment, currentFragment,
            HomeFragment::class.java.simpleName).commit()

    }


    override fun onBackPressed() {
        if (currentFragment !is HomeFragment) {
            val h = getHomeFragment()
            h?.let {
                val t = supportFragmentManager.beginTransaction().hide(currentFragment).show(it)
                t.remove(currentFragment)
                t.commit()
                currentFragment = it
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onLoginSuccess(data: User) {
        user = data
        this.onBackHome()
        LogUtil.debug(TAG, "$user")
    }

    override fun onLoginFail(message: String) {
        toastErrorMessage(message)
    }

    private fun toastErrorMessage(message: String) {
        Toast.makeText(this.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFragmentChange(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(currentFragment).add(R.id.fragment, fragment).commit()
        currentFragment = fragment
    }

    override fun onBackHome() {
        getHomeFragment()?.let {
            val t = supportFragmentManager.beginTransaction().hide(currentFragment).show(it)
            t.remove(currentFragment)
            t.commit()
            currentFragment = it
        }

    }

    private fun getHomeFragment() = supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
}
