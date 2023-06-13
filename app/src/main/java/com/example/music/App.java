package com.example.music;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.music.Utils.PlaySerializer;

import java.io.File;


public class App extends Application {

    public static final String CHANNEL_ID = "exampleServiceChannel";
    public static Bitmap themeBitmap;
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        setupThemeBitmap();
        setupPlayMode();
    }

    private void setupPlayMode() {
        SharedPreferences sharedPreferences = getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE);
        PlaySerializer.getInstance().setPlayMode(sharedPreferences.getInt("PlayMode", 0));
    }

    private void setupThemeBitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                themeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.music_theme_bg);
            }
        }).start();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
