package com.example.music;

import static com.example.music.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {
//    private MediaPlayer mediaPlayer;
    MusicController musicController;
    private boolean isPlaying;
    @Override
    public void onCreate() {
        super.onCreate();
//        mediaPlayer = MediaPlayer.getInstance();
        musicController = MusicController.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("Fuck")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if (action.equals("PLAY")) {
                Uri musicUri = intent.getParcelableExtra("URI");
                playMusic(musicUri);
            } else if (action.equals("PAUSE")) {
                pauseMusic();
            } else if(action.equals("NEXT")) {
                nextMusic();
            }else if(action.equals("PREVIOUS")){
                previousMusic();
            }
        }

        return START_STICKY;
    }

    private void playMusic(Uri uri) {
        musicController.playMusic(this, uri);
    }

    private void pauseMusic() {
        musicController.pauseMusic();
    }
    private void nextMusic(){

    }
    private void previousMusic(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
