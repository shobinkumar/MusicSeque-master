package com.musicseque.artist.artist_adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.artist.fragments.PendingEventsListArtistFragment
import com.musicseque.event_manager.model.EventListModel
import kotlinx.android.synthetic.main.list_item_pending_events_artist.view.*

class PendingEventsArtistAdapter(val ctxFrag: PendingEventsListArtistFragment, val arrayList: ArrayList<EventListModel>) : RecyclerView.Adapter<PendingEventsArtistAdapter.MyHolder>() {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: EventListModel) {

                itemView.tvVenueRequestedPendingArtist.visibility = View.VISIBLE
                itemView.tvVenuePendingArtist.text = model.venue_full_name

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PendingEventsArtistAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_pending_events_artist, parent, false)
        return PendingEventsArtistAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: PendingEventsArtistAdapter.MyHolder, pos: Int) {
        val model=arrayList.get(pos)
        holder.bind(model)
    }
}