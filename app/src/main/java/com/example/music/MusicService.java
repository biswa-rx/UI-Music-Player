package com.example.music;

import static com.example.music.App.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MusicService extends Service {
//    private MediaPlayer mediaPlayer;
    MusicController musicController;
    public static final int NOTIFICATION_ID = 101;
    private boolean isPlaying;
    @Override
    public void onCreate() {
        super.onCreate();
        musicController = MusicController.getInstance();
        // Set up the media session
        MediaSessionCompat mediaSession = new MediaSessionCompat(getApplicationContext(), "tag");
        mediaSession.setActive(true);

// Set the media session metadata
        MediaMetadataCompat mediaMetadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Song Title")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist Name")
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .build();
        mediaSession.setMetadata(mediaMetadata);

        RemoteViews customNotificationLayout = new RemoteViews(getPackageName(), R.layout.notifi_seekbar);

// Find the ProgressBar view in the custom layout
        // Set the progress of the ProgressBar using setProgressBar method
        customNotificationLayout.setProgressBar(R.id.media_progressbar, 100, 35, false);



        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("Fuck")
                .setSmallIcon(R.drawable.icon_playlist)
                .setContentIntent(pendingIntent)
                .setCustomContentView(customNotificationLayout)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .addAction(R.drawable.notifi_previous,"Previous",null)
                .addAction(R.drawable.notifi_play,"Play",null)
                .addAction(R.drawable.notifi_pause,"Pause",null)
                .addAction(R.drawable.notifi_previous,"Next",null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2)) // Set which buttons to display in compact view
                .setSubText("Sub text")
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if (action.equals("PLAY")) {
                Uri musicUri = intent.getParcelableExtra("URI");
                if(checkNotification(NOTIFICATION_ID)){
                    //recreate notification
                }
                playMusic(musicUri);
            } else if (action.equals("PAUSE")) {
                pauseMusic();
            } else if(action.equals("NEXT")) {
                nextMusic();
            }else if(action.equals("PREVIOUS")){
                previousMusic();
            } else if(action.equals("RESUME")) {
                resumeMusic();
            }
        }

        return START_STICKY;
    }

    private void resumeMusic() {
        musicController.resumeMusic();
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
    private boolean checkNotification(int NOTIFICATION_ID) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = new StatusBarNotification[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notifications = mNotificationManager.getActiveNotifications();
        }
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == NOTIFICATION_ID) {
                return true;
            }
        }
        return false;
    }
}
