package com.example.music.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.Models.RecentActivityModels;
import com.example.music.Models.songListModel;
import com.example.music.R;

import java.io.File;
import java.util.ArrayList;

public class songListAdapter extends RecyclerView.Adapter<songListAdapter.viewHolder>{
    ArrayList<songListModel> songList;
    Context context;
    OnSongClickListener onSongClickListener;
    private static final String TAG = "songListAdapter";
    public songListAdapter(ArrayList<songListModel> songList, Context context,OnSongClickListener onSongClickListener){
        this.songList = songList;
        this.context = context;
        this.onSongClickListener = onSongClickListener;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_layout_songlist,parent,false);
        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.songName.setText(songList.get(position).getSongName());
    }
    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        TextView Duration;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.song_name_tv);
            Duration = itemView.findViewById(R.id.song_duration_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSongClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
    public interface OnSongClickListener{
        public void onItemClick(int position);
    }
}
