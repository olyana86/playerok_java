package com.example.playerok.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.playerok.adapters.AllPlaylistsAdapter;
import com.example.playerok.PlayerokDatabase;
import com.example.playerok.models.PlaylistModel;
import com.example.playerok.R;

import java.util.ArrayList;

public class AllPlaylistsActivity extends AppCompatActivity {

    TextView myPlaylistsTitle;
    RecyclerView listOfPlaylistsRecyclerView;
    Button newPlaylistButton;
    ArrayList<PlaylistModel> allPlaylistsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_playlists);

        myPlaylistsTitle = findViewById(R.id.my_playlists_title);
        listOfPlaylistsRecyclerView = findViewById(R.id.list_of_playlists_recyclerview);
        newPlaylistButton = findViewById(R.id.new_playlist_button);

        PlayerokDatabase playerokDatabase = new PlayerokDatabase(getApplicationContext());
        allPlaylistsArrayList = playerokDatabase.readPlaylists();

        listOfPlaylistsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listOfPlaylistsRecyclerView.setItemViewCacheSize(allPlaylistsArrayList.size());
        listOfPlaylistsRecyclerView.setAdapter(new AllPlaylistsAdapter(allPlaylistsArrayList,getApplicationContext()));


        newPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewPlaylistActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }
}