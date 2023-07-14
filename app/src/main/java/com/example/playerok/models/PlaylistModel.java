package com.example.playerok.models;

public class PlaylistModel {
    String playlistTitle;
    String playlistCount;
    String playlistDuration;
    String playlistPath;

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getPlaylistCount() {
        return playlistCount;
    }

    public void setPlaylistCount(String playlistCount) {
        this.playlistCount = playlistCount;
    }

    public String getPlaylistDuration() {
        return playlistDuration;
    }

    public void setPlaylistDuration(String playlistDuration) {
        this.playlistDuration = playlistDuration;
    }

    public String getPlaylistPath() {
        return playlistPath;
    }

    public void setPlaylistPath(String playlistPath) {
        this.playlistPath = playlistPath;
    }

    public PlaylistModel(String playlistTitle, String playlistCount, String playlistDuration, String playlistPath) {
        this.playlistTitle = playlistTitle;
        this.playlistCount = playlistCount;
        this.playlistDuration = playlistDuration;
        this.playlistPath = playlistPath;
    }
}
