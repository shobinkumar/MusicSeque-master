package com.musicseque.artist.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_accept_reject_event_request.*
import kotlinx.android.synthetic.main.activity_accept_reject_event_request.tvEventType
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONObject

class AcceptRejectEventRequestActivity:BaseActivity(),View.OnClickListener,MyInterface {
    var mEventId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_reject_event_request)
        initViews()
        listeners()
        hitAPI(Constants.FOR_EVENT_DETAIL, "")
    }

    private fun initViews() {

        mEventId = intent.getStringExtra("event_id")
        tv_title.text = "Event Details"
        img_right_icon.visibility = View.GONE


//        if (type == Constants.FOR_PENDING_REQ) {
//            llButtons.visibility = View.VISIBLE
//        } else {
//            llButtons.visibility = View.GONE
//        }
    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        tvAcceptEventArtist.setOnClickListener(this)
        tvRejectEventArtist.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_first_icon -> {
                finish()
            }
            R.id.tvAcceptEventArtist,R.id.tvRejectEventArtist -> {
                val json = JSONObject()
                if(v.id==R.id.tvAcceptEventArtist)
                    json.put("ArtistBookingStatus", "B")
                else
                    json.put("ArtistBookingStatus", "R")


                json.put("EventId",mEventId)
                json.put("ArtistId",SharedPref.getString(Constants.USER_ID,""))
                hitAPI(Constants.FOR_ACCEPT_REJECT_EVENT_REQ_ARTIST, json.toString())

            }

        }

    }

    private fun hitAPI(API_TYPE: Int, args: String) {
        if (KotlinUtils.isNetConnected(this)) {
            Utils.initializeAndShow(this)
            if (API_TYPE == Constants.FOR_EVENT_DETAIL) {
                val obj = JSONObject()
                obj.put("EventId", intent.getStringExtra("event_id"))
                KotlinHitAPI.callAPI(obj.toString(), Constants.FOR_EVENT_DETAIL, this)
            } else if (API_TYPE == Constants.FOR_ACCEPT_REJECT_EVENT_REQ_ARTIST) {
                KotlinHitAPI.callAPI(args, Constants.FOR_ACCEPT_REJECT_EVENT_REQ_ARTIST, this)
            }

        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_EVENT_DETAIL -> {
                val jsonInner = JSONObject(response.toString())
                if (jsonInner.getString("Status").equals("Success", true)) {
                    val array = jsonInner.getJSONArray("result")
                    val json = array.getJSONObject(0)
                    mBookingId = json.getInt("VenueBookingId").toString()
                    mVenueId = SharedPref.getString(Constants.USER_ID, "")


                    tvEventName.text = json.getString("EventManagerName")
                    tvEventContact.text = json.getString("EMPhone")
                    tvEventEmail.text = json.getString("EMUsername")
                    tvEventGigName.text = json.getString("EventTitle")

                    tvEventDescription.text = json.getString("EventDescription")
                    tvEventType.text=json.getString("EventTypeName")

                    val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(json.getString("VenueBookedFromDate"), json.getString("VenueBookedToDate"))
                    tvEventStartDateTime.text = mFromDate + " ," + json.getString("EventTimeFrom")
                    tvEventEndtDateTime.text = mToDate + " ," + json.getString("EventTimeTo")
                } else {

                }


            }
            Constants.FOR_ACCEPT_EVENT_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    Utils.showToast(this,json.getString("Message"))
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Utils.showToast(this,json.getString("Message"))
                }


            }
            Constants.FOR_REJECT_EVENT_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    Utils.showToast(this,json.getString("Message"))
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Utils.showToast(this,json.getString("Message"))
                }
            }
        }

    }

}