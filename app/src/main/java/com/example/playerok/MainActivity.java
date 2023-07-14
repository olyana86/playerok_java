package com.example.playerok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playerok.activities.AllPlaylistsActivity;
import com.example.playerok.adapters.SongsListAdapter;
import com.example.playerok.models.SongModel;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    TextView noSongsAvailable;
    RecyclerView allSongsRecyclerView;
    Button playAllButton, allPlaylistsButton;
    FragmentContainerView nowPlayingWidget;
    ArrayList<SongModel> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, NowPlayingFragment.class, null)
                    .commit();
        }

        noSongsAvailable = findViewById(R.id.no_songs_available);
        playAllButton = findViewById(R.id.play_all_button);
        allSongsRecyclerView = findViewById(R.id.all_songs_available);
        nowPlayingWidget = findViewById(R.id.fragment_container_view);
        allPlaylistsButton = findViewById(R.id.playlists_button);

        if(!checkPermission()) {
            requestPermission();
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            SongModel songData = new SongModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            if (new File(songData.getPath()).exists()) {
                songsList.add(songData);
            }
        }
        cursor.close();

        NowPlayingFragment.songsList = songsList;

        if (songsList.size() == 0) {
            noSongsAvailable.setVisibility(View.VISIBLE);
        } else {
            allSongsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            allSongsRecyclerView.setAdapter(new SongsListAdapter(songsList, getApplicationContext(), this));
        }

        playAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = 0;
                NowPlayingFragment nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
                assert nowPlayingFragment != null;
                nowPlayingFragment.setResourcesWithMusic();
                nowPlayingFragment.playMusic();
            }
        });

        allPlaylistsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllPlaylistsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Нужно разрешение на поиск музыки на устройстве. Пожалуйста, отметьте его в настройках", Toast.LENGTH_LONG).show();
        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }

    @Override
    public void onItemClick(int position) {
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.currentIndex = position;
        NowPlayingFragment nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
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