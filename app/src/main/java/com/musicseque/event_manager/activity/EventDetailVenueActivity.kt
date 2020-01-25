package com.musicseque.event_manager.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_ACCEPT_EVENT_REQ
import com.musicseque.utilities.Constants.FOR_EVENT_DETAIL
import com.musicseque.utilities.Constants.FOR_REJECT_EVENT_REQ
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_event_detail_venue.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONObject

class EventDetailVenueActivity : BaseActivity(), View.OnClickListener, MyInterface {


    var type: Int = 0
    var mEventId = ""
    var mBookingId = ""
    var mVenueId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail_venue)
        initViews()
        listeners()
        hitAPI(Constants.FOR_EVENT_DETAIL, "")
    }


    private fun initViews() {

        type = intent.getIntExtra("event_type", 0)
        mEventId = intent.getStringExtra("event_id")
        tv_title.text = "Event Details"
        img_right_icon.visibility = View.GONE


        if (type == Constants.FOR_PENDING_REQ) {
            llButtons.visibility = View.VISIBLE
        } else {
            llButtons.visibility = View.GONE
        }
    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        tvAccept.setOnClickListener(this)
        tvReject.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_first_icon -> {
                finish()
            }
            R.id.tvAccept -> {
                val json = JSONObject()
                json.put("VenueBookingId", mBookingId)
                json.put("VenueId", mVenueId)
                json.put("BookingStatus", "B")
                hitAPI(FOR_ACCEPT_EVENT_REQ, json.toString())

            }
            R.id.tvReject -> {
                val json = JSONObject()
                json.put("VenueBookingId", mBookingId)
                json.put("VenueId", mVenueId)
                json.put("BookingStatus", "R")
                hitAPI(FOR_REJECT_EVENT_REQ, json.toString())
            }
        }

    }

    private fun hitAPI(API_TYPE: Int, args: String) {
        if (KotlinUtils.isNetConnected(this)) {
            Utils.initializeAndShow(this)
            if (API_TYPE == Constants.FOR_EVENT_DETAIL) {
                val obj = JSONObject()
                obj.put("EventId", intent.getStringExtra("event_id"))
                KotlinHitAPI.callAPI(obj.toString(), FOR_EVENT_DETAIL, this)
            } else if (API_TYPE == Constants.FOR_ACCEPT_EVENT_REQ) {
                KotlinHitAPI.callAPI(args, FOR_ACCEPT_EVENT_REQ, this)
            } else if (API_TYPE == Constants.FOR_REJECT_EVENT_REQ) {
                KotlinHitAPI.callAPI(args, FOR_REJECT_EVENT_REQ, this)
            }

        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            FOR_EVENT_DETAIL -> {
                val jsonInner = JSONObject(response.toString())
                if (jsonInner.getString("Status").equals("Success", true)) {
                    val array = jsonInner.getJSONArray("result")
                    val json = array.getJSONObject(0)
                    mBookingId = json.getInt("VenueBookingId").toString()
                    mVenueId = SharedPref.getString(Constants.USER_ID, "")


                    tvEventManagerName.text = json.getString("EventManagerName")
                    tvEventManagerContact.text = json.getString("EMPhone")
                    tvEventManagerEmail.text = json.getString("EMUsername")
                    tvEventManagerGigName.text = json.getString("EventTitle")

                    tvEventManagerDescription.text = json.getString("EventDescription")
                    tvBudget.text=json.getString("EventBudget")
                    tvEventType.text=json.getString("EventTypeName")

                    val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(json.getString("VenueBookedFromDate"), json.getString("VenueBookedToDate"))
                    tvEventManagerStartDateTime.text = mFromDate + " ," + json.getString("EventTimeFrom")
                    tvEventManagerEndtDateTime.text = mToDate + " ," + json.getString("EventTimeTo")
                    tvEventManagerGathering.text = json.getString("EventEstimatedGuest")
                } else {

                }


            }
            FOR_ACCEPT_EVENT_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    Utils.showToast(this,json.getString("Message"))
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Utils.showToast(this,json.getString("Message"))
                }


            }
            FOR_REJECT_EVENT_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    Utils.showToast(this,json.getString("Message"))
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Utils.showToast(this,json.getString("Message"))
                }
            }
        }

    }
}