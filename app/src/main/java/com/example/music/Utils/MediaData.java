package com.example.music.Utils;

import android.media.MediaMetadataRetriever;

public class MediaData {
    public static String getSongArtist(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) == null
                ? "Unknown artist"
                : retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }
}
