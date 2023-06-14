package com.example.music.Media;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;

import com.example.music.CallbackInterface.MusicCompletionCallback;
import com.example.music.CallbackInterface.NotificationCallback;
import com.example.music.CallbackInterface.SeekbarUpdateCallback;

import java.io.File;

//For Singleton Class Create
public class MediaPlayer {
    private static MediaPlayer instance;
    private Context context;
    private Uri uri;
    private volatile boolean isPlaying;
    private volatile boolean isPause;
    android.media.MediaPlayer mediaPlayer;
    NotificationCallback notificationCallback;
    SeekbarUpdateCallback seekbarUpdateCallback;
    MusicCompletionCallback musicCompletionCallback;
    int musicDuration = 0;
    public static volatile boolean seekUpdate = true;

    private MediaPlayer() {
        // Private constructor to prevent instantiation
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (seekUpdate) {
                        if (seekbarUpdateCallback != null && isPlaying) {
                            try {
                                seekbarUpdateCallback.onSeekbarUpdate(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                            } catch (Exception e) {
                                System.out.println(e.toString() + " Exception in seekbar update...");
                            }
                        }
                        SystemClock.sleep(500);
//                            System.out.println("Running");
                    }
                }
            }
        }).start();
    }

    public static synchronized MediaPlayer getInstance() {
        if (instance == null) {
            instance = new MediaPlayer();
        }
        return instance;
    }

    public void play(Context context, Uri uri) {
        isPlaying = false;
        this.context = context;
        this.uri = uri;
        // Implement play logic here using the provided context and Uri
        mediaPlayer = android.media.MediaPlayer.create(context, uri);
        musicDuration = mediaPlayer.getDuration();
        mediaPlayer.start();
        System.out.println(new File(uri.getPath()).getPath());
        if (notificationCallback != null) {
            notificationCallback.onNotificationTextUpdate(new File(uri.getPath()));
        }
        isPlaying = true;
        isPause = false;
        mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(android.media.MediaPlayer mediaPlayer) {
                if (musicCompletionCallback != null) {
                    musicCompletionCallback.onMusicCompletion();
                }
            }
        });
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

    public int getCurrentPosition() {
        if (isPlaying) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
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

    public void setMusicCompletionCallback(MusicCompletionCallback musicCompletionCallback) {
        this.musicCompletionCallback = musicCompletionCallback;
    }

    public void musicSeekTo(int duration) {
        mediaPlayer.seekTo(duration);
    }
}
