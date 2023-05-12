package com.example.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.music.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedViewModel sharedViewModel;
    MusicController musicController;
    ConstraintLayout bottom_sheet_player;

    private static final String TAG = "MainActivity";


    ImageView smallPlayerAlbum;
    ToggleButton mainUiPlayBT;
    TextView tvMainSongName;

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
                R.id.listenNow, R.id.myLibrary, R.id.playlists,R.id.action_settings,R.id.action_help,R.id.action_feed_back)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        tvMainSongName = findViewById(R.id.tv_main_song_name);
        tvMainSongName.setSelected(true);

        smallPlayerAlbum = findViewById(R.id.music_image);
        mainUiPlayBT = findViewById(R.id.play_button_main_ui);


        bottom_sheet_player = findViewById(R.id.botton_sheet_view_triger);
        bottom_sheet_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicController.isMusicPlaying()){
                    startActivity(new Intent(MainActivity.this,PlayMusicActivity.class));
                }else{
                    Toast.makeText(MainActivity.this,"Currently no song playing", Toast.LENGTH_SHORT).show();
                }
            }
        });


        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getCurrentSong().observe(this, new Observer<File>() {
            @Override
            public void onChanged(File file) {
                mainUiPlayBT.setChecked(false);
                tvMainSongName.setText(file.getName().replace(".mp3",""));

                byte[] image = getAlbumArt(file.getPath());
                if(image != null){
                    Glide.with(getBaseContext()).asBitmap()
                            .load(image)
                            .into(smallPlayerAlbum);
                }else{
                    Glide.with(getBaseContext()).asBitmap()
                            .load(R.drawable.icon)
                            .into(smallPlayerAlbum);
                }
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menu_action_refresh:
                triggerRebirth(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private byte[] getAlbumArt(String uri){
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
    protected void onPause() {
        super.onPause();
//        Animatoo.INSTANCE.animateSlideUp(this);
    }
}