package com.musicseque.event_manager.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.musicseque.event_manager.adapter.SearchArtistAdapterEventManager
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_SEARCH_ARTIST_EVENT_MANAGER
import com.musicseque.utilities.Constants.FOR_SHOW_EVENTS_LIST
import com.musicseque.utilities.Constants.GET_ARTIST_LIST
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_search_artist_event_manager.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SearchArtistActivityEventManager : BaseActivity(), MyInterface, View.OnClickListener {
    lateinit private var listPopupWindow: ListPopupWindow
    public var arrayList = ArrayList<ArtistModel>()
    var mEventName = ""
    public var mEventId = ""

    public var eventsList = ArrayList<EventListModel>()
    public var eventsListTemp = ArrayList<EventListModel>()

    lateinit var arrEventName: Array<String>
    var alEventName = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_artist_event_manager)
        ButterKnife.bind(this)
        initOtherViews()
        initViews()
        listeners()
        hitAPI(FOR_SHOW_EVENTS_LIST, "")
    }


    private fun initOtherViews() {
        tv_title.setText("Artist")
        img_first_icon.setVisibility(View.VISIBLE)
        img_right_icon.setVisibility(View.GONE)
    }

    private fun initViews() {
        recyclerArtistSearchArtistEventManager.layoutManager = LinearLayoutManager(this)


    }

    private fun listeners() {

        rlEvents.setOnClickListener(this)
        img_first_icon.setOnClickListener(this)


        etSearch.setOnFocusChangeListener { view, b ->
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
        etSearch.addTextChangedListener(object : TextWatcher {
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
                        jsonObject.put("ProfileTypeId", "1")

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    if (mString.equals("")) {
                        hitAPI(GET_ARTIST_LIST, "")

                    } else {
                        hitAPI(FOR_SEARCH_ARTIST_EVENT_MANAGER, jsonObject.toString())

                    }


                }, 1000)


            }
        })
    }

    private fun hitAPI(type: Int, args: String) {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this@SearchArtistActivityEventManager)
            if (type == GET_ARTIST_LIST) {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                    jsonObject.put("ProfileTypeId", "1")

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                RetrofitAPI.callAPI(jsonObject.toString(), Constants.GET_ARTIST_LIST, this@SearchArtistActivityEventManager)
            } else if (type == FOR_SEARCH_ARTIST_EVENT_MANAGER) {
                RetrofitAPI.callAPI(args.toString(), Constants.FOR_SEARCH_ARTIST_EVENT_MANAGER, this@SearchArtistActivityEventManager)

            }
            if (type == Constants.FOR_SHOW_EVENTS_LIST) {
                val obj = JSONObject()
                obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
                obj.put("EventStatus", "4")
                RetrofitAPI.callAPI(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this)
            }

        } else {
            Utils.showToast(this, R.string.err_no_internet.toString())
        }


    }

    override fun sendResponse(response: Any, TYPE: Int) {

        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.GET_ARTIST_LIST ->

                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        recyclerArtistSearchArtistEventManager.visibility = View.VISIBLE
                        tvNoRecordSearchArtistEventManager.visibility = GONE
                        arrayList.clear()
                        val jsonArray = jsonObject.getJSONArray("result")

                        if (jsonArray.length() > 0) {
                            arrayList = Gson().fromJson<ArrayList<ArtistModel>>(jsonArray.toString(), object : TypeToken<ArrayList<ArtistModel>>() {

                            }.type)

                            recyclerArtistSearchArtistEventManager.adapter = SearchArtistAdapterEventManager(this, arrayList, SharedPref.getString(Constants.USER_ID, ""))
                        }

                    } else {
                        recyclerArtistSearchArtistEventManager.visibility = View.GONE
                        tvNoRecordSearchArtistEventManager.visibility = VISIBLE
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            Constants.FOR_SEARCH_ARTIST_EVENT_MANAGER -> try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                    tvNoRecordSearchArtistEventManager.setVisibility(View.GONE)
                    recyclerArtistSearchArtistEventManager.setVisibility(View.VISIBLE)

                    arrayList.clear()
                    val jsonArray = jsonObject.getJSONArray("result")
                    if (jsonArray.length() > 0) {
                        arrayList = Gson().fromJson<ArrayList<ArtistModel>>(jsonArray.toString(), object : TypeToken<ArrayList<ArtistModel>>() {

                        }.type)
                        recyclerArtistSearchArtistEventManager.adapter = SearchArtistAdapterEventManager(this, arrayList, SharedPref.getString(Constants.USER_ID, ""))

                    }

                } else {
                    recyclerArtistSearchArtistEventManager.setVisibility(View.GONE)
                    tvNoRecordSearchArtistEventManager.setVisibility(View.VISIBLE)

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            FOR_SHOW_EVENTS_LIST -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", false)) {
                    val arr = obj.getJSONArray("result")
                    val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
                    eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
                    for (i in eventsList) {
                        if (i.artist_confirmation_status.equals("")) {
                            eventsListTemp.add(i)
                            alEventName.add(i.event_title)
                        }

                    }
                    arrEventName = alEventName.toTypedArray()
                } else {
                    arrEventName = arrayOf<String>()

                }


                hitAPI(GET_ARTIST_LIST, "")
            }

        }
    }


    override fun onClick(view: View) {
        when (view.id) {

            R.id.img_first_icon -> finish()
            R.id.rlEvents -> {
                if (arrEventName.size == 0) {
                    Utils.showToast(this, "No Events is created yet")
                } else {
                    showDropdown(arrEventName, tvEventsArtistEventManager, SpinnerData { mId, mName ->
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


}