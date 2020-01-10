package com.musicseque.venue_manager.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import com.musicseque.venue_manager.others.TimeInterface
import kotlinx.android.synthetic.main.item_time.view.*
import kotlin.collections.ArrayList

class TimeRecyclerAdapter(internal var activity: Activity, internal var alTime: ArrayList<String>?, internal var strArray: Array<String>, internal var timeInterface: TimeInterface) : RecyclerView.Adapter<TimeRecyclerAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time, null))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binder(strArray!!.get(position),timeInterface)
    }

    override fun getItemCount(): Int {
        return strArray.size
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binder(value: String, timeInterface: TimeInterface) {
            if (alTime!!.contains(value)) {
                itemView.tvTime.setClickable(false)
                itemView.tvTime.setText(strArray[position])
                itemView.tvTime.setBackgroundColor(activity.resources.getColor(R.color.round_grey_color))



            } else {
                itemView.tvTime.setClickable(true)
                itemView.tvTime.setText(strArray[position])
                itemView.tvTime.setBackgroundColor(activity.resources.getColor(R.color.color_white))
                itemView.tvTime.setClickable(true)
            }
            itemView.tvTime.setOnClickListener(View.OnClickListener {
                if (!alTime!!.contains(strArray[position]))
                    timeInterface.getTime(strArray[position])
            })
        }
    }
}
