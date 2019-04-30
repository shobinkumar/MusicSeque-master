package com.musicseque.artist.band.band_adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.artist.band.band_model.BandMemberStatusModel
import kotlinx.android.synthetic.main.row_band_member_status.view.*


class BandMemberStatusAdapter(var userList: ArrayList<BandMemberStatusModel>, val context: Context):RecyclerView.Adapter<BandMemberStatusAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: BandMemberStatusModel) {
            itemView.tvBandName.text="Hello"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BandMemberStatusAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_band_member_status, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: BandMemberStatusAdapter.ViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
