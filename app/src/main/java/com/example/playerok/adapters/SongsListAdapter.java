package com.example.playerok.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.playerok.R;
import com.example.playerok.RecyclerViewInterface;
import com.example.playerok.models.SongModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    ArrayList<SongModel> songsList;
    Context context;

    public SongsListAdapter(ArrayList<SongModel> songsList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.songsList = songsList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_song_item, parent, false);
        return new SongsListAdapter.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(SongsListAdapter.ViewHolder holder, int position) {
        SongModel songData = songsList.get(position);
        holder.songTitleTextView.setText(songData.getTitle());
        holder.songArtistTextView.setText(songData.getArtist());
        holder.songDurationTextView.setText(convertToMMSS(songData.getDuration()));
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitleTextView, songArtistTextView, songDurationTextView;
        ImageView songIconImageView;

        public ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            songTitleTextView = itemView.findViewById(R.id.song_title_text);
            songArtistTextView = itemView.findViewById(R.id.song_artist_text);
            songDurationTextView = itemView.findViewById(R.id.song_duration);
            songIconImageView = itemView.findViewById(R.id.song_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int newPosition = getAdapterPosition();

                        if (newPosition != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(newPosition);
                        }
                    }
                }
            });
        }
    }
    public static String convertToMMSS(String duration) {
        Long millis = Long.parseLong(duration);
        if (TimeUnit.MILLISECONDS.toHours(millis) == 0) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)); }
        else {
            return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        }
    }
}
