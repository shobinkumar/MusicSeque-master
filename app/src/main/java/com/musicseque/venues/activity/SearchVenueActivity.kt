package com.musicseque.venues.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
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
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_search_venue.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class SearchVenueActivity : BaseActivity(), View.OnClickListener, MyInterface {


    lateinit private var alBand: ArrayList<BandListModel>

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
        if (Utils.isNetworkConnected(this@SearchVenueActivity)) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("LoggedInUserId", sharedPreferences.getString(Constants.USER_ID, ""))

                jsonObject.put("SearchText", "")

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            KotlinHitAPI.callAPI(jsonObject.toString(), Constants.FOR_SEARCH_VENUE_LIST, this@SearchVenueActivity)

        } else {
            Utils.showToast(this@SearchVenueActivity, R.string.err_no_internet.toString())
        }

    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                Handler().postDelayed({
                    if (Utils.isNetworkConnected(this@SearchVenueActivity)) {
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("LoggedInUserId", sharedPreferences.getString(Constants.USER_ID, ""))

                            jsonObject.put("SearchText", editable.toString())

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        hitAPI(jsonObject.toString())

                    } else {
                        Utils.showToast(this@SearchVenueActivity, R.string.err_no_internet.toString())
                    }
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
        if (KotlinUtils.isNetConnected(this))
            KotlinHitAPI.callAPI(params, Constants.FOR_SEARCH_VENUE_LIST, this@SearchVenueActivity)
        else
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        when (TYPE) {
            Constants.FOR_SEARCH_VENUE_LIST -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success")) {
                    rvVenue.visibility = View.VISIBLE
                    tvNoRecord.visibility = View.GONE
                    val array = json.getJSONArray("result")
                    alBand = Gson().fromJson<ArrayList<BandListModel>>(array.toString(), object : TypeToken<ArrayList<BandListModel>>() {

                    }.type)
                    rvVenue.adapter = SearchBandAdapter(alBand)

                } else {
                    rvVenue.visibility = View.GONE
                    tvNoRecord.visibility = View.VISIBLE
                }
            }
        }
    }




}