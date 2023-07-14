package com.example.playerok.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.playerok.MyMediaPlayer;
import com.example.playerok.NowPlayingFragment;
import com.example.playerok.R;
import com.example.playerok.RecyclerViewInterface;
import com.example.playerok.adapters.SongsListAdapter;
import com.example.playerok.models.SongModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SinglePlaylistActivity extends AppCompatActivity implements RecyclerViewInterface {

    TextView singlePlaylistTitle, noSongsAvailable;
    Button editPlaylistButton;
    RecyclerView singlePlaylistRecyclerView;
    FragmentContainerView nowPlayingWidget;
    ArrayList<SongModel> singlePlaylistSongs = new ArrayList<>();
    ArrayList<String> allSongsPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_playlist);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.single_playlist_player_fragment_container_view,
                            NowPlayingFragment.class, null)
                    .commit();
        }

        singlePlaylistTitle = findViewById(R.id.single_playlist_title);
        noSongsAvailable = findViewById(R.id.no_songs_available_single_playlist);
        editPlaylistButton = findViewById(R.id.edit_playlist_button);
        singlePlaylistRecyclerView = findViewById(R.id.single_playlist_recyclerview);
        nowPlayingWidget = findViewById(R.id.single_playlist_player_fragment_container_view);

        String playlistTitle = getIntent().getStringExtra("playlistTitle");
        singlePlaylistTitle.setText(playlistTitle);

        String stringPathArray = getIntent().getStringExtra("playlistPath");
        allSongsPath = stringToArray(stringPathArray);

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, null);
        while (cursor.moveToNext()) {
            SongModel songData = new SongModel(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3));
            if (new File(songData.getPath()).exists()) {
                for (String path : allSongsPath) {
                    if (path.equals(songData.getPath())) {
                        singlePlaylistSongs.add(songData);
                    }
                }
            }
        }
        cursor.close();

        NowPlayingFragment.songsList = singlePlaylistSongs;

        if (singlePlaylistSongs.size() == 0) {
            noSongsAvailable.setVisibility(View.VISIBLE);
      } else {
            singlePlaylistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            singlePlaylistRecyclerView.setAdapter(new SongsListAdapter(singlePlaylistSongs, getApplicationContext(), this));
      }

        editPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditablePlaylistActivity.class);
                intent.putExtra("playlistTitle", playlistTitle);
                intent.putExtra("playlistPath", stringPathArray);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.currentIndex = position;
        NowPlayingFragment nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.single_playlist_player_fragment_container_view);
        assert nowPlayingFragment != null;
        nowPlayingFragment.setResourcesWithMusic();
        nowPlayingFragment.playMusic();
    }

    public ArrayList<String> stringToArray (String stringPathArray){
        ArrayList<String> pathArray = new ArrayList<>(Arrays.asList(stringPathArray.split("AND")));
        return pathArray;
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyMediaPlayer.getInstance().isPlaying();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyMediaPlayer.getInstance().stop();
    }
}