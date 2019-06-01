package com.musicseque.artist.band.band_fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.artist.band.band_activity.SearchBandMemberActivity
import com.musicseque.artist.band.band_adapter.BandMemberStatusAdapter
import com.musicseque.artist.band.band_model.BandMemberStatusModel
import com.musicseque.artist.fragments.BaseFragment
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.RemoveMemberInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_band_members.*
import kotlinx.android.synthetic.main.fragment_band_members.view.*
import org.json.JSONException
import org.json.JSONObject

class BandMemberStatusFragment : BaseFragment(), MyInterface, View.OnClickListener, RemoveMemberInterface {


    var alModel = ArrayList<BandMemberStatusModel>()
    var mBandId: String = ""
    lateinit var views: View

    val GET_MEMBERS = 1
    val REMOVE_MEMBER = 2
    var API_TYPE: Int = 0

     var position:Int=-1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_band_members, container, false)
        initOtherViews()
        initViews()
        listeners()
        API_TYPE = GET_MEMBERS
        hitAPI(API_TYPE, mBandId);
        return views

    }

    private fun listeners() {
        views.tvAddBandMember.setOnClickListener(this)
    }

    private fun initOtherViews() {

    }

    private fun initViews() {
        views.recyclerBandMember.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBandId = arguments!!.getString("band_id")
    }

    private fun hitAPI(API_TYPE: Int, bandId: String) {
        Utils.initializeAndShow(activity)
        if (Utils.isNetworkConnected(activity)) {


            val obj = JSONObject();
            if (API_TYPE == GET_MEMBERS) {
                obj.put("BandManagerId", sharedPref.getString(Constants.USER_ID, ""))
                obj.put("BandId", bandId)

                RetrofitAPI.callAPI(obj.toString(), Constants.FOR_BAND_MEMBER_STATUS, this);
//            } else if(API_TYPE==REMOVE_MEMBER){
//                val jsonObject = JSONObject()
//                try {
//                    jsonObject.put("BandId", mBandId)
//                    jsonObject.put("BandManagerId", sharedPref.getString(Constants.USER_ID, ""))
//                    jsonObject.put("ArtistId", data.artistUserId)
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_REMOVE_BAND_MEMBER, this)


            }


        } else {
            Utils.showToast(activity, getString(R.string.err_no_internet))
        }

    }

    override fun sendResponse(response: Any?, TYPE: Int) {

             Utils.hideProgressDialog()
        when (TYPE)
        {
            Constants.FOR_BAND_MEMBER_STATUS -> {

                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success")) {

                    val data = obj.getJSONArray("result")
                   // tvAddBandMember.visibility = View.GONE
                    recyclerBandMember.visibility = View.VISIBLE
                    val gson = Gson()
                    val itemType = object : TypeToken<ArrayList<BandMemberStatusModel>>() {}.type
                    alModel = gson.fromJson<ArrayList<BandMemberStatusModel>>(data.toString(), itemType)


//                    if (alModel.size < 5) {
//                        views.tvAddBandMember.visibility = View.VISIBLE
//
//                    } else {
//                        views.tvAddBandMember.visibility = View.GONE
//                    }

                    val adapter = BandMemberStatusAdapter(alModel, activity!!, this)
                    recyclerBandMember.adapter = adapter
                } else {
                    tvAddBandMember.visibility = View.VISIBLE
                    recyclerBandMember.visibility = View.GONE

                }


            }

            Constants.FOR_REMOVE_BAND_MEMBER->
            {
               val jsonObject=JSONObject(response.toString())

                if(jsonObject.get("Status")=="Success")
                {
                    API_TYPE = GET_MEMBERS
                    hitAPI(API_TYPE, mBandId);
                }
                else
                {

                }

            }
        }


    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvAddBandMember -> {
                val intent = Intent(activity, SearchBandMemberActivity::class.java)
                intent.putExtra("band_id",mBandId)
                startActivity(intent)


            }
        }
    }


    override fun sendMemberId(id: String) {

    }

    override fun addOrRemoveMember(id: String, type: String, data: BandMemberStatusModel, pos: Int) {
        position=pos
        API_TYPE = REMOVE_MEMBER
        val jsonObject = JSONObject()
        try {
            jsonObject.put("BandId", mBandId)
            jsonObject.put("BandManagerId", sharedPref.getString(Constants.USER_ID, ""))
            jsonObject.put("BandMemberId", data.artistUserId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_REMOVE_BAND_MEMBER, this)

    }
}

