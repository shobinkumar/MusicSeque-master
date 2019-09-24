package com.musicseque.event_manager.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.event_manager.activity.CreateEventActivity
import com.musicseque.event_manager.activity.EventsListActivity
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.utilities.KotlinUtils
import kotlinx.android.synthetic.main.row_upcoming_event_list.view.*
import java.text.SimpleDateFormat

class UpcomingEventAdapter(var al: ArrayList<EventListModel>, var type: Int, val activitys: Context, val eventsListActivity: EventsListActivity) : RecyclerView.Adapter<UpcomingEventAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UpcomingEventAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_upcoming_event_list, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return al.size

    }

    override fun onBindViewHolder(viewHolder: UpcomingEventAdapter.MyHolder, position: Int) {
        val model: EventListModel = al.get(position)

        viewHolder.bindItems(model, position, activitys, eventsListActivity)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("NewApi")
        fun bindItems(data: EventListModel, pos: Int, activity: Context, eventsListActivity: EventsListActivity) {

            itemView.tvEventName.text = data.event_title
            itemView.tvTime.text = data.event_time_from + " - " + data.event_time_to
            val newFormat = SimpleDateFormat("dd/MM/yyyy")
            val oldFormat = SimpleDateFormat("MM-dd-yyyy")

            val dateOldF = oldFormat.parse(data.event_from_date)

            val dateNewF = newFormat.format(dateOldF)
            val mNewType = KotlinUtils.monthToReadFormat(dateNewF)
            itemView.tvDay.text = mNewType.split("/")[0]
            itemView.tvMonth.text = mNewType.split("/")[1]
            itemView.ivEdit.setOnClickListener {
                val intent = Intent(activitys, CreateEventActivity::class.java).putExtra("event_id", data.event_id)
                activitys.startActivity(intent)
            }
            itemView.ivDelete.setOnClickListener {
                eventsListActivity.deleteEvent(data.event_id)

            }


        }
    }
}