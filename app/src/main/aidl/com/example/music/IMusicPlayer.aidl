// IMusicPlayer.aidl
package com.example.music;
import com.example.music.entity.Song;
import com.example.music.IMusicCallback;

// Declare any non-default types here with import statements

interface IMusicPlayer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */


    void receive(in List<Song> songs);
    int currentPosition();
    int duration();
    void playFrom(int position);
    void seekTo(int position);
    void next();
    void last();
    void parse();
    void clear();
    void mode(int mode);
    void restart();
    void registerCallback(in IMusicCallback callback);
    void clearAllCallback();
    boolean isPlaying();
    Song currentPlaying();
    boolean showBar();
    int getMode();



}
