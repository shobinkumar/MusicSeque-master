package com.musicseque.event_manager.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.artist.artist_models.ArtistModel
import com.musicseque.event_manager.activity.ArtistDetailEventManagerActivity
import com.musicseque.event_manager.activity.SearchArtistActivityEventManager
import com.musicseque.service.LocationService
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.row_search_artist.view.*
import java.text.DecimalFormat

class SearchArtistAdapterEventManager(val act: SearchArtistActivityEventManager, val arrayList: ArrayList<ArtistModel>, val user_id: String) : RecyclerView.Adapter<SearchArtistAdapterEventManager.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): com.musicseque.event_manager.adapter.SearchArtistAdapterEventManager.MyHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.row_search_artist, parent, false)


        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val model: ArtistModel = arrayList.get(position)

        holder.bindItems(model, act)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(artistModel: ArtistModel, act: SearchArtistActivityEventManager) {
            itemView.rlMain.setVisibility(View.VISIBLE)
            itemView.tvArtistName.setText(artistModel.getFirstName() + " " + artistModel.getLastName())

            val mDistance = Utils.calculateDistance(java.lang.Double.parseDouble(LocationService.mLatitude), java.lang.Double.parseDouble(LocationService.mLongitude), java.lang.Double.parseDouble(artistModel.getUserLatitude()), java.lang.Double.parseDouble(artistModel.getUserLongitude()))
            var decimalFormat = DecimalFormat(".##")

            if(mDistance==0.0)
            {
                itemView.tvDistance.setText("0 miles")

            }
            else
            {
                itemView.tvDistance.setText(decimalFormat.format(mDistance) + " miles")

            }
            itemView.tvProfileType.setText(artistModel.getExpertise() + "," + artistModel.getGenreTypeName())
            itemView.tvCountry.setText(artistModel.getCity() + "," + artistModel.getCountryName())
            if (!artistModel.getSocialImageUrl().equals("", ignoreCase = true)) {
                Glide.with(itemView.context).load(artistModel.getSocialImageUrl()).into(itemView.ivArtistImage)

            } else if (!artistModel.getProfilePic().equals("", ignoreCase = true)) {
                Glide.with(itemView.context).load(artistModel.getServerpath() + artistModel.getProfilePic()).into(itemView.ivArtistImage)

            } else {
                Glide.with(itemView.context).load(R.drawable.icon_img_dummy).into(itemView.ivArtistImage)

            }

            if (artistModel.getNewStatus().equals("Available", ignoreCase = true)) {
                itemView.ivIndicator.setImageDrawable(itemView.context.getResources().getDrawable(R.drawable.icon_green))
            } else if (artistModel.getNewStatus().equals("Offline", ignoreCase = true)) {
                itemView.ivIndicator.setImageDrawable(itemView.context.getResources().getDrawable(R.drawable.icon_invisible))
            } else {
                itemView.ivIndicator.setImageDrawable(itemView.context.getResources().getDrawable(R.drawable.icon_red))
            }
            itemView.setOnClickListener(View.OnClickListener {
                if (act.eventsList.size == 0) {

                } else {
                    if (act.mEventId.equals("")) {
                        Utils.showToast(act, "Select the event from the list")
                    } else {
                        itemView.context.startActivity(Intent(itemView.context, ArtistDetailEventManagerActivity::class.java).putExtra("id", artistModel.getUserId()).putExtra("event_id", act.mEventId))

                    }
                }
            })


        }
    }
}