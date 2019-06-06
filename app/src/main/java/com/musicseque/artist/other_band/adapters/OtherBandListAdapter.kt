package com.musicseque.artist.other_band.adapters

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.musicseque.R
import com.musicseque.artist.artist_models.BandDataModel
import com.musicseque.artist.band.band_fragment.BandProfileDetailFragment
import kotlinx.android.synthetic.main.row_other_band_item.view.*

class OtherBandListAdapter(val act: Activity, val al: ArrayList<BandDataModel>) : RecyclerView.Adapter<OtherBandListAdapter.ViewHolder>() {
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

    }

    class MyHolder {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): OtherBandListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_other_band_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return al.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(band_model: BandDataModel) {

            itemView.tvBandName.text = band_model.bandName
            itemView.tvBandType.text = band_model.genreTypeName
            itemView.tvCityCountry.text = band_model.bandCity + "," + band_model.countryName
            if (band_model.getBandImg().equals("", ignoreCase = true)) {
                Glide.with(itemView.context)
                        .load(R.drawable.icon_img_dummy)
                        .apply(RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(itemView.ivBandImage)
            } else {
                Glide.with(itemView.context)
                        .load(band_model.getBandImgPath() + band_model.getBandImg())
                        .apply(RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(itemView.ivBandImage)

            }

            itemView.setOnClickListener(
                    {
                        val ldf = BandProfileDetailFragment()
                        val args = Bundle()
                        args.putString("band_id", band_model.getBandId()!!.toString())
                        ldf.arguments = args

                        (itemView.getContext() as FragmentActivity).supportFragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, ldf)
                                .commit()

                    }
            )

        }
    }
}