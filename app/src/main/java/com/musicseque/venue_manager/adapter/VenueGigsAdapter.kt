package com.musicseque.venue_manager.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.artist.artist_models.GigsModel
import com.musicseque.event_manager.activity.EventDetailActivity
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.row_gigs.view.*

class VenueGigsAdapter(val arrayList: ArrayList<GigsModel>) : RecyclerView.Adapter<VenueGigsAdapter.MyHolder>() {
    companion object
    {
        var mPreviousValue:String?=""
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_gigs, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(arrayList.get(pos))
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun bind(model: GigsModel) {
//            if (model.getBandProfileImg().equals("", ignoreCase = true)) {
//                Glide.with(itemView.context)
//                        .load(R.drawable.icon_img_dummy)
//                        .into(itemView.ivBandImage)
//            } else {
//                Glide.with(itemView.context)
//                        .load(model.getBandProfileImgPath() + model.getBandProfileImg())
//                        .apply(RequestOptions()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL))
//                        .into(itemView.ivBandImage)
//
//            }



            if(mPreviousValue.equals(model.venue_booking_status))
            {
                itemView.tvHeading.visibility=GONE
                itemView.tvEventName.setText("Event Name: "+model.event_title)
                itemView.tvVenueName.setText("Venue Name: "+model.venue_name)


                val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(model.venue_from_date, model.venue_to_date)

                itemView.tvVenueTimmings.setText("Timings: "+mFromDate+" "+model.venue_from_time + " - " + mToDate+" "+model.venue_to_time)

            }
            else
            {
                itemView.tvHeading.visibility= VISIBLE
                if(model.venue_booking_status.equals("R",true))
                {
                    itemView.tvHeading.text="Rejected"
                }
                else if(model.venue_booking_status.equals("P",true))
                {
                    itemView.tvHeading.text="Pending"
                }
                else
                {
                    itemView.tvHeading.text="Booked"
                }

                mPreviousValue=model.venue_booking_status
                itemView.tvEventName.setText("Event Name:"+model.event_title)
                itemView.tvVenueName.setText("Venue Name:"+model.venue_name)
                itemView.tvVenueTimmings.setText("Timings:"+model.venue_from_date+" "+model.venue_from_time + "-" + model.venue_to_date+" "+model.venue_to_time)

            }

            itemView.setOnClickListener(View.OnClickListener { view ->
                itemView.context.startActivity(Intent(itemView.context,EventDetailActivity::class.java).putExtra("event_id", model.event_id!!.toString()))


            })

        }

    }

}