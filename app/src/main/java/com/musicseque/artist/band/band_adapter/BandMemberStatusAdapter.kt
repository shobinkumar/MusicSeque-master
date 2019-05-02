package com.musicseque.artist.band.band_adapter

import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.MyApplication.context
import com.musicseque.R
import com.musicseque.artist.band.band_model.BandMemberStatusModel
import kotlinx.android.synthetic.main.row_band_member_status.view.*


class BandMemberStatusAdapter(var al: ArrayList<BandMemberStatusModel>, val act: FragmentActivity) : RecyclerView.Adapter<BandMemberStatusAdapter.ViewHolder>() {

     var sType:String=""

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BandMemberStatusAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_band_member_status, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return al.size
    }

    override fun onBindViewHolder(viewHolder: BandMemberStatusAdapter.ViewHolder, position: Int) {
        val model: BandMemberStatusModel = al.get(position)

        viewHolder.bind(model)
    }


  inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: BandMemberStatusModel)
        {

            if ((sType != model.artistIsBandMember || sType == model.artistIsBandMember) && model.artistIsBandMember == "Y") {
                sType = model.artistIsBandMember!!
                itemView.tvHeading.visibility=View.GONE

            } else
            {

                val value = if (sType == model.artistIsBandMember) true
                else
                    false


                if (value) {
                    itemView.tvHeading.visibility=View.GONE

                } else {
                    sType= model.artistIsBandMember!!
                    itemView.tvHeading.visibility=View.VISIBLE
                    itemView.tvHeading.text="Pending"
                }

            }
            itemView.tvBandName.text = model.artistFirstName + " " + model.artistLastName
            itemView.tvProfileType.text = model.artistGenreTypeName + ", " + model.artistExpertise
            itemView.tvCountry.text = model.artistCity + " , " + model.artistCountryName

            if (model.artistProfilePic == "") {

            } else if (model.artistProfilePic != "") {
                Glide.with(itemView.context)
                        .load(model.artistProfilePicServerPath + model.artistProfilePic)
                        .into(itemView.ivArtistImage)
            } else if (model.artistSocialImageUrl != "") {
                Glide.with(itemView.context)
                        .load(model.artistSocialImageUrl).into(itemView.ivArtistImage)
            }

        }

    }

}
