package com.musicseque.event_manager.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_past_event_detail.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONObject

class EventDetailActivity : BaseActivity(), View.OnClickListener, MyInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_event_detail)
        initViews()
        listeners()
        hitAPI();

    }


    private fun initViews() {
        tv_title.text = "Event Details"
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

    private fun hitAPI() {
        if (Utils.isNetworkConnected(this)) {
            val obj = JSONObject()
            obj.put("EventId", intent.getStringExtra("event_id"))
            KotlinHitAPI.callAPI(obj.toString(), Constants.FOR_EVENT_DETAIL, this)
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }

    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        if (TYPE == Constants.FOR_EVENT_DETAIL) {
            val json = JSONObject(response.toString())
            if (json.getString("Status").equals("Success", true)) {
                val array = json.getJSONArray("result")
                val jsonInner = array.getJSONObject(0)
                tvEventName.setText(jsonInner.getString("EventTitle"))
                tvEventDescription.setText(jsonInner.getString("EventDescription"))
                tvCapacity.setText(jsonInner.getString("EventEstimatedGuest"))
                tvBudget.text=jsonInner.getString("EventBudget")
                tvEventType.text=jsonInner.getString("EventTypeName")
//                if (jsonInner.getString("VenueName").equals("", true)) {
//                    val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(jsonInner.getString("EventDateFrom"), jsonInner.getString("EventDateTo"))
//
//                    tvStartDateTime.setText(mFromDate + " " + jsonInner.getString("EventTimeFrom"))
//
//                    tvEndDateTime.setText(mToDate + " " + jsonInner.getString("EventTimeTo"))
//                } else {
                    tvVenueName.setText(jsonInner.getString("VenueName"))

                    val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(jsonInner.getString("VenueBookedFromDate"), jsonInner.getString("VenueBookedToDate"))

                    tvStartDateTime.setText(mFromDate + " " + jsonInner.getString("EventTimeFrom"))

                    tvEndDateTime.setText(mToDate + " " + jsonInner.getString("EventTimeTo"))


               // }




                if (jsonInner.getString("EventPromotionImg").equals("")) {
                    Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfile)

                } else {
                    Glide.with(this).load(jsonInner.getString("EventPromotionImgPath") + jsonInner.getString("EventPromotionImg")).into(ivProfile)

                }


            }

        }

    }

}