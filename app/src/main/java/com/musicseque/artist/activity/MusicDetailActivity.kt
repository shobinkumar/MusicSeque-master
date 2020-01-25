package com.musicseque.artist.activity

import android.app.Activity
import android.os.Bundle
import com.musicseque.R

class MusicDetailActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_detail)
    }

    override fun onBackPressed() {
        finish()
    }
}