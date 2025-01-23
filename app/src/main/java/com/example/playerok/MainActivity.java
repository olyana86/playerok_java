package com.example.playerok;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playerok.presentation.activities.AllPlaylistsActivity;
import com.example.playerok.presentation.adapters.SinglePlaylistAdapter;
import com.example.playerok.presentation.fragments.NowPlayingFragment;
import com.example.playerok.presentation.media.MyMediaPlayer;
import com.example.playerok.presentation.models.SongModel;
import com.example.playerok.utils.interfaces.RecyclerViewInterface;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    TextView noSongsAvailable;
    RecyclerView allSongsRecyclerView;
    Button playAllButton, allPlaylistsButton;
    FragmentContainerView nowPlayingWidget;
    ArrayList<SongModel> playlist = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
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

        String permission = getPermission();
        checkPermission(permission, 1);

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (true) {
            assert cursor != null;
            if (!cursor.moveToNext()) break;
            SongModel songData = new SongModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            if (new File(songData.getPath()).exists()) {
                playlist.add(songData);
            }
        }
        cursor.close();

        NowPlayingFragment.playlist = playlist;

        if (playlist.isEmpty()) {
            noSongsAvailable.setVisibility(View.VISIBLE);
        } else {
            allSongsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            allSongsRecyclerView.setAdapter(new SinglePlaylistAdapter(playlist, getApplicationContext(), this));
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

    @Override
    public void onItemClick(int position) {
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.currentIndex = position;
        NowPlayingFragment nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
        assert nowPlayingFragment != null;
        nowPlayingFragment.setResourcesWithMusic();
        nowPlayingFragment.playMusic();
    }


    public static String getPermission() {
        String p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = Manifest.permission.READ_MEDIA_AUDIO;
        } else {
            p =  Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        return p;
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
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
