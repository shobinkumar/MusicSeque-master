package com.musicseque.event_manager.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.event_manager.model.EventListModel
import kotlinx.android.synthetic.main.row_event_list.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventListAdapter(var al: ArrayList<EventListModel>) : RecyclerView.Adapter<EventListAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): EventListAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_event_list, parent, false)
        return MyHolder(v)    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(viewHolder: EventListAdapter.MyHolder, position: Int) {
        val model: EventListModel = al.get(position)

        viewHolder.bindItems(model,position)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data: EventListModel, pos:Int) {
            if(data.event_promotion_image.equals("",false))
            {
                Glide.with(itemView.context).load(R.drawable.d_demo).into(itemView.ivEvent)

            }
            else{
                Glide.with(itemView.context).load(data.event_promotion_path + data.event_promotion_image).into(itemView.ivEvent)

            }

            itemView.tvEventName.text=data.event_title
            itemView.eventType.text=data.event_type_name
            var mDate=LocalDate.parse(data.event_from_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))









        }
    }
}