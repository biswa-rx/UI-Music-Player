package com.example.music.Notification;

import static com.example.music.App.CHANNEL_ID;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.music.App;
import com.example.music.CallbackInterface.MusicCompletionCallback;
import com.example.music.CallbackInterface.NotificationCallback;
import com.example.music.MainActivity;
import com.example.music.Media.MediaPlayer;
import com.example.music.Media.MusicController;
import com.example.music.R;
import com.example.music.Utils.PlaySerializer;

import java.io.File;
import java.util.Locale;


public class MusicService extends Service implements NotificationCallback, MusicCompletionCallback {
    //    private MediaPlayer mediaPlayer;
    MusicController musicController;
    static MediaMetadataCompat mediaMetadata;
    static MediaSessionCompat mediaSession;
    static NotificationCompat.Builder builder;
    static NotificationManager notificationManager;
    public static final int NOTIFICATION_ID = 124;
    private boolean isPlaying;
    PendingIntent playPendingIntent,previousPendingIntent,nextPendingIntent;
    Bitmap themeBitmap;
    @Override
    public void onCreate() {
        super.onCreate();
        musicController = MusicController.getInstance();
        //Problem shower the performance due to BitmapFactory.decodeResource(getResources(), R.drawable.music_theme_bg)
        themeBitmap = App.themeBitmap;

        playPendingIntent = initIntent("ACTION_PLAY");
        previousPendingIntent = initIntent("ACTION_NEXT");
        nextPendingIntent = initIntent("ACTION_PREVIOUS");
        // Set up the media session
        mediaSession = new MediaSessionCompat(getApplicationContext(), "tag");
        mediaSession.setActive(true);
        // Set the media session metadata
        mediaMetadata = new MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Song Title")
//                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist Name")
//                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .build();
        mediaSession.setMetadata(mediaMetadata);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("Song Name")
                .setSmallIcon(R.drawable.icon_playlist)
                .setContentIntent(pendingIntent)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .addAction(R.drawable.notifi_previous, "Previous", previousPendingIntent)
                .addAction(R.drawable.notifi_play, "Play", playPendingIntent)
                .addAction(R.drawable.notifi_next, "Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2)) // Set which buttons to display in compact view
                .setSubText("Sub text")
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(null);

        startForeground(NOTIFICATION_ID, builder.build());

        MediaPlayer.getInstance().setNotificationCallback(this);
        MediaPlayer.getInstance().setMusicCompletionCallback(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if (action.equals("PLAY")) {
                Uri musicUri = intent.getParcelableExtra("URI");
//                if (checkNotification()) {
//                    updateNotification(new File(String.valueOf(musicUri)));
//                }
                playMusic(musicUri);
            } else if (action.equals("PAUSE")) {
                pauseMusic();
            } else if (action.equals("NEXT")) {
                nextMusic();
            } else if (action.equals("PREVIOUS")) {
                previousMusic();
            } else if (action.equals("RESUME")) {
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

    private void nextMusic() {
    }

    private void previousMusic() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private boolean checkNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = new StatusBarNotification[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notifications = mNotificationManager.getActiveNotifications();
        }
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == MusicService.NOTIFICATION_ID) {
                return true;
            }
        }
        return false;
    }

    public void updateNotification(File file) {
        /*MediaMetadataRetriever class doing high CPU intensive tack so all work done in background to avoid stuck in UI*/
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file.getPath());
        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        // Extract the album artwork as a byte array
        byte[] artworkBytes = retriever.getEmbeddedPicture();
        retriever.release();
        // Convert the byte array to a Bitmap
        if (artworkBytes != null) {
            Bitmap artworkBitmap = BitmapFactory.decodeByteArray(artworkBytes, 0, artworkBytes.length);

            mediaMetadata = new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, file.getName())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, artworkBitmap)
                    .build();
        } else {
            mediaMetadata = new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, file.getName())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, themeBitmap)
                    .build();
        }
        mediaSession.setMetadata(mediaMetadata);
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.getSessionToken())
                .setShowActionsInCompactView(0, 1 , 2))
                .clearActions()
                .addAction(R.drawable.notifi_previous, "Previous", previousPendingIntent)
                .addAction(MediaPlayer.getInstance().isPause()?R.drawable.notifi_play:R.drawable.notifi_pause, "Play/Pause", playPendingIntent)
                .addAction(R.drawable.notifi_next, "Next", nextPendingIntent);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(MediaPlayer.getInstance().isPause()){
            notificationManager.notify(NOTIFICATION_ID, builder.build());
            stopForeground(false);
        }else{
            startForeground(NOTIFICATION_ID,builder.build());
        }
    }


    @Override
    public void onNotificationTextUpdate(File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateNotification(file);//problem
            }
        }).start();
    }

    private PendingIntent initIntent(String action){
        Intent playIntent = new Intent(this, NotificationActionReceiver.class);
        playIntent.setAction(action);
        return PendingIntent.getBroadcast(getApplicationContext(), 0, playIntent, PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public void onMusicCompletion() {
        MusicController.getInstance().playMusic(getApplicationContext(), Uri.parse(PlaySerializer.getInstance().getNextMusicFile(PlaySerializer.getInstance().getPlayMode()).toString()));
        Intent updateIntent = new Intent("com.example.ACTION_UPDATE_VIEW");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(updateIntent);
    }
}
