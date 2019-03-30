package com.musicseque.artist.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musicseque.Fonts.Noyhr;
import com.musicseque.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends BaseFragment {
    View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, null);
        ButterKnife.bind(this, view);

        initOtherViews();
        initViews();
        listeners();
        return view;
    }


    private void initOtherViews() {

    }

    private void initViews() {

        // recyclerSongs.setAdapter(new MusicAdapter(recyclerSongs,getActivity()));
    }


    private void listeners() {

    }
}
