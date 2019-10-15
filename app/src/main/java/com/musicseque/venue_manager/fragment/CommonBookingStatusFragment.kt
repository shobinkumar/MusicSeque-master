package com.musicseque.venue_manager.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.artist.fragments.BaseFragment
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants.*
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils

class CommonBookingStatusFragment : BaseFragment(), MyInterface {


    var type: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments!!.getInt("type")
        hitAPI()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_common_booking_status, null)
    }


    private fun hitAPI() {
        if (KotlinUtils.isNetConnected(requireContext())) {
            Utils.initializeAndShow(requireContext())
            if (type == FOR_ACCEPTED_REQ) {
                KotlinHitAPI.callAPI("", FOR_ACCEPTED_REQ, this)
            } else if (type == FOR_PENDING_REQ) {
                KotlinHitAPI.callAPI("", FOR_PENDING_REQ, this)
            } else if (type == FOR_REJECTED_REQ) {
                KotlinHitAPI.callAPI("", FOR_REJECTED_REQ, this)
            }
        } else {
            Utils.showToast(requireContext(), resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            FOR_ACCEPTED_REQ -> {

            }
            FOR_PENDING_REQ -> {

            }
            FOR_REJECTED_REQ -> {

            }
        }
    }
    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }
}