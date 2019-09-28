package com.musicseque.artist.band.band_activity


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.band.band_adapter.SearchBandMemberAdapter
import com.musicseque.artist.band.band_model.BandMemberStatusModel

import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.RemoveMemberInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_search_artist.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject


class SearchBandMemberActivity : BaseActivity(), MyInterface, RemoveMemberInterface {


    var activity: Activity = this
    var alModel = ArrayList<BandMemberStatusModel>()
    var mBandId: String = ""
    var mPosition: Int = 0

    lateinit var modelClass: BandMemberStatusModel
    lateinit var adapter: SearchBandMemberAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_artist)
        initOtherViews()
        initViews()
        listener()
        hitAPI("")
        callEditTextListener()
    }


    private fun initOtherViews() {
        mBandId = intent.getStringExtra("band_id")

    }

    private fun initViews() {

        tv_title.text = "Invite Band Member"
        img_first_icon.visibility = View.VISIBLE
        img_right_icon.visibility = View.GONE
        recyclerArtist.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    private fun listener() {
        img_first_icon.setOnClickListener {

            finish()
        }
    }

    private fun callEditTextListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Handler().postDelayed({
                    hitAPI(s.toString())
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
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success")) {

                    val data = obj.getJSONArray("result")

                    recyclerArtist.visibility = View.VISIBLE
                    val gson = Gson()
                    val itemType = object : TypeToken<ArrayList<BandMemberStatusModel>>() {}.type
                    alModel = gson.fromJson<ArrayList<BandMemberStatusModel>>(data.toString(), itemType)
                    adapter = SearchBandMemberAdapter(alModel, activity!!, this)
                    recyclerArtist.adapter = adapter
                }
            }
            Constants.FOR_ADD_BAND_MEMBER -> {
                modelClass.artistIsBandMember = "P"
                adapter.notifyItemChanged(mPosition)

            }
            Constants.FOR_REMOVE_BAND_MEMBER -> {
                modelClass.artistIsBandMember = "N"
                adapter.notifyItemChanged(mPosition)

            }


        }


    }


    fun hitAPI(str: String) {
        if (Utils.isNetworkConnected(activity)) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("SearchTypeValue", str)
                jsonObject.put("BandManagerId", SharedPref.getString(Constants.USER_ID, ""))
                jsonObject.put("ArtistProfileTypeId", "1")
                jsonObject.put("BandId", mBandId)
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_SEARCH_BAND_MEMBER, this@SearchBandMemberActivity)

            } catch (e: JSONException) {
                e.printStackTrace()
            }


        } else {
            Utils.showToast(this@SearchBandMemberActivity, getString(R.string.err_no_internet))
        }
    }

    override fun sendMemberId(id: String) {

    }

    override fun addOrRemoveMember(memberId: String, type: String, data: BandMemberStatusModel, pos: Int) {
        var sType = if (type == "add")
            "add"
        else
            "remove"


        if (sType == "add") {
            if (Utils.isNetworkConnected(activity)) {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("BandId", mBandId)
                    jsonObject.put("BandManagerId", SharedPref.getString(Constants.USER_ID, ""))
                    jsonObject.put("ArtistId", data.artistUserId)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_ADD_BAND_MEMBER, this@SearchBandMemberActivity)

            } else {
                Utils.showToast(this@SearchBandMemberActivity, getString(R.string.err_no_internet))
            }
        } else {
            if (Utils.isNetworkConnected(activity)) {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("BandId", mBandId)
                    jsonObject.put("BandManagerId", SharedPref.getString(Constants.USER_ID, ""))
                    jsonObject.put("BandMemberId", data.artistUserId)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_REMOVE_BAND_MEMBER, this@SearchBandMemberActivity)

            } else {
                Utils.showToast(this@SearchBandMemberActivity, getString(R.string.err_no_internet))
            }
        }
        modelClass = data
        mPosition = pos

    }
}
