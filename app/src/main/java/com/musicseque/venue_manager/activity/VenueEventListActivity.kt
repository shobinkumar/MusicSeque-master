package com.musicseque.venue_manager.activity

import android.os.Bundle
import android.view.View
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.utilities.Constants
import kotlinx.android.synthetic.main.toolbar_top.*

class VenueEventListActivity : BaseActivity() {
    private var mEventId: String = ""
    var mType: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_event_list)
        initOtherViews()
        initViews()
    }

    private fun initOtherViews() {
        mType = intent.getIntExtra("type", 0);

        img_right_icon.visibility = View.GONE
        img_first_icon.setOnClickListener { finish() }



        if (mType == 1) {
            tv_title.text = "Past Events"

        } else if (mType == 2) {
            tv_title.text = "Upcoming Events"

        }

    }

    private fun initViews() {

      //  hitAPI(Constants.FOR_SHOW_EVENTS_LIST);

    }
}