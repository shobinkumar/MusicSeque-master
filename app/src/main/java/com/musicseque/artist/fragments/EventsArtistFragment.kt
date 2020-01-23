package com.musicseque.artist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.musicseque.R
import com.musicseque.artist.artist_adapters.EventStatusArtistPagerAdapter
import com.musicseque.utilities.KotlinBaseFragment
import kotlinx.android.synthetic.main.fragment_event_artist.*

class EventsArtistFragment : KotlinBaseFragment() {
    lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_event_artist, null)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ButterKnife.bind(this, v)
        initViews()
    }

    private fun initViews() {

        val fragmentAdapter = EventStatusArtistPagerAdapter(fragmentManager!!)
        viewPagerArtistEvents.setOffscreenPageLimit(fragmentAdapter.getCount());

        fragmentAdapter.notifyDataSetChanged()
        viewPagerArtistEvents.adapter = fragmentAdapter

        tab_layout_artist_events.setupWithViewPager(viewPagerArtistEvents)
    }



}