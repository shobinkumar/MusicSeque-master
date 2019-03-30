package com.musicseque.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musicseque.R;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyHolder> {
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_videos, parent, false);
        MyHolder vh = new MyHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvVideoName, tvSinger, tvVideoTime;
        public MyHolder(View itemView) {
            super(itemView);
            tvVideoName = (TextView) itemView.findViewById(R.id.tvVideoName);
            tvSinger = (TextView) itemView.findViewById(R.id.tvSinger);
            tvVideoTime = (TextView) itemView.findViewById(R.id.tvVideoTime);
        }
    }
}
