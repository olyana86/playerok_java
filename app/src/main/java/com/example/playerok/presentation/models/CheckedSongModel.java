package com.example.playerok.presentation.models;

import java.io.Serializable;

public class CheckedSongModel implements Serializable {
    String title;
    String artist;
    String path;
    String duration;
    boolean isSelected;

    public CheckedSongModel(String title, String artist, String path, String duration, boolean isSelected) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public String getDuration() {
        return duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
