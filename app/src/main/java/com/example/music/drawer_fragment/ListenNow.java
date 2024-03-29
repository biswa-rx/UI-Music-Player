package com.example.music.drawer_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music.Adapters.RecentActivityAdapter;
import com.example.music.FileAccess.playListModel;
import com.example.music.MainActivity;
import com.example.music.Models.RecentActivityModels;
import com.example.music.R;
import com.example.music.ViewModel.SharedViewModel;

import java.io.File;
import java.util.ArrayList;

public class ListenNow extends Fragment implements RecentActivityAdapter.OnItemClickListener {
    Context context;
    private RecyclerView recentRecyclerView;
    private SharedViewModel sharedViewModel;
    private ArrayList<RecentActivityModels> list;
    private RecentActivityAdapter recentActivityAdapter;
    NavController navController;
    static ProgressBar loadSongProgress;
    public static TextView tvPath;
    public ListenNow() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listen_now, container, false);
        recentRecyclerView = view.findViewById(R.id.recent_recycler_view);
        tvPath = view.findViewById(R.id.tv_path);
        loadSongProgress = view.findViewById(R.id.search_progress_bar);
        loadSongProgress.setProgress(4);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        list = new ArrayList<>();
        list.add(new RecentActivityModels(R.drawable.icon_shuffle,"Shuffle all",""));
//        list.add(new RecentActivityModels(R.drawable.icon,"All song",""));

        recentActivityAdapter = new RecentActivityAdapter(list,context,this);
        recentRecyclerView.setAdapter(recentActivityAdapter);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,3);
        recentRecyclerView.setLayoutManager(gridLayoutManager);

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
                list.add(1,new RecentActivityModels(R.drawable.icon,"All song",files.size()+""));
                recentActivityAdapter.notifyDataSetChanged();
                loadSongProgress.setVisibility(View.GONE);
                tvPath.setText("Recently Played");
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(sharedPreferences.getInt("PlaylistNumber",-1) == -1){
                            sharedViewModel.setMutableCurrentSongList(sharedViewModel.getAllSongList().getValue());
                        }else{
                            sharedViewModel.setMutableCurrentSongListFromFolder(sharedPreferences.getInt("PlaylistNumber",-1));
                        }
                        sharedViewModel.setCurrentSongNumber(sharedPreferences.getInt("SongNumber",0));//causing error due to faster access from thread...
                    }
                },1000);
            }
        });
    }
public static int selectedPlaylistNumber;
    @Override
    public void onItemClick(int position) {
        if(position>=2) {
            navController.navigate(R.id.action_listenNow_to_fragment_song_list);
            sharedViewModel.setMutableCurrentSongListFromFolder(position - 2);
            selectedPlaylistNumber = position - 2;
        }else if(position==1){
            navController.navigate(R.id.action_listenNow_to_fragment_song_list);
            sharedViewModel.setMutableCurrentSongList(sharedViewModel.getAllSongList().getValue());
            selectedPlaylistNumber = -1;
        }
    }
    public static void setProgress(int max,int progress){
        loadSongProgress.setMax(max);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            loadSongProgress.setProgress(progress,true);
        }
    }
}