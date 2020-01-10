package com.musicseque.venue_manager.others

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.musicseque.R
import com.musicseque.venue_manager.adapter.TimeRecyclerAdapter
import com.musicseque.venue_manager.adapter.ToTimeRecyclerAdapter
import kotlinx.android.synthetic.main.time_dialog.*
import java.text.ParseException
import java.util.*
import kotlin.collections.ArrayList


class ToDialogTime(var c: Activity, private val id: Int, internal var timeAL: ArrayList<String>?, internal var listener: TimeInterface) : Dialog(c) {
    var d: Dialog? = null
    private var al=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.time_dialog)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.setGravity(Gravity.BOTTOM)

        recyclerTime.layoutManager = LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false)



            val adapter = ToTimeRecyclerAdapter(c, timeAL, c.resources.getStringArray(R.array.my_to_time_array), TimeInterface { time_str ->
                listener.getTime(time_str)
                this.dismiss()
            })
            recyclerTime.adapter = adapter



    }



}