package com.example.music;

import android.content.Context;
import android.net.Uri;

import java.io.File;

//For Singleton Class Create
public class MediaPlayer {
    private static MediaPlayer instance;
    private Context context;
    private Uri uri;
    private boolean isPlaying;
    private boolean isPause;
    android.media.MediaPlayer mediaPlayer;
    NotificationCallback notificationCallback;
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
        if (notificationCallback != null) {
            notificationCallback.onNotificationTextUpdate(new File(uri.getPath()));
        }
         isPlaying = true;
        isPause = false;
    }
    public void start(){
        mediaPlayer.start();
        isPlaying = true;
        isPause = false;
    }

    public void pause() {
        // Implement pause logic here
        mediaPlayer.pause();
        isPause = true;
    }
    public boolean isPause(){
        return isPause;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    // Additional methods and properties
    public int getMusicDuration() {
        return musicDuration;
    }
    public void clear(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
    }
    public void setNotificationCallback(NotificationCallback callback) {
        this.notificationCallback = callback;
    }

}
