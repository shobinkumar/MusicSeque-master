package com.musicseque.venue_manager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.musicseque.Fonts.BoldNoyhr
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.artist.fragments.BaseFragment
import com.musicseque.utilities.Constants.FOR_ACCEPTED_REQ
import com.musicseque.utilities.Constants.FOR_PENDING_REQ
import com.musicseque.utilities.Constants.FOR_REJECTED_REQ
import kotlinx.android.synthetic.main.fragment_booking_status.*

class VenueBookingStatusFragment : BaseFragment(), View.OnClickListener {

    private var imgRight: ImageView? = null
    private var tvHeading: BoldNoyhr? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        imgRight = activity?.findViewById<ImageView>(R.id.img_right_icon)
        tvHeading = activity?.findViewById<BoldNoyhr>(R.id.tvHeading)
        val tvDone = (activity as MainActivity).findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE
        imgRight?.setVisibility(View.GONE)
        tvHeading?.setText("Venue Booking Status")
        return inflater.inflate(R.layout.fragment_booking_status, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment=CommonBookingStatusFragment()
        val bundle=Bundle()
        bundle.putInt("type",FOR_PENDING_REQ)
        fragment.arguments=bundle
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
        changeColor(R.color.color_orange, R.color.color_white, R.color.color_white)


        tvAccepted.setOnClickListener(this)
        tvPending.setOnClickListener(this)
        tvRejected.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvAccepted -> {
                if (tvAccepted.currentTextColor != R.color.color_orange) {
                  val fragment=CommonBookingStatusFragment()
                    val bundle=Bundle()
                    bundle.putInt("type",FOR_ACCEPTED_REQ)
                    fragment.arguments=bundle
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.frame_layout, fragment)
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                }
                changeColor(R.color.color_white, R.color.color_orange, R.color.color_white)
            }
            R.id.tvRejected -> {
                if (tvAccepted.currentTextColor != R.color.color_orange) {
                    val fragment=CommonBookingStatusFragment()
                    val bundle=Bundle()
                    bundle.putInt("type", FOR_REJECTED_REQ)
                    fragment.arguments=bundle
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.frame_layout, fragment)
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                }
                changeColor(R.color.color_white, R.color.color_white, R.color.color_orange)
            }
            R.id.tvPending -> {
                if (tvAccepted.currentTextColor != R.color.color_orange) {
                    val fragment=CommonBookingStatusFragment()
                    val bundle=Bundle()
                    bundle.putInt("type", FOR_PENDING_REQ)
                    fragment.arguments=bundle
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.frame_layout, fragment)
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                }
                changeColor(R.color.color_orange, R.color.color_white, R.color.color_white)

            }
        }


    }

    private fun changeColor(color1: Int, color2: Int, color3: Int) {
        tvPending.setTextColor(resources.getColor(color1))
        tvAccepted.setTextColor(resources.getColor(color2))
        tvRejected.setTextColor(resources.getColor(color3))

    }
}