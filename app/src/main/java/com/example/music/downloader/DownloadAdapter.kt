package com.example.music.downloader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.entity.Song
import com.example.music.util.DownLoader

class DownloadAdapter : RecyclerView.Adapter<DownloadAdapter.DownloadHolder>() {
    // 获取下载列表的数据
    private var data: MutableList<Song>? = DownLoader.getDownloadData()
    val holderList = mutableListOf<DownloadHolder>()


    class DownloadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.download_item_name) // 歌曲名
        val layout: LinearLayout = itemView.findViewById(R.id.download_progress_layout)
        val clickText: TextView = itemView.findViewById(R.id.download_click) // 暂停下载时显示
        val progressText: TextView = itemView.findViewById(R.id.download_progress_text) // 下载进度
        val progressBar: ProgressBar = itemView.findViewById(R.id.download_progress) // 进度条
        val deleteButton: ImageButton = itemView.findViewById(R.id.download_delete) // 清除下载任务
    }

    fun clear() {
        data = null
        holderList.clear()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.relativelayout_download_list_item,
            parent, false)
        return DownloadHolder(view)
    }

    override fun onBindViewHolder(holder: DownloadHolder, position: Int) {
        holderList.add(position, holder)
        val item = data?.get(position)
        item?.let {
            holder.name.text = item.name
        }

        // 如果视图显示的下载任务已开始，则隐藏clickText,显示layout
        if (DownLoader.taskIsStarted(position)) {
            holder.clickText.visibility = View.GONE
            holder.layout.visibility = View.VISIBLE
        } else {
            holder.clickText.visibility = View.VISIBLE
            holder.layout.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (DownLoader.taskIsStarted(position)) {
                holder.layout.visibility = View.GONE
                holder.clickText.visibility = View.VISIBLE
                DownLoader.pauseTask(position)
            } else {
                holder.layout.visibility = View.VISIBLE
                holder.clickText.visibility = View.GONE
                DownLoader.restartTask(position)
            }
        }

        holder.deleteButton.setOnClickListener {
            DownLoader.deleteTask(position)
        }

    }


    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

}