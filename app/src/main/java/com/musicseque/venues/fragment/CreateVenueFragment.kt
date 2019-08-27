package com.musicseque.venues.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.CountryModel
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_create_venue.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.musicseque.utilities.Constants.*


class CreateVenueFragment : KotlinBaseFragment(), View.OnClickListener, MyInterface {


    private lateinit var arrCountryCode: Array<String>
    private val countryAL = ArrayList<CountryModel>()
    private val countryCodeAL = ArrayList<String>()
    private val countryNameAL = ArrayList<String>()
    lateinit var v: View
    //var mVenueName
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_create_venue, null)
        initViews()
        listeners()
        hitAPI("countries_list")
        return v
    }


    private fun initViews() {

    }

    private fun listeners() {
        ivVenueImage.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.ivVenueImage -> {
                checkPermissions("image", "com.musicseque.venues.fragment.CreateVenueFragment")
            }
            R.id.btnSubmit -> {






            }

        }
    }


    private fun hitAPI(type: String) {

        if (KotlinUtils.isNetConnected(activity!!.applicationContext)) {
            Utils.initializeAndShow(activity!!.applicationContext)
            if (type == "countries_list") {
                KotlinHitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this)


            } else if (type == "profile") {
                try {
                    val jsonObject = JSONObject()
                  //  jsonObject.put("UserId", getSharedPref().getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }


        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()

        when (TYPE) {
            FOR_COUNTRIES_LIST -> {
                try {
                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val model = CountryModel()
                        model.setCountryId(jsonObject1.getString("CountryId"))
                        model.setCountryName(jsonObject1.getString("CountryName"))
                        model.setCountryCode(jsonObject1.getString("CountryCode"))

                        countryNameAL.add(jsonObject1.getString("CountryName"))
                        countryCodeAL.add(jsonObject1.getString("CountryCode"))
                        countryAL.add(model)
                    }


                    arrCountryCode = countryCodeAL.toTypedArray()
                    hitAPI("profile")


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            FOR_USER_PROFILE -> {
                try {


                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }
            FOR_CREATE_UPDATE_VENUE_PROFILE->
            {

            }


        }


    }
}