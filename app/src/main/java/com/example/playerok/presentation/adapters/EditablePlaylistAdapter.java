package com.example.playerok.presentation.adapters;

import static com.example.playerok.utils.converters.TimeConverter.convertToMMSS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playerok.R;
import com.example.playerok.presentation.models.CheckedSongModel;

import java.util.ArrayList;


public class EditablePlaylistAdapter extends RecyclerView.Adapter<EditablePlaylistAdapter.ViewHolder> {
    ArrayList<CheckedSongModel> editablePlaylist;
    Context context;
    public static ArrayList<String> checkedSongsPath;
    ArrayList<String> oldCheckedSongsPath = new ArrayList<>();
    public static long songsDuration = 0;
    public static int songsCount = 0;

    public EditablePlaylistAdapter(ArrayList<CheckedSongModel> newPlaylist, Context context, ArrayList<String> oldCheckedSongsPath, ArrayList<String> checkedSongsPath, long songsDuration, int songsCount) {
        this.editablePlaylist = newPlaylist;
        this.context = context;
        this.oldCheckedSongsPath = oldCheckedSongsPath;
        this.checkedSongsPath = checkedSongsPath;
        this.songsDuration = songsDuration;
        this.songsCount = songsCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_checked_song_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditablePlaylistAdapter.ViewHolder holder, int position) {
        CheckedSongModel songData = editablePlaylist.get(position);
        holder.songTitleTextView.setText(songData.getTitle());
        holder.songArtistTextView.setText(songData.getArtist());
        holder.songDurationTextView.setText(convertToMMSS(songData.getDuration()));

        for (String path : oldCheckedSongsPath) {
            if (path.equals(editablePlaylist.get(position).getPath())) {
                songData = editablePlaylist.get(holder.getAdapterPosition());
                String songPath = songData.getPath();
                editablePlaylist.get(holder.getAdapterPosition()).setSelected(true);
                checkedSongsPath.add(songPath);
                songsDuration += Long.parseLong(songData.getDuration());
                songsCount = songsCount + 1;
                holder.songCheckbox.setChecked(true);
            }
        }

        holder.songCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckedSongModel songData = editablePlaylist.get(holder.getAdapterPosition());
                String songPath = songData.getPath();

                if (isChecked) {
                    editablePlaylist.get(holder.getAdapterPosition()).setSelected(true);
                    checkedSongsPath.add(songPath);
                    songsDuration += Long.parseLong(songData.getDuration());
                    songsCount = songsCount + 1;
                } else {
                    editablePlaylist.get(holder.getAdapterPosition()).setSelected(false);
                    checkedSongsPath.remove(songPath);
                    songsDuration -= Long.parseLong(songData.getDuration());
                    songsCount = songsCount - 1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return editablePlaylist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitleTextView, songArtistTextView, songDurationTextView;
        CheckBox songCheckbox;

        public ViewHolder(View itemView) {
            super(itemView);
            songTitleTextView = itemView.findViewById(R.id.ed_song_title_text);
            songArtistTextView = itemView.findViewById(R.id.ed_song_artist_text);
            songDurationTextView = itemView.findViewById(R.id.ed_song_duration);
            songCheckbox = itemView.findViewById(R.id.ed_song_checkbox);
        }
    }
}
