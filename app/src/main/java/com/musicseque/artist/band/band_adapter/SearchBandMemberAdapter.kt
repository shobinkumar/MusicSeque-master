package com.musicseque.artist.band.band_adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.artist.activity.other_artist_activity.OtherProfileActivity
import com.musicseque.artist.band.band_model.BandMemberStatusModel
import com.musicseque.interfaces.RemoveMemberInterface
import kotlinx.android.synthetic.main.row_band_member_status.view.ivArtistImage
import kotlinx.android.synthetic.main.row_band_member_status.view.tvBandName
import kotlinx.android.synthetic.main.row_band_member_status.view.tvCountry
import kotlinx.android.synthetic.main.row_band_member_status.view.tvProfileType
import kotlinx.android.synthetic.main.row_search_band_member.view.*

class SearchBandMemberAdapter(var al: ArrayList<BandMemberStatusModel>, var activity: Activity, var intefce: RemoveMemberInterface) : RecyclerView.Adapter<SearchBandMemberAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindRecord(model: BandMemberStatusModel,pos:Int) {

            if (model.artistIsBandMember.equals("Y",ignoreCase = true) || model.artistIsBandMember .equals("P",ignoreCase = true)) {

                itemView.ivMemberStatus.setImageResource(R.drawable.icon_delete_media)

            } else {
                itemView.ivMemberStatus.setImageResource(R.drawable.icon_add_member)


            }
            itemView.tvBandName.text = model.artistFirstName + " " + model.artistLastName
            itemView.tvProfileType.text = model.artistGenreTypeName + ", " + model.artistExpertise
            itemView.tvCountry.text = model.artistCity + " , " + model.artistCountryName

            if (model.artistProfilePic == "" && model.artistSocialImageUrl=="") {
                Glide.with(itemView.context)
                        .load(R.drawable.icon_img_dummy)
                        .into(itemView.ivArtistImage)
            }
            else if (model.artistProfilePic != "") {
                Glide.with(itemView.context)
                        .load(model.artistProfilePicServerPath + model.artistProfilePic)
                        .into(itemView.ivArtistImage)
            }

            else if (model.artistSocialImageUrl != "") {
                Glide.with(itemView.context)
                        .load(model.artistSocialImageUrl).into(itemView.ivArtistImage)
            }
            itemView.ivMemberStatus.setOnClickListener { view ->
                if (model.artistIsBandMember == "N")
                    intefce.addOrRemoveMember(model.artistUserId.toString(), "add", model,pos)
                else
                    intefce.addOrRemoveMember(model.artistUserId.toString(), "remove", model,pos)
            }
            itemView.setOnClickListener {


                activity.startActivity(Intent(activity, OtherProfileActivity::class.java).putExtra("id", model.artistUserId))
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SearchBandMemberAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_search_band_member, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
     return al.size
    }

    override fun onBindViewHolder(holder: SearchBandMemberAdapter.ViewHolder, position: Int) {
        val model: BandMemberStatusModel = al.get(position)
        holder.bindRecord(model,position)


    }
}