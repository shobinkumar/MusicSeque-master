package com.musicseque.venue_manager.activity

import android.os.Bundle
import android.view.View
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONObject

class BookVenueActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_venue)
        initViews()
        listeners()
        hitAPI("get_timmings")
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

    private fun hitAPI(type: String) {
        if(Utils.isNetworkConnected(this))
        {
            if (type.equals("get_timmings")) {
                val json= JSONObject()
                json.put("VenueId","");

            }
        }
        else{
            Utils.showToast(this,resources.getString(R.string.err_no_internet))
        }



    }
}