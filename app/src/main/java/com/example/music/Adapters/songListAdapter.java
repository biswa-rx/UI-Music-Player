package com.example.music.Adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
        String text = songList.get(position).getSongName().replace("_"," ").replace(".mp3","");
        if(text.length()>32){
            String truncatedText = text.substring(0, 28) + " . . .";
            holder.songName.setText(truncatedText);
            holder.songName.setEllipsize(TextUtils.TruncateAt.END);
        }else{
            holder.songName.setText(text);
        }
        byte[] image = getAlbumArt(songList.get(position).getPath());
        if(image != null){
            Glide.with(context).asBitmap()
                    .load(image)
                    .centerCrop()
                    .into(holder.albumImage);
        }
        holder.artistName.setText(songList.get(position).getSongArtist());
    }
    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        TextView artistName;
        ImageView albumImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.song_name_tv);
            albumImage = itemView.findViewById(R.id.listAlbumImage);
            artistName = itemView.findViewById(R.id.artist_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSongClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
    public interface OnSongClickListener{
        void onItemClick(int position);
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
