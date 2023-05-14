package com.example.music.drawer_fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.Adapters.songListAdapter;
import com.example.music.Models.songListModel;
import com.example.music.MusicController;
import com.example.music.MusicService;
import com.example.music.R;
import com.example.music.SharedViewModel;

import java.io.File;
import java.util.ArrayList;

public class fragment_song_list extends Fragment implements songListAdapter.OnSongClickListener {
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
        sharedViewModel.getCurrentSongList().observe(getViewLifecycleOwner(), files -> {
            for (int i = 0; i < files.size(); i++) {
                songList.add(new songListModel(files.get(i).getName(), files.get(i).getPath()));
            }
            listAdapter = new songListAdapter(songList, container.getContext(), fragment_song_list.this);
            recyclerView.setAdapter(listAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
            recyclerView.setLayoutManager(layoutManager);
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(int position) {
        ArrayList<File> songList = sharedViewModel.getCurrentSongList().getValue();
        File file = songList.get(position);
        Uri uri = Uri.parse(file.toString());
        Intent playIntent = new Intent(requireContext(), MusicService.class);
        playIntent.setAction("PLAY");
        playIntent.putExtra("URI", uri);
        requireContext().startService(playIntent);
        sharedViewModel.setCurrentSongNumber(position);
        MusicController.getInstance().setSongNumber(position);
        MusicController.getInstance().setSongList(songList);
    }
}