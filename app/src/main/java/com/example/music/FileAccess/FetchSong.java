package com.example.music.FileAccess;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.example.music.R;
import com.example.music.drawer_fragment.ListenNow;

import java.io.File;
import java.util.ArrayList;

public class FetchSong {
    public static FetchSong fetchSong;

    public static FetchSong getInstance() {
        if (fetchSong == null) {
            return new FetchSong();
        }
        return fetchSong;
    }

    public ArrayList<File> scanSongs(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        Handler handler = new Handler(Looper.getMainLooper());

        File[] songs = file.listFiles();

        if (songs != null) {
            for (File myFile : songs) {
                if (!myFile.isHidden() && myFile.isDirectory()) {
                    arrayList.addAll(scanSongs(myFile));
                } else {
                    if (myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".") && myFile.length() >= 1024 * 128) {
                        //Ignore small file which size less than 128KB
                        arrayList.add(myFile);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                ListenNow.tvPath.setText(myFile.getPath());
                            }
                        };
                        handler.post(runnable);
                    }
                }
            }
        }
        return arrayList;
    }

    public ArrayList<playListModel> scanValidPlaylist(File file) {
        Handler handler = new Handler(Looper.getMainLooper());
        final int[] progress = {0};
        ArrayList<playListModel> playList = new ArrayList<>();

        File[] myFile = file.listFiles();
        final int[] scanedFile = {0};
        int totalFile = 0;

        if (myFile != null) {
            totalFile = myFile.length;
            for (File folder : myFile) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(!folder.getName().equals("Android")) {
                            ArrayList<File> playFile = scanSongs(folder);
                            if (!playFile.isEmpty()) {
                                playListModel tempPlayList = new playListModel();
                                tempPlayList.setPlayListName(folder.getName());
                                tempPlayList.setSongList(playFile);
                                playList.add(tempPlayList);
                            }
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    ListenNow.setProgress(myFile.length, progress[0]++);
                                }
                            };
                            handler.post(runnable);
                        }
                        scanedFile[0]++;
                    }
                }).start();
            }
        }
        while(totalFile!=scanedFile[0]){
            SystemClock.sleep(2);
        }
        return playList;
    }

}
