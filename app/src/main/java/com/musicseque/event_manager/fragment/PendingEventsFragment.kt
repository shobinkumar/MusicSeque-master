package com.musicseque.event_manager.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.event_manager.adapter.EventStatusCommonAdapter
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.utilities.*
import com.musicseque.utilities.Constants.FOR_SHOW_EVENTS_LIST
import kotlinx.android.synthetic.main.fragment_pending_events.*
import org.json.JSONObject

class PendingEventsFragment : KotlinBaseFragment(), MyInterface {
    lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_pending_events, null)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hitAPI(FOR_SHOW_EVENTS_LIST)

    }

    fun hitAPI(TYPE: Int) {
        if (TYPE == Constants.FOR_SHOW_EVENTS_LIST) {
            val obj = JSONObject()
            obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
            obj.put("EventStatus", "2")
            APIHit.sendPostData(obj.toString(), FOR_SHOW_EVENTS_LIST, this@PendingEventsFragment, activity!!)
        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()

        val obj = JSONObject(response.toString())
        if (obj.getString("Status").equals("Success", false)) {
            recyclerPendingEvents.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
            val arr = obj.getJSONArray("result")
            val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
            val eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
            recyclerPendingEvents.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
            recyclerPendingEvents.adapter = EventStatusCommonAdapter(activity!!, eventsList, 1)
        } else {
            recyclerPendingEvents.visibility = View.GONE
            tvNoData.visibility = View.VISIBLE
        }


    }
}