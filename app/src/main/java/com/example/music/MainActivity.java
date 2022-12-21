package com.example.music;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.music.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedViewModel sharedViewModel;
    static MediaPlayer mediaPlayer;
    BottomSheetDialog bottomSheetDialog;
    ConstraintLayout bottom_sheet_player;

    private static final String TAG = "MainActivity";

    ConstraintLayout bs_main;
    ImageView songImageView,bsMenu,bs_down_arrow,smallPlayerAlbum;
    Button btNext,btPrevious,btRepeat,btSuffle;
    ToggleButton btPlayPause;
    TextView tvSongName,tvSongEnd,tvSongLive,tvMainSongName;
    SeekBar bs_seekBar;

    Thread update_seek;
    boolean updateSeekFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.listenNow, R.id.myLibrary, R.id.playlists,R.id.action_settings,R.id.action_help,R.id.action_feed_back)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        tvMainSongName = findViewById(R.id.tv_main_song_name);
        tvMainSongName.setSelected(true);

        smallPlayerAlbum = findViewById(R.id.music_image);

        createBottomSheetDialog();

        bottom_sheet_player = findViewById(R.id.botton_sheet_view_triger);
        bottom_sheet_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
                updateSeekFlag = false;
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                updateSeekFlag = true;
            }
        });

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        sharedViewModel.getCurrentSong().observe(this, new Observer<File>() {
            @Override
            public void onChanged(File file) {
                updateSeekFlag = true;
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                Uri uri = Uri.parse(file.toString());
                mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(m_CompletionListener);
                btPlayPause.setChecked(false);
                bs_seekBar.setMax(mediaPlayer.getDuration());
                bs_seekBar.setProgress(mediaPlayer.getCurrentPosition());

                tvSongEnd.setText(createTime(mediaPlayer.getDuration()));

                updateSeekFlag=false;//false means start seekbar, this flag due to error on request current duration when no music playing

                tvSongName.setText(file.getName().replace(".mp3",""));
                tvMainSongName.setText(file.getName().replace(".mp3",""));

                byte[] image = getAlbumArt(file.getPath());
                if(image != null){
                    Glide.with(getBaseContext()).asBitmap()
                            .load(image)
                            .into(songImageView);
                    Glide.with(getBaseContext()).asBitmap()
                            .load(image)
                            .into(smallPlayerAlbum);
                }else{
                    Glide.with(getBaseContext()).asBitmap()
                            .load(R.drawable.icon)
                            .into(songImageView);
                    Glide.with(getBaseContext()).asBitmap()
                            .load(R.drawable.icon)
                            .into(smallPlayerAlbum);
                }
            }
        });

        btPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btPlayPause.isChecked()){
                    mediaPlayer.pause();
                }else{
                    mediaPlayer.start();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateSeekFlag = true;
    }

    private void createBottomSheetDialog() {
        if (bottomSheetDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_player, null);
            btPlayPause = view.findViewById(R.id.play_button);
            btNext = view.findViewById(R.id.next_button);
            btPrevious = view.findViewById(R.id.previous_button);
            btSuffle = view.findViewById(R.id.suffle_button);
            btRepeat = view.findViewById(R.id.repeat_button);
            songImageView = view.findViewById(R.id.bs_song_image);
            bsMenu = view.findViewById(R.id.bs_menu);
            tvSongName = view.findViewById(R.id.bs_song_name_tv);
            bs_down_arrow = view.findViewById(R.id.down_arrow_button);
            bs_main = view.findViewById(R.id.bs_main_layout);
            bs_seekBar = view.findViewById(R.id.bs_player_seekBar);
            tvSongEnd = view.findViewById(R.id.song_end_tv);
            tvSongLive = view.findViewById(R.id.song_live_tv);

            tvSongName.setSelected(true);

            bs_main.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

            btNext.setOnClickListener(this);
            btPrevious.setOnClickListener(this);
            btSuffle.setOnClickListener(this);
            btRepeat.setOnClickListener(this);
            bsMenu.setOnClickListener(this);
            bs_down_arrow.setOnClickListener(this);

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }

        bs_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                String currentTime = createTime(progress);
                tvSongLive.setText(currentTime);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                updateSeekFlag = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                updateSeekFlag = false;

            }
        });

        update_seek = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;

                while(true) {
                    if (!updateSeekFlag) {
                        if(mediaPlayer != null) {
                            currentPosition = mediaPlayer.getCurrentPosition();
                            bs_seekBar.setProgress(currentPosition);
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        update_seek.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private String createTime(int Duration) {
        String time="0";
        int sec=Duration/1000;
        int min=sec/60;
        time+=min+":";
        sec=sec%60;
        if(sec<10) {
            time+='0';
        }
        time+=sec;
        return time;
    }

    MediaPlayer.OnCompletionListener m_CompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            updateSeekFlag = true;
//            Log.e(TAG, "onCompletionCompletion: " );
            btNext.performClick();
//            int currentSongNumber = sharedViewModel.getCurrentSongNumber();
//            int currentSongListSize = sharedViewModel.getCurrentSongListSize();
//            ArrayList<File> songList = sharedViewModel.getCurrentSongListBackgroundPlay();
//            if(currentSongNumber!=currentSongListSize-1){
//                currentSongNumber = currentSongNumber+1;
//            }else {
//                currentSongNumber = 0;
//            }
//
//            /////////////Media player access
//            if(mediaPlayer != null){
//                mediaPlayer.stop();
//                mediaPlayer.release();
//            }
//            Uri uri = Uri.parse(songList.get(currentSongNumber).toString());
//            mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
//            mediaPlayer.start();
//            mediaPlayer.setOnCompletionListener(m_CompletionListener);
//            bs_seekBar.setMax(mediaPlayer.getDuration());
//            bs_seekBar.setProgress(mediaPlayer.getCurrentPosition());
//
//            tvSongEnd.setText(createTime(mediaPlayer.getDuration()));
//            updateSeekFlag = false;
        }
    };

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next_button:
                next_music();
                break;
            case R.id.previous_button:
                previous_music();
                break;
        }
    }

    private void next_music(){
        updateSeekFlag = true;
        int currentSongNumber = sharedViewModel.getCurrentSongNumber();
        int currentSongListSize = sharedViewModel.getCurrentSongListSize();

        if(currentSongNumber!=currentSongListSize-1){
            sharedViewModel.setCurrentSongNumber(currentSongNumber+1);
        }else {
            sharedViewModel.setCurrentSongNumber(0);
        }
    }

    private  void previous_music(){
        updateSeekFlag = true;
        int currentSongNumber = sharedViewModel.getCurrentSongNumber();
        int currentSongListSize = sharedViewModel.getCurrentSongListSize();

        if(currentSongNumber!=0){
            sharedViewModel.setCurrentSongNumber(currentSongNumber-1);
        }else {
            sharedViewModel.setCurrentSongNumber(currentSongListSize-1);
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

}