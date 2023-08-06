package com.example.music;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.music.Media.MusicController;
import com.example.music.Notification.MusicService;
import com.example.music.Utils.MediaData;
import com.example.music.Utils.PlaySerializer;
import com.example.music.ViewModel.SharedViewModel;
import com.example.music.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedViewModel sharedViewModel;
    MusicController musicController;
    ConstraintLayout bottom_sheet_player;
    ImageView smallPlayerAlbum;
    ToggleButton mainUiPlayBT;
    TextView tvMainSongName, tvMainSongArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        musicController = new MusicController();

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.listenNow, R.id.myLibrary, R.id.playlists, R.id.action_settings, R.id.action_help, R.id.action_feed_back)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        tvMainSongName = findViewById(R.id.tv_main_song_name);
        tvMainSongName.setSelected(true);
        tvMainSongArtist = findViewById(R.id.artist_name);

        smallPlayerAlbum = findViewById(R.id.music_image);
        mainUiPlayBT = findViewById(R.id.play_button_main_ui);
        mainUiPlayBT.setChecked(true);

        SharedPreferences sharedPreferences = getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE);


        bottom_sheet_player = findViewById(R.id.botton_sheet_view_triger);
        bottom_sheet_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicController.isMusicPlaying()) {
                    startActivity(new Intent(MainActivity.this, PlayMusicActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Currently no song playing", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainUiPlayBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainUiPlayBT.isChecked()) {
                    if (MusicController.getInstance().isMusicPlaying()) {
                        MusicController.getInstance().pauseMusic();
                    }
                } else {
                    if (sharedViewModel.getCurrentSong().getValue() == null || !MusicController.getInstance().isMusicPlaying()) {
                        System.out.println("Nothing to  play... Here I have to implement shared preference for previous playlist detect and play");
                        if(sharedPreferences.getInt("PlaylistNumber",-1) == -1){
                            System.out.println("Playlist "+ -1);
                            sharedViewModel.setMutableCurrentSongList(sharedViewModel.getAllSongList().getValue());
                        }else{
                            System.out.println("Playlist "+sharedPreferences.getInt("PlaylistNumber ",-1));
                            System.out.println("SongNumber "+sharedPreferences.getInt("SongNumber ",0));
                            sharedViewModel.setMutableCurrentSongListFromFolder(sharedPreferences.getInt("PlaylistNumber",-1));
                        }
                        sharedViewModel.setCurrentSongNumber(sharedPreferences.getInt("SongNumber",0));
                        ArrayList<File> songList = sharedViewModel.getCurrentSongList().getValue();
                        PlaySerializer.getInstance().setMusicList(songList);
                        PlaySerializer.getInstance().setSelectedIndex(sharedPreferences.getInt("SongNumber",0));
                        File file = songList.get(sharedPreferences.getInt("SongNumber",0));
                        Uri uri = Uri.parse(file.toString());
                        Intent playIntent = new Intent(getApplicationContext(), MusicService.class);
                        playIntent.setAction("PLAY");
                        playIntent.putExtra("URI", uri);
                        getApplicationContext().startService(playIntent);
                        MusicController.getInstance().setSongNumber(sharedPreferences.getInt("SongNumber",0));

                    } else {
                        MusicController.getInstance().justPlay();
                    }
                }
            }
        });


        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getCurrentSong().observe(this, new Observer<File>() {
            @Override
            public void onChanged(File file) {
                tvMainSongName.setText(file.getName().replace(".mp3", ""));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] image = getAlbumArt(file.getPath());
                        String artistName = MediaData.getSongArtist(file.getPath());
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvMainSongArtist.setText(artistName);
                                if (image != null) {
                                    Glide.with(getBaseContext()).asBitmap()
                                            .load(image)
                                            .into(smallPlayerAlbum);
                                } else {
                                    Glide.with(getBaseContext()).asBitmap()
                                            .load(R.drawable.icon)
                                            .into(smallPlayerAlbum);
                                }
                            }
                        });

                    }
                }).start();

            }
        });
        // Register the receiver
        IntentFilter filter = new IntentFilter("com.example.ACTION_UPDATE_VIEW");
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, filter);
        // Register the receiver
        IntentFilter musicPauseFilter = new IntentFilter("com.example.ACTION_MUSIC");
        LocalBroadcastManager.getInstance(this).registerReceiver(pauseReceiver, musicPauseFilter);
    }

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sharedViewModel.setCurrentSongNumber(PlaySerializer.getInstance().getSelectedIndex());
        }
    };
    private BroadcastReceiver pauseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MusicController.getInstance().isMusicPaused()) {
                mainUiPlayBT.setChecked(true);
            } else {
                mainUiPlayBT.setChecked(false);
            }
        }
    };

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_action_refresh:
                triggerRebirth(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MusicController.getInstance().isMusicPaused() || !MusicController.getInstance().isMusicPlaying()) {
            mainUiPlayBT.setChecked(true);
        } else {
            mainUiPlayBT.setChecked(false);
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister the receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pauseReceiver);
        super.onDestroy();
    }
}