package com.example.music.entity

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.example.respository.bean.SongDetailJson

class Song(
    var id: Long = errorId, var name: String? = errorString,
    var albumId: Long = errorId, var albumPic: String? = errorString,
    var albumName: String? = errorString, var artists: ArrayList<Artist> = arrayListOf()): Parcelable{

//    var artists: MutableList<Artist> = mutableListOf()

    var url: String = errorString

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(Artist::class.java.classLoader) as ArrayList<Artist>
    ) {

    }

    fun errorUrl(): Boolean = url == errorString




    override fun toString(): String {
        return "[Song id = $id, name = $name, albumId = $albumName, albumName = $albumName\n" +
                "albumPic = $albumPic, artist = $artists"
    }




    class Artist(var id: Long = errorId, var name: String? = errorString): Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString()
        ) {
        }

        override fun toString(): String {
            return "name = $name"
        }



        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeLong(id)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Artist> {
            override fun createFromParcel(parcel: Parcel): Artist {
                return Artist(parcel)
            }

            override fun newArray(size: Int): Array<Artist?> {
                return arrayOfNulls(size)
            }

            fun isError(artist: Artist): Boolean = artist.id == errorId || artist.name == errorString
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeLong(albumId)
        parcel.writeString(albumPic)
        parcel.writeString(albumName)
        parcel.writeList(artists as List<*>?)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }

        const val errorId: Long = -1L
        const val errorString: String = ""

        fun valueOfList(songDetailJson: SongDetailJson): MutableList<Song> {
            val l = songDetailJson.songs
            val r = mutableListOf<Song>()
            l?.let {
                for (s in it) {
                    val song = valueOf(s)
                    if (!isError(song)) {
                        r.add(song)
                    }
                }
            }
            return r
        }

        private fun valueOf(s: SongDetailJson.Song): Song {
            val song = Song()
            val id = s.id
            val name = s.name
            val albumId = s.al?.id
            val albumPic = s.al?.picUrl
            val albumName = s.al?.name
            id?.let { song.id = id }
            name?.let { song.name = name }
            albumId?.let { song.albumId = albumId }
            albumPic?.let { song.albumPic = albumPic }
            albumName?.let { song.albumName = albumName }
            val artists = s.ar
            artists?.let {
                for (a in artists) {
                    val artist = Artist()
                    val aId = a.id
                    val aName = a.name
                    aId?.let {
                        artist.id = aId
                    }
                    aName?.let { artist.name = aName }
                    if (!Artist.isError(artist)) {
                        song.artists.add(artist)
                    }
                }
            }
            return song
        }

        private fun isError(song: Song): Boolean = song.id == errorId || song.name == errorString
                || song.albumId == errorId || song.albumPic == errorString
    }


}