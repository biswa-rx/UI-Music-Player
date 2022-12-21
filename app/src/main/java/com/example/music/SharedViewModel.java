package com.example.music;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.music.FileAccess.FetchSong;
import com.example.music.FileAccess.playListModel;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private static final String TAG = "SharedViewModel";
    private MutableLiveData<ArrayList<playListModel>> mutablePlayList =  new MutableLiveData<>();
    private MutableLiveData<ArrayList<File>> mutableAllSongList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<File>> mutableCurrentSongList = new MutableLiveData<>();
    private MutableLiveData<File> mutableCurrentSong = new MutableLiveData<>();
    private MutableLiveData<Integer> currentSongNumber = new MutableLiveData<>();

    public LiveData<ArrayList<playListModel>> getSongPlaylist() {
        if (mutablePlayList.getValue() == null){
            new LoadPlayList().execute();
        }
        return mutablePlayList;
    }

    public LiveData<ArrayList<File>> getCurrentSongList() {
        return mutableCurrentSongList;
    }

    public Integer getCurrentSongListSize() {
        return mutableCurrentSongList.getValue().size();
    }

    public void setMutableCurrentSongListFromFolder(int position){
        ArrayList<playListModel> tempSongList = mutablePlayList.getValue();
        assert tempSongList != null;
        playListModel tempPlaylistModel = tempSongList.get(position);
        mutableCurrentSongList.setValue(tempPlaylistModel.getSongList());
    }

    public void setMutableCurrentSongList(ArrayList<File> songList){
        mutableCurrentSongList.setValue(songList);
    }

    public LiveData<ArrayList<File>> getAllSongList() {
        return mutableAllSongList;
    }

    public LiveData<File> getCurrentSong () { return mutableCurrentSong;}
    public void setCurrentSong(File file) {
        mutableCurrentSong.setValue(file);
    }
    public void setCurrentSongNumber(int position){
        File tempSongList = mutableCurrentSongList.getValue().get(position);
        mutableCurrentSong.setValue(tempSongList);
        currentSongNumber.setValue(position);
    }
    public LiveData<Integer> getLiveCurrentSongNumber() {
        return currentSongNumber;
    }

    public Integer getCurrentSongNumber(){
        return currentSongNumber.getValue();
    }

    class LoadPlayList extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            FetchSong fetchSong = new FetchSong();
            ArrayList<playListModel> myPlayList=fetchSong.scanValidPlaylist(Environment.getExternalStorageDirectory());
            mutablePlayList.postValue(myPlayList);
            ArrayList<File> mySongList = new ArrayList<>();
            for(playListModel playLIst:myPlayList){
                mySongList.addAll(playLIst.getSongList());
            }
            mutableAllSongList.postValue(mySongList);
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

}

