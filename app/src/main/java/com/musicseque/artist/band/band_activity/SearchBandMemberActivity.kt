package com.musicseque.artist.band.band_activity


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import com.musicseque.R
import com.musicseque.activities.BaseActivity

import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_search_artist.*
import org.json.JSONException
import org.json.JSONObject


class SearchBandMemberActivity : BaseActivity(), MyInterface {

    var activity: Activity = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_artist)
        initOtherViews()
        initViews()
        callEditTextListener()
    }

    private fun initOtherViews() {


    }

    private fun initViews() {
        recyclerArtist.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    private fun callEditTextListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Handler().postDelayed({
                    if (Utils.isNetworkConnected(activity)) {
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("SearchTypeValue", s.toString())
                            jsonObject.put("BandManagerId", sharedPreferences.getString(Constants.USER_ID, ""))
                            jsonObject.put("ArtistProfileTypeId","1")

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_SEARCH_BAND_MEMBER, this@SearchBandMemberActivity)

                    } else {
                        Utils.showToast(this@SearchBandMemberActivity, getString(R.string.err_no_internet))
                    }
                }, 1000)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }


    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_SEARCH_BAND_MEMBER -> {

            }


        }


    }
}
