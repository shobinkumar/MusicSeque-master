package com.musicseque.event_manager.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.event_manager.adapter.EventListAdapter
import com.musicseque.event_manager.adapter.UpcomingEventAdapter
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_SHOW_EVENTS_LIST
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_events_list.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONObject


class EventsListActivity : BaseActivity(), MyInterface {


    private var mEventId: String=""
    var mType: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_list)
        mType = intent.getIntExtra("type", 0);

        img_right_icon.visibility = View.GONE
        img_first_icon.setOnClickListener { finish() }



        if (mType == 0) {
            tv_title.text = "Ongoing Events"
        } else if (mType == 1) {
            tv_title.text = "Past Events"

        } else if (mType == 2) {
            tv_title.text = "Upcoming Events"

        }
        hitAPI(FOR_SHOW_EVENTS_LIST);

    }

    private fun hitAPI(type: Int) {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this)
            if(type== FOR_SHOW_EVENTS_LIST)
            {
                val obj = JSONObject()
                obj.put("EventManagerId", sharedPreferences.getString(Constants.USER_ID, ""))
                obj.put("EventStatus", mType)
                KotlinHitAPI.callAPI(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this)
            }
            else if(type==Constants.FOR_DELETE_EVENT)
            {
                val obj = JSONObject()
                obj.put("EventId",mEventId)
                KotlinHitAPI.callAPI(obj.toString(), Constants.FOR_DELETE_EVENT, this)
            }


        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }
    }



    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()

        if(TYPE==FOR_SHOW_EVENTS_LIST)
        {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                recycler_events.visibility = View.VISIBLE
                tvNoList.visibility = View.GONE
                val arr = obj.getJSONArray("result")
                val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
                val eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
                if (mType == 2) {
                    recycler_events.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                    recycler_events.adapter = UpcomingEventAdapter(eventsList, mType, this, this)
                } else {
                    recycler_events.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                    recycler_events.adapter = EventListAdapter(eventsList, mType, this)
                }


            } else {
                recycler_events.visibility = View.GONE
                tvNoList.visibility = View.VISIBLE
            }
        }
        else if(TYPE==Constants.FOR_DELETE_EVENT)
        {
            hitAPI(FOR_SHOW_EVENTS_LIST);
        }


    }

    public fun deleteEvent(eventId: String) {
        mEventId=eventId
        hitAPI(Constants.FOR_DELETE_EVENT)
    }

}