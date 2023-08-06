package com.example.music.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.Models.songListModel;
import com.example.music.R;
import com.example.music.Utils.MediaData;

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
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        String text = songList.get(position).getSongName().replace("_"," ").replace(".mp3","");
        if(text.length()>32){
            String truncatedText = text.substring(0, 28) + " . . .";
            holder.songName.setText(truncatedText);
            holder.songName.setEllipsize(TextUtils.TruncateAt.END);
        }else{
            holder.songName.setText(text);
        }
        new Thread(new Runnable() {
            final int temp = position;
            Bitmap bitmap;
            String artist;
            Drawable drawable;
            @Override
            public void run() {
                byte[] image = MediaData.getAlbumArt(songList.get(temp).getPath());
                artist = songList.get(temp).getSongArtist();
                if(image != null) {
                    bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.albumImage.setImageBitmap(bitmap);
                            holder.artistName.setText(artist);
                        }
                    });
                }else{
                     drawable = context.getResources().getDrawable(R.drawable.icon);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.albumImage.setImageDrawable(drawable);
                            holder.artistName.setText(artist);
                        }
                    });
                }
            }
        }).start();
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
}
