package com.musicseque.artist.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.artist.artist_adapters.PendingEventsArtistAdapter
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.utilities.*
import kotlinx.android.synthetic.main.fragment_pending_events.*
import org.json.JSONObject

class PendingEventsListArtistFragment : KotlinBaseFragment(), MyInterface {
    private var _hasLoadedOnce: Boolean = false
    lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_pending_events, null)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    fun hitAPI(TYPE: Int) {
        if (TYPE == Constants.FOR_SHOW_EVENTS_LIST) {
            val obj = JSONObject()
            obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
            obj.put("EventStatus", "4")
            APIHit.sendPostData(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this@PendingEventsListArtistFragment, activity!!)
        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        Log.e("Fragment", "PEnding")

        val obj = JSONObject(response.toString())
        if (obj.getString("Status").equals("Success", false)) {
            recyclerPendingEvents.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
            val arr = obj.getJSONArray("result")
            val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
            val eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
            recyclerPendingEvents.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
            recyclerPendingEvents.adapter = PendingEventsArtistAdapter(this, eventsList)
        } else {
            recyclerPendingEvents.visibility = View.GONE
            tvNoData.visibility = View.VISIBLE
        }


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
        hitAPI(Constants.FOR_SHOW_EVENTS_LIST)
    }






}