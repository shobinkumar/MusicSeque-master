package com.musicseque.artist.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import com.musicseque.artist.artist_adapters.OtherArtistBandMemberStatusAdapter
import com.musicseque.artist.band.band_model.BandMemberStatusModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_band_members.view.*
import org.json.JSONObject

class OtherArtistBandMemberStatusFragment : BaseFragment(), MyInterface {


    private var recyclerBandMember: RecyclerView?=null
    var tvNoMember:Noyhr?=null
    var alModel = ArrayList<BandMemberStatusModel>()
    var mBandId: String = ""
    var mManagerId: String = ""

    lateinit var views: View

    val GET_MEMBERS = 1

    var API_TYPE: Int = 0

    var position:Int=-1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_other_artist_band_members_status, container, false)
        initOtherViews()
        initViews()
        listeners()
        API_TYPE = GET_MEMBERS
        hitAPI(API_TYPE, mBandId);
        return views

    }

    private fun listeners() {
    }

    private fun initOtherViews() {

    }

    private fun initViews() {
        tvNoMember=views.findViewById<Noyhr>(R.id.tvNoMember)
        recyclerBandMember=views.findViewById<RecyclerView>(R.id.recyclerBandMember)
        views.recyclerBandMember.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBandId = arguments!!.getString("band_id")
        mManagerId=arguments!!.getString("manager_id")
    }

    private fun hitAPI(API_TYPE: Int, bandId: String) {
        Utils.initializeAndShow(activity)
        if (Utils.isNetworkConnected(activity)) {


            val obj = JSONObject();
            if (API_TYPE == GET_MEMBERS) {
                obj.put("BandManagerId", mManagerId)
                obj.put("BandId", bandId)

                RetrofitAPI.callAPI(obj.toString(), Constants.FOR_BAND_MEMBER_STATUS, this);


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
                     tvNoMember?.visibility = View.GONE
                    recyclerBandMember?.visibility = View.VISIBLE
                    val gson = Gson()
                    val itemType = object : TypeToken<ArrayList<BandMemberStatusModel>>() {}.type
                    alModel = gson.fromJson<ArrayList<BandMemberStatusModel>>(data.toString(), itemType)


                    val adapter =  OtherArtistBandMemberStatusAdapter(alModel, activity!!)
                    recyclerBandMember?.adapter = adapter
                } else {
                    tvNoMember?.visibility = View.VISIBLE
                    recyclerBandMember?.visibility = View.GONE

                }


            }


        }


    }







}