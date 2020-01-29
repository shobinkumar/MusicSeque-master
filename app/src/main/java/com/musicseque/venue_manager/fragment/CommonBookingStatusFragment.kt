package com.musicseque.venue_manager.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.artist.fragments.BaseFragment
import com.musicseque.interfaces.BookingAcceptReject
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_ACCEPTED_REQ
import com.musicseque.utilities.Constants.FOR_ACCEPT_EVENT_REQ
import com.musicseque.utilities.Constants.FOR_PENDING_REQ
import com.musicseque.utilities.Constants.FOR_REJECTED_REQ
import com.musicseque.utilities.Constants.FOR_REJECT_EVENT_REQ
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import com.musicseque.venue_manager.adapter.AcceptedAndRejectedBookingAdapter
import com.musicseque.venue_manager.adapter.PendingBookingAdapter
import com.musicseque.venue_manager.model.BookingStatusModel
import kotlinx.android.synthetic.main.fragment_common_booking_status.*
import org.json.JSONObject

class CommonBookingStatusFragment : BaseFragment(), MyInterface, BookingAcceptReject {


    var arrayList = ArrayList<BookingStatusModel>()
    var type: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments!!.getInt("type")

        initViews()
        hitAPI(type, "")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_common_booking_status, null)
    }

    private fun initViews() {
        commonRecycler.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
    }

    private fun hitAPI(type: Int, args: String) {
        if (KotlinUtils.isNetConnected(requireContext())) {
            Utils.initializeAndShow(requireContext())
            arrayList.clear()
            if (type == FOR_ACCEPTED_REQ) {

                val json = JSONObject()
                json.put("VenueId", SharedPref.getString(Constants.USER_ID, ""))
                json.put("VenueStatus", "B")

                RetrofitAPI.callAPI(json.toString(), FOR_ACCEPTED_REQ, this)
            } else if (type == FOR_PENDING_REQ) {
                val json = JSONObject()
                json.put("VenueId", SharedPref.getString(Constants.USER_ID, ""))
                json.put("VenueStatus", "P")

                RetrofitAPI.callAPI(json.toString(), FOR_PENDING_REQ, this)
            } else if (type == FOR_REJECTED_REQ) {
                val json = JSONObject()
                json.put("VenueId", SharedPref.getString(Constants.USER_ID, ""))
                json.put("VenueStatus", "R")

                RetrofitAPI.callAPI(json.toString(), FOR_REJECTED_REQ, this)
            } else if (type == FOR_ACCEPT_EVENT_REQ) {
                RetrofitAPI.callAPI(args, FOR_ACCEPT_EVENT_REQ, this)
            } else if (type == FOR_REJECT_EVENT_REQ) {
                RetrofitAPI.callAPI(args, FOR_REJECT_EVENT_REQ, this)
            }
        } else {
            Utils.showToast(requireContext(), resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            FOR_ACCEPTED_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    val jsonArray = json.getJSONArray("result")
                    if (jsonArray.length() > 0) {
                        commonRecycler.visibility=View.VISIBLE
                        tvNoRecord.visibility=View.GONE

                        arrayList = Gson().fromJson<java.util.ArrayList<BookingStatusModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<BookingStatusModel>>() {

                        }.type)

                        val adapter=AcceptedAndRejectedBookingAdapter(requireActivity(),arrayList)
                        commonRecycler.adapter=adapter
                    }
                    else
                    {
                        commonRecycler.visibility=View.GONE
                        tvNoRecord.visibility=View.VISIBLE
                    }

                } else {
                    commonRecycler.visibility=View.GONE
                    tvNoRecord.visibility=View.VISIBLE
                }

            }
            FOR_PENDING_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    val jsonArray = json.getJSONArray("result")
                    if (jsonArray.length() > 0) {
                        commonRecycler.visibility=View.VISIBLE
                        tvNoRecord.visibility=View.GONE

                        arrayList = Gson().fromJson<java.util.ArrayList<BookingStatusModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<BookingStatusModel>>() {

                        }.type)

                        val adapter=PendingBookingAdapter(requireActivity(),arrayList,this)
                        commonRecycler.adapter=adapter
                    }
                    else
                    {
                        commonRecycler.visibility=View.GONE
                        tvNoRecord.visibility=View.VISIBLE
                    }

                } else {
                    commonRecycler.visibility=View.GONE
                    tvNoRecord.visibility=View.VISIBLE
                }

            }
            FOR_REJECTED_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    val jsonArray = json.getJSONArray("result")
                    if (jsonArray.length() > 0) {
                        commonRecycler.visibility=View.VISIBLE
                        tvNoRecord.visibility=View.GONE

                        arrayList = Gson().fromJson<java.util.ArrayList<BookingStatusModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<BookingStatusModel>>() {

                        }.type)

                        val adapter=AcceptedAndRejectedBookingAdapter(requireActivity(),arrayList)
                        commonRecycler.adapter=adapter
                    }
                    else
                    {
                        commonRecycler.visibility=View.GONE
                        tvNoRecord.visibility=View.VISIBLE
                    }

                } else {
                    commonRecycler.visibility=View.GONE
                    tvNoRecord.visibility=View.VISIBLE
                }

            }
            FOR_ACCEPT_EVENT_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    Utils.showToast(requireContext(), json.getString("Message"))
                    hitAPI(FOR_PENDING_REQ, "")
                } else {
                    Utils.showToast(requireContext(), json.getString("Message"))
                }

            }
            FOR_REJECT_EVENT_REQ -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    Utils.showToast(requireContext(), json.getString("Message"))
                    hitAPI(FOR_PENDING_REQ, "")
                } else {
                    Utils.showToast(requireContext(), json.getString("Message"))
                }

            }
        }
    }

    override fun details(venue_id: String, booking_id: String, booking_status: String) {
        if (booking_status.equals("B")) {
            val json = JSONObject()
            json.put("VenueBookingId", booking_id)
            json.put("VenueId", venue_id)
            json.put("BookingStatus", booking_status)
            hitAPI(FOR_ACCEPT_EVENT_REQ, json.toString())
        } else if (booking_status.equals("R")) {
            val json = JSONObject()
            json.put("VenueBookingId", booking_id)
            json.put("VenueId", venue_id)
            json.put("BookingStatus", booking_status)
            hitAPI(FOR_REJECT_EVENT_REQ, json.toString())
        }
    }
}