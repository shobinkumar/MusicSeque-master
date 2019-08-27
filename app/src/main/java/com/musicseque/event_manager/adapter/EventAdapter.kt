package com.musicseque.event_manager.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.event_manager.adapter.EventAdapter.MyHolder
import com.musicseque.event_manager.model.EventModel
import kotlinx.android.synthetic.main.row_event_item.view.*

class EventAdapter(val act: Context, var al: ArrayList<EventModel>) : RecyclerView.Adapter<MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_event_item, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return al.size
    }

    override fun onBindViewHolder(viewHolder: MyHolder, position: Int) {
        val model: EventModel = al.get(position)

        viewHolder.bindItems(model,position)
    }

   inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun bindItems(data: EventModel, pos:Int) {


            if(data.isSelected)
            {
                itemView.setBackgroundResource(R.color.color_orange);
            }
            else
            {
                itemView.setBackgroundResource(R.color.mtrl_btn_transparent_bg_color);
            }
            itemView.tvEventName.text = data.name

            itemView.setOnClickListener {
                if(data.isSelected)
                {
                    itemView.setBackgroundResource(R.color.mtrl_btn_transparent_bg_color);
                    al.get(pos).isSelected=false
                    notifyDataSetChanged()

                }
                else
                {
                    itemView.setBackgroundResource(R.color.color_orange);
                    al.get(pos).isSelected=true
                    notifyDataSetChanged()
                }


            }


        }
    }
}