package com.example.playerok.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.playerok.presentation.models.PlaylistModel;

import java.util.ArrayList;

public class PlayerOkDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "playerok_db";
    private static final String TABLE_NAME = "playlists_table";
    private static final int DATABASE_VERSION = 1;
    private static final String ID = "id";
    private static final String COLUMN_PLAYLIST_NAME = "playlist_name";
    private static final String COLUMN_PLAYLIST_SONGS = "playlist_songs";
    private static final String COLUMN_PLAYLIST_SONGS_NUMBER = "playlist_songs_number";
    private static final String COLUMN_PLAYLIST_SONGS_DURATION = "playlist_songs_duration";
    private Context context;

    public PlayerOkDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context = this.context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_PLAYLIST_NAME + " TEXT,"
                    + COLUMN_PLAYLIST_SONGS + " TEXT, "
                    + COLUMN_PLAYLIST_SONGS_NUMBER + " TEXT, "
                    + COLUMN_PLAYLIST_SONGS_DURATION + " TEXT)";
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPlaylist(String playlistName, String playlistSongs,
                            String playlistNumber, String playlistDuration) {
        SQLiteDatabase playerok_db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_NAME, playlistName);
        values.put(COLUMN_PLAYLIST_SONGS, playlistSongs);
        values.put(COLUMN_PLAYLIST_SONGS_NUMBER, playlistNumber);
        values.put(COLUMN_PLAYLIST_SONGS_DURATION, playlistDuration);
        playerok_db.insert(TABLE_NAME, null, values);
        playerok_db.close();
    }

    public void updatePlayList(String originalPlaylistName, String playlistName,
                               String playlistSongs, String playlistNumber,
                               String playlistDuration) {
        SQLiteDatabase playerok_db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_NAME, playlistName);
        values.put(COLUMN_PLAYLIST_SONGS, playlistSongs);
        values.put(COLUMN_PLAYLIST_SONGS_NUMBER, playlistNumber);
        values.put(COLUMN_PLAYLIST_SONGS_DURATION, playlistDuration);
        playerok_db.update(TABLE_NAME, values, "playlist_name=?", new String[]{originalPlaylistName});
        playerok_db.close();
    }

    public void deletePlayList(String originalPlaylistName) {
        SQLiteDatabase playerok_db = this.getWritableDatabase();
        playerok_db.delete(TABLE_NAME, "playlist_name=?", new String[]{originalPlaylistName});
        playerok_db.close();
    }

    public ArrayList<PlaylistModel> readPlaylists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses
                = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<PlaylistModel> allPlaylistsArrayList = new ArrayList<>();
        if (cursorCourses.moveToFirst()) {
            do {
                allPlaylistsArrayList.add(new PlaylistModel(
                        cursorCourses.getString(1),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4),
                        cursorCourses.getString(2)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return allPlaylistsArrayList;
    }

    public boolean checkIfPlaylistTitleIsExist(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PLAYLIST_NAME + " = " + "'" + title + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
