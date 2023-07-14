package com.example.playerok.models;

import java.io.Serializable;

public class SongModel implements Serializable {
    String title;
    String artist;
    String path;
    String duration;

    public SongModel(String title, String artist, String path, String duration) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
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

}
