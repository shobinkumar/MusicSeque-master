package com.musicseque.artist.artist_adapters

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.artist.activity.other_artist_activity.OtherProfileActivity
import com.musicseque.artist.band.band_model.BandMemberStatusModel
import kotlinx.android.synthetic.main.row_band_member_status.view.*

class OtherArtistBandMemberStatusAdapter(var al: ArrayList<BandMemberStatusModel>, val act: FragmentActivity) : RecyclerView.Adapter<OtherArtistBandMemberStatusAdapter.ViewHolder>() {

    var sType:String=""

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OtherArtistBandMemberStatusAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_other_band_member_status, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return al.size
    }

    override fun onBindViewHolder(viewHolder: OtherArtistBandMemberStatusAdapter.ViewHolder, position: Int) {
        val model: BandMemberStatusModel = al.get(position)

        viewHolder.bind(model,position)
    }


    inner  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: BandMemberStatusModel, pos:Int)
        {

            if ((sType != model.artistIsBandMember || sType == model.artistIsBandMember) && model.artistIsBandMember .equals("Y",ignoreCase = true)) {
                sType = model.artistIsBandMember!!
                itemView.tvHeading.visibility= View.GONE

            } else
            {

                val value = if (sType == model.artistIsBandMember) true
                else
                    false


                if (value) {
                    itemView.tvHeading.visibility= View.GONE

                } else {
                    sType= model.artistIsBandMember!!
                    itemView.tvHeading.visibility= View.VISIBLE
                    itemView.tvHeading.text="Pending"
                }

            }
            itemView.tvBandName.text = model.artistFirstName + " " + model.artistLastName
            itemView.tvProfileType.text = model.artistGenreTypeName + ", " + model.artistExpertise
            itemView.tvCountry.text = model.artistCity + " , " + model.artistCountryName

            if (model.artistProfilePic == "" && model.artistSocialImageUrl == "") {
                Glide.with(itemView.context)
                        .load(R.drawable.icon_img_dummy)
                        .into(itemView.ivArtistImage)
            } else if (model.artistProfilePic != "") {
                Glide.with(itemView.context)
                        .load(model.artistProfilePicServerPath + model.artistProfilePic)
                        .into(itemView.ivArtistImage)
            } else if (model.artistSocialImageUrl != "") {
                Glide.with(itemView.context)
                        .load(model.artistSocialImageUrl).into(itemView.ivArtistImage)
            }
            itemView.setOnClickListener(View.OnClickListener {

                val intent=Intent(itemView.context, OtherProfileActivity::class.java).putExtra("id",model.artistUserId)
                itemView.context.startActivity(intent)
            })


        }


    }

}
