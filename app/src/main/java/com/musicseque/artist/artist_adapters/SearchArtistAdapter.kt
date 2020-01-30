package com.musicseque.artist.artist_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import com.musicseque.artist.activity.other_artist_activity.OtherProfileActivity
import com.musicseque.artist.activity.other_artist_activity.SearchArtistActivity
import com.musicseque.artist.artist_models.ArtistModel
import com.musicseque.service.LocationService
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.row_search_artist.view.*
import java.text.DecimalFormat
import java.util.*

class SearchArtistAdapter(context: SearchArtistActivity, arrayList: ArrayList<ArtistModel>, user_id: String) : RecyclerView.Adapter<SearchArtistAdapter.ViewHolder>() {
    var arrayList = ArrayList<ArtistModel>()
    var context: Context
    var mUserId: String
    var df2: DecimalFormat
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val inflater = LayoutInflater.from(
                context)
        val v = inflater.inflate(R.layout.row_search_artist, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val artistModel = arrayList.get(i)
        viewHolder.bindItems(artistModel)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("NewApi")
        fun bindItems(artistModel: ArtistModel) {

            itemView.tvArtistName!!.text = artistModel.firstName + " " + artistModel.lastName
            val mDistance = Utils.calculateDistance(LocationService.mLatitude.toDouble(), LocationService.mLongitude.toDouble(), artistModel.userLatitude.toDouble(), artistModel.userLongitude.toDouble())
            itemView.tvDistance!!.text = df2.format(mDistance) + " miles"
            itemView.tvProfileType!!.text = artistModel.expertise + "," + artistModel.genreTypeName

            itemView.tvCountry!!.text = artistModel.city + "," + artistModel.countryName
            if (!artistModel.socialImageUrl.equals("", ignoreCase = true)) {
                Glide.with(context).load(artistModel.socialImageUrl).into(itemView.ivArtistImage!!)
            } else if (!artistModel.profilePic.equals("", ignoreCase = true)) {
                Glide.with(context).load(artistModel.serverpath + artistModel.profilePic).into(itemView.ivArtistImage!!)
            } else {
                Glide.with(context).load(R.drawable.icon_img_dummy).into(itemView.ivArtistImage!!)
            }

            when (artistModel.newStatus) {
                "Available" -> itemView.ivIndicator!!.setImageDrawable(context.resources.getDrawable(R.drawable.icon_green))
                "Offline" -> itemView.ivIndicator!!.setImageDrawable(context.resources.getDrawable(R.drawable.icon_invisible))
                "Do_not_disturb" ->
                    itemView.ivIndicator!!.setImageDrawable(context.resources.getDrawable(R.drawable.icon_red))


            }


            itemView.setOnClickListener { context.startActivity(Intent(context, OtherProfileActivity::class.java).putExtra("id", artistModel.userId)) }

        }

    }

    init {
        this.arrayList = arrayList
        this.context = context
        mUserId = user_id
        df2 = DecimalFormat(".##")
    }
}