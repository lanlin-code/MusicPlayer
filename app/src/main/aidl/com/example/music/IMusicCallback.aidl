// IMusicCallback.aidl
package com.example.music;

// Declare any non-default types here with import statements

interface IMusicCallback {
    void getCurrentSong(String imgUrl, String name);
    void playCallback(int position);
    void obtainLrc(long sid);
    void closeBar();
    void playStatusChange(boolean playing);

}
