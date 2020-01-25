package com.musicseque.artist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.musicseque.R

class CollaboratorsFragment : BaseFragment() {
    var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_collaborators, null)
        initOtherViews()
        initViews()
        listeners()
        return view
    }

    private fun initOtherViews() {}
    private fun initViews() {}
    private fun listeners() {}
}