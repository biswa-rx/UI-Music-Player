package com.example.music.Models;

public class songListModel {
    private String songName;
    private String songDuration;
    private String path;

    public songListModel(String songName, String songDuration,String path) {
        this.songName = songName;
        this.songDuration = songDuration;
        this.path = path;
    }

    public songListModel() {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
