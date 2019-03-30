package com.musicseque.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.musicseque.R;

public class UploadFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout llPhotos, llAudio, llVideos;
    ImageView ivUploadVideo, ivUploadAudio, ivUploadPhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        initOtherViews();
        initViews();
        listeners();
        return view;
    }

    private void initOtherViews() {

    }

    private void initViews() {
        llPhotos = (LinearLayout) view.findViewById(R.id.llPhotos);
        llAudio = (LinearLayout) view.findViewById(R.id.llAudio);
        llVideos = (LinearLayout) view.findViewById(R.id.llVideos);
        ivUploadVideo = (ImageView) view.findViewById(R.id.ivUploadVideo);
        ivUploadAudio = (ImageView) view.findViewById(R.id.ivUploadAudio);
        ivUploadPhoto = (ImageView) view.findViewById(R.id.ivUploadPhoto);

    }

    private void listeners() {
        llPhotos.setOnClickListener(this);
        llAudio.setOnClickListener(this);
        llVideos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.llPhotos:

                break;
            case R.id.llAudio:

                break;
            case R.id.llVideos:

                break;
        }
    }
}
