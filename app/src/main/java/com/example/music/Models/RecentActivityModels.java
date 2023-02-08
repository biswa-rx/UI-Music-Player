package com.example.music.Models;

public class RecentActivityModels {
    int image;
    String songName;
    String Total_Song;

    public RecentActivityModels(int image, String songName, String total_Song) {
        this.image = image;
        this.songName = songName;
        Total_Song = total_Song;
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

    public String getTotal_Song() {
        return Total_Song;
    }

    public void setTotal_Song(String total_Song) {
        Total_Song = total_Song;
    }

}
