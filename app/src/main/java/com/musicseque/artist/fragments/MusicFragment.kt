package com.musicseque.artist.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import kotlinx.android.synthetic.main.fragment_songs.*

class MusicFragment : BaseFragment() {
    var v: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_songs, null)
        initOtherViews()
        initViews()
        listeners()
        return view
    }

    private fun initOtherViews() {}
    private fun initViews() {
        recyclerSongs!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerSongs!!.visibility = View.GONE
        tvNoAudio!!.visibility = View.VISIBLE
        // recyclerSongs.setAdapter(new MusicAdapter(recyclerSongs,getActivity()));
    }

    private fun listeners() {}
}