package com.example.music.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.Models.RecentActivityModels;
import com.example.music.R;
import com.example.music.SharedViewModel;

import java.util.ArrayList;

public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.viewHolder>{
    ArrayList<RecentActivityModels> list;
    Context context;
    OnItemClickListener onItemClickListener;
    public RecentActivityAdapter(ArrayList<RecentActivityModels> list, Context context ,OnItemClickListener onItemClickListener ) {
        this.list = list;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_layout_recent_activity,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecentActivityModels recentActivityModels = list.get(position);
        holder.imageView.setImageResource(recentActivityModels.getImage());
        holder.songName.setText(recentActivityModels.getSongName());
        holder.artistName.setText(recentActivityModels.getArtist());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView songName;
        TextView artistName;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.songImage);
            songName = itemView.findViewById(R.id.songName);
            artistName = itemView.findViewById(R.id.artistName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
    public interface OnItemClickListener{
        public void onItemClick(int position);
    }
}
