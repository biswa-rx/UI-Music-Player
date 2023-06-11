package com.example.music;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.music.CallbackInterface.SeekbarUpdateCallback;
import com.example.music.Media.MediaPlayer;
import com.example.music.Media.MusicController;
import com.example.music.Utils.TimeConverter;
import com.example.music.ViewModel.SharedViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener, SeekbarUpdateCallback {
    private SharedViewModel sharedViewModel;
    ConstraintLayout bs_main;
    ImageView songImageView, bs_down_arrow;
    ImageButton btNext, btPrevious, btRepeat, btSuffle;
    ;
    ToggleButton btPlayPause;
    TextView tvSongName, tvSongEnd, tvSongLive, tvArtistName;
    SeekBar bs_seekBar;
    ImageButton downArrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);//overridePendingTransition(creating,pausing)
        setContentView(R.layout.activity_play_music);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initUi();
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            Uri fileUri = intent.getData();
            MusicController.getInstance().playMusic(this, fileUri);
            btPlayPause.setChecked(false);
        }
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.getCurrentSong().observe(this, new Observer<File>() {
            @Override
            public void onChanged(File file) {
                playMusicThemeGenerator(file);
            }
        });

        btPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btPlayPause.isChecked()) {
                    MusicController.getInstance().pauseMusic();
                } else {
                    MusicController.getInstance().justPlay();
                }
            }
        });
        downArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        MediaPlayer.getInstance().setSeekbarUpdateCallback(this);
        bs_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    String currentTime = TimeConverter.millisecondToString(progress);
                    tvSongLive.setText(currentTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                MediaPlayer.seekUpdate = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicController.getInstance().musicSeekTo(seekBar.getProgress());
                MediaPlayer.seekUpdate = true;
            }
        });
    }


    void initUi() {
        btPlayPause = findViewById(R.id.play_button);
        btNext = findViewById(R.id.next_button);
        btPrevious = findViewById(R.id.previous_button);
        btSuffle = findViewById(R.id.suffle_button);
        btRepeat = findViewById(R.id.repeat_button);
        songImageView = findViewById(R.id.bs_song_image);
        tvSongName = findViewById(R.id.bs_song_name_tv);
        bs_main = findViewById(R.id.bs_main_layout);
        bs_seekBar = findViewById(R.id.bs_player_seekBar);
        tvSongEnd = findViewById(R.id.song_end_tv);
        tvSongLive = findViewById(R.id.song_live_tv);
        tvArtistName = findViewById(R.id.bs_song_artist_name);
        downArrowButton = findViewById(R.id.down_arrow_button);

        tvSongName.setSelected(true);

        btNext.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        btSuffle.setOnClickListener(this);
        btRepeat.setOnClickListener(this);

        if (MediaPlayer.getInstance().isPause()) {
            btPlayPause.setChecked(true);
        } else {
            btPlayPause.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {

    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }

    private void setTextVibrantColor(TextView textView, byte[] imageData) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int dominantColor = palette.getDominantColor(getResources().getColor(R.color.pink));
                // Invert the RGB values of the dominant color
                int invertedColor = dominantColor ^ 0x00FFFFFF; // XOR with 0x00FFFFFF
                textView.setTextColor(invertedColor);
                updateButtonColor(invertedColor);
            }
        });
    }

    private void updateButtonColor(int invertedColor) {
        ColorStateList colorStateList = ColorStateList.valueOf(invertedColor);
        btPlayPause.setBackgroundTintList(colorStateList);
        btNext.setBackgroundTintList(colorStateList);
        btPrevious.setBackgroundTintList(colorStateList);
        btRepeat.setBackgroundTintList(colorStateList);
        btSuffle.setBackgroundTintList(colorStateList);
    }

    private void playMusicThemeGenerator(File file) {
        if (file != null) {
            btPlayPause.setChecked(false);
            tvSongName.setText(file.getName().replace(".mp3", "").replace("_", " "));
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(file.getPath());
            tvArtistName.setText(
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) == null
                            ? "Unknown artist" : retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            );
            byte[] image = getAlbumArt(file.getPath());
            if (image != null) {
                setTextVibrantColor(tvSongName, image);
                Glide.with(PlayMusicActivity.this)
                        .load(image)
                        .apply(new RequestOptions().transforms(new CropTransformation(150, 300, CropTransformation.CropType.TOP),
                                new BlurTransformation(10, 1),
                                new ColorFilterTransformation(Color.argb(100, 0, 0, 0))
                        ))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                                        @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                bs_main.setBackgroundDrawable(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                bs_main.setBackground(null);
                            }
                        });
                Glide.with(getBaseContext()).asBitmap()
                        .load(image)
                        .into(songImageView);
            } else {
                Glide.with(getBaseContext()).asBitmap()
                        .load(R.drawable.music_theme_bg)
                        .into(songImageView);
                Glide.with(PlayMusicActivity.this)
                        .load(R.drawable.layout_bg)
                        .apply(new RequestOptions().transforms(new CropTransformation(150, 300, CropTransformation.CropType.TOP),
                                new BlurTransformation(3, 1),
                                new ColorFilterTransformation(Color.argb(20, 0, 0, 0))
                        ))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                                        @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                                bs_main.setBackgroundDrawable(resource);
                            }
                        });
            }
        }
    }

    // Method to convert Drawable to Bitmap
    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    // Method to convert Drawable to byte array
    public byte[] drawableToBytes(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onSeekbarUpdate(int mediaProgress, int mediaMaxProgress) {
        System.out.println("MEDIA PROGRESS " + mediaProgress + " --- MAX PROGRESS " + mediaMaxProgress);
        bs_seekBar.setMax(mediaMaxProgress);
        bs_seekBar.setProgress(mediaProgress);
        tvSongEnd.setText(TimeConverter.millisecondToString(mediaMaxProgress));
        tvSongLive.setText(TimeConverter.millisecondToString(mediaProgress));
    }

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