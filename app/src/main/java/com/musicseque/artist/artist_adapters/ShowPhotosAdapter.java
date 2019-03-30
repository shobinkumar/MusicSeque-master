package com.musicseque.artist.artist_adapters;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.musicseque.R;
import com.musicseque.models.ImageModel;

import java.util.ArrayList;

public class ShowPhotosAdapter extends RecyclerView.Adapter<ShowPhotosAdapter.MyHolder> {
      ShowPhotosAdapter.UploadImage uploadImage;
    Context context;
    ArrayList<ImageModel> imageAL;

    public ShowPhotosAdapter(Context context, ArrayList<ImageModel> imageAL) {
        this.context = context;
        this.imageAL = imageAL;

    }

    @NonNull
    @Override
    public ShowPhotosAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                context);
        View v = inflater.inflate(R.layout.row_show_images, parent, false);
        ShowPhotosAdapter.MyHolder vh = new ShowPhotosAdapter.MyHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowPhotosAdapter.MyHolder holder, int position) {
        ImageModel imageModel = imageAL.get(position);
        holder.pBar.setVisibility(View.VISIBLE);
        Glide.with(context).load(imageModel.getBase_url() + imageModel.getImage_url()).into(holder.ivImage);
//        if (imageModel.isImage()) {
//            holder.pBar.setVisibility(View.VISIBLE);
//            Glide.with(context).load(imageModel.getBase_url()+imageModel.getImage_url()).into(holder.ivImage);
//        } else {
//            Glide.with(context).load(R.drawable.icon_add_icon).into(holder.ivImage);
//            holder.pBar.setVisibility(View.GONE);
//            holder.ivImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    uploadImage.callMethod();
//                }
//            });
//        }

    }

    @Override
    public int getItemCount() {
        return imageAL.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ProgressBar pBar;
        ImageView ivImage;

        public MyHolder(View itemView) {
            super(itemView);
            pBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

    public  interface UploadImage {
        public void callMethod();
    }

}
