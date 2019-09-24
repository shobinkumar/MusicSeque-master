package com.musicseque.venue_manager.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicseque.R
import kotlinx.android.synthetic.main.row_timmings.view.*

class VenueTimmingsAdapter(val widths: Int, val activitys: Activity?,val hashMap: MutableMap<String, ArrayList<String>>) : RecyclerView.Adapter<VenueTimmingsAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): VenueTimmingsAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_timmings, parent, false)
        return VenueTimmingsAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {

        return 24;
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: VenueTimmingsAdapter.MyHolder, position: Int) {
        if (activitys != null) {
            holder.bind(widths, position, activitys,hashMap)
        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @SuppressLint("ResourceAsColor")
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(widths: Int, pos: Int, activitys: Activity, hashMap:MutableMap<String, ArrayList<String>>) {

            val ones = itemView.findViewById<View>(R.id.one)
            val twos = itemView.findViewById<View>(R.id.two)
            val threes = itemView.findViewById<View>(R.id.three)
            val fours = itemView.findViewById<View>(R.id.four)
            val fives = itemView.findViewById<View>(R.id.five)
            val sixs = itemView.findViewById<View>(R.id.six)
            val sevens = itemView.findViewById<View>(R.id.seven)
            ones.id = pos

            itemView.tvTime.text = pos.toString()

            ones.getLayoutParams().height = widths / 7;
            ones.getLayoutParams().width = widths / 7;

            twos.getLayoutParams().height = widths / 7;
            twos.getLayoutParams().width = widths / 7;
            threes.getLayoutParams().height = widths / 7;
            threes.getLayoutParams().width = widths / 7;
            fours.getLayoutParams().height = widths / 7;
            fours.getLayoutParams().width = widths / 7;
            fives.getLayoutParams().height = widths / 7;
            fives.getLayoutParams().width = widths / 7;
            sixs.getLayoutParams().height = widths / 7;
            sixs.getLayoutParams().width = widths / 7;
            sevens.getLayoutParams().height = widths / 7;
            sevens.getLayoutParams().width = widths / 7;




            ones.setOnClickListener {
                pos.toString()
                ones.setBackgroundDrawable(activitys.getDrawable(R.drawable.sq_box_orange))


            }
            twos.setOnClickListener {
                pos.toString()
                ones.setBackgroundDrawable(activitys.getDrawable(R.drawable.sq_box_orange))


            }
            threes.setOnClickListener {
                pos.toString()
                ones.setBackgroundDrawable(activitys.getDrawable(R.drawable.sq_box_orange))


            }
            fours.setOnClickListener {
                pos.toString()
                ones.setBackgroundDrawable(activitys.getDrawable(R.drawable.sq_box_orange))


            }
            fives.setOnClickListener {
                pos.toString()
                ones.setBackgroundDrawable(activitys.getDrawable(R.drawable.sq_box_orange))


            }
            sixs.setOnClickListener {
                pos.toString()
                ones.setBackgroundDrawable(activitys.getDrawable(R.drawable.sq_box_orange))


            }
            sevens.setOnClickListener {
                pos.toString()
                ones.setBackgroundDrawable(activitys.getDrawable(R.drawable.sq_box_orange))


            }

        }
    }





}