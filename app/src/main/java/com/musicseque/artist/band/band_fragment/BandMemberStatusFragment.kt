package com.musicseque.artist.band.band_fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.musicseque.R
import com.musicseque.artist.band.band_model.BandMemberStatusModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_band_members.*
import org.json.JSONObject

class BandMemberStatusFragment : Fragment(), MyInterface, View.OnClickListener {


    val users = ArrayList<BandMemberStatusModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_band_members, container, false)
        initOtherViews()
        initViews()
        hitAPI();
        return view;

//        val adapter = BandMemberStatusAdapter(users, activity)
//        recyclerBandMember.adapter = adapter

    }

    private fun initOtherViews() {

    }

    private fun initViews() {
        recyclerBandMember.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun hitAPI() {
        Utils.initializeAndShow(activity)
        if (Utils.isNetworkConnected(activity)) {
            val obj = JSONObject();
            obj.put("BandManagerId", "")
            obj.put("BandId", "")

            RetrofitAPI.callAPI(obj.toString(), Constants.FOR_BAND_MEMBER_STATUS, this);
        } else {
            Utils.showToast(activity, getString(R.string.err_no_internet))
        }

    }

    override fun sendResponse(response: Any?, TYPE: Int) {

        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_BAND_MEMBER_STATUS -> {

                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success")) {

                    tvAddBandMember.visibility = View.GONE
                    recyclerBandMember.visibility = View.VISIBLE

                } else {
                    tvAddBandMember.visibility = View.VISIBLE
                    recyclerBandMember.visibility = View.GONE

                }


            }


        }


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvAddBandMember -> {

            }
        }
    }


}

