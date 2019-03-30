package com.musicseque.artist.activity;

import android.app.Activity;
import android.os.Bundle;

import com.musicseque.R;

public class MusicDetailActivity extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
