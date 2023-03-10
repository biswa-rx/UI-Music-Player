package com.example.music.drawer_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music.Adapters.RecentActivityAdapter;
import com.example.music.Adapters.songListAdapter;
import com.example.music.FileAccess.playListModel;
import com.example.music.Models.RecentActivityModels;
import com.example.music.Models.songListModel;
import com.example.music.R;
import com.example.music.SharedViewModel;

import java.io.File;
import java.util.ArrayList;

public class fragment_song_list extends Fragment implements songListAdapter.OnSongClickListener{
    SharedViewModel sharedViewModel;
    songListAdapter listAdapter;
    RecyclerView recyclerView;
    ArrayList<songListModel> songList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        recyclerView = view.findViewById(R.id.song_list_rv);
        songList = new ArrayList<>();
        sharedViewModel.getCurrentSongList().observe(getViewLifecycleOwner(), new Observer<ArrayList<File>>() {
            @Override
            public void onChanged(ArrayList<File> files) {
                for(int i=0;i<files.size();i++){
                    songList.add(new songListModel(files.get(i).getName(),"10",files.get(i).getPath()));
                }
                listAdapter = new songListAdapter(songList,container.getContext(), fragment_song_list.this);
                recyclerView.setAdapter(listAdapter);

                LinearLayoutManager layoutManager=new LinearLayoutManager(container.getContext());
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {
        sharedViewModel.setCurrentSongNumber(position);
    }
}