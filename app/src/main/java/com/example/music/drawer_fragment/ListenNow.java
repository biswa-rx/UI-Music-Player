package com.example.music.drawer_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music.Adapters.RecentActivityAdapter;
import com.example.music.FileAccess.FetchSong;
import com.example.music.FileAccess.playListModel;
import com.example.music.MainActivity;
import com.example.music.Models.RecentActivityModels;
import com.example.music.R;
import com.example.music.SharedViewModel;

import java.io.File;
import java.util.ArrayList;

public class ListenNow extends Fragment implements RecentActivityAdapter.OnItemClickListener{
    Context context;
    private RecyclerView recentRecyclerView;
    private SharedViewModel sharedViewModel;
    private ArrayList<RecentActivityModels> list;
    private RecentActivityAdapter recentActivityAdapter;
    NavController navController;
    public ListenNow() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listen_now, container, false);
        recentRecyclerView = view.findViewById(R.id.recent_recycler_view);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        list = new ArrayList<>();
        list.add(new RecentActivityModels(R.drawable.icon_shuffle,"Shuffle all",""));
//        list.add(new RecentActivityModels(R.drawable.icon,"All song",""));
        list.add(new RecentActivityModels(R.drawable.icon_cloud_network,"Recent song","No"));

        recentActivityAdapter = new RecentActivityAdapter(list,context,this);
        recentRecyclerView.setAdapter(recentActivityAdapter);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3);
        recentRecyclerView.setLayoutManager(gridLayoutManager);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(container.getContext());
//        recentRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        sharedViewModel.getSongPlaylist().observe(getViewLifecycleOwner(), new Observer<ArrayList<playListModel>>() {
            @Override
            public void onChanged(ArrayList<playListModel> playListModels) {
                if(!playListModels.isEmpty()){
                    for(int i=0;i<playListModels.size();i++){
                        list.add(new RecentActivityModels(R.drawable.icon_playlist,playListModels.get(i).getPlayListName(),playListModels.get(i).getSongList().size()+""));
                    }
                }else{
                    Toast.makeText(requireActivity(),"NO LOCAL SONG FILE",Toast.LENGTH_SHORT).show();
                }
                recentActivityAdapter.notifyDataSetChanged();
            }
        });
        sharedViewModel.getAllSongList().observe(getViewLifecycleOwner(), new Observer<ArrayList<File>>() {
            @Override
            public void onChanged(ArrayList<File> files) {
//                list.remove(1);
                list.add(1,new RecentActivityModels(R.drawable.icon,"All song",files.size()+""));
                recentActivityAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(getContext(),position+"hgfd",Toast.LENGTH_SHORT).show();
        if(position>2) {
            navController.navigate(R.id.action_listenNow_to_fragment_song_list);
            sharedViewModel.setMutableCurrentSongListFromFolder(position - 3);
        }else if(position==1){
            navController.navigate(R.id.action_listenNow_to_fragment_song_list);
            sharedViewModel.setMutableCurrentSongList(sharedViewModel.getAllSongList().getValue());
        }
    }
}