package com.example.music;

import android.content.Context;
import android.net.Uri;

//For Controlling music playback
public class MusicController {
    private MediaPlayer mediaPlayer;

    public MusicController() {
        mediaPlayer = MediaPlayer.getInstance();
    }

    public void playMusic(Context context, Uri uri) {
        mediaPlayer.play(context, uri);
    }

    public void pauseMusic() {
        mediaPlayer.pause();
    }

    public boolean isMusicPlaying() {
        return mediaPlayer.isPlaying();
    }

    // Additional music control methods
}
