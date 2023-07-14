package com.example.playerok.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.playerok.activities.SinglePlaylistActivity;
import com.example.playerok.models.PlaylistModel;
import com.example.playerok.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AllPlaylistsAdapter extends RecyclerView.Adapter<AllPlaylistsAdapter.ViewHolder> {
    ArrayList<PlaylistModel> allPlaylistsArrayList;
    Context context;

    public AllPlaylistsAdapter(ArrayList<PlaylistModel> allPlaylistsArrayList, Context context) {
        this.allPlaylistsArrayList = allPlaylistsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_playlist_item, parent, false);
        return new AllPlaylistsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllPlaylistsAdapter.ViewHolder holder, int position) {
        PlaylistModel playlistData = allPlaylistsArrayList.get(position);
        holder.playlistTitle.setText(playlistData.getPlaylistTitle());
        String songsDuration = playlistData.getPlaylistDuration();
        holder.playlistDuration.setText(convertToMMSS(songsDuration));
        String songsCount = "Всего песен: " + playlistData.getPlaylistCount();
        holder.playlistCount.setText(songsCount);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistModel playlistData = allPlaylistsArrayList.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, SinglePlaylistActivity.class);
                intent.putExtra("playlistTitle", playlistData.getPlaylistTitle());
                intent.putExtra("playlistPath", playlistData.getPlaylistPath());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {return allPlaylistsArrayList.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView playlistTitle, playlistCount, playlistDuration;
        ImageView playlistIconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            playlistTitle = itemView.findViewById(R.id.playlist_title_in_the_list);
            playlistCount = itemView.findViewById(R.id.total_amount_of_songs_number);
            playlistDuration = itemView.findViewById(R.id.playlist_duration);
            playlistIconImageView = itemView.findViewById(R.id.playlist_icon);
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
