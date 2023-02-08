package com.example.music.FileAccess;

import java.io.File;
import java.util.ArrayList;

public class playListModel {
    String playListName;
    ArrayList<File> songList;

    public playListModel() {
        songList = new ArrayList<>();
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public ArrayList<File> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<File> songList) {
        this.songList = songList;
    }
}