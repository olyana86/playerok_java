package com.example.playerok.presentation.adapters;

import static com.example.playerok.utils.converters.TimeConverter.convertToMMSS;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.playerok.R;
import com.example.playerok.presentation.models.SongModel;
import com.example.playerok.utils.interfaces.RecyclerViewInterface;
import java.util.ArrayList;

public class SinglePlaylistAdapter extends RecyclerView.Adapter<SinglePlaylistAdapter.ViewHolder> {
private final RecyclerViewInterface recyclerViewInterface;
ArrayList<SongModel> playList;
    Context context;

public SinglePlaylistAdapter(ArrayList<SongModel> playList, Context context, RecyclerViewInterface recyclerViewInterface) {
    this.playList = playList;
    this.context = context;
    this.recyclerViewInterface = recyclerViewInterface;
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_song_item, parent, false);
        return new SinglePlaylistAdapter.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(SinglePlaylistAdapter.ViewHolder holder, int position) {
        SongModel songData = playList.get(position);
        holder.songTitleTextView.setText(songData.getTitle());
        holder.songArtistTextView.setText(songData.getArtist());
        holder.songDurationTextView.setText(convertToMMSS(songData.getDuration()));
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
}
