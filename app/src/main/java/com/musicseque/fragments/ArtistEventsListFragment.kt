package com.musicseque.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.event_manager.adapter.EventListAdapter
import com.musicseque.event_manager.adapter.UpcomingEventAdapter
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_SHOW_EVENTS_LIST
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_events_list.*
import org.json.JSONObject


@SuppressLint("ValidFragment")
class ArtistEventsListFragment(val value: Int) : KotlinBaseFragment(), MyInterface {


    private var _hasLoadedOnce:Boolean=false
    private var mEventId: String = ""
    var mType: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_artist_events_list, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mType = value
    }


    private fun hitAPI(type: Int) {
        if (Utils.isNetworkConnected(activity!!)) {
            Utils.initializeAndShow(activity)
            if (type == FOR_SHOW_EVENTS_LIST) {
                val obj = JSONObject()
                obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
                obj.put("EventStatus", mType)
                RetrofitAPI.callAPI(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this)
            } else if (type == Constants.FOR_DELETE_EVENT) {
                val obj = JSONObject()
                obj.put("EventId", mEventId)
                RetrofitAPI.callAPI(obj.toString(), Constants.FOR_DELETE_EVENT, this)
            }


        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }
    }


    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()

        if (TYPE == FOR_SHOW_EVENTS_LIST) {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                recycler_events.visibility = View.VISIBLE
                tvNoList.visibility = View.GONE
                val arr = obj.getJSONArray("result")
                val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
                val eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
                if (mType == 2) {
                    recycler_events.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
                    recycler_events.adapter = UpcomingEventAdapter(eventsList, mType, activity!!)
                } else {
                    recycler_events.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
                    recycler_events.adapter = EventListAdapter(eventsList, mType, activity!!)
                }


            } else {
                recycler_events.visibility = View.GONE
                tvNoList.visibility = View.VISIBLE
            }
        } else if (TYPE == Constants.FOR_DELETE_EVENT) {
            hitAPI(FOR_SHOW_EVENTS_LIST);
        }


    }

    public fun deleteEvent(eventId: String) {
        mEventId = eventId
        hitAPI(Constants.FOR_DELETE_EVENT)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (this.isVisible) { // we check that the fragment is becoming visible
            if (isVisibleToUser && !_hasLoadedOnce) {

                onResume();
                _hasLoadedOnce = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!getUserVisibleHint()) {
            return;
        }
        hitAPI(FOR_SHOW_EVENTS_LIST)
    }
}