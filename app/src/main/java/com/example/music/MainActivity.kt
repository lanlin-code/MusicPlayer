package com.example.music

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.music.autoLogin.AutoLoginFragment
import com.example.music.entity.User
import com.example.music.home.HomeFragment
import com.example.music.login.LoginFragment
import com.example.music.login.LoginModel
import com.example.music.login.LoginPresenter
import com.example.music.util.LogUtil
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), LoginCallback, FragmentChangeListener, AutoLoginCallback, LoadStatusListener {

    private lateinit var currentFragment: Fragment
    private var user: User = User()
    private val TAG: String = "MainActivity"
    private var loginSuccessListener: OnLoginSuccessListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val h = HomeFragment()
        currentFragment = h
        loginSuccessListener = h
        supportFragmentManager.beginTransaction().add(R.id.fragment, currentFragment,
            HomeFragment::class.java.simpleName).commit()
        supportFragmentManager.beginTransaction().hide(currentFragment).commit()
        currentFragment = AutoLoginFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment, currentFragment).commit()


    }

    override fun onDestroy() {
        super.onDestroy()
        loginSuccessListener = null

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
        loginSuccessListener?.onSuccess(user.avatar, this.applicationContext)
        LogUtil.debug(TAG, "$user")
    }

    override fun onLoginFail(message: String) {
        toastErrorMessage(message)
    }

    private fun toastErrorMessage(message: String) {
        Toast.makeText(this.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun setHome() {
        val h = supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
        LogUtil.debug("sethome", "before transaction")
        h?.let {
            supportFragmentManager.beginTransaction().hide(currentFragment).
            show(it).remove(currentFragment).commit()
            currentFragment = h
        }
        LogUtil.debug("setHome", "End")
    }

    /**
     * 碎片跳转时回调
     */
    override fun onFragmentChange(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(currentFragment).add(R.id.fragment, fragment).commit()
        currentFragment = fragment
    }

    /**
     * 移除当前碎片，返回主页
     */
    override fun onBackHome() {
        getHomeFragment()?.let {
            val t = supportFragmentManager.beginTransaction().hide(currentFragment).show(it)
            t.remove(currentFragment)
            t.commit()
            currentFragment = it
        }

    }

    /**
     * 得到主页碎片
     */
    private fun getHomeFragment() = supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

    interface OnLoginSuccessListener {
        fun onSuccess(avatarUrl: String, context: Context)
    }

    /**
     * 自动登录成功回调
     */
    override fun onAutoLoginSuccess(user: User) {
        this.user = user
        Log.d(TAG, "$user")
        setHome()
        loginSuccessListener?.onSuccess(user.avatar, this.applicationContext)
    }

    /**
     *  自动登录失败回调
     */
    override fun onAutoLoginFail(message: String) {
        toastErrorMessage(message)
       setHome()
    }

    /**
     * 显示用户信息的fragment调用，加载用户头像和名称
     */
    override fun onLoadStatus(imageView: ImageView, textView: TextView) {
        textView.text = user.nickname
        val width = 200
        val height = 200
        Picasso.with(this.applicationContext).load(user.avatar).resize(width, height).into(imageView)
    }
}
