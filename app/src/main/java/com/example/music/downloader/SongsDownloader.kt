package com.example.music.downloader

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.music.MusicApplication
import com.example.music.util.DownLoader
import com.example.music.util.LogUtil

class SongsDownloader(var adapter: DownloadAdapter? = null,
                      var recyclerView: RecyclerView? = null,
                      var textView: TextView? = null) : DownLoader.DownloadListener {

    private val handler = Handler(Looper.getMainLooper())
    private val tag = "SongsDownloader"

    // 更新进度
    override fun onProgress(position: Int, currentPosition: Int, total: Long) {
        handler.post {
            adapter?.holderList?.get(position)?.progressBar?.max = total.toInt()
            adapter?.holderList?.get(position)?.progressBar?.progress = currentPosition
            val current = 1.0*currentPosition/(1024*1024)
            val t = 1.0*total/(1024*1024)
            val s = "${current.toString().substring(0, 4)}M/${t.toString().substring(0, 4)}M"
            LogUtil.debug(tag, s)
            adapter?.holderList?.get(position)?.progressText?.text = s
        }

    }

    // 下载完成后清除相应的视图
    override fun onSuccess(msg: String, position: Int) {
        handler.post {
            Toast.makeText(MusicApplication.context, msg, Toast.LENGTH_SHORT).show()
            adapter?.holderList?.removeAt(position)
            adapter?.notifyItemRemoved(position)
            if (!DownLoader.hasTasks()) {
                noTasks()
            }
        }
    }

    // 没有下载任务时调用
    private fun noTasks() {
        recyclerView?.visibility = View.GONE
        textView?.visibility = View.VISIBLE
    }


    override fun onFail(msg: String) {
        handler.post {
            Toast.makeText(MusicApplication.context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause(position: Int) {
        handler.post {
            adapter?.holderList?.get(position)?.layout?.visibility = View.GONE
            adapter?.holderList?.get(position)?.clickText?.visibility = View.VISIBLE
        }
    }

    override fun callback(msg: String) {

    }

    override fun onDelete(position: Int) {
        handler.post {
            adapter?.notifyItemRemoved(position)
            if (!DownLoader.hasTasks()) {
                noTasks()
            }
        }
    }
}