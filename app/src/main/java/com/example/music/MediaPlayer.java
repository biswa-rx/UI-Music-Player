package com.example.music;

import android.content.Context;
import android.net.Uri;

//For Singleton Class Create
public class MediaPlayer {
    private static MediaPlayer instance;
    private Context context;
    private Uri uri;
    private boolean isPlaying;
    android.media.MediaPlayer mediaPlayer;

    int musicDuration=0;
    private MediaPlayer() {
        // Private constructor to prevent instantiation
    }

    public static synchronized MediaPlayer getInstance() {
        if (instance == null) {
            instance = new MediaPlayer();
        }
        return instance;
    }

    public void play(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
        // Implement play logic here using the provided context and Uri
         mediaPlayer = android.media.MediaPlayer.create(context, uri);
         musicDuration = mediaPlayer.getDuration();
         mediaPlayer.start();
         isPlaying = true;
    }

    public void pause() {
        // Implement pause logic here
        mediaPlayer.pause();
        isPlaying = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    // Additional methods and properties
    public int getMusicDuration() {
        return musicDuration;
    }

}
