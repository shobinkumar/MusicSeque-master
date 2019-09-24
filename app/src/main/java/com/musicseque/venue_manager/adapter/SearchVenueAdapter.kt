package com.musicseque.venue_manager.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.musicseque.R
import com.musicseque.venue_manager.activity.OtherVenueProfileActivity
import com.musicseque.venue_manager.model.VenueSearchModel
import kotlinx.android.synthetic.main.row_venue_item.view.*

class SearchVenueAdapter(val activity: Activity, val arrayList: ArrayList<VenueSearchModel>) : RecyclerView.Adapter<SearchVenueAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_venue_item, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(arrayList.get(pos), activity)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bandDataModel: VenueSearchModel, activity: Activity) {
            if (bandDataModel.venueManagerProfileImg.equals("", true)) {
                Glide.with(itemView.context)
                        .load(R.drawable.icon_img_dummy)
                        .into(itemView.ivVenueImage)
            } else {
                Glide.with(itemView.context)
                        .load(bandDataModel.venueManagerProfileImgPath + bandDataModel.venueManagerProfileImg)
                        .apply(RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(itemView.ivVenueImage)

            }
            itemView.tvVenueName.text = bandDataModel.venueName
            itemView.tvVenueAddress.text = bandDataModel.venueAddress
            itemView.tvVenueCountry.text = bandDataModel.venueCity + "," + bandDataModel.venueCountry
            itemView.setOnClickListener {
                itemView.setOnClickListener {

                    val intent = Intent(activity, OtherVenueProfileActivity::class.java).putExtra("venue_id", bandDataModel.venueManagerId)
                    activity.startActivity(intent)
                }


            }

        }

    }
}