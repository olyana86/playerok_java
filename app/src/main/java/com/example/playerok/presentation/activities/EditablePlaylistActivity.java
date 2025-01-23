package com.example.playerok.presentation.activities;

import static com.example.playerok.utils.converters.ArrayStringConverter.arrayToString;
import static com.example.playerok.utils.converters.ArrayStringConverter.stringToArray;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerok.R;
import com.example.playerok.data.PlayerOkDatabase;
import com.example.playerok.presentation.adapters.EditablePlaylistAdapter;
import com.example.playerok.presentation.models.CheckedSongModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class EditablePlaylistActivity extends AppCompatActivity {

    EditText editablePlaylistTitle;
    Button titleReadyButton, saveEditedPlaylistButton, deletePlaylistButton;
    RecyclerView editablePlaylistRecycleView;
    ArrayList<CheckedSongModel> allSongsArrayList = new ArrayList<>();
    ArrayList<String> checkedSongsPath = new ArrayList<>();
    ArrayList<String> oldCheckedSongsPath = new ArrayList<>();
    String thisPlaylistTitle, songsPath, originalPlaylistName, changedPlaylistTitle,
            playlistName, playlistSongs, playlistNumber, playlistDuration;
    long songsDuration = 0;
    int songsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_playlist);

        editablePlaylistTitle = findViewById(R.id.existed_title_of_playlist);
        titleReadyButton = findViewById(R.id.title_ready_button);
        saveEditedPlaylistButton = findViewById(R.id.save_edited_playlist_button);
        deletePlaylistButton = findViewById(R.id.delete_playlist_button);
        editablePlaylistRecycleView = findViewById(R.id.editable_playlist_recyclerview);

        thisPlaylistTitle = getIntent().getStringExtra("playlistTitle");
        originalPlaylistName = thisPlaylistTitle;
        editablePlaylistTitle.setText(thisPlaylistTitle);

        titleReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerOkDatabase playerOkDatabase = new PlayerOkDatabase(getApplicationContext());
                String title = editablePlaylistTitle.getText().toString().trim();
                if (playerOkDatabase.checkIfPlaylistTitleIsExist(title)) {
                    Toast.makeText(EditablePlaylistActivity.this, "Такое название уже существует. Предложите другое!", Toast.LENGTH_LONG).show();
                } else
                    changedPlaylistTitle = editablePlaylistTitle.getText().toString().trim();
            }
        });

        songsPath = getIntent().getStringExtra("playlistPath");
        assert songsPath != null;
        oldCheckedSongsPath = stringToArray(songsPath);

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
            CheckedSongModel songData = new CheckedSongModel(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3),false);
            if (new File(songData.getPath()).exists()) {
                allSongsArrayList.add(songData);
            }
        }
        cursor.close();

        editablePlaylistRecycleView.setLayoutManager(new LinearLayoutManager(this));
        editablePlaylistRecycleView.setItemViewCacheSize(allSongsArrayList.size());
        editablePlaylistRecycleView.setAdapter(new EditablePlaylistAdapter(allSongsArrayList, getApplicationContext(), oldCheckedSongsPath, checkedSongsPath, songsDuration, songsCount));

        saveEditedPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editablePlaylistTitle == null) {
                    Toast.makeText(EditablePlaylistActivity.this, "Введите название плейлиста.", Toast.LENGTH_LONG).show();
                } else
                if (EditablePlaylistAdapter.songsCount == 0) {
                    Toast.makeText(EditablePlaylistActivity.this, "Выберите песни для плейлиста.", Toast.LENGTH_LONG).show();
                } else {
                    if (changedPlaylistTitle == null) {
                        playlistName = thisPlaylistTitle;
                    } else
                        playlistName = changedPlaylistTitle;
                }
                playlistSongs = arrayToString(EditablePlaylistAdapter.checkedSongsPath);
                playlistNumber = Integer.toString(EditablePlaylistAdapter.songsCount);
                playlistDuration = Long.toString(EditablePlaylistAdapter.songsDuration);
                PlayerOkDatabase playerOkDatabase = new PlayerOkDatabase(getApplicationContext());
                playerOkDatabase.updatePlayList(originalPlaylistName, playlistName, playlistSongs, playlistNumber, playlistDuration);
                Toast.makeText(EditablePlaylistActivity.this, "Ваш плейлист успешно обновлён", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AllPlaylistsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });

        deletePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerOkDatabase playerOkDatabase = new PlayerOkDatabase(getApplicationContext());
                playerOkDatabase.deletePlayList(originalPlaylistName);
                Toast.makeText(EditablePlaylistActivity.this, "Ваш плейлист удалён", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AllPlaylistsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }
}