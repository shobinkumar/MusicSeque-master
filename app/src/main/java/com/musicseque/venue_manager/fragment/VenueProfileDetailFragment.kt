package com.musicseque.venue_manager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.artist.fragments.BaseFragment

class VenueProfileDetailFragment : BaseFragment() {

    lateinit var v: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v=inflater.inflate(R.layout.fragment_venue_profile_detail,null)
        return v
    }


}