package com.musicseque.event_manager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.event_manager.activity.PastEventDetailActivity
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.utilities.KotlinUtils
import kotlinx.android.synthetic.main.row_event_list.view.*
import kotlinx.android.synthetic.main.row_event_list.view.tvDay
import kotlinx.android.synthetic.main.row_event_list.view.tvEventName
import kotlinx.android.synthetic.main.row_event_list.view.tvMonth
import kotlinx.android.synthetic.main.row_event_list.view.tvTime
import kotlinx.android.synthetic.main.row_upcoming_event_list.view.*
import java.text.SimpleDateFormat

class EventListAdapter(var al: ArrayList<EventListModel>, var type: Int,val activitys: Context) : RecyclerView.Adapter<EventListAdapter.MyHolder>() {

    val newFormat = SimpleDateFormat("dd/MM/yyyy")
    val oldFormat = SimpleDateFormat("MM-dd-yyyy")
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventListAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_event_list, parent, false)
        return MyHolder(v)    }

    override fun getItemCount(): Int {
       return al.size

    }

    override fun onBindViewHolder(viewHolder: EventListAdapter.MyHolder, position: Int) {
        val model: EventListModel = al.get(position)

        viewHolder.bindItems(model,position,activitys)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("NewApi")
        fun bindItems(data: EventListModel, pos: Int, activitys: Context) {
            itemView.tvEventName.text = data.event_title
            itemView.tvTime.text = data.event_time_from + " - " + data.event_time_to


            if(data.venue_name.equals(""))
            {
                itemView.tvTime.text = data.event_time_from + " - " + data.event_time_to


                val dateOldF = oldFormat.parse(data.event_from_date)

                val dateNewF = newFormat.format(dateOldF)
                val mNewType = KotlinUtils.monthToReadFormat(dateNewF)
                itemView.tvDay.text = mNewType.split("/")[0]
                itemView.tvMonth.text = mNewType.split("/")[1]
            }
            else
            {
                itemView.tvTime.text = data.venue_from_time + " - " + data.venue_to_time


                val dateOldF = oldFormat.parse(data.venue_from_date)

                val dateNewF = newFormat.format(dateOldF)
                val mNewType = KotlinUtils.monthToReadFormat(dateNewF)
                itemView.tvDay.text = mNewType.split("/")[0]
                itemView.tvMonth.text = mNewType.split("/")[1]
            }






            itemView.setOnClickListener { val intent= Intent(activitys,PastEventDetailActivity::class.java).putExtra("event_id",data.event_id)
                activitys.startActivity(intent)
            }






        }
    }
}