package com.musicseque.venue_manager.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.artist.activity.other_artist_activity.OtherProfileActivity
import com.musicseque.event_manager.activity.EventDetailVenueActivity
import com.musicseque.interfaces.BookingAcceptReject
import com.musicseque.utilities.Constants.FOR_PENDING_REQ
import com.musicseque.venue_manager.model.BookingStatusModel
import kotlinx.android.synthetic.main.row_pending_booking_item.view.*

class PendingBookingAdapter(val activity: Activity, val arrayList: ArrayList<BookingStatusModel>, val intfc: BookingAcceptReject) : RecyclerView.Adapter<PendingBookingAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PendingBookingAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_pending_booking_item, parent, false)
        return PendingBookingAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(p0: PendingBookingAdapter.MyHolder, p1: Int) {
        p0.bind(arrayList.get(p1), activity, intfc)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: BookingStatusModel, activity: Activity, intfc: BookingAcceptReject) {


            val textShow= "<b>" + model.ArtistName + "</b> " + " "+"has sent you venue booking request"

            itemView.tvArtistName.text =  Html.fromHtml(textShow)
            itemView.tvRequestSentTimmings.text = model.BookedOn + " " + model.BookingTime

            itemView.tvAcceptRequest.setOnClickListener { intfc.details(model.VenueId.toString(), model.VenueBookingId.toString(), "B") }
            itemView.tvRejectRequest.setOnClickListener { intfc.details(model.VenueId.toString(), model.VenueBookingId.toString(), "R") }

            if(model.ArtistImg.equals("",true))
            {
                Glide.with(itemView.context)
                        .load(R.drawable.icon_img_dummy)
                        .into(itemView.ivEventManagerImage)
            }
            else
            {
                Glide.with(itemView.context)
                        .load(model.ArtistImgPath + model.ArtistImg)
                        .into(itemView.ivEventManagerImage)
            }


            itemView.setOnClickListener {

                val intent= Intent(activity, EventDetailVenueActivity::class.java).putExtra("event_id",model.EventId.toString()).putExtra("event_type",FOR_PENDING_REQ)
                activity.startActivity(intent)


            }
            itemView.tvArtistName.setOnClickListener {
                val intent= Intent(activity, OtherProfileActivity::class.java).putExtra("id",model.ArtistId)
                activity.startActivity(intent)


            }

        }

    }
}