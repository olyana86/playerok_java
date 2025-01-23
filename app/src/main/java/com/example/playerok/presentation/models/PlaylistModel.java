package com.example.playerok.presentation.models;

public class PlaylistModel {
    String playlistTitle;
    String playlistCount;
    String playlistDuration;
    String playlistPath;

    public PlaylistModel(String playlistTitle, String playlistCount, String playlistDuration, String playlistPath) {
        this.playlistTitle = playlistTitle;
        this.playlistCount = playlistCount;
        this.playlistDuration = playlistDuration;
        this.playlistPath = playlistPath;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public String getPlaylistCount() {
        return playlistCount;
    }

    public String getPlaylistDuration() {
        return playlistDuration;
    }

    public String getPlaylistPath() {
        return playlistPath;
    }
}
