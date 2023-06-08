package com.example.music;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;

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
    SeekbarUpdateCallback seekbarUpdateCallback;
    int musicDuration = 0;
    boolean seekUpdate = false;
    Thread progressUpdate;

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
        seekUpdate = false;
        if(progressUpdate != null) {
            progressUpdate.stop();
        }
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

        progressUpdate = new Thread(new Runnable() {
            @Override
            public void run() {
                while (seekUpdate) {
                    if (seekbarUpdateCallback != null) {
//                        seekbarUpdateCallback.onSeekbarUpdate(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                    }
                    SystemClock.sleep(500);
                }
            }
        });
        progressUpdate.start();
        seekUpdate = true;
    }


    public void start() {
        mediaPlayer.start();
        isPlaying = true;
        isPause = false;
        if (notificationCallback != null) {
            notificationCallback.onNotificationTextUpdate(new File(uri.getPath()));
        }
    }

    public void pause() {
        // Implement pause logic here
        mediaPlayer.pause();
        isPause = true;
        if (notificationCallback != null) {
            notificationCallback.onNotificationTextUpdate(new File(uri.getPath()));
        }
    }

    public boolean isPause() {
        return isPause;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    // Additional methods and properties
    public int getMusicDuration() {
        return musicDuration;
    }

    public void clear() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
    }

    public void setNotificationCallback(NotificationCallback callback) {
        this.notificationCallback = callback;
    }

    public void setSeekbarUpdateCallback(SeekbarUpdateCallback seekbarUpdateCallback) {
        this.seekbarUpdateCallback = seekbarUpdateCallback;
    }
}
