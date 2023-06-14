package com.example.music.Media;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.music.Utils.PlaySerializer;
import com.example.music.drawer_fragment.ListenNow;

import java.io.File;
import java.util.ArrayList;

//For Controlling music playback
public class MusicController {
    private static MediaPlayer mediaPlayer;
    private static int currentSongNumber = 0;
    Context context;
//    private ArrayList<File> songList;

    //Singleton instance
    private static MusicController instance;
    public static synchronized MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }
    public MusicController() {
        mediaPlayer = MediaPlayer.getInstance();
    }
    public void playMusic(Context context, Uri uri) {
        mediaPlayer.clear();
        mediaPlayer.play(context, uri);
        this.context = context;
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SongNumber", PlaySerializer.getInstance().getSelectedIndex());
        editor.putInt("PlaylistNumber", ListenNow.selectedPlaylistNumber);
        editor.apply();
        Intent updateIntent = new Intent("com.example.ACTION_MUSIC");
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(updateIntent);
    }
    public boolean isMusicPaused(){
        return mediaPlayer.isPause();
    }
//    public void setSongList(ArrayList<File> songList){
//        this.songList = songList;
//    }
//    public ArrayList<File> getSongList(){
//        return songList;
//    }
    public void setSongNumber(int songNumber){
        currentSongNumber = songNumber;
    }
    public int getCurrentSongNumber(){
        return currentSongNumber;
    }
//    public File getCurrentSongPath(){
//        return songList.get(currentSongNumber);
//    }
    public void pauseMusic() {
        mediaPlayer.pause();
        Intent updateIntent = new Intent("com.example.ACTION_MUSIC");
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(updateIntent);
    }

    public boolean isMusicPlaying() {
        return mediaPlayer.isPlaying();
    }
    public void justPlay(){
        mediaPlayer.start();
        Intent updateIntent = new Intent("com.example.ACTION_MUSIC");
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(updateIntent);
    }
    public void musicSeekTo(int duration){
        mediaPlayer.musicSeekTo(duration);
    }

    // Additional music control methods
    public void nextMusic(){


    }

    public void previousMusic(){

    }

    public void resumeMusic(){

    }

}


//    private void next_music(){
//        int currentSongNumber = sharedViewModel.getCurrentSongNumber();
//        int currentSongListSize = sharedViewModel.getCurrentSongListSize();
//
//        if(currentSongNumber!=currentSongListSize-1){
//            sharedViewModel.setCurrentSongNumber(currentSongNumber+1);
//        }else {
//            sharedViewModel.setCurrentSongNumber(0);
//        }
//    }
//
//    private  void previous_music(){
//        int currentSongNumber = sharedViewModel.getCurrentSongNumber();
//        int currentSongListSize = sharedViewModel.getCurrentSongListSize();
//
//        if(currentSongNumber!=0){
//            sharedViewModel.setCurrentSongNumber(currentSongNumber-1);
//        }else {
//            sharedViewModel.setCurrentSongNumber(currentSongListSize-1);
//        }
//    }

//    android.media.MediaPlayer.OnCompletionListener m_CompletionListener = new android.media.MediaPlayer.OnCompletionListener() {
//        @Override
//        public void onCompletion(android.media.MediaPlayer mediaPlayer) {
////            btNext.performClick();
//        }
//    };

