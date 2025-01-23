package com.example.playerok.presentation.adapters;

import static com.example.playerok.utils.converters.TimeConverter.convertToMMSS;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playerok.R;
import com.example.playerok.presentation.activities.SinglePlaylistActivity;
import com.example.playerok.presentation.models.PlaylistModel;

import java.util.ArrayList;


public class AllPlaylistsAdapter extends RecyclerView.Adapter<AllPlaylistsAdapter.ViewHolder> {
    ArrayList<PlaylistModel> allPlaylistsArrayList;
    Context context;

    public AllPlaylistsAdapter(ArrayList<PlaylistModel> allPlaylistsArrayList, Context context) {
        this.allPlaylistsArrayList = allPlaylistsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
}
