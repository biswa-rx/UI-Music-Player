package com.example.music.Models;

public class songListModel {
    private String songName;
    private String songDuration;

    public songListModel(String songName, String songDuration) {
        this.songName = songName;
        this.songDuration = songDuration;
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
}
