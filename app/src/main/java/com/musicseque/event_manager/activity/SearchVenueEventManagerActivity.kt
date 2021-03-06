package com.musicseque.event_manager.activity

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.TextView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.artist_models.ArtistModel
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.utilities.APIHit
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import com.musicseque.event_manager.adapter.SearchVenueEventManagerAdapter
import com.musicseque.venue_manager.model.VenueSearchModel
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_search_venue_event_manager.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class SearchVenueEventManagerActivity : BaseActivity(), MyInterface, View.OnClickListener {
    lateinit private var listPopupWindow: ListPopupWindow
    var arrayListArtist = ArrayList<ArtistModel>()
    var mEventName = ""
    var mEventId = ""

    var eventsList = ArrayList<EventListModel>()
    var eventsListMain = ArrayList<EventListModel>()

    lateinit var arrEventName: Array<String>
    var alEventName = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_venue_event_manager)
        ButterKnife.bind(this)
        initOtherViews()
        initViews()
        listeners()
        showDialog()
        hitAPI(Constants.FOR_SHOW_EVENTS_LIST, "")
    }


    private fun initOtherViews() {
        tv_title.setText("Venue")
        img_first_icon.setVisibility(View.VISIBLE)
        img_right_icon.setVisibility(View.GONE)
    }

    private fun initViews() {
        recyclerArtistSearchVenueEventManager.layoutManager = LinearLayoutManager(this)


    }

    private fun listeners() {

        rlEventsVenue.setOnClickListener(this)
        img_first_icon.setOnClickListener(this)


        etSearchVenue.setOnFocusChangeListener { view, b ->
            if (b) {
                if (eventsList.size == 0) {
                    callTextWatcher()
                } else {
                    if (mEventId.equals("")) {
                        Utils.showToast(this, "Select the event first")
                    } else {
                        callTextWatcher()
                    }
                }

            }
        }


    }

    private fun callTextWatcher() {
        etSearchVenue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {

                Handler().postDelayed({

                    var mString = editable.toString()
                    val jsonObject = JSONObject()
                    try {

                        jsonObject.put("SearchTypeValue", mString)
                        jsonObject.put("EventId", mEventId)
                        jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                        jsonObject.put("ProfileTypeId", "6")

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    if (mString.equals("")) {
                        hitAPI(Constants.GET_ARTIST_LIST, "")

                    } else {
                        hitAPI(Constants.FOR_SEARCH_VENUE_EVENT_MANAGER, jsonObject.toString())

                    }


                }, 1000)


            }
        })
    }

    private fun hitAPI(type: Int, args: String) {

        if (type == Constants.GET_ARTIST_LIST) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                jsonObject.put("ProfileTypeId", "6")

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            APIHit.sendPostData(jsonObject.toString(), Constants.GET_ARTIST_LIST, this@SearchVenueEventManagerActivity, this)
        } else if (type == Constants.FOR_SEARCH_VENUE_EVENT_MANAGER) {
            APIHit.sendPostData(args.toString(), Constants.FOR_SEARCH_VENUE_EVENT_MANAGER, this@SearchVenueEventManagerActivity, this)

        }
        if (type == Constants.FOR_SHOW_EVENTS_LIST) {
            val obj = JSONObject()
            obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
            obj.put("EventStatus", "4")
            APIHit.sendPostData(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this, this)
        }


    }

    override fun sendResponse(response: Any, TYPE: Int) {

        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.GET_ARTIST_LIST ->

                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        recyclerArtistSearchVenueEventManager.visibility = View.VISIBLE
                        tvNoRecordSearchVenueEventManager.visibility = View.GONE
                        arrayListArtist.clear()
                        val jsonArray = jsonObject.getJSONArray("result")

                        if (jsonArray.length() > 0) {
                            arrayListArtist = Gson().fromJson<ArrayList<ArtistModel>>(jsonArray.toString(), object : TypeToken<ArrayList<ArtistModel>>() {

                            }.type)

                            recyclerArtistSearchVenueEventManager.adapter = SearchVenueEventManagerAdapter(this, arrayListArtist)
                        }

                    } else {
                        recyclerArtistSearchVenueEventManager.visibility = View.GONE
                        tvNoRecordSearchVenueEventManager.visibility = View.VISIBLE
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            Constants.FOR_SEARCH_ARTIST_EVENT_MANAGER -> try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                    tvNoRecordSearchVenueEventManager.setVisibility(View.GONE)
                    recyclerArtistSearchVenueEventManager.setVisibility(View.VISIBLE)

                    arrayListArtist.clear()
                    val jsonArray = jsonObject.getJSONArray("result")
                    if (jsonArray.length() > 0) {
                        arrayListArtist = Gson().fromJson<ArrayList<ArtistModel>>(jsonArray.toString(), object : TypeToken<ArrayList<VenueSearchModel>>() {}.type)
                        recyclerArtistSearchVenueEventManager.adapter = SearchVenueEventManagerAdapter(this, arrayListArtist)

                    }

                } else {
                    recyclerArtistSearchVenueEventManager.setVisibility(View.GONE)
                    tvNoRecordSearchVenueEventManager.setVisibility(View.VISIBLE)

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Constants.FOR_SHOW_EVENTS_LIST -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", false)) {
                    val arr = obj.getJSONArray("result")
                    val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
                    eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
                    for (i in eventsList) {
                        if (!i.artist_confirmation_status.equals("") && !i.artist_confirmation_status.equals("P", true))
                            alEventName.add(i.event_title)
                        eventsListMain.add(i)
                    }
                    arrEventName = alEventName.toTypedArray()
                } else {
                    arrEventName = arrayOf<String>()

                }


                hitAPI(Constants.GET_ARTIST_LIST, "")
            }

        }
    }


    override fun onClick(view: View) {
        when (view.id) {

            R.id.img_first_icon -> finish()
            R.id.rlEventsVenue -> {
                if (arrEventName.size == 0) {
                    Utils.showToast(this, "No Events is created yet")
                } else {
                    showDropdown(arrEventName, tvEventsVenueEventManager, SpinnerData { mId, mName ->
                        mEventName = mName
                        mEventId = mId
                    }, 500)
                }

            }
        }
    }


    fun showDropdown(array: Array<String>, textView: TextView, spinnerData: SpinnerData, width: Int) {
        listPopupWindow = ListPopupWindow(
                this)
        listPopupWindow.setAdapter(ArrayAdapter(
                this,
                R.layout.row_profile_spinner, array))
        listPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_black))
        listPopupWindow.setAnchorView(textView)
        listPopupWindow.setWidth(width)
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            if (textView.id == R.id.tvEventsArtistEventManager) {
                spinnerData.getData(eventsList.get(position).event_id, array[position])
            }
            textView.text = array[position]

            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }

    fun showDialog() {

        var builder = AlertDialog.Builder(this);
        builder.setMessage("Events in the list will be shown if created by the artist and it is approved by the artist first.")
                .setCancelable(false)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })


        val alert = builder.create();
        alert.show();
    }
}