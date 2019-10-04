package com.musicseque.venue_manager.activity

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.*
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import com.musicseque.venue_manager.model.MySelectedTimeModel
import com.musicseque.venue_manager.others.DialogTime
import com.musicseque.venue_manager.others.TimeInterface
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_book_venue.*
import kotlinx.android.synthetic.main.activity_book_venue.tvEndDate
import kotlinx.android.synthetic.main.activity_book_venue.tvEndTime
import kotlinx.android.synthetic.main.activity_book_venue.tvStartDate
import kotlinx.android.synthetic.main.activity_book_venue.tvStartTime
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BookVenueActivity : BaseActivity(), View.OnClickListener, MyInterface, DatePickerDialog.OnDateSetListener {


    private var timeAL=ArrayList<String>()
    private lateinit var timeFormat:SimpleDateFormat
    private var dpd: DatePickerDialog? = null
    internal var now = Calendar.getInstance()
    internal var addSixMonthsCalendar: Calendar = Calendar.getInstance()
    private var sDateSixMonths: String? = null
    internal lateinit var dateFormat: DateFormat
    internal lateinit var dateSixMonth: Date
    val FOR_START_TIME = 1
    lateinit private var alBookings: java.util.ArrayList<MySelectedTimeModel>
    lateinit private var alBookingsEnd: java.util.ArrayList<MySelectedTimeModel>

    val hashMap = HashMap<String, ArrayList<String>>()
    var hashMapSorted = HashMap<String, ArrayList<String>>()
    val listStartDate = ArrayList<String>()

    var FOR_START_DATE = false;


    private lateinit var eventsList: ArrayList<EventListModel>
    var mStartDate = ""
    var mStartTime = ""
    var mEndDate = ""
    var mEndTime = ""
    var mEventName = ""
    var mEventId = ""
    var mVenueId = ""


    lateinit var listPopupWindow: ListPopupWindow
    var mWidthCode: Int = 0


    internal var endHashMap = HashMap<String, java.util.ArrayList<String>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_venue)
        initViews()
        listeners()
        getSixMonthDate()
        hitAPI("get_events_list")
    }

    private fun getSixMonthDate() {
        addSixMonthsCalendar.add(Calendar.MONTH, 6)
        sDateSixMonths = dateFormat.format(addSixMonthsCalendar.getTime())
        dateSixMonth = dateFormat.parse(sDateSixMonths)
    }


    private fun initViews() {

        mVenueId = intent.getStringExtra("venue_id")
        tv_title.text = "Book Venue"
        img_right_icon.visibility = View.GONE

        rl.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener { mWidthCode = rl.getMeasuredWidth() })
        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        timeFormat=SimpleDateFormat("HH:mm")


    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        tvEventName.setOnClickListener(this)
        tvStartDate.setOnClickListener(this)
        tvStartTime.setOnClickListener(this)
        tvEndDate.setOnClickListener(this)
        tvEndTime.setOnClickListener(this)
        tvSendRequest.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> {
                finish()
            }
            R.id.tvSendRequest -> {

                if (mEventName.equals("")) {
                    Utils.showToast(this, resources.getString(R.string.err_event_name))
                } else if (mStartDate.equals("")) {
                    Utils.showToast(this, resources.getString(R.string.err_event_start_date))
                } else if (mStartTime.equals("")) {
                    Utils.showToast(this, resources.getString(R.string.err_event_start_time))
                } else if (mEndDate.equals("")) {
                    Utils.showToast(this, resources.getString(R.string.err_event_end_date))
                } else if (mEndTime.equals("")) {
                    Utils.showToast(this, resources.getString(R.string.err_event_end_time))

                } else {
                    val json = JSONObject()
                    json.put("VenueId", "2596")
                    json.put("EventId", "9")
                    json.put("ArtistId", SharedPref.getString(Constants.USER_ID, ""))
                    json.put("VenueBookingFrom", "10-10-2019")
                    json.put("VenueBookingTo", "10-12-2019")
                    json.put("VenueBookingFromTime", "10:00:00")
                    json.put("VenueBookingToTime", "12:00:00")
                    json.put("BookingType", "0")
                    json.put("BookingStatus", "P")
                    KotlinHitAPI.callAPI(json.toString(), FOR_VENUE_BOOK, this)
                }

                val json = JSONObject()
                json.put("VenueId", mVenueId)
                json.put("EventId", mEventId)
                json.put("ArtistId", SharedPref.getString(Constants.USER_ID, ""))
                json.put("VenueBookingFrom", mStartDate)
                json.put("VenueBookingTo", mEndDate)
                json.put("VenueBookingFromTime", mStartTime)
                json.put("VenueBookingToTime", mEndTime)
                json.put("BookingType", "0")
                json.put("BookingStatus", "P")
                KotlinHitAPI.callAPI(json.toString(), FOR_VENUE_BOOK, this)


            }
            R.id.tvEventName -> {
                var list = ArrayList<String>()

                for ((index, value) in eventsList.withIndex()) {
                    list.add(eventsList.get(index).event_title)
                }
                val eventArray = arrayOfNulls<String>(list.size)
                list.toArray(eventArray)

                if (eventArray != null) {
                    showDropdown(eventArray, tvEventName, SpinnerData { mData, mData1 ->
                        mEventName = mData
                        mEventId = mData1
                        tvEventName.text = mEventName
                    }, mWidthCode)
                }

            }
            R.id.tvStartDate -> {
                if (mEventName.equals("")) {
                    Utils.showToast(this, "Please select Event name")
                } else {
                    FOR_START_DATE = true
                    dpd = null
                    if (dpd == null) {
                        dpd = DatePickerDialog.newInstance(
                                this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        )
                    }
                    dpd?.minDate = now
                    dpd?.maxDate = addSixMonthsCalendar

                    if (alBookings.size > 0) {
                        getDisabledDays()

                        val alDate = java.util.ArrayList<Calendar>()
                        for (i in listStartDate) {
                            val calendar = Calendar.getInstance()
                            val data = i.split("-")

                            calendar.set(Calendar.YEAR, Integer.parseInt(data[2]))
                            calendar.set(Calendar.MONTH, Integer.parseInt(data[1]) - 1)
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[0]))
                            alDate.add(calendar)
                        }
                        var days = arrayOfNulls<Calendar>(alDate.size)
                        days = alDate.toTypedArray<Calendar?>()

                        dpd!!.disabledDays = days

                    }


                    dpd?.show(fragmentManager, "Datepickerdialog")

                    val al = java.util.ArrayList<Calendar>()
//                    for (i in checkDatesAL.indices) {
//                        val calendar = Calendar.getInstance()
//                        calendar.set(Calendar.YEAR, Integer.parseInt(getYear(checkDatesAL.get(i))))
//                        calendar.set(Calendar.MONTH, Integer.parseInt(getMonth(checkDatesAL.get(i))) - 1)
//                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getDay(checkDatesAL.get(i))))
//                        al.add(calendar)
//                    }
//                    var days = arrayOfNulls<Calendar>(al.size)
//                    days = al.toTypedArray<Calendar>()
//
//                    dpd?.selectableDays = days


                }


            }
            R.id.tvStartTime -> {
                getDateTimeDetails()
                if (mStartDate.equals(""))
                    Utils.showToast(this, resources.getString(R.string.err_event_start_date))
                else {



//                    val myArray = resources.getStringArray(R.array.my_time_array)
//                    if (hashMap.get(tvStartDate.text.toString()) == null) {
//                        for (hours in myArray) {
//                            timeAL!!.add(hours)
//                        }
//                    }



                    if(timeAL.size>0)
                    {
                        val timeDialog = DialogTime(this, FOR_START_TIME, timeAL, object : TimeInterface {
                            override fun getTime(time_str: String) {
                                tvStartTime.setText(time_str)
                            }
                        })
                        timeDialog?.show()
                    }
                    else
                    {
                        val timeDialog = DialogTime(this, FOR_START_TIME, hashMap.get(tvStartDate.text.toString()), object : TimeInterface {
                            override fun getTime(time_str: String) {
                                tvStartTime.setText(time_str)
                            }
                        })
                        timeDialog?.show()
                    }



//                    val timeDialog = DialogTime(this, FOR_START_TIME, availableTimesForBookingsMap.get(sCheckInDate), object : TimeInterface() {
//                        fun getTime(time_str: String) {
//                            make_offer_check_in_time_tv.setText(time_str)
//                        }
//                    })
//                    timeDialog.show()
                }

            }
            R.id.tvEndDate -> {
                getDateTimeDetails()
                dpd = null
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    )
                }

                if (mStartDate.equals("", ignoreCase = true))
                    Utils.showToast(this, resources.getString(R.string.err_event_start_date))
                else if (mStartTime.equals("", ignoreCase = true))
                    Utils.showToast(this, resources.getString(R.string.err_event_start_time))
                else {

                    hitAPI("to_time")


//                    val calMin = Calendar.getInstance()
//
//                    val dateMin = dateFormat.parse(mStartDate)
//                    calMin.time = dateMin
//
//                    dpd!!.minDate = calMin
//                    dpd!!.maxDate = addSixMonthsCalendar
//                    getCheckOutDates(mStartDate, mStartTime)
//
//
//                    dpd!!.show(fragmentManager, "Datepickerdialog")





                }


            }
            R.id.tvEndTime -> {

            }
        }
    }

    private fun getDisabledDays() {
        for ((k, v) in hashMap) {
            if (v.size == 24) {
                listStartDate.add(k)
            }

        }
    }

    private fun getDateTimeDetails() {
        mStartDate = tvStartDate.text.toString()
        mStartTime = tvStartTime.text.toString()
        mEndDate = tvEndDate.text.toString()
        mEndTime = tvEndTime.text.toString()

    }


    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var dateCheckOut: Date? = null
        var sMonth = ""
        var sDay = ""
        val month = monthOfYear + 1
        if (month < 10)
            sMonth = "0$month"
        else
            sMonth = month.toString() + ""
        if (dayOfMonth < 10)
            sDay = "0$dayOfMonth"
        else
            sDay = dayOfMonth.toString() + ""


        val sSelectedDateDialog = "$sDay-$sMonth-$year"
        val dateSelected = convertToDate(sSelectedDateDialog)


        if (FOR_START_DATE) {
            tvStartDate.setText(sSelectedDateDialog)
            if (!mEndDate.equals("", ignoreCase = true)) {
                // dateCheckOut = getBookingsAvailable.getCheckOutDate(sCheckOutDate)
                if (dateSelected!!.after(dateCheckOut)) {
                    tvStartTime.setText("")
                    tvEndDate.setText("")
                    tvEndTime.setText("")
                }

            }
        } else {
            tvEndDate.setText(sSelectedDateDialog)

        }

    }

    private fun hitAPI(type: String) {
        if (Utils.isNetworkConnected(this)) {
            if (type.equals("get_events_list")) {
                val obj = JSONObject()
                obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
                obj.put("EventStatus", "2")
                KotlinHitAPI.callAPI(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this)

            } else if (type.equals("get_timmings")) {
                val json = JSONObject()
                json.put("VenueId", mVenueId)
                json.put("BookingAsOnDate", "01-01-1900")
                KotlinHitAPI.callAPI(json.toString(), Constants.FOR_VENUE_TIMMINGS, this)
            }
            else if(type.equals("to_time"))
            {
                val json = JSONObject()
                json.put("VenueId", mVenueId)
                json.put("FromDate", mStartDate)
                json.put("FromTime", mStartTime)

                KotlinHitAPI.callAPI(json.toString(), Constants.FOR_VENUE_TO_TIMMINGS, this)

            }
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()

        if (TYPE == Constants.FOR_SHOW_EVENTS_LIST) {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                val arr = obj.getJSONArray("result")
                val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
                eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
            }
            hitAPI("get_timmings")
        } else if (TYPE == FOR_VENUE_TIMMINGS) {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                val jsonArray = obj.getJSONArray("result")
                alBookings = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)

                getTimmingsHashMap(alBookings,hashMap)


            }
        }
        else if (TYPE == FOR_VENUE_TO_TIMMINGS) {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                val jsonArray = obj.getJSONArray("result")
                alBookings = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)
                getTimmingsHashMap(alBookingsEnd,endHashMap)



            }
        }
    }


    fun convertToDate(sSelectedDate: String): Date? {
        var date: Date? = null
        try {
            var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
            date = simpleDateFormat.parse(sSelectedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date

    }

    fun showDropdown(array: Array<String?>, textView: TextView, spinnerData: SpinnerData, width: Int) {
        listPopupWindow = ListPopupWindow(this)
        listPopupWindow.setAdapter(ArrayAdapter(
                this,
                R.layout.row_profile_spinner, array))
        listPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_black))
        listPopupWindow.setAnchorView(textView)
        listPopupWindow.setWidth(width)
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            spinnerData.getData(array[position], position.toString())
            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }


    private fun getTimmingsHashMap(arraylist: java.util.ArrayList<MySelectedTimeModel>, hashmap: HashMap<String, java.util.ArrayList<String>>) {
        for ((index, value) in arraylist.withIndex()) {
            if (hashmap.containsKey(value.venue_booking_date)) {

                var al = ArrayList<String>()
                al = hashmap.get(value.venue_booking_date)!!
                al.add(value.booking_time)
                hashmap.put(value.venue_booking_date, al)


            } else {
                val al = ArrayList<String>()
                al.add(value.booking_time)
                hashmap.put(value.venue_booking_date, al)
            }

        }


    }

//    fun getHourWithoutSeconds(sTime: String): String {
//        var sTime = sTime
//        if (sTime.length == 5) {
//
//        } else {
//            sTime = "0" + sTime
//        }
//        sTime = sTime.substring(0, 2)
//        return sTime.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//
//    }
//
//    fun getMinuteWithoutSeconds(sTime: String): String {
//        var sTime = sTime
//        if (sTime.length == 5) {
//
//        } else {
//            sTime = "0" + sTime
//        }
//        sTime = sTime.substring(2)
//        return sTime.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
//
//    }

  //  @Throws(ParseException::class)
//    fun getCheckOutDates(sCheckInDate: String, sCheckInTime: String) {
//
//        val hashMapSorted = hashMap.toList().sortedBy { (key, _) -> key }.toMap()
//
//        hashmapCheckOut.clear()
//        datesAvailableCheckOut.clear()
//
//
//        val calNextHour = Calendar.getInstance()
//        val calStartTime = Calendar.getInstance()
//        val startTimeHour = Integer.parseInt(getHourWithoutSeconds(sCheckInTime))
//        val startTimeMinute = Integer.parseInt(getHourWithoutSeconds(sCheckInTime))
//        calStartTime.set(Calendar.HOUR_OF_DAY, startTimeHour)
//        calStartTime.set(Calendar.MINUTE, startTimeMinute)
//
//        val dCheckDate = dateFormat.parse(sCheckInDate)
//        for (entry in hashMapSorted.entries) {
//
//            val key = entry.key
//            val dHashMapDate = dateFormat.parse(key)
//
//
//            if (dHashMapDate.before(dCheckDate)) {
//
//            }
//
//            else if (dHashMapDate.toString() == dCheckDate.toString()) {
//                if (mStartTime.equals("23:00")) {
//                    break
//                } else {
//
//                    val alDateBooked = ArrayList<Date>()
//                    val al = hashMap.get(key)
//
//                    //Getting date and its time in Hashmap at particular position
//                    for (time in al!!.iterator()) {
//                        val cal = Calendar.getInstance()
//                        cal.set(HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]))
//                        cal.set(MINUTE, Integer.parseInt(time.split(":")[1]))
//                        alDateBooked.add(cal.time)
//                    }
//
//
//                    //Next hour calendar  and it will increase after every loop
//                    calNextHour.set(HOUR_OF_DAY, startTimeHour)
//                    calNextHour.set(MINUTE, startTimeMinute)
//                    calNextHour.add(HOUR_OF_DAY, 1)
//
//                    //GEt all the arry from STring file
//                    val myArray = resources.getStringArray(R.array.my_time_array)
//
//                    //Loop through the array to check all the data in the Hashmap
//                    for (hours in myArray) {
//
//                        //Convert to Cal data at particular position
//                        val calFixedTime = Calendar.getInstance()
//                        calFixedTime.set(HOUR_OF_DAY, Integer.parseInt(hours.split(":")[0]))
//                        calFixedTime.set(MINUTE, Integer.parseInt(hours.split(":")[1]))
//
//
//                        //If aarray value is before starttime or equal then skip
//                        if (calFixedTime.before(calStartTime) || calFixedTime.equals(calStartTime)) {
//
//                        } else {
//
//                            //dates for endtime contains key then add data
//                            if (datesAvailableCheckOut.contains(key)) {
//                                if (alDateBooked.contains(calNextHour.time)) {
//
//
//                                    val al=hashMapSorted.get(key)
//                                    al!!.add(timeFormat.format(alDateBooked))
//                                    datesAvailableCheckOut.put(key,al)
//                                }
//                                calNextHour.add(HOUR_OF_DAY, 1)
//                            } else {
//
//                                //if datebooked next hour it means break else add to list
//                                if (alDateBooked.contains(calNextHour.time)) {
//
//                                    val al=ArrayList<String>()
//                                    al.add(timeFormat.format(alDateBooked))
//                                    datesAvailableCheckOut.put(key,al)
//                                    //Increase hour by one
//                                    calNextHour.add(HOUR_OF_DAY, 1)
//                                }
//                                else
//                                {
//                                    break
//                                }
//
//                            }
//
//
//                        }
//
//                    }
//
//
//
//                }
//            }
//
//        }
//    }




//    fun getYear(sDate: String): String {
//        return sDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//
//    }
//
//    fun getMonth(sDate: String): String {
//        return sDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
//
//    }
//
//    fun getDay(sDate: String): String {
//        return sDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
//
//    }

}

