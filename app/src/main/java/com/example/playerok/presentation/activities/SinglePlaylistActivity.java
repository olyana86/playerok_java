package com.example.playerok.presentation.activities;

import static com.example.playerok.utils.converters.ArrayStringConverter.stringToArray;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerok.R;
import com.example.playerok.presentation.adapters.SinglePlaylistAdapter;
import com.example.playerok.presentation.fragments.NowPlayingFragment;
import com.example.playerok.presentation.media.MyMediaPlayer;
import com.example.playerok.presentation.models.SongModel;
import com.example.playerok.utils.interfaces.RecyclerViewInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
        assert stringPathArray != null;
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
        while (true) {
            assert cursor != null;
            if (!cursor.moveToNext()) break;
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

        NowPlayingFragment.playlist = singlePlaylistSongs;

        if (singlePlaylistSongs.isEmpty()) {
            noSongsAvailable.setVisibility(View.VISIBLE);
        } else {
            singlePlaylistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            singlePlaylistRecyclerView.setAdapter(new SinglePlaylistAdapter(singlePlaylistSongs, getApplicationContext(), this));
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