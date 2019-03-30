package com.musicseque.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musicseque.R;
import com.musicseque.models.SongsModel;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyHolder> {
    ArrayList<SongsModel> songsAL;
    Context context;

    public SongsAdapter(ArrayList<SongsModel> songsAL, Context context) {
        this.songsAL = songsAL;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_songs, parent, false);
        MyHolder vh = new MyHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        SongsModel songsModel = songsAL.get(position);
        holder.tvMusicName.setText(songsModel.getSongName());
        holder.tvMusicSinger.setText(songsModel.getSongSinger());
        holder.tvMusicTime.setText(songsModel.getSongTime());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvMusicName, tvMusicSinger, tvMusicTime;

        public MyHolder(View itemView) {
            super(itemView);
            tvMusicName = (TextView) itemView.findViewById(R.id.tvMusicName);
            tvMusicSinger = (TextView) itemView.findViewById(R.id.tvMusicSinger);
            tvMusicTime = (TextView) itemView.findViewById(R.id.tvMusicTime);

        }
    }
}
