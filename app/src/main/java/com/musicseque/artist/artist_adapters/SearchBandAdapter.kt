package com.musicseque.artist.artist_adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.musicseque.R
import com.musicseque.artist.activity.other_artist_activity.ArtistBandDetailActivity
import com.musicseque.artist.artist_models.BandDataModel
import com.musicseque.artist.artist_models.BandListModel
import kotlinx.android.synthetic.main.row_artist_band_item.view.*

class SearchBandAdapter(val arrayList: ArrayList<BandListModel>) : RecyclerView.Adapter<SearchBandAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_artist_band_item, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {
        holdr.bind(arrayList.get(pos))
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bandDataModel: BandListModel) {
            if (bandDataModel.getBandProfileImg().equals("", ignoreCase = true)) {
                Glide.with(itemView.context)
                        .load(R.drawable.icon_img_dummy)
                        .into(itemView.ivBandImage)
            } else {
                Glide.with(itemView.context)
                        .load(bandDataModel.getBandProfileImgPath() + bandDataModel.getBandProfileImg())
                        .apply(RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(itemView.ivBandImage)

            }
            itemView.tvBandName.setText(bandDataModel.getBandName())
            itemView.tvBandType.setText(bandDataModel.getBandGenreType())
            itemView.tvCityCountry.setText(bandDataModel.getBandCity() + "," + bandDataModel.getBandCountry())

            itemView.setOnClickListener(View.OnClickListener { view ->
                itemView.context.startActivity(Intent(itemView.context, ArtistBandDetailActivity::class.java).putExtra("band_id", bandDataModel.getBandId()!!.toString()))


            })

        }

    }

}