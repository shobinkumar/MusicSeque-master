package com.musicseque.artist.artist_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.musicseque.R;
import com.musicseque.models.ImageModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    RecyclerView recyclerView;
    Context context;
    ArrayList<ImageModel> arrayList;

    public ImagesAdapter(RecyclerView recyclerView, Context context, ArrayList<ImageModel> arrayList) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_images, viewGroup, false);

        return new ImagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ViewHolder viewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivArtistImages)
        ImageView ivArtistImages;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }


}
