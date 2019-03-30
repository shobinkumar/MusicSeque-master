package com.musicseque.artist.artist_adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.musicseque.R;
import com.musicseque.artist.activity.PlayVideoActivity;
import com.musicseque.artist.artist_models.UploadedMediaModel;
import com.musicseque.artist.fragments.UploadVideoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UploadVideoAdapter extends RecyclerView.Adapter<UploadVideoAdapter.MyHolder> {
    Activity activity;
    ArrayList<UploadedMediaModel> uploadedAl;
    int mPercent;
    UploadVideoFragment uploadVideoFragment;

    public UploadVideoAdapter(Activity activity, ArrayList<UploadedMediaModel> uploadedAl, int percent, UploadVideoFragment fragment) {
        this.activity = activity;
        this.uploadedAl = uploadedAl;
        mPercent = percent;
        uploadVideoFragment = fragment;
    }

    @NonNull
    @Override
    public UploadVideoAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.row_upload_video, parent, false);
        MyHolder vh = new MyHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadVideoAdapter.MyHolder holder, int position) {

        UploadedMediaModel uploadedMediaModel = uploadedAl.get(position);
        holder.tvVideoName.setText(uploadedMediaModel.getFileName());
       // holder.tvAuthorName.setText("by " + uploadedMediaModel.getArtist_name());


        if (uploadedMediaModel.isUploaded()) {
            holder.progressBar.setVisibility(View.GONE);
            holder.ivPlay.setVisibility(View.VISIBLE);
            holder.ivDeleteVideo.setVisibility(View.VISIBLE);
            holder.ivDeleteVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    uploadVideoFragment.deleteVideo(uploadedAl.get(position));
                }
            });
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.ivDeleteVideo.setVisibility(View.GONE);
            holder.progressBar.setProgress(mPercent);
        }

        if (uploadedMediaModel.getFilePath() != null)
            Glide.with(activity).load(uploadedMediaModel.getFilePath() + uploadedMediaModel.getFileName()).into(holder.ivIcon);

        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, PlayVideoActivity.class).putExtra("path", uploadedMediaModel.getFilePath() + uploadedMediaModel.getFileName()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploadedAl.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvVideoName)
        TextView tvVideoName;

        @BindView(R.id.tvAuthorName)
        TextView tvAuthorName;


        @BindView(R.id.ivPlay)
        ImageView ivPlay;

        @BindView(R.id.ivIcon)
        CircleImageView ivIcon;

        @BindView(R.id.ivDeleteVideo)
        ImageView ivDeleteVideo;

        @BindView(R.id.ivPause)
        ImageView ivPause;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}