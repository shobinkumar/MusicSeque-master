package com.musicseque.venues.activity

import android.os.Bundle
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants.FOR_VENUE_PROFILE
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import org.json.JSONObject

class OtherVenueProfileActivity : BaseActivity(), MyInterface {

    lateinit var mVenueId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_venue_profile)
        initViews()
        listeners()
    }

    private fun initViews() {
        mVenueId = intent.getStringExtra("venue_id")
        hitAPI("profile")


    }

    private fun listeners() {

    }

    private fun hitAPI(text: String) {
        if (KotlinUtils.isNetConnected(this)) {
            val json = JSONObject()
            Utils.initializeAndShow(this)
            if (text == "profile") {

                json.put("VenueId", mVenueId)
                KotlinHitAPI.callAPI(json.toString(), FOR_VENUE_PROFILE, this)
            } else {

            }

        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        if(TYPE== FOR_VENUE_PROFILE)
        {

        }
    }

}