package com.musicseque.event_manager.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.event_manager.model.EventStatusModel

class EventStatusCommonAdapter(val ctx: Context, val al: ArrayList<EventStatusModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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
        when(getItemViewType(position))
        {
            FOR_PENDING->
            {
                val model=al.get(position)
                val pendingHolder=holder as PendingViewHolder
                pendingHolder.bindView(model)
            }
            FOR_UPCOMING->
            {
                val model=al.get(position)
                val upcomingHolder=holder as UpcomingViewHolder
                upcomingHolder.bindView(model)
            }
            FOR_PAST->
            {
                val model=al.get(position)
                val pastHolder=holder as PastViewHolder
                pastHolder.bindView(model)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
        return when (al.get(position).type) {
            FOR_PENDING ->
                return FOR_PENDING
            FOR_UPCOMING ->
                return FOR_UPCOMING
            FOR_PAST ->
                return FOR_PAST
        }
    }

    class PendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(model: EventStatusModel) {
        }
    }

    class UpcomingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(model: EventStatusModel) {
        }
    }

    class PastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(model: EventStatusModel) {
        }
    }
}