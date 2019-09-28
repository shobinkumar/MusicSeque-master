package com.musicseque.venue_manager.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.artist_adapters.SearchBandAdapter
import com.musicseque.artist.artist_models.BandListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import com.musicseque.venue_manager.adapter.SearchVenueAdapter
import com.musicseque.venue_manager.model.VenueSearchModel
import kotlinx.android.synthetic.main.activity_search_venue.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class SearchVenueActivity : BaseActivity(), View.OnClickListener, MyInterface {


    lateinit private var alVenue: ArrayList<VenueSearchModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_venue)
        initOtherViews()
        initViews()
        listeners()
    }

    private fun initOtherViews() {
        tv_title.text = "Venue"
        img_right_icon.visibility = View.GONE
    }

    private fun initViews() {
        rvVenue.layoutManager = LinearLayoutManager(this)
        hitAPI("")
//        if (Utils.isNetworkConnected(this@SearchVenueActivity)) {
//            val jsonObject = JSONObject()
//            try {
//                jsonObject.put("LoggedInUserId", SharedPref.getString(Constants.USER_ID, ""))
//
//                jsonObject.put("SearchText", "")
//
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            KotlinHitAPI.callAPI(jsonObject.toString(), Constants.FOR_SEARCH_VENUE_LIST, this@SearchVenueActivity)
//
//        } else {
//            Utils.showToast(this@SearchVenueActivity, R.string.err_no_internet.toString())
//        }

    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                Handler().postDelayed({

                    hitAPI(editable.toString())


                }, 1000)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> {
                finish()
            }
        }
    }

    fun hitAPI(params: String) {
        if (Utils.isNetworkConnected(this@SearchVenueActivity)) {
            Utils.initializeAndShow(this)
            val jsonObject = JSONObject()
            try {
                jsonObject.put("LoggedInUserId", SharedPref.getString(Constants.USER_ID, ""))

                jsonObject.put("SearchText", params)
                KotlinHitAPI.callAPI(jsonObject.toString(), Constants.FOR_SEARCH_VENUE_LIST, this@SearchVenueActivity)
            } catch (e: JSONException) {
                e.printStackTrace()
            }


        } else {
            Utils.showToast(this@SearchVenueActivity, R.string.err_no_internet.toString())
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {

            Constants.FOR_SEARCH_VENUE_LIST -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success")) {
                    rvVenue.visibility = View.VISIBLE
                    tvNoRecord.visibility = View.GONE
                    val array = json.getJSONArray("result")
                    alVenue = Gson().fromJson<ArrayList<VenueSearchModel>>(array.toString(), object : TypeToken<ArrayList<VenueSearchModel>>() {

                    }.type)
                    rvVenue.adapter = SearchVenueAdapter(this, alVenue)

                } else {
                    rvVenue.visibility = View.GONE
                    tvNoRecord.visibility = View.VISIBLE
                }
            }
        }
    }


}