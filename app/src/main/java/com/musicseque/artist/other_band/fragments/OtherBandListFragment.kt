package com.musicseque.artist.other_band.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.Fonts.BoldNoyhr
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.artist.artist_models.BandDataModel
import com.musicseque.artist.band.band_adapter.BandListAdapter
import com.musicseque.artist.fragments.BaseFragment
import com.musicseque.artist.other_band.adapters.OtherBandListAdapter
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_OTHER_BAND_LIST
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_other_band_list.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class OtherBandListFragment : BaseFragment(), MyInterface {
    var v: View? = null

    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: BandListAdapter? = null
    var alBand = ArrayList<BandDataModel>()
    lateinit var tvHeading: BoldNoyhr
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_other_band_list, null)
        initOtherViews()
        initViews()
        return v
    }

    private fun initViews() {
        tvHeading!!.text = "Bands"
        recyclerBand!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        hitAPIs(FOR_OTHER_BAND_LIST)
    }

    private fun initOtherViews() {
        tvHeading = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as BoldNoyhr
        val img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        img_right_icon.visibility = View.GONE
    }

    private fun hitAPIs(type: Int) {
        if (Utils.isNetworkConnected(activity)) {
            Utils.initializeAndShow(activity)
            try {
                if (type == FOR_OTHER_BAND_LIST) {
                    val jsonObject = JSONObject()
                    jsonObject.put("LoggedInArtistId", SharedPref.getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_OTHER_BAND_LIST, this@OtherBandListFragment)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }
    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_OTHER_BAND_LIST -> try {
                val `object` = JSONObject(response.toString())
                if (`object`.getString("Status").equals("Success", ignoreCase = true)) {
                    val jsonArray = `object`.getJSONArray("result")
                    alBand = Gson().fromJson(jsonArray.toString(), object : TypeToken<ArrayList<BandDataModel?>?>() {}.type)
                    tvNoBand!!.visibility = View.GONE
                    recyclerBand!!.visibility = View.VISIBLE
                    recyclerBand!!.adapter = OtherBandListAdapter(activity!!, alBand)
                } else {
                    recyclerBand!!.visibility = View.GONE
                    tvNoBand!!.visibility = View.VISIBLE
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}