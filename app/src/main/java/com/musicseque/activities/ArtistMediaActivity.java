package com.musicseque.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.musicseque.R;
import com.musicseque.fragments.SongsFragment;
import com.musicseque.fragments.VideosFragment;

public class ArtistMediaActivity extends Activity implements View.OnClickListener {
    TextView tvMp3,tvVideos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_media);
        initOtherViews();
        initViews();
        listeners();
        openFragment(new SongsFragment());



    }

    private void initOtherViews() {

    }

    private void initViews() {
        tvMp3=(TextView)findViewById(R.id.tvMp3);
        tvVideos=(TextView)findViewById(R.id.tvVideos);

    }

    private void listeners() {
    tvMp3.setOnClickListener(this);
    tvVideos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvMp3:
                openFragment(new SongsFragment());
                break;
            case R.id.tvVideos:
                openFragment(new VideosFragment());
                break;
        }
    }


    void openFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMedia,fragment,"").commit();
    }

}
