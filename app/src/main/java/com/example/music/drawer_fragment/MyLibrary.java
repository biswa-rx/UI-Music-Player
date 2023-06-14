package com.example.music.drawer_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.Adapters.LibraryAdapter;
import com.example.music.Adapters.RecentActivityAdapter;
import com.example.music.FileAccess.playListModel;
import com.example.music.Media.MusicController;
import com.example.music.Models.LibraryModel;
import com.example.music.Models.RecentActivityModels;
import com.example.music.Notification.MusicService;
import com.example.music.R;
import com.example.music.Utils.PlaySerializer;
import com.example.music.ViewModel.SharedViewModel;

import java.io.File;
import java.util.ArrayList;

public class MyLibrary extends Fragment implements LibraryAdapter.OnItemClickListener{

    Context context;
    private RecyclerView recyclerView;
    private SharedViewModel sharedViewModel;
    private ArrayList<LibraryModel> list;
    LibraryAdapter libraryAdapter;
    public MyLibrary() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_library, container, false);
        recyclerView = view.findViewById(R.id.rv_library);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        list = new ArrayList<>();

        libraryAdapter = new LibraryAdapter(list,context,this);
        recyclerView.setAdapter(libraryAdapter);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel.getAllSongList().observe(getViewLifecycleOwner(), new Observer<ArrayList<File>>() {
            @Override
            public void onChanged(ArrayList<File> files) {
                if(!files.isEmpty()) {
                    for(int i = 0; i<files.size();i++){
                        list.add(new LibraryModel(files.get(i).getPath(),files.get(i).getName()
                                .replace("_"," ").replace(".mp3","")));
                    }
                }else{
                    Toast.makeText(requireActivity(),"NO LOCAL SONG FILE",Toast.LENGTH_SHORT).show();
                }
                libraryAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        sharedViewModel.setMutableCurrentSongList(sharedViewModel.getAllSongList().getValue());
        sharedViewModel.setCurrentSongNumber(position);
        ArrayList<File> songList = sharedViewModel.getCurrentSongList().getValue();
        PlaySerializer.getInstance().setMusicList(songList);
        PlaySerializer.getInstance().setSelectedIndex(position);
        File file = songList.get(position);
        Uri uri = Uri.parse(file.toString());
        Intent playIntent = new Intent(context.getApplicationContext(), MusicService.class);
        playIntent.setAction("PLAY");
        playIntent.putExtra("URI", uri);
        context.getApplicationContext().startService(playIntent);
        MusicController.getInstance().setSongNumber(position);
        ListenNow.selectedPlaylistNumber = -1;
    }
}