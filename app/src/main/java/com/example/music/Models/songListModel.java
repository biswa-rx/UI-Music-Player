package com.example.music.Models;

import android.media.MediaMetadataRetriever;

public class songListModel {
    private String songName;
    private String artistName;
    private String path;

    public songListModel(String songName,String path) {
        this.songName = songName;
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

    public String getSongArtist() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) == null
                        ? "Unknown artist"
                : retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
