package com.musicseque.event_manager.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.utilities.KotlinUtils
import kotlinx.android.synthetic.main.list_item_pending_events.view.*
import kotlinx.android.synthetic.main.list_item_upcoming_past_events.view.*

class EventStatusCommonAdapter(val ctx: Context, val al: ArrayList<EventListModel>, val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var FOR_PENDING = 1
    var FOR_UPCOMING = 2
    var FOR_PAST = 3
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FOR_PENDING -> {
                PendingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_pending_events, parent, false))
            }
            FOR_UPCOMING -> {
                UpcomingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_upcoming_past_events, parent, false))

            }
            else
            -> {
                PastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_upcoming_past_events, parent, false))

            }
        }
    }

    override fun getItemCount(): Int {
        return al.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            FOR_PENDING -> {
                val model = al.get(position)
                val pendingHolder = holder as PendingViewHolder
                pendingHolder.bindView(model, ctx)
            }
            FOR_UPCOMING -> {
                val model = al.get(position)
                val upcomingHolder = holder as UpcomingViewHolder
                upcomingHolder.bindView(model, ctx)
            }
            FOR_PAST -> {
                val model = al.get(position)
                val pastHolder = holder as PastViewHolder
                pastHolder.bindView(model, ctx)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
        return when (type) {
            FOR_PENDING ->
                return FOR_PENDING
            FOR_UPCOMING ->
                return FOR_UPCOMING
            FOR_PAST ->
                return FOR_PAST
        }
    }

    class PendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(model: EventListModel, ctx: Context) {
            if (model.event_promotion_image.equals("")) {
                Glide.with(ctx)
                        .load(R.drawable.icon_img_dummy).into(itemView.ivEventImagePending)
            } else {
                Glide.with(ctx)
                        .load(model.event_promotion_path + model.event_promotion_image).into(itemView.ivEventImagePending)
            }

            itemView.tvEventNamePending.text = model.event_title
            val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(model.venue_from_date, model.venue_to_date)
            itemView.tvEventDayPending.text = mFromDate.split("/")[0]
            itemView.tvEventDayWeekPending.text = mFromDate.split("/")[1]

            if (model.artist_confirmation_status.equals("")) {
                itemView.ivEditEventPending.visibility = View.VISIBLE
                itemView.ivDeleteEventPending.visibility = View.VISIBLE
                itemView.tvArtistRequestedPending.visibility = View.GONE
                itemView.tvArtistPending.text = "Not Selected"
                itemView.tvVenueRequestedPending.visibility = View.GONE
                itemView.tvVenuePending.text = "Not Selected"
            } else if (!model.artist_confirmation_status.equals("")) {
                itemView.ivEditEventPending.visibility = View.GONE
                itemView.ivDeleteEventPending.visibility = View.GONE
                if (model.artist_confirmation_status.equals("P", true)) {

                    itemView.tvArtistRequestedPending.visibility = View.VISIBLE
                    itemView.tvArtistPending.text = model.artist_full_name
                    itemView.tvVenueRequestedPending.visibility = View.GONE
                    itemView.tvVenuePending.text = "Not Selected"
                } else {
                    itemView.tvArtistRequestedPending.visibility = View.GONE
                    itemView.tvArtistPending.text = model.artist_full_name
                    if (model.venue_confirmation_status.equals("")) {
                        itemView.tvVenueRequestedPending.visibility = View.GONE
                        itemView.tvVenuePending.text = "Not Selected"
                    } else if (model.venue_confirmation_status.equals("P", true)) {
                        itemView.tvVenueRequestedPending.visibility = View.VISIBLE
                        itemView.tvVenuePending.text = model.venue_full_name
                    } else {
                        itemView.tvVenueRequestedPending.visibility = View.GONE
                        itemView.tvVenuePending.text = model.venue_full_name
                    }

                }

            }
        }
    }

    class UpcomingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(model: EventListModel, ctx: Context) {
            if (model.event_promotion_image.equals("")) {
                Glide.with(ctx)
                        .load(R.drawable.icon_img_dummy).into(itemView.ivEventImageUpcomingPast)
            } else {
                Glide.with(ctx)
                        .load(model.event_promotion_path + model.event_promotion_image).into(itemView.ivEventImageUpcomingPast)
            }


            val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(model.venue_from_date, model.venue_to_date)
            itemView.tvEventDayUpcomingPast.text = mFromDate.split("/")[0]
            itemView.tvEventDayWeekUpcomingPast.text = mFromDate.split("/")[1]
            itemView.tvArtistPending.text = model.artist_full_name
            itemView.tvVenuePending.text = model.venue_full_name


        }
    }

    class PastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(model: EventListModel, ctx: Context) {
        }
    }
}