package com.musicseque.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musicseque.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistMusicFragment extends Fragment {
    View view;
    @BindView(R.id.recyclerMusic)
    RecyclerView recyclerMusic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_artist_music,null,false);
        ButterKnife.bind(this, view);
        recyclerMusic.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        return view;

    }
}
