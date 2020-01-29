package com.musicseque.artist.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.artist.artist_adapters.ArtistGigsAdapter
import com.musicseque.artist.artist_models.GigsModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_ARTIST_GIGS
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_gigs_venue.*
import org.json.JSONObject
import java.util.ArrayList

class GigsFragment : Fragment(), MyInterface {
    lateinit private var alGigs: ArrayList<GigsModel>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gigs_artist, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerViewGigs.layoutManager=LinearLayoutManager(activity,VERTICAL,false)
        hitAPI(FOR_ARTIST_GIGS)
    }

    private fun hitAPI(APITYPE: Int) {
        if (Utils.isNetworkConnected(activity)) {
           // Utils.initializeAndShow(activity)
            if (APITYPE == FOR_ARTIST_GIGS) {
                val json = JSONObject()
                json.put("LoggedInUserId", SharedPref.getString(Constants.USER_ID, ""))
                RetrofitAPI.callAPI(json.toString(), FOR_ARTIST_GIGS, this)
            }
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {

       // Utils.hideProgressDialog()
        if (TYPE == FOR_ARTIST_GIGS) {
            val json = JSONObject(response.toString())
            if (json.getString("Status").equals("Success", true)) {
                recyclerViewGigs.visibility= VISIBLE
                tvNoData.visibility= GONE
                val array=json.getString("result")
                alGigs = Gson().fromJson<ArrayList<GigsModel>>(array.toString(), object : TypeToken<ArrayList<GigsModel>>() {
                }.type)
                val adapter=ArtistGigsAdapter(alGigs)
                recyclerViewGigs.adapter=adapter

            }
            else
            {
                recyclerViewGigs.visibility=GONE
                tvNoData.visibility= VISIBLE
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        ArtistGigsAdapter.mPreviousValue=""
    }

}