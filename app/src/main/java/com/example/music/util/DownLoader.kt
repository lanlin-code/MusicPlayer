package com.example.music.util

import android.util.Log
import android.widget.Toast
import com.example.music.MusicApplication
import com.example.music.ResponseCallback
import com.example.music.entity.Song
import com.example.music.entity.SongData
import com.example.music.song.SongModel
import com.example.music.song.SongPresenter
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.Exception
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

object DownLoader {
    private val list = Collections.synchronizedList(mutableListOf<Song>()) // 下载任务列表
    private val status = Collections.synchronizedList(mutableListOf<Int>()) // 下载状态
    private val client = OkHttpClient()
    private const val MAX_SIZE = 8 // 下载任务列表最大数量
    var cacheDir: File? = null // 缓存路径
    var listener: DownloadListener? = null
    private val cacheList = mutableListOf<Song>() // 缓冲区
    private val error = "网络不佳"
    private val start = 1 // 正在下载
    private val pause = 2 //  暂停下载
    private val delete = 3 // 删除下载任务
    private val tag = "DownLoader"
    private val model = SongModel()
    private val presenter = SongPresenter()
    val failString = "添加下载任务失败"

    init {
        presenter.listener = object : ResponseCallback<MutableList<SongData>> {
            override fun onSuccess(data: MutableList<SongData>) {
                for (d in data) {
                    if (d.error()) {
                        Toast.makeText(MusicApplication.context, failString, Toast.LENGTH_SHORT).show()
                        break
                    }
                    for (s in cacheList) {
                        if (s.id == d.id) {
                            s.url = d.url
                            cacheList.remove(s)
                            addTask(s)
                            break
                        }
                    }
                }
            }

            override fun onError(message: String) {

            }

        }
    }

    // 添加下载任务
    fun addTask(song: Song) {
        val successString = "添加下载任务成功"
        LogUtil.debug(tag, "$cacheDir")
        if (list.size >= MAX_SIZE || cacheDir == null) {
            Toast.makeText(MusicApplication.context, failString, Toast.LENGTH_SHORT).show()
        } else {
            // 如果URL错误，加载
            if (song.errorUrl()) {
                model.getSongUrl(song.id, presenter)
                if (!exist(song)) cacheList.add(song)
                return
            }
            // 文件名存在，删除它
            song.name?.let {
                if (checkFile(it)) {
                    val f = File("$cacheDir/$it.mp3")
                    f.delete()
                }
            }

            list.add(song)
            status.add(pause)
            // 如果只有一个下载任务，开始下载
            if (list.size == 1) {
                startTask(0)
            }
            Toast.makeText(MusicApplication.context, successString, Toast.LENGTH_SHORT).show()

        }
    }

    // 检查cacheList是否存在该歌曲
    private fun exist(song: Song): Boolean {
        var e = false
        for (c in cacheList) {
            if (c.id == song.id) {
                e = true
                break
            }
        }
        return e
    }

    // 是否存在下载任务
    fun hasTasks() = list.size > 0

    // 第position+1个任务是否开始
    fun taskIsStarted(position: Int) = status[position] == start

    // 得到下载歌曲
    fun getDownloadData(): MutableList<Song> {
        return list
    }

    // 删除任务
    fun deleteTask(position: Int) {
        if (status[position] != start) {
            list[position].name?.let {
                if (checkFile(it)) {
                    list.removeAt(position)
                    status.removeAt(position)
                    val f = File("$cacheDir/$it.mp3")
                    f.delete()
                    listener?.onDelete(position)
                }
            }

        } else status[position] = delete
    }

    // 暂停下载
    fun pauseTask(position: Int) {
        status[position] = pause
    }

    // 恢复下载
    fun restartTask(position: Int) {
        if (cacheDir != null) {
            status[position] = start
            val task = list[position]
            if (!checkFile(task.name!!)) {
                startTask(position)
            } else {
                val f = File("$cacheDir/${task.name}.mp3")
                val len = f.length()
                val r = Request.Builder().url(task.url).build()
                client.newCall(r).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        listener?.onFail(error)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.body?.let {
                            val total = it.contentLength()
                            if (total == -1L) {
                                listener?.onFail(error)
                                return
                            }

                            val request = Request.Builder().addHeader("RANGE", "bytes=$len-$total")
                                .url(task.url).build()
                            LogUtil.debug(tag, "${request.headers}")
                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    listener?.onFail(error)
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    response.body?.let {
                                        it1 ->
                                        try {
                                            val input = it1.byteStream()
                                            val save = RandomAccessFile(f, "rw")
                                            save.seek(len)
                                            val b = ByteArray(1024)
                                            var perLen = input.read(b)
                                            var fileTotal = 0
                                            while (perLen != -1) {
                                                if (status[position] == pause) {
                                                    save.close()
                                                    it1.close()
                                                    listener?.onPause(position)
                                                    return
                                                } else if (status[position] == delete){
                                                    list.removeAt(position)
                                                    status.removeAt(position)
                                                    save.close()
                                                    it1.close()
                                                    f.delete()
                                                    listener?.onDelete(position)
                                                    return
                                                }

                                                save.write(b, 0, perLen)
                                                fileTotal += perLen
                                                listener?.onProgress(position, (fileTotal + len).toInt(), total)
                                                perLen = input.read(b)
                                            }
                                            save.close()
                                            it1.close()
                                            list.removeAt(position)
                                            status.removeAt(position)
                                            listener?.onSuccess("歌曲${task.name}-${task.appendArtists()}下载完成", position)

                                        } catch (e: Exception) {
                                            Log.d(tag, e.message, e)
                                            pauseTask(position)
                                            listener?.onPause(position)
                                        }


                                    }

                                }

                            })

                        }
                    }

                })
            }
        }
    }

    // 检查文件是否存在
    private fun checkFile(name: String): Boolean {
        val f = File("$cacheDir/$name.mp3")
        return f.exists()
    }

    // 开始下载
    private fun startTask(position: Int) {
        if (cacheDir != null) {
            val task = list[position]
            if (status.size >= position + 1) {
                status[position] = start
            } else {
                status.add(position, start)
            }
            val request = Request.Builder().url(task.url).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener?.onFail(error)
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body
                    body?.let {
                        try {
                            val file = File("$cacheDir")
                            file.mkdirs()

                            val f = File("$cacheDir/${task.name}.mp3")
                            f.createNewFile()
                            val out = FileOutputStream(f)
                            val total = body.contentLength()
                            if (total == -1L) {
                                listener?.onFail(error)
                                return
                            }
                            val input = body.byteStream()
                            val byteArray = ByteArray(1024)
                            var perLength: Int
                            var downloadLength = 0
                            do {
                                if (status[position] == pause) {
                                    out.flush()
                                    body.close()
                                    out.close()
                                    listener?.onPause(position)
                                    return
                                } else if (status[position] == delete) {
                                    list.removeAt(position)
                                    status.removeAt(position)
                                    body.close()
                                    out.close()
                                    f.delete()
                                    listener?.onDelete(position)
                                    return
                                }
                                perLength = input.read(byteArray)
                                if (perLength != -1) {
                                    downloadLength += perLength
                                    out.write(byteArray, 0, perLength)
                                    listener?.onProgress(position, downloadLength, total)
                                }
                            } while (perLength != -1)
                            out.flush()
                            body.close()
                            out.close()
                            list.removeAt(position)
                            status.removeAt(position)
                            listener?.onSuccess("歌曲${task.name}-${task.appendArtists()}下载完成", position)
                        } catch (e: Exception) {
                            Log.d(tag, e.message, e)
                            pauseTask(position)
                            listener?.onPause(position)
                        }


                    }


                }

            })


        }

    }

    interface DownloadListener {
        fun onProgress(position: Int, currentPosition: Int, total: Long)
        fun onSuccess(msg: String, position: Int)
        fun onFail(msg: String)
        fun onPause(position: Int)
        fun callback(msg: String)
        fun onDelete(position: Int)
    }

}