package com.example.music.Models;

public class RecentActivityModels {
    int image;
    String songName;
    String Artist;

    public RecentActivityModels(int image, String songName, String artist) {
        this.image = image;
        this.songName = songName;
        Artist = artist;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }
}
