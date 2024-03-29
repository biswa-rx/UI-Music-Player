package com.example.music.ViewModel;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.music.FileAccess.FetchSong;
import com.example.music.FileAccess.playListModel;

import java.io.File;
import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private static final String TAG = "SharedViewModel";
    private static final MutableLiveData<ArrayList<playListModel>> mutablePlayList =  new MutableLiveData<>();
    private static final MutableLiveData<ArrayList<File>> mutableAllSongList = new MutableLiveData<>();
    private static final MutableLiveData<ArrayList<File>> mutableCurrentSongList = new MutableLiveData<>();
    private static final MutableLiveData<File> mutableCurrentSong = new MutableLiveData<>();
    private static final MutableLiveData<Integer> currentSongNumber = new MutableLiveData<>();

    public LiveData<ArrayList<playListModel>> getSongPlaylist() {
        if (mutablePlayList.getValue() == null){
            new LoadPlayList().execute();
        }
        return mutablePlayList;
    }

    public LiveData<ArrayList<File>> getCurrentSongList() {
        return mutableCurrentSongList;
    }

    public int getCurrentSongListSize() {
        int size = 0;
        if(mutableCurrentSongList.getValue()!=null) {
            size = mutableCurrentSongList.getValue().size();
        }
        return size;
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
        if(mutableCurrentSongList.getValue()!= null) {
            if(getCurrentSongListSize() > position && position >= 0) {
                File tempSongList = mutableCurrentSongList.getValue().get(position);
                mutableCurrentSong.setValue(tempSongList);
                currentSongNumber.setValue(position);
            }else{
                Log.e(TAG, "setCurrentSongNumber: Current song number out of bound of current playlist size" );
            }
        }
    }
    public LiveData<Integer> getLiveCurrentSongNumber() {
        return currentSongNumber;
    }

    public Integer getCurrentSongNumber(){
        return currentSongNumber.getValue();
    }
    static class LoadPlayList extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<playListModel> myPlayList = FetchSong.getInstance().scanValidPlaylist(Environment.getExternalStorageDirectory());
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

