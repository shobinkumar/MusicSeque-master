package com.musicseque.artist.band.band_fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.artist.artist_adapters.ArtistGigsAdapter
import com.musicseque.artist.artist_models.GigsModel
import com.musicseque.interfaces.MyInterface

import java.util.ArrayList

class BandGigsFragment : Fragment() {
    lateinit private var alGigs: ArrayList<GigsModel>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gigs_bands, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }



}