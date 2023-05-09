package com.example.music;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if (action.equals("PLAY")) {
                Uri musicUri = intent.getParcelableExtra("URI");
                playMusic(musicUri);
            } else if (action.equals("PAUSE")) {
                pauseMusic();
            }
        }

        return START_STICKY;
    }

    private void playMusic(Uri uri) {
        if (!isPlaying) {
            // Perform any necessary setup and start playing the music using the provided Uri
            mediaPlayer.play(this, uri);
            isPlaying = true;
            // Update notification or perform any other necessary tasks
        }
    }

    private void pauseMusic() {
        if (isPlaying) {
            // Pause the music playback
            mediaPlayer.pause();
            isPlaying = false;
            // Update notification or perform any other necessary tasks
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
