package com.musicseque.venue_manager.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.artist.activity.other_artist_activity.OtherProfileActivity
import com.musicseque.event_manager.activity.EventDetailVenueActivity
import com.musicseque.venue_manager.model.BookingStatusModel
import kotlinx.android.synthetic.main.row_accepted_rejected_booking_item.view.*
import android.text.Spannable
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import com.musicseque.utilities.Constants


class AcceptedAndRejectedBookingAdapter(val activity: Activity, val arrayList: ArrayList<BookingStatusModel>) : RecyclerView.Adapter<AcceptedAndRejectedBookingAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AcceptedAndRejectedBookingAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_accepted_rejected_booking_item, parent, false)
        return AcceptedAndRejectedBookingAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: AcceptedAndRejectedBookingAdapter.MyHolder, pos: Int) {

        holder.bind(arrayList.get(pos), activity)

    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: BookingStatusModel, activity: Activity) {

            spannableString(model.ArtistName,itemView.tvArtistName)
           // itemView.tvArtistName.text= Html.fromHtml("<b>" + model.ArtistName + "</b> ")
            itemView.tvRequestSentTimmings.text= model.BookedOn + " " + model.BookingTime

            if(model.BookingStatus.equals("B",true))
            {
                val textShow=  model.ArtistName + " "+" has booked venue for music event ";
                itemView.tvArtistNameRequest.text=Html.fromHtml(textShow)
            }
            else
            {
                val textShow=  model.ArtistName + " "+"has rejected venue for music event ";
                itemView.tvArtistNameRequest.text=Html.fromHtml(textShow)
            }



            itemView.tvEventName.text="Event Name:"+model.EventName
            val textDate= "<b>" + "Date:" + "</b> " + " "+model.event_from_date+" to "+model.event_to_date
            val textTime="<b>" + "Time:" + "</b> " + " "+model.event_time_from+" to "+model.event_time_to
            itemView.tvEventDate.text=Html.fromHtml(textDate)
            itemView.tvEventTime.text=Html.fromHtml(textTime)

            if(model.ArtistImg.equals("",true))
            {
                Glide.with(itemView.context)
                        .load(com.musicseque.R.drawable.icon_img_dummy)
                        .into(itemView.ivEventManagerImage)
            }
            else
            {
                Glide.with(itemView.context)
                        .load(model.ArtistImgPath + model.ArtistImg)
                        .into(itemView.ivEventManagerImage)
            }




            itemView.tvEventName.setOnClickListener {
                if(model.BookingStatus.equals("B",true))
                {
                    val intent= Intent(activity, EventDetailVenueActivity::class.java).putExtra("event_id",model.EventId.toString()).putExtra("event_type", Constants.FOR_ACCEPTED_REQ)
                    activity.startActivity(intent)
                }
                else if(model.BookingStatus.equals("R",true))
                {
                    val intent= Intent(activity, EventDetailVenueActivity::class.java).putExtra("event_id",model.EventId.toString()).putExtra("event_type", Constants.FOR_REJECTED_REQ)
                    activity.startActivity(intent)
                }

            }

            itemView.tvArtistName.setOnClickListener {
                val intent= Intent(activity, OtherProfileActivity::class.java).putExtra("id",model.ArtistId)
                activity.startActivity(intent)
            }
            itemView.setOnClickListener {

                if(model.BookingStatus.equals("B",true))
                {
                    val intent= Intent(activity, EventDetailVenueActivity::class.java).putExtra("event_id",model.EventId.toString()).putExtra("event_type", Constants.FOR_ACCEPTED_REQ)
                    activity.startActivity(intent)
                }
               else if(model.BookingStatus.equals("R",true))
                {
                    val intent= Intent(activity, EventDetailVenueActivity::class.java).putExtra("event_id",model.EventId.toString()).putExtra("event_type", Constants.FOR_REJECTED_REQ)
                    activity.startActivity(intent)
                }

            }
            itemView.tvArtistName.setOnClickListener {
                val intent= Intent(activity, OtherProfileActivity::class.java).putExtra("id",model.ArtistId)
                activity.startActivity(intent)


            }


        }
        private fun spannableString(text: String, textView: Noyhr) {
            val sb =  SpannableStringBuilder(text);

            val bss =  StyleSpan(Typeface.BOLD); // Span to make text bold
            //Span to make text italic
            sb.setSpan(bss, 0, text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
            textView.text=sb

        }

    }


}