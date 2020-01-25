package com.musicseque.artist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.musicseque.R

class VideoFragment : BaseFragment() {
    var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_video, null)
        initOtherViews()
        initViews()
        listeners()
        return v
    }

    private fun initOtherViews() {}
    private fun initViews() { // recyclerSongs.setAdapter(new MusicAdapter(recyclerSongs,getActivity()));
    }

    private fun listeners() {}
}