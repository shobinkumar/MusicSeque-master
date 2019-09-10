package com.musicseque.event_manager.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.event_manager.adapter.EventListAdapter
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.event_manager.model.EventModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_events_list.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONObject


class EventsListActivity : BaseActivity(), MyInterface {


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
        hitAPI();

    }

    fun hitAPI() {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this)
            val obj = JSONObject()
            obj.put("EventManagerId", sharedPreferences.getString(Constants.USER_ID, ""))
            obj.put("EventStatus", mType)
            KotlinHitAPI.callAPI(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this)

        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        val obj = JSONObject(response.toString())
        if (obj.getString("Status").equals("Success", false)) {
            recycler_events.visibility = View.VISIBLE
            tvNoList.visibility = View.GONE
            val arr=obj.getJSONArray("result")
            val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
           val eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
            recycler_events.layoutManager=LinearLayoutManager(this,LinearLayout.VERTICAL,false)
                recycler_events.adapter=EventListAdapter(eventsList)






        } else {
            recycler_events.visibility = View.GONE
            tvNoList.visibility = View.VISIBLE
        }
    }
}