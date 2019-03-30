package com.musicseque.artist.artist_adapters;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.musicseque.R;
import com.musicseque.artist.artist_models.UploadedMediaModel;
import com.musicseque.artist.fragments.UploadMusicFragment;
import com.musicseque.utilities.Utils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadAudioAdapter extends RecyclerView.Adapter<UploadAudioAdapter.MyHolder> {
    Activity activity;
    ArrayList<UploadedMediaModel> uploadedAl;
    int mPercent;
    MediaPlayer mp;
    private boolean isPLAYING = false;
    UploadedMediaModel lastPlayingModel;
    UploadMusicFragment uploadMusicFragment;
    int lastPostion;
    int currentPos;

    public UploadAudioAdapter(Activity activity, ArrayList<UploadedMediaModel> uploadedAl, int percent, UploadMusicFragment fragment) {
        this.activity = activity;
        this.uploadedAl = uploadedAl;
        mPercent = percent;
        uploadMusicFragment = fragment;
    }

    public UploadAudioAdapter() {
    }

    @NonNull
    @Override
    public UploadAudioAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.row_upload_music, parent, false);
        MyHolder vh = new MyHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadAudioAdapter.MyHolder holder, int position) {

        UploadedMediaModel uploadedMediaModel = uploadedAl.get(position);
        holder.tvMusicName.setText(uploadedMediaModel.getFileName());
        //   holder.tvAuthorName.setText("by " + uploadedMediaModel.getArtist_name());
        if (uploadedMediaModel.isUploaded()) {
            holder.progressBar.setVisibility(View.GONE);
            holder.ivPlay.setVisibility(View.VISIBLE);
            holder.ivDeleteAudio.setVisibility(View.VISIBLE);
            holder.ivDeleteAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopPlaying();
                    uploadedAl.get(currentPos).setPlaying(false);
                    uploadMusicFragment.deleteMusic(uploadedAl.get(position));
                }
            });
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.ivDeleteAudio.setVisibility(View.GONE);
            holder.progressBar.setProgress(mPercent);
        }

        if (uploadedMediaModel.isPlaying()) {
            holder.ivPlay.setVisibility(View.GONE);
            holder.ivPause.setVisibility(View.VISIBLE);
            uploadedMediaModel.setPlaying(true);
            ((UploadMusicFragment) uploadMusicFragment).pBar.setVisibility(View.VISIBLE);


            onRadioClick(uploadedMediaModel.getFilePath() + uploadedMediaModel.getFileName());
        } else {
            // holder.ivPlay.setVisibility(View.VISIBLE);
            holder.ivPause.setVisibility(View.GONE);
            uploadedMediaModel.setPlaying(false);

        }
        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (((UploadMusicFragment) uploadMusicFragment).pBar.getVisibility() == View.VISIBLE) {
//
//                } else {
//                    ((UploadMusicFragment) uploadMusicFragment).pBar.setVisibility(View.VISIBLE);
//                }

                holder.ivPlay.setVisibility(View.GONE);
                holder.ivPause.setVisibility(View.VISIBLE);
                uploadedMediaModel.setPlaying(true);
                if (lastPlayingModel == null) {
                    lastPlayingModel = uploadedMediaModel;
                    lastPostion = position;
                    currentPos = position;

                    onRadioClick(uploadedMediaModel.getFilePath() + uploadedMediaModel.getFileName());
                } else if (lastPlayingModel.getFileName().equalsIgnoreCase(uploadedMediaModel.getFileName())) {
                    lastPostion = currentPos;
                    onRadioClick(uploadedMediaModel.getFilePath() + uploadedMediaModel.getFileName());

                } else {
                    uploadedAl.get(lastPostion).setPlaying(false);
                    lastPostion = currentPos;
                    currentPos = position;
                    lastPlayingModel = uploadedMediaModel;
                    isPLAYING = false;
                    stopPlaying();
                    // notifyItemChanged(pos);
                    notifyDataSetChanged();
                }

            }
        });
        holder.ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivPlay.setVisibility(View.VISIBLE);
                holder.ivPause.setVisibility(View.GONE);
                uploadedMediaModel.setPlaying(false);
                // lastPlayingModel=null;

                stopPlaying();
            }
        });

    }

    @Override
    public int getItemCount() {
        return uploadedAl.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMusicName)
        TextView tvMusicName;
        @BindView(R.id.ivPlay)
        ImageView ivPlay;

        @BindView(R.id.ivPause)
        ImageView ivPause;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        @BindView(R.id.tvAuthorName)
        TextView tvAuthorName;

        @BindView(R.id.ivDeleteAudio)
        ImageView ivDeleteAudio;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void onRadioClick(String path) {

        if (!isPLAYING) {
            isPLAYING = true;
            //uploadMusicFragment.showDialog();

            mp = new MediaPlayer();
            try {
                mp.setDataSource(path);
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        ((UploadMusicFragment) uploadMusicFragment).pBar.setVisibility(View.GONE);










                    }
                });
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }
        } else {
            isPLAYING = false;
            stopPlaying();
        }
    }

    public void stopPlaying() {
        if (mp != null) {
            uploadedAl.get(lastPostion).setPlaying(false);
            mp.release();
            mp = null;
            isPLAYING = false;
        }

    }


}
