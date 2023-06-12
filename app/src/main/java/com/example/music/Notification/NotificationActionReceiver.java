package com.example.music.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.music.Media.MusicController;
import com.example.music.Utils.PlaySerializer;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the notification click event here
        String action = intent.getAction();

        if (action != null && action.equals("ACTION_PLAY")) {
            if (MusicController.getInstance().isMusicPaused()) {
                MusicController.getInstance().justPlay();
            } else {
                MusicController.getInstance().pauseMusic();
            }
        } else if (action != null && action.equals("ACTION_NEXT")) {
            MusicController.getInstance().playMusic(context.getApplicationContext(), Uri.parse(PlaySerializer.getInstance().getNextMusicFile(PlaySerializer.SHUFFLE).toString()));
            Intent updateIntent = new Intent("com.example.ACTION_UPDATE_VIEW");
            LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
        } else if (action != null && action.equals("ACTION_PREVIOUS")) {
            MusicController.getInstance().playMusic(context.getApplicationContext(), Uri.parse(PlaySerializer.getInstance().getNextMusicFile(PlaySerializer.SHUFFLE).toString()));
            Intent updateIntent = new Intent("com.example.ACTION_UPDATE_VIEW");
            LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
        }
    }
}