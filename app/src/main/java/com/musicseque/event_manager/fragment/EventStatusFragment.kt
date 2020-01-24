package com.musicseque.event_manager.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.musicseque.R
import com.musicseque.event_manager.activity.CreateEventActivity
import com.musicseque.event_manager.adapter.EventStatusPagerAdapter
import com.musicseque.utilities.KotlinBaseFragment
import kotlinx.android.synthetic.main.fragment_event_status.*

class EventStatusFragment : KotlinBaseFragment(), View.OnClickListener {
    lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_event_status, null)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()
    }

    private fun listeners() {
        fab.setOnClickListener(this)
    }

    private fun initViews() {

        viewPager.offscreenPageLimit=0
        val fragmentAdapter = EventStatusPagerAdapter(fragmentManager!!)
        viewPager.adapter = fragmentAdapter

        tab_layout.setupWithViewPager(viewPager)
    }


    override fun onClick(v: View) {

        startActivity(Intent(activity, CreateEventActivity::class.java))
    }

}