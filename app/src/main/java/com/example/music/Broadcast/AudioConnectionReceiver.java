package com.example.music.Broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.music.Media.MusicController;

import javax.security.auth.login.LoginException;

public class AudioConnectionReceiver extends BroadcastReceiver {
    private static final String TAG = "AudioConnectionReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null) {
            if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
                // Earphone plug event
                int state = intent.getIntExtra("state", -1);
                if (state == 0) {
                    // Earphone unplugged
                    // Pause music playback
                    if(MusicController.getInstance().isMusicPlaying()) {
                        MusicController.getInstance().pauseMusic();
                    }
                    Log.e(TAG, "onReceive: Headset unplug" );
                }
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                // Bluetooth device disconnect event
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    // Check if the disconnected device is a Bluetooth sound device
                    // Pause music playback
                    if(MusicController.getInstance().isMusicPlaying()) {
                        MusicController.getInstance().pauseMusic();
                    }
                    Log.e(TAG, "onReceive: Bluetooth device disconnect" );
                }
            }
        }
    }
}
