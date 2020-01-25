package com.musicseque.artist.band.band_fragment

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.artist.artist_models.BandDataModel
import com.musicseque.artist.band.band_adapter.BandListAdapter
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_BAND_LIST
import com.musicseque.utilities.Constants.FOR_DELETE_BAND
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_band_list.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class BandListFragment : KotlinBaseFragment(), MyInterface, View.OnClickListener {
   lateinit  var views: View

    var tvTitle: TextView? = null
    var adapter: BandListAdapter? = null
    var alBand = ArrayList<BandDataModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_band_list, null)

        return views
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOtherViews()
        initViews()
        listeners()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun listeners() {
        btnAddBand.setOnClickListener(this)
    }

    private fun initViews() {

        recycleBand.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        hitAPIs(FOR_BAND_LIST)
    }

    private fun initOtherViews() {
        tvTitle = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as TextView
        val img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        img_right_icon.visibility = View.GONE
        tvTitle!!.text = "Bands"
    }

    private fun hitAPIs(type: Int) {
        if (Utils.isNetworkConnected(activity)) {
            Utils.initializeAndShow(activity)
            try {
                if (type== FOR_BAND_LIST) {
                    val jsonObject = JSONObject()
                    jsonObject.put("LoginUserId", SharedPref.getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_BAND_LIST, this@BandListFragment)
                } else if (type==FOR_DELETE_BAND) {
                    val jsonObject = JSONObject()
                    jsonObject.put("BandManagerId", SharedPref.getString(Constants.USER_ID, ""))
                    jsonObject.put("BandId", "")
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_DELETE_BAND, this@BandListFragment)
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
            Constants.FOR_BAND_LIST -> try {
                val `object` = JSONObject(response.toString())
                if (`object`.getString("Status").equals("Success", ignoreCase = true)) {
                    val jsonArray = `object`.getJSONArray("result")
                    alBand = Gson().fromJson(jsonArray.toString(), object : TypeToken<ArrayList<BandDataModel?>?>() {}.type)
                    recycleBand!!.visibility = View.VISIBLE
                    btnAddBand!!.visibility = View.GONE
                    recycleBand!!.adapter = BandListAdapter(activity, alBand)
                } else {
                    recycleBand!!.visibility = View.GONE
                    btnAddBand!!.visibility = View.VISIBLE
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Constants.FOR_DELETE_BAND -> {
            }
        }
    }

   override fun onClick(view: View) {
       if(view.id==R.id.btnAddBand)
       {
           val ldf = BandFormFragment()
           val args = Bundle()
           args.putString("band_id", "0")
           ldf.arguments = args
           (view.context as FragmentActivity).supportFragmentManager.beginTransaction()
                   .replace(R.id.frameLayout, ldf)
                   .commit()
       }

    }
}