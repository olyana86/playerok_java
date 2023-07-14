package com.example.playerok;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.playerok.models.SongModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NowPlayingFragment extends Fragment {

    SeekBar seekBar;
    TextView playingSongTitle, playingSongArtist, currentTime, totalTime;
    ImageView playPause, skipPrevious, skipNext, shuffle, repeatOne;
    public static ArrayList<SongModel> songsList;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    SongModel currentSong;
    Runnable mTicker;
    boolean flagRepeatOneSong = false;
    boolean flagPlayShuffle = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        seekBar = view.findViewById(R.id.seekbar);
        playingSongTitle = view.findViewById(R.id.playing_song_title);
        playingSongArtist = view.findViewById(R.id.playing_song_artist);
        currentTime = view.findViewById(R.id.current_time);
        totalTime = view.findViewById(R.id.total_time);
        playPause = view.findViewById(R.id.play_pause);
        skipPrevious = view.findViewById(R.id.skip_previous);
        skipNext = view.findViewById(R.id.skip_next);
        shuffle = view.findViewById(R.id.shuffle);
        repeatOne = view.findViewById(R.id.repeat_one);

        playingSongTitle.setSelected(true);

        try {
            setResourcesWithMusic();
        } catch (IndexOutOfBoundsException ignored) {

        }

    }

    public void setResourcesWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        playingSongTitle.setText(currentSong.getTitle());
        playingSongArtist.setText(currentSong.getArtist());
        totalTime.setText(convertToMMSS(currentSong.getDuration()));

        playPause.setOnClickListener(v-> playMusic());
        skipNext.setOnClickListener(v-> playNextSong());
        skipPrevious.setOnClickListener(v-> playPreviousSong());
        shuffle.setOnClickListener(v -> playShuffle());
        repeatOne.setOnClickListener(v -> repeatOneSong());
    }

    public void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTicker = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(convertToMMSS(String.valueOf(mediaPlayer.getCurrentPosition())));

                    if (mediaPlayer.isPlaying()) {
                        playPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    } else {
                        playPause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    }
                }
                new Handler().postDelayed((Runnable) this, 100);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (mediaPlayer != null && fromUser) {
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                if (!flagRepeatOneSong) {
                    mediaPlayer.setOnCompletionListener(mp -> skipNext.performClick());
                } else {
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            skipNext.performClick();
                            skipPrevious.performClick();
                        }
                    });
                }
            }
        };
        mTicker.run();
        playPause.setOnClickListener(v-> pausePlay());
    }

    public void playNextSong() {
        if (!flagPlayShuffle) {
            if (MyMediaPlayer.currentIndex == songsList.size() - 1) {
                MyMediaPlayer.currentIndex = 0;
            } else {
                MyMediaPlayer.currentIndex += 1;
            }
        } else {
            MyMediaPlayer.currentIndex = new Random().nextInt(songsList.size() + 1);
        }
        mediaPlayer.reset();
        setResourcesWithMusic();
        playMusic();
    }

    public void playPreviousSong() {
        if(MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
        playMusic();
    }

    public void pausePlay() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    public void repeatOneSong() {
        flagPlayShuffle = false;
        shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);

        if (flagRepeatOneSong) {
            flagRepeatOneSong = false;
            repeatOne.setImageResource(R.drawable.ic_baseline_repeat_one_24);
        } else {
            flagRepeatOneSong = true;
            repeatOne.setImageResource(R.drawable.ic_baseline_repeat_one_on_24);//
        }
    }

    public void playShuffle() {
        flagRepeatOneSong = false;
        repeatOne.setImageResource(R.drawable.ic_baseline_repeat_one_24);

        if (flagPlayShuffle) {
            flagPlayShuffle = false;
            shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
        } else {
            flagPlayShuffle = true;
            shuffle.setImageResource(R.drawable.ic_baseline_shuffle_on_24);
        }
    }

    public static String convertToMMSS(String duration) {
        Long millis = Long.parseLong(duration);
        if (TimeUnit.MILLISECONDS.toHours(millis) == 0) {
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)); }
        else {
            return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        }
    }
}