package com.musicseque.venue_manager.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_venue_availability.*
import kotlinx.android.synthetic.main.toolbar_top.*
import java.util.*

class CheckVenueAvailabilityActivity : Activity(), View.OnClickListener {


    private var width: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_availability)
        initViews()
        listeners()
        // hitAPI("get_timmings")
        setSlotsMethod()
    }


    private fun initViews() {
        img_right_icon.visibility = View.GONE
        tv_title.text = "AVAILABILITY"
        val dimension = DisplayMetrics()
        getWindowManager()?.getDefaultDisplay()?.getMetrics(dimension)
        width = dimension.widthPixels
        val height = dimension.heightPixels

    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        calendarView.setSelectedDate(Date())
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->


        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> {
                finish()
            }
            R.id.tvBook->
            {
                Log.e("","")
                val intent= Intent(this,BookVenueActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun hitAPI(sType: String) {
        if (KotlinUtils.isNetConnected(this)) {
            if (sType.equals("get_timmings")) {

            }
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }
    }

    fun setSlotsMethod() {
        for (i in 0..23) {
            val v = layoutInflater.inflate(R.layout.row_time_availability, null)
            val view1 = v.findViewById(R.id.view1) as View
            val view2 = v.findViewById(R.id.view2) as View
            val view3 = v.findViewById(R.id.view3) as View
            val view4 = v.findViewById(R.id.view4) as View
            val view5 = v.findViewById(R.id.view5) as View
            val view6 = v.findViewById(R.id.view6) as View
            val view7 = v.findViewById(R.id.view7) as View
            val view8 = v.findViewById(R.id.view8) as View
            val view9 = v.findViewById(R.id.view9) as View
            val view10 = v.findViewById(R.id.view10) as View
            val view11 = v.findViewById(R.id.view11) as View
            val view12 = v.findViewById(R.id.view12) as View
            val view13 = v.findViewById(R.id.view13) as View
            val view14 = v.findViewById(R.id.view14) as View
            val view15 = v.findViewById(R.id.view15) as View
            val view16 = v.findViewById(R.id.view16) as View
            val view17 = v.findViewById(R.id.view17) as View
            val view18 = v.findViewById(R.id.view18) as View
            val view19 = v.findViewById(R.id.view19) as View
            val view20 = v.findViewById(R.id.view20) as View
            val view21 = v.findViewById(R.id.view21) as View
            val view22 = v.findViewById(R.id.view22) as View
            val view23 = v.findViewById(R.id.view23) as View
            val view24 = v.findViewById(R.id.view24) as View
            if (i == 0) {

            } else if (i == 1) {

            } else if (i == 2) {

            } else if (i == 3) {

            } else if (i == 4) {

            } else if (i == 5) {

            } else if (i == 6) {

            } else if (i == 7) {

            } else if (i == 8) {

            } else if (i == 9) {

            } else if (i == 10) {

            } else if (i == 11) {

            } else if (i == 12) {

            } else if (i == 13) {

            } else if (i == 14) {

            } else if (i == 15) {

            } else if (i == 16) {

            } else if (i == 17) {

            } else if (i == 18) {

            } else if (i == 19) {

            } else if (i == 20) {

            } else if (i == 21) {

            } else if (i == 22) {

            } else if (i == 23) {

            }
            llAvailability.addView(v)

        }

    }
}


