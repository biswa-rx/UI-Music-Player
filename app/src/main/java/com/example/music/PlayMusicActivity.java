package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener{
    ConstraintLayout bs_main;
    ImageView songImageView,bsMenu,bs_down_arrow,smallPlayerAlbum;
    ImageButton btNext,btPrevious,btRepeat,btSuffle;;
    ToggleButton btPlayPause,mainUiPlayBT;
    TextView tvSongName,tvSongEnd,tvSongLive,tvMainSongName;
    SeekBar bs_seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        btPlayPause = findViewById(R.id.play_button);
        btNext = findViewById(R.id.next_button);
        btPrevious = findViewById(R.id.previous_button);
        btSuffle = findViewById(R.id.suffle_button);
        btRepeat = findViewById(R.id.repeat_button);
        songImageView = findViewById(R.id.bs_song_image);
        tvSongName = findViewById(R.id.bs_song_name_tv);
        bs_down_arrow = findViewById(R.id.down_arrow_button);
        bs_main = findViewById(R.id.bs_main_layout);
        bs_seekBar = findViewById(R.id.bs_player_seekBar);
        tvSongEnd = findViewById(R.id.song_end_tv);
        tvSongLive = findViewById(R.id.song_live_tv);

        tvSongName.setSelected(true);

        btNext.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        btSuffle.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        bs_down_arrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

//       bs_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
//            String currentTime = createTime(progress);
//            tvSongLive.setText(currentTime);
//        }
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            updateSeekFlag = true;
//        }
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            mediaPlayer.seekTo(seekBar.getProgress());
//            updateSeekFlag = false;
//
//        }
//    });
//
//    update_seek = new Thread(){
//        @Override
//        public void run() {
//            int currentPosition = 0;
//
//            while(true) {
//                if (!updateSeekFlag) {
//                    if(mediaPlayer != null) {
//                        currentPosition = mediaPlayer.getCurrentPosition();
//                        bs_seekBar.setProgress(currentPosition);
//                    }
//                }
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };
//        update_seek.start();
}

//btPlayPause.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//        if(btPlayPause.isChecked()){
//        mediaPlayer.pause();
//        }else{
//        mediaPlayer.start();
//        }
//        }
//        });
//
//        mainUiPlayBT.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//        if(mediaPlayer!=null) {
//        if (mainUiPlayBT.isChecked()) {
//        mediaPlayer.pause();
//        } else {
//        mediaPlayer.start();
//        }
//        }
//        }
//        });
//}