package com.example.playerok.models;

import java.io.Serializable;

public class CheckedSongModel implements Serializable {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isSelected() {return isSelected;}

    public void setSelected(boolean selected) {isSelected = selected;}

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
}