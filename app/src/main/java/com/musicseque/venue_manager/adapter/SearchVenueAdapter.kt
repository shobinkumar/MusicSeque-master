package com.musicseque.venue_manager.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.venue_manager.model.VenueListModel

class SearchVenueAdapter(val arrayList: ArrayList<VenueListModel>) : RecyclerView.Adapter<SearchVenueAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_venue_item, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(arrayList.get(pos))
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bandDataModel: VenueListModel) {
//            if (bandDataModel.getBandProfileImg().equals("", ignoreCase = true)) {
//                Glide.with(itemView.context)
//                        .load(R.drawable.icon_img_dummy)
//                        .into(itemView.ivBandImage)
//            } else {
//                Glide.with(itemView.context)
//                        .load(bandDataModel.getBandProfileImgPath() + bandDataModel.getBandProfileImg())
//                        .apply(RequestOptions()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL))
//                        .into(itemView.ivBandImage)
//
//            }
//            itemView.tvBandName.setText(bandDataModel.getBandName())
//            itemView.tvBandType.setText(bandDataModel.getBandGenreType())
//            itemView.tvCityCountry.setText(bandDataModel.getBandCity() + "," + bandDataModel.getBandCountry())
//
//            itemView.setOnClickListener(View.OnClickListener { view ->
//                itemView.context.startActivity(Intent(itemView.context, ArtistBandDetailActivity::class.java).putExtra("band_id", bandDataModel.getBandId()!!.toString()))
//
//
//            })

        }

    }

}