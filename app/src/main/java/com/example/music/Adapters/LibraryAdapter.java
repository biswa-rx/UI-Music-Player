package com.example.music.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.Models.LibraryModel;
import com.example.music.Models.RecentActivityModels;
import com.example.music.R;
import com.example.music.Utils.MediaData;

import java.io.File;
import java.util.ArrayList;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.viewHolder>{
    ArrayList<LibraryModel> list;
    Context context;
    OnItemClickListener onItemClickListener;
    public LibraryAdapter(ArrayList<LibraryModel> list, Context context , OnItemClickListener onItemClickListener ) {
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
        LibraryModel libraryModel = list.get(position);
        holder.songName.setText(libraryModel.getFileName());
        holder.songName.setSelected(true);
        new Thread(new Runnable() {
            final int temp = position;
            Bitmap bitmap;
            String artist;
            Drawable drawable;
            @Override
            public void run() {
                byte[] image = MediaData.getAlbumArt(list.get(temp).getFilePath());
                artist = MediaData.getSongArtist(list.get(temp).getFilePath());
                if(image != null) {
                    bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.imageView.setImageBitmap(bitmap);
                            holder.artistName.setText(artist);
                        }
                    });
                }else{
                    drawable = context.getResources().getDrawable(R.drawable.icon);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.imageView.setImageDrawable(drawable);
                            holder.artistName.setText(artist);
                        }
                    });
                }
            }
        }).start();
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