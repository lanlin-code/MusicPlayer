package com.example.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.music.autoLogin.AutoLoginFragment
import com.example.music.entity.Song
import com.example.music.entity.User
import com.example.music.entity.UserPlaylist
import com.example.music.home.HomeFragment
import com.example.music.login.LoginFragment
import com.example.music.login.LoginModel
import com.example.music.login.LoginPresenter
import com.example.music.play.SongPlayFragment
import com.example.music.search.SearchFragment
import com.example.music.search.result.SearchResultFragment
import com.example.music.service.MyService
import com.example.music.util.LogUtil
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), LoginCallback, FragmentChangeListener, AutoLoginCallback,
    LoadStatusListener, LoadPlaylistListener, DataObtainListener, SongsListener {

    private lateinit var currentFragment: Fragment
    private var user: User = User()
    private val TAG: String = "MainActivity"
    private var loginSuccessListener: OnLoginSuccessListener? = null
    private var userPlaylists: MutableList<UserPlaylist> = mutableListOf()
    private var player: IMusicPlayer? = null
    private var connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            player = IMusicPlayer.Stub.asInterface(service)
            player?.registerCallback(musicCallback)
        }

    }
    private val handler = Handler(Looper.getMainLooper())

    private var musicCallback: IMusicCallback? = object : IMusicCallback.Stub() {
        override fun getCurrentSong(imgUrl: String?, name: String?) {
            handler.post {
                playBar.visibility = View.VISIBLE
                imgUrl?.let {
                    if (it.isNotEmpty()) {
                        Picasso.with(this@MainActivity).load(imgUrl)
                            .placeholder(R.drawable.place_holder).error(R.drawable.place_holder).into(imageView)
                    }
                }
                textView.text = name
                playStatus.setImageResource(R.drawable.play)

            }
        }

        override fun playCallback(position: Int) {

        }

        override fun obtainLrc(sid: Long) {

        }


    }

    private lateinit var playBar: RelativeLayout
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var playStatus: ImageButton


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
        initPlayBar()
        startService()
    }

    private fun initPlayBar() {
        playBar = findViewById(R.id.play_bar)
        imageView = findViewById(R.id.play_bar_img)
        textView = findViewById(R.id.play_bar_song_name)
        playStatus = findViewById(R.id.play_state)
        playBar.setOnClickListener { onFragmentChange(SongPlayFragment(player)) }
        playStatus.setOnClickListener {
            player?.let {
                if (it.isPlaying) {
                    it.parse()
                    playStatus.setImageResource(R.drawable.parse_48)
                } else {
                    it.restart()
                    playStatus.setImageResource(R.drawable.play)
                }
            }
        }
    }

    private fun startService() {
        val intent = Intent(this, MyService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        loginSuccessListener = null
        musicCallback = null
        player?.clearAllCallback()
    }

    override fun onBackPressed() {
        if (currentFragment !is HomeFragment) {
            val h = getHomeFragment()
            h?.let {
                val t = supportFragmentManager.beginTransaction().hide(currentFragment).show(it)
                showPlayBar()
                t.remove(currentFragment)
                t.commit()
                currentFragment = it
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun showPlayBar() {
        player?.let {
            if (it.showBar()) {
                if (it.isPlaying) {
                    playStatus.setImageResource(R.drawable.play)
                } else {
                    playStatus.setImageResource(R.drawable.parse_48)
                }
                playBar.visibility = View.VISIBLE
            } else {
                playBar.visibility = View.GONE
            }
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
        h?.let {
            supportFragmentManager.beginTransaction().hide(currentFragment).
            show(it).remove(currentFragment).commit()
            currentFragment = h
        }
    }

    /**
     * 碎片跳转时回调
     */
    override fun onFragmentChange(fragment: Fragment) {
        if (fragment is SongPlayFragment) {
            playBar.visibility = View.GONE
        }
        supportFragmentManager.beginTransaction().hide(currentFragment).add(R.id.fragment, fragment).commit()
        if (currentFragment is SearchResultFragment) {
            supportFragmentManager.beginTransaction().remove(currentFragment).commit()
        }
        currentFragment = fragment
    }

    /**
     * 移除当前碎片，返回主页
     */
    override fun onBackHome() {
        if (currentFragment is SearchResultFragment) {
            onFragmentChange(SearchFragment())
            return
        }
        getHomeFragment()?.let {
            val t = supportFragmentManager.beginTransaction().hide(currentFragment).show(it)
            showPlayBar()
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

    override fun onLoadPlaylistSuccess(playlists: MutableList<UserPlaylist>) {
        userPlaylists = playlists
    }


    override fun onLoadPlaylistFail(message: String) {
        toastErrorMessage(message)
    }

    override fun obtainUserId(): Long {
        return user.userId
    }

    override fun obtainUserAvatar(): String {
        return user.avatar
    }

    override fun obtainUsername(): String {
        return user.nickname
    }

    override fun onFail(msg: String) {
        toastErrorMessage(msg)
    }

    override fun transmitData(songs: MutableList<Song>) {
        player?.clear()
        playBar.visibility = View.VISIBLE
        playStatus.setImageResource(R.drawable.play)
        player?.receive(songs)
    }

    override fun playFrom(position: Int) {
        player?.playFrom(position)
    }

    override fun clearData() {
        player?.clear()
        playBar.visibility = View.GONE
    }

    override fun seekTo(position: Int) {
        player?.seekTo(position)
    }

    override fun addToNext(song: Song) {
        player?.addSong(song)
    }

    override fun addAndPlay(song: Song) {
        player?.addAndPlay(song)
    }
}
