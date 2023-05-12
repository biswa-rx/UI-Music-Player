package com.example.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
            // Handle the next action
            // ...
        } else if (action != null && action.equals("ACTION_PREVIOUS")) {
            // Handle the previous action
            // ...
        }
    }
}