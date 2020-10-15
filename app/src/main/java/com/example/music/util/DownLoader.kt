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
    private val list = Collections.synchronizedList(mutableListOf<Song>())
    private val status = Collections.synchronizedList(mutableListOf<Int>())
    private val client = OkHttpClient()
    private const val MAX_SIZE = 8
    var cacheDir: File? = null
    var listener: DownloadListener? = null
    private val cacheList = mutableListOf<Song>()
    private val error = "网络不佳"
    private val start = 1
    private val pause = 2
    private val delete = 3
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


    fun addTask(song: Song) {
//        val failString = "添加下载任务失败"
        val successString = "添加下载任务成功"
//        val existString = "任务已存在或完成"

        LogUtil.debug(tag, "$cacheDir")


        if (list.size >= MAX_SIZE || cacheDir == null) {
//            listener?.callback(failString)
            Toast.makeText(MusicApplication.context, failString, Toast.LENGTH_SHORT).show()
        } else {
            if (song.errorUrl()) {
                model.getSongUrl(song.id, presenter)
                cacheList.add(song)
                return
            }
            song.name?.let {
                if (checkFile(it)) {
                    val f = File("$cacheDir/$it.mp3")
                    f.delete()
                }
            }

            list.add(song)
            status.add(pause)
            if (list.size == 1) {
                startTask(0)
            }
//            listener?.callback(successString)
            Toast.makeText(MusicApplication.context, successString, Toast.LENGTH_SHORT).show()

        }
    }



    fun hasTasks() = list.size > 0

    fun taskIsStarted(position: Int) = status[position] == start

    fun getDownloadData(): MutableList<Song> {
        return list
    }

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



    fun pauseTask(position: Int) {
        status[position] = pause
    }

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
                                                    it1.close()
                                                    listener?.onPause(position)
                                                    return
                                                } else if (status[position] == delete){
                                                    list.removeAt(position)
                                                    status.removeAt(position)
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

    private fun checkFile(name: String): Boolean {
        val f = File("$cacheDir/$name.mp3")
        return f.exists()
    }

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