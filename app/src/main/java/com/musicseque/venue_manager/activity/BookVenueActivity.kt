package com.musicseque.venue_manager.activity

import android.os.Bundle
import android.view.View
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import kotlinx.android.synthetic.main.toolbar_top.*

class BookVenueActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_venue)
        initViews()
        listeners()
    }


    private fun initViews() {

        tv_title.text = "Book Venue"
        img_right_icon.visibility = View.GONE
    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> {
            finish()
            }
        }
    }
}