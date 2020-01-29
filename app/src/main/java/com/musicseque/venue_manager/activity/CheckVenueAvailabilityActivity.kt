package com.musicseque.venue_manager.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_VENUE_TIMMINGS
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import com.musicseque.venue_manager.model.MySelectedTimeModel
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_venue_availability.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckVenueAvailabilityActivity : Activity(), View.OnClickListener, MyInterface {


    private var mVenueName: String = ""
    var arrayList_details: ArrayList<String> = ArrayList()
    private var mVenueId: String = ""
    private var width: Int = 0
    var al: ArrayList<MySelectedTimeModel> = ArrayList()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")

    var sSelectedDate: String = ""


    lateinit var currentDate: Date
    lateinit var selectedDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_availability)
        initViews()
        listeners()
        hitAPI(FOR_VENUE_TIMMINGS)
    }


    private fun initViews() {
        img_right_icon.visibility = View.GONE
        tv_title.text = "Availability"
        val dimension = DisplayMetrics()
        getWindowManager()?.getDefaultDisplay()?.getMetrics(dimension)
        width = dimension.widthPixels
        mVenueId = intent.getStringExtra("venue_id")
        mVenueName = intent.getStringExtra("venue_name")


        selectedDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().time))
        sSelectedDate = dateFormat.format(Calendar.getInstance().time)
        currentDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().time))
    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        calendarView.setSelectedDate(Date())
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            Log.e("", date.toString());
            sSelectedDate = dateFormat.format(date.date)
            selectedDate = dateFormat.parse(dateFormat.format(date.date))

            hitAPI(FOR_VENUE_TIMMINGS)
            arrayList_details.clear()
            // KotlinUtils.dateFormatToSend()
        })
        calendarView.setOnMonthChangedListener { widget, date ->
            var dCurrent = Calendar.getInstance().time
            dCurrent = dateFormat.parse(dateFormat.format(Calendar.getInstance().time))
            val mDay=date.day
            val mMonth=date.month+1
            val mYear=date.year

            val mDate=mDay.toString()+"-"+mMonth.toString()+"-"+mYear.toString()
            val dSelected = dateFormat.parse(mDate)
//            var cAfter = Calendar.getInstance()
//
//            cAfter.add(Calendar.DAY_OF_MONTH, date.day)
//            cAfter.add(Calendar.DAY_OF_MONTH, -1)
//            val dateAfter = dateFormat.parse(dateFormat.format(cAfter.time))

            if (dCurrent.equals(dSelected)) {

            }
            else
            {
              calendarView.setSelectedDate(dSelected)
                sSelectedDate = dateFormat.format(dSelected)
                selectedDate = dateFormat.parse(dateFormat.format(dSelected))
                hitAPI(FOR_VENUE_TIMMINGS)
                arrayList_details.clear()
            }


        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> {
                finish()
            }
            R.id.tvBook -> {
                Log.e("", "")

                if (SharedPref.getString(Constants.PROFILE_TYPE, "").equals("Venue Manager")) {
                    Utils.showToast(this, "You can't book venue as a Venue Manager")
                }
               else if (SharedPref.getString(Constants.PROFILE_TYPE, "").equals("Music Lover")) {
                    Utils.showToast(this, "You can't book venue as a Music Lover")
                }


                else if (selectedDate.before(currentDate)) {
                    Utils.showToast(this, "Selected date has passed")

                } else {
                    if (arrayList_details.size == 24) {
                        Utils.showToast(this, "No booking time available")
                    } else {
                        val intent = Intent(this, BookVenueActivity::class.java).putExtra("venue_id", mVenueId).putExtra("selected_date", sSelectedDate).putExtra("venue_name", mVenueName)
                        startActivity(intent)
                    }

                }

            }

        }
    }

    private fun hitAPI(sType: Int) {
        if (KotlinUtils.isNetConnected(this)) {
            Utils.initializeAndShow(this)
            if (sType == FOR_VENUE_TIMMINGS) {
                val json = JSONObject()
                json.put("VenueId", mVenueId)
                json.put("BookingAsOnDate", sSelectedDate)
                RetrofitAPI.callAPI(json.toString(), FOR_VENUE_TIMMINGS, this)
            }
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }
    }


    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        if (TYPE == FOR_VENUE_TIMMINGS) {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                val jsonArray = obj.getJSONArray("result")
                al = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)
                var size: Int = jsonArray.length()

                for (i in 0..size - 1) {
                    var json_objectdetail: JSONObject = jsonArray.getJSONObject(i)
                    var str = json_objectdetail.getString("BookingTime")
                    if (str.length == 4) {
                        str = "0" + str
                    } else {

                    }
                    arrayList_details.add(str)
                }

            }
        }
        setSlotsMethod();


    }

    fun setSlotsMethod() {
        llAvailability.removeAllViews()
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


        if (arrayList_details.contains("01:00")) {
            view2.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("02:00")) {
            view3.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("03:00")) {
            view4.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("04:00")) {
            view5.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("05:00")) {
            view6.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("06:00")) {
            view7.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("07:00")) {
            view8.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("08:00")) {
            view9.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("09:00")) {
            view10.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("10:00")) {
            view11.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("11:00")) {
            view12.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("12:00")) {
            view13.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("13:00")) {
            view14.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("14:00")) {
            view15.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("15:00")) {
            view16.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("16:00")) {
            view17.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("17:00")) {
            view18.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("18:00")) {
            view19.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("19:00")) {
            view20.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("20:00")) {
            view21.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("21:00")) {
            view22.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("22:00")) {
            view23.setBackgroundResource(R.drawable.rect_time_not_available)

        }

        if (arrayList_details.contains("23:00")) {
            view24.setBackgroundResource(R.drawable.rect_time_not_available)

        }
        if (arrayList_details.contains("00:00")) {
            view1.setBackgroundResource(R.drawable.rect_time_not_available)

        }


        llAvailability.addView(v)
    }
}


