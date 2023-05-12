package com.example.music.FileAccess;

import android.os.Handler;
import android.os.Looper;

import com.example.music.drawer_fragment.ListenNow;

import java.io.File;
import java.util.ArrayList;

public class FetchSong {
    public static FetchSong fetchSong;
    public static FetchSong getInstance(){
        if(fetchSong == null){
            return new FetchSong();
        }
        return fetchSong;
    }
    public ArrayList<File> scanSongs(File file){
        ArrayList<File> arrayList=new ArrayList<>();

        File [] songs = file.listFiles();

        if(songs !=null) {
            for(File myFile: songs) {
                if(!myFile.isHidden()  &&  myFile.isDirectory()) {
                    arrayList.addAll(scanSongs(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3")&& !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                        System.out.println("Calling");
                        Handler handler = new Handler(Looper.getMainLooper());
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
    public ArrayList<playListModel> scanValidPlaylist(File file){
        ArrayList<playListModel> playList =  new ArrayList<>();

        File [] myFile = file.listFiles();

        if(myFile!=null) {
            for (File folder : myFile) {
                if(folder.getName().equals("Android")){
                    continue;
                }
                ArrayList<File> playFile = scanSongs(folder);
                if (!playFile.isEmpty()) {
                    playListModel tempPlayList = new playListModel();
                    tempPlayList.setPlayListName(folder.getName());
                    tempPlayList.setSongList(playFile);
                    playList.add(tempPlayList);
                }
            }
        }
        return  playList;
    }

}
