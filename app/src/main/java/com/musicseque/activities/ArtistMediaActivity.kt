package com.musicseque.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.musicseque.R
import com.musicseque.fragments.SongsFragment
import com.musicseque.fragments.VideosFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class ArtistMediaActivity : AppCompatActivity(), View.OnClickListener {
    var tvMp3: TextView? = null
    var tvVideos: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_media)
        initOtherViews()
        initViews()
        listeners()
        openFragment(SongsFragment())
    }

    private fun initOtherViews() {}
    private fun initViews() {
        tvMp3 = findViewById<View>(R.id.tvMp3) as TextView
        tvVideos = findViewById<View>(R.id.tvVideos) as TextView
    }

    private fun listeners() {
        tvMp3!!.setOnClickListener(this)
        tvVideos!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvMp3 -> openFragment(SongsFragment())
            R.id.tvVideos -> openFragment(VideosFragment())
        }
    }

    fun openFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutMedia, fragment).commit()
    }
}