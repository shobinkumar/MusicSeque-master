package com.musicseque.venue_manager.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.venue_manager.model.VenueListModel

class VenueTimmingsAdapter : RecyclerView.Adapter<VenueTimmingsAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): VenueTimmingsAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_timmings, parent, false)
        return VenueTimmingsAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: VenueTimmingsAdapter.MyHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bandDataModel: VenueListModel) {

        }
    }

}