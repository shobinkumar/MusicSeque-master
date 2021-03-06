package com.musicseque.event_manager.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.artist.artist_models.ArtistModel
import com.musicseque.event_manager.activity.ArtistDetailEventManagerActivity
import com.musicseque.event_manager.activity.SearchVenueEventManagerActivity
import com.musicseque.event_manager.activity.VenueDetailEventManagerActivity
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.event_manager.model.EventModelParcelable
import com.musicseque.service.LocationService
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.list_row_venue_search_event_manager.view.*
import java.text.DecimalFormat

class SearchVenueEventManagerAdapter(val activity: SearchVenueEventManagerActivity, val arrayList: ArrayList<ArtistModel>) : RecyclerView.Adapter<SearchVenueEventManagerAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_row_venue_search_event_manager, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(arrayList.get(pos), activity, pos)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(artistModel: ArtistModel, act: SearchVenueEventManagerActivity, pos: Int) {
            itemView.llMainSearchVenueEventManager.setVisibility(View.VISIBLE)
            itemView.tvArtistNameSearchVenueEventManager.setText(artistModel.firstName + " " + artistModel.lastName)


                itemView.tvDistanceSearchVenueEventManager.setText(artistModel.distanceFromLoggedInUserInMiles+" miles")


            itemView.tvProfileTypeSearchVenueEventManager.setText(artistModel.getExpertise() + "," + artistModel.getGenreTypeName())
            itemView.tvCountrySearchVenueEventManager.setText(artistModel.getCity() + "," + artistModel.getCountryName())
            if (!artistModel.getSocialImageUrl().equals("", ignoreCase = true)) {
                Glide.with(itemView.context).load(artistModel.getSocialImageUrl()).into(itemView.ivArtistImageSearchVenueEventManager)

            } else if (!artistModel.getProfilePic().equals("", ignoreCase = true)) {
                Glide.with(itemView.context).load(artistModel.getServerpath() + artistModel.getProfilePic()).into(itemView.ivArtistImageSearchVenueEventManager)

            } else {
                Glide.with(itemView.context).load(R.drawable.icon_img_dummy).into(itemView.ivArtistImageSearchVenueEventManager)

            }

            if (artistModel.getNewStatus().equals("Available", ignoreCase = true)) {
                itemView.ivIndicatorSearchVenueEventManager.setImageDrawable(itemView.context.getResources().getDrawable(R.drawable.icon_green))
            } else if (artistModel.getNewStatus().equals("Offline", ignoreCase = true)) {
                itemView.ivIndicatorSearchVenueEventManager.setImageDrawable(itemView.context.getResources().getDrawable(R.drawable.icon_invisible))
            } else {
                itemView.ivIndicatorSearchVenueEventManager.setImageDrawable(itemView.context.getResources().getDrawable(R.drawable.icon_red))
            }
            itemView.setOnClickListener(View.OnClickListener {
                if (act.eventsList.size == 0) {

                } else {
                    if (act.mEventId.equals("")) {
                        Utils.showToast(act, "Select the event from the list")
                    } else {
                        val model = act.eventsListMain.get(pos)
                        val model1 = EventModelParcelable(model.event_id, model.event_title, model.event_description, model.event_type_id, model.venue_from_date, model.venue_to_date, model.venue_from_time, model.venue_to_time, model.event_estimated_guest, model.event_currency_id, model.event_budget)
                        itemView.context.startActivity(Intent(itemView.context, VenueDetailEventManagerActivity::class.java).putExtra("data", model1).putExtra("venue_id",artistModel.userId   ))

                    }
                }
            })

        }

    }
}