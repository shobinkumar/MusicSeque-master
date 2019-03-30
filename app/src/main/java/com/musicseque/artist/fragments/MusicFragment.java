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

public class MusicFragment extends BaseFragment {
    View view;
    @BindView(R.id.recyclerSongs)
    RecyclerView recyclerSongs;
    @BindView(R.id.tvNoAudio)
    Noyhr tvNoAudio;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_songs, null);
        ButterKnife.bind(this, view);

        initOtherViews();
        initViews();
        listeners();
        return view;
    }


    private void initOtherViews() {

    }

    private void initViews() {
        recyclerSongs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerSongs.setVisibility(View.GONE);

        tvNoAudio.setVisibility(View.VISIBLE);
        // recyclerSongs.setAdapter(new MusicAdapter(recyclerSongs,getActivity()));
    }


    private void listeners() {

    }
}
