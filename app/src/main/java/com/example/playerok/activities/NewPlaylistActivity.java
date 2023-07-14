package com.example.playerok.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playerok.adapters.NewPlaylistAdapter;
import com.example.playerok.PlayerokDatabase;
import com.example.playerok.R;
import com.example.playerok.models.CheckedSongModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class NewPlaylistActivity extends AppCompatActivity {

    EditText editableTitleOfPlaylist;
    Button saveSongsListButton, saveTitleButton;
    TextView chooseSongsTextView;
    RecyclerView newPlaylistRecyclerView;
    ArrayList<CheckedSongModel> newPlaylist = new ArrayList<>();
    ArrayList<String> checkedSongsPath = new ArrayList<>();
    long songsDuration = 0;
    int songsCount = 0;
    String playlistTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_playlist);

        editableTitleOfPlaylist = findViewById(R.id.editable_title_of_playlist);
        saveTitleButton = findViewById(R.id.ready_button);
        saveSongsListButton = findViewById(R.id.save_playlist_button);
        chooseSongsTextView = findViewById(R.id.choose_songs_textview);
        newPlaylistRecyclerView = findViewById(R.id.new_playlist_recyclerview);

        saveTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerokDatabase playerokDatabase = new PlayerokDatabase(getApplicationContext());
                String title = editableTitleOfPlaylist.getText().toString().trim();
                if (playerokDatabase.checkTitleExist(title)) {
                    Toast.makeText(NewPlaylistActivity.this, "Такое название уже существует. Предложите другое!", Toast.LENGTH_LONG).show();
                } else
                    playlistTitle = editableTitleOfPlaylist.getText().toString().trim();
            }
        });

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            CheckedSongModel songData = new CheckedSongModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), false);
            if (new File(songData.getPath()).exists()) {
                newPlaylist.add(songData);
            }
        }
        cursor.close();

        newPlaylistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newPlaylistRecyclerView.setItemViewCacheSize(newPlaylist.size());
        newPlaylistRecyclerView.setAdapter(new NewPlaylistAdapter(newPlaylist, getApplicationContext(), checkedSongsPath, songsDuration, songsCount));

        saveSongsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playlistTitle == null) {
                    Toast.makeText(NewPlaylistActivity.this, "Введите название плейлиста.", Toast.LENGTH_LONG).show();
                } else
                    if (NewPlaylistAdapter.songsCount == 0) {
                    Toast.makeText(NewPlaylistActivity.this, "Выберите песни для плейлиста.", Toast.LENGTH_LONG).show();
                    } else {
                        String songslistName = playlistTitle;
                        String songslistSongs = arrayToString(NewPlaylistAdapter.checkedSongsPath);
                        String songslistNumber = Integer.toString(NewPlaylistAdapter.songsCount);
                        String songslistDuration = Long.toString(NewPlaylistAdapter.songsDuration);
                        PlayerokDatabase playerokDatabase = new PlayerokDatabase(getApplicationContext());
                        playerokDatabase.addSongslist(songslistName, songslistSongs, songslistNumber, songslistDuration);
                        Toast.makeText(NewPlaylistActivity.this, "Ваш плейлист успешно сохранён", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), AllPlaylistsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }
            }
        });
    }
    public String arrayToString (ArrayList<String> pathArray) {
        StringBuilder builder = new StringBuilder();
        builder.append(pathArray.get(0));
        for (int i = 1; i < pathArray.size(); i++) {
            builder.append("AND").append(pathArray.get(i));
        }
        String stringPathArray = builder.toString();
        return  stringPathArray;
    }


}