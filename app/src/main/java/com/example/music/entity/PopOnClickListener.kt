package com.example.music.entity

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.SongsListener
import com.example.music.adapter.ButtonPopAdapter
import com.example.music.service.MusicPosition
import com.example.music.util.LogUtil
import com.example.music.widget.SongPopupWindow

class PopOnClickListener(var songsListener: SongsListener? = null) : View.OnClickListener {
    var playState: ImageButton? = null // 播放界面的播放模式按钮

    private var adapter: ButtonPopAdapter? = ButtonPopAdapter()
    private var show = false

    override fun onClick(v: View?) {
        v?.let {
            songsListener?.let {
                // 获取播放列表
                val list = songsListener?.obtainData()
                // 获取播放模式
                val currentMode = songsListener?.mode() ?: MusicPosition.order
                list?.let {
                    val contentView = LayoutInflater.from(v.context).inflate(R.layout.cardview_bt_window, null)
                    val popupWindow = SongPopupWindow(v.context, contentView, true)
                    val count = contentView.findViewById<TextView>(R.id.bt_window_list_count) // 显示播放歌曲数目
                    val stringCount = "(${list.size})"
                    count.text = stringCount
                    val imageView = contentView.findViewById<ImageView>(R.id.bt_window_bt_iv) // 显示播放模式图片
                    val textView = contentView.findViewById<TextView>(R.id.bt_window_bt_tv) // 显示播放模式的文本
                    updateButtonLayout(imageView, textView, currentMode)
                    val layout = contentView.findViewById<LinearLayout>(R.id.bt_window_bt_layout)
                    layout.setOnClickListener {
                        val mode = songsListener?.mode()
                        mode?.let {
                            val transferMode = (mode + 1) % MusicPosition.modeSize
                            updateButtonLayout(imageView, textView, transferMode)
                            songsListener?.transferMode(transferMode)
                            updatePlayState(transferMode)
                        }

                    }
                    val song = songsListener?.obtainCurrentPlaying()
                    var position = 0 // 当前播放歌曲的位置
                    if (song != null) {
                        for (i in 0 until list.size) {
                            if (list[i].id == song.id) {
                                position = i
                                break
                            }
                        }
                    }

                    val recyclerView = contentView.findViewById<RecyclerView>(R.id.bt_window_list)
                    val manager = LinearLayoutManager(v.context)
                    recyclerView.layoutManager = manager
                    adapter?.currentPosition = position
                    adapter?.data = list
                    adapter?.count = count
                    adapter?.window = popupWindow
                    adapter?.listener = songsListener
//                    val adapter = ButtonPopAdapter(list, popupWindow, songsListener, count, position)
                    recyclerView.adapter = adapter
                    manager.scrollToPositionWithOffset(position, 0)
                    val clearAll = contentView.findViewById<ImageButton>(R.id.bt_window_clear_all) // 清除所有歌曲
                    clearAll.setOnClickListener {
                        songsListener?.clearData()
                        popupWindow.dismiss()
                    }
                    val window = songsListener?.onObtainWindow()
                    // 设置透明度
                    window?.let {
                        w -> w.attributes.alpha = 0.5f
                        w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    }
                    popupWindow.setOnDismissListener {
                        show = false
                        window?.let {
                            w -> w.attributes.alpha = 1.0f
                            w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        }
                    }
                    popupWindow.showAtLocation(v, Gravity.BOTTOM, 0 , 0)
                    show = true

                    LogUtil.debug("onClick", "show")
                }

            }

        }
    }

    // 更新视图
    fun onChange(position: Int) {
        if (show) {
            adapter?.currentPosition = position
            adapter?.notifyDataSetChanged()
        }
    }

    // 更新播放模式按钮的图片
    private fun updatePlayState(mode: Int) {
        playState?.let {
            when(mode) {
                MusicPosition.order -> it.setImageResource(R.drawable.list_circle)
                MusicPosition.random -> it.setImageResource(R.drawable.random)
                MusicPosition.circle -> it.setImageResource(R.drawable.only_one_circle)
            }
        }
    }

    fun destroy() {
        adapter = null
    }

    // 更新播放模式的视图
    private fun updateButtonLayout(imageView: ImageView, textView: TextView, mode: Int) {
        when(mode) {
            MusicPosition.order -> {
                imageView.setImageResource(R.drawable.list_circle)
                textView.setText(R.string.list_circle)
            }
            MusicPosition.circle -> {
                imageView.setImageResource(R.drawable.only_one_circle)
                textView.setText(R.string.one_circle)
            }
            MusicPosition.random -> {
                imageView.setImageResource(R.drawable.random)
                textView.setText(R.string.random_play)
            }
        }
    }
}