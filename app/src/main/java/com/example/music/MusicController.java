package com.example.music;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.ViewModelProvider;

//For Controlling music playback
public class MusicController {
    private static MediaPlayer mediaPlayer;

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
    }

    public void pauseMusic() {
        mediaPlayer.pause();
    }

    public boolean isMusicPlaying() {
        return mediaPlayer.isPlaying();
    }

    // Additional music control methods
    public void nextMusic(){

    }

    public void previousMusic(){

    }

    public void setSongNumber(int songNumber){

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



//    private String createTime(int Duration) {
//        String time="0";
//        int sec=Duration/1000;
//        int min=sec/60;
//        time+=min+":";
//        sec=sec%60;
//        if(sec<10) {
//            time+='0';
//        }
//        time+=sec;
//        return time;
//    }

