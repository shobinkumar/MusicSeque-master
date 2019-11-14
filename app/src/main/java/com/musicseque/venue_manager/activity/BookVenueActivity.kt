package com.musicseque.venue_manager.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.MainActivity
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
import com.musicseque.venue_manager.others.ToDialogTime
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
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BookVenueActivity : BaseActivity(), View.OnClickListener, MyInterface, DatePickerDialog.OnDateSetListener {

    var alTime: ArrayList<String> = ArrayList()

    lateinit var dialog: Dialog
    private var timeAL = ArrayList<String>()
    private lateinit var timeFormat: SimpleDateFormat
    private var dpd: DatePickerDialog? = null
    internal var now = Calendar.getInstance()
    internal var addSixMonthsCalendar: Calendar = Calendar.getInstance()
    private var sDateSixMonths: String? = null
    internal lateinit var dateFormat: DateFormat
    internal lateinit var dateSixMonth: Date
    val FOR_START_TIME = 1
    val FOR_END_TIME = 2

    private var alBookings = ArrayList<MySelectedTimeModel>()
    private var alBookingsEnd = java.util.ArrayList<MySelectedTimeModel>()
    private var eventsList = ArrayList<EventListModel>()

    val hashMap = HashMap<String, ArrayList<String>>()


    var hashMapSorted = HashMap<String, ArrayList<String>>()
    val listStartDate = ArrayList<String>()
    val listEndDate = ArrayList<String>()


    var FOR_START_DATE = false;
    val oldFormat = SimpleDateFormat("dd-MM-yyyy")
    val newFormat = SimpleDateFormat("MM-dd-yyyy")


    val hourFormat = SimpleDateFormat("HH:mm")


    val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")

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
        addTimeMethod()
        hitAPI(FOR_SHOW_EVENTS_LIST, "")
    }

    private fun getSixMonthDate() {
        addSixMonthsCalendar.add(Calendar.MONTH, 2)
        sDateSixMonths = dateFormat.format(addSixMonthsCalendar.getTime())
        dateSixMonth = dateFormat.parse(sDateSixMonths)
    }


    private fun initViews() {

        mVenueId = intent.getStringExtra("venue_id")
        tv_title.text = "Book Venue"
        img_right_icon.visibility = View.GONE

        rl.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener { mWidthCode = rl.getMeasuredWidth() })
        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        timeFormat = SimpleDateFormat("HH:mm")


    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        tvEventName.setOnClickListener(this)
        tvStartDate.setOnClickListener(this)
        tvStartTime.setOnClickListener(this)
        tvEndDate.setOnClickListener(this)
        tvEndTime.setOnClickListener(this)
        tvSendRequest.setOnClickListener(this)
        eventArrow.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> {
                finish()
            }
            R.id.tvSendRequest -> {
                getDateTimeDetails()
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
//                    mStartDate = newFormat.format(oldFormat.parse(mStartDate))
//                    mEndDate = newFormat.format(oldFormat.parse(mEndDate))
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
                    hitAPI(FOR_VENUE_BOOK, json.toString())
                    //KotlinHitAPI.callAPI(json.toString(), FOR_VENUE_BOOK, this)
                }


            }
            R.id.tvEventName -> {
                var list = ArrayList<String>()
                if (eventsList.size > 0) {
                    for ((index, value) in eventsList.withIndex()) {
                        if(!eventsList.get(index).booking_status.equals("B",true))
                        list.add(eventsList.get(index).event_title)
                    }

                    if(list.size>0)
                    {
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
                    else
                    {
                        Utils.showToast(this, "You don't have event. Please create event.")
                    }


                } else {
                    Utils.showToast(this, "You don't have event. Please create event.")
                }


            }
            R.id.eventArrow -> {
                var list = ArrayList<String>()
                if (eventsList.size > 0) {
                    for ((index, value) in eventsList.withIndex()) {
                        if(!eventsList.get(index).booking_status.equals("B",true))
                            list.add(eventsList.get(index).event_title)
                    }

                    if(list.size>0)
                    {
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
                    else
                    {
                        Utils.showToast(this, "You don't have event. Please create event.")
                    }


                } else {
                    Utils.showToast(this, "You don't have event. Please create event.")
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
                        listStartDate.clear()
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


                }


            }
            R.id.tvStartTime -> {
                getDateTimeDetails()
                if (mStartDate.equals(""))
                    Utils.showToast(this, resources.getString(R.string.err_event_start_date))
                else {

                    val currentDateTime = getCurrentDateTime()
                    val dStartDate = oldFormat.parse(mStartDate)
                    val mCurrentDate = oldFormat.format(Calendar.getInstance().time)
                    val dCurrentDate = oldFormat.parse(mCurrentDate)
                    val mCurrentTime = hourFormat.format(Calendar.getInstance().time)
                    val dCurrentTime = hourFormat.parse(mCurrentTime)

                    if (dStartDate.equals(dCurrentDate)) {


                        if (timeAL.size > 0) {
                            val timeDialog = DialogTime(this, FOR_START_TIME, timeAL, object : TimeInterface {
                                override fun getTime(time_str: String) {
                                    var selectedDate = mStartDate + " " + time_str

                                    val selectedDateTime = getSelectedDateTime(selectedDate)
                                    if (selectedDateTime.before(currentDateTime)) {
                                        Utils.showToast(this@BookVenueActivity, "Time must be after current time")
                                    } else {
                                        tvStartTime.setText(time_str)
                                    }

                                }
                            })
                            timeDialog?.show()
                        } else {


                            val al = hashMap.get(tvStartDate.text.toString())


                            for (time in alTime) {
                                val dTimeList = hourFormat.parse(time)

                                if (dTimeList.before(dCurrentTime)) {

                                    if (al!!.contains(time)) {

                                    } else {
                                        al.add(time)
                                    }
                                }
                                else
                                {
                                    break
                                }


                            }


                            val timeDialog = DialogTime(this, FOR_START_TIME,al, object : TimeInterface {
                                override fun getTime(time_str: String) {
                                    var strDate = mStartDate + " " + time_str
                                    val selectedDateTime = getSelectedDateTime(strDate)
                                    if (selectedDateTime.before(currentDateTime)) {
                                        Utils.showToast(this@BookVenueActivity, "Time must be after current time")
                                    } else {
                                        tvStartTime.setText(time_str)
                                    }

                                }
                            })
                            timeDialog?.show()
                        }
                    } else {
                        if (timeAL.size > 0) {
                            val timeDialog = DialogTime(this, FOR_START_TIME, timeAL, object : TimeInterface {
                                override fun getTime(time_str: String) {
                                    var selectedDate = mStartDate + " " + time_str

                                    val selectedDateTime = getSelectedDateTime(selectedDate)
                                    if (selectedDateTime.before(currentDateTime)) {
                                        Utils.showToast(this@BookVenueActivity, "Time must be after current time")
                                    } else {
                                        tvStartTime.setText(time_str)
                                    }

                                }
                            })
                            timeDialog?.show()
                        } else {
                            val timeDialog = DialogTime(this, FOR_START_TIME, hashMap.get(tvStartDate.text.toString()), object : TimeInterface {
                                override fun getTime(time_str: String) {
                                    var strDate = mStartDate + " " + time_str
                                    val selectedDateTime = getSelectedDateTime(strDate)
                                    if (selectedDateTime.before(currentDateTime)) {
                                        Utils.showToast(this@BookVenueActivity, "Time must be after current time")
                                    } else {
                                        tvStartTime.setText(time_str)
                                    }

                                }
                            })
                            timeDialog?.show()
                        }
                    }


                }

            }
            R.id.tvEndDate -> {
                FOR_START_DATE = false
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
                    hashMapSorted.clear()
                    listEndDate.clear()
                    alBookingsEnd.clear()
                    hitAPI(FOR_VENUE_TO_TIMMINGS, "")


                }


            }
            R.id.tvEndTime -> {
                getDateTimeDetails()
                if (mStartDate.equals(""))
                    Utils.showToast(this, resources.getString(R.string.err_event_start_date))
                else if (mStartTime.equals("", ignoreCase = true))
                    Utils.showToast(this, resources.getString(R.string.err_event_start_date))
                else if (mEndDate.equals("", ignoreCase = true))
                    Utils.showToast(this, resources.getString(R.string.err_event_end_date))
                else {

                    val timeDialog = ToDialogTime(this, FOR_END_TIME, endHashMap.get(tvEndDate.text.toString()), object : TimeInterface {
                        override fun getTime(time_str: String) {
                            tvEndTime.setText(time_str)
                        }
                    })
                    timeDialog?.show()
                }


            }
        }
    }

    private fun getSelectedDateTime(selectedDate: String): Date {
        val date = dateTimeFormat.parse(selectedDate)
        return date

    }

    private fun getCurrentDateTime(): Date {
        var currentDate = Calendar.getInstance().time
        currentDate = dateTimeFormat.parse(dateTimeFormat.format(currentDate))
        return currentDate
    }

    private fun getDisabledDays() {
        val hashMapSorted = hashMap.toList().sortedBy { (key, _) -> key }.toMap()
        for ((k, v) in hashMapSorted) {
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

            // dateCheckOut = getBookingsAvailable.getCheckOutDate(sCheckOutDate)

            tvStartTime.setText("")
            tvEndDate.setText("")
            tvEndTime.setText("")


        } else {
            tvEndDate.setText(sSelectedDateDialog)

        }

    }

    private fun hitAPI(type: Int, str: String) {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this)
            if (type == FOR_SHOW_EVENTS_LIST) {
                val obj = JSONObject()
                obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
                obj.put("EventStatus", "2")
                KotlinHitAPI.callAPI(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this)

            } else if (type == FOR_VENUE_FROM_TIMMINGS) {
                val json = JSONObject()
                json.put("VenueId", mVenueId)
                // json.put("BookingAsOnDate", "01-01-1900")
                KotlinHitAPI.callAPI(json.toString(), Constants.FOR_VENUE_FROM_TIMMINGS, this)
            } else if (type == FOR_VENUE_TO_TIMMINGS) {
                val json = JSONObject()
                json.put("VenueId", mVenueId)
                json.put("FromDate", mStartDate)
                json.put("FromTime", mStartTime)

                KotlinHitAPI.callAPI(json.toString(), Constants.FOR_VENUE_TO_TIMMINGS, this)

            } else if (type == FOR_VENUE_BOOK) {
                KotlinHitAPI.callAPI(str, FOR_VENUE_BOOK, this)
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
            hitAPI(FOR_VENUE_FROM_TIMMINGS, "")
        }
//        else if (TYPE == FOR_VENUE_TIMMINGS) {
//            val obj = JSONObject(response.toString())
//            if (obj.getString("Status").equals("Success", false)) {
//                val jsonArray = obj.getJSONArray("result")
//                alBookings = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)
//
//                getTimmingsHashMap(alBookings, hashMap)
//
//
//            }
//        }

        else if (TYPE == FOR_VENUE_TO_TIMMINGS) {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                val jsonArray = obj.getJSONArray("result")
                alBookingsEnd = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)
                getEndTimmingsHashMap(alBookingsEnd, endHashMap)
                Log.e("", "")



                if (alBookingsEnd.size > 0) {
                    //getEnabledDays()
                    val hashMapSorted = endHashMap.toList().sortedBy { (key, _) -> key }.toMap()
                    val alDate = java.util.ArrayList<Calendar>()
                    for ((k, v) in hashMapSorted) {


                        val calendar = Calendar.getInstance()
                        val data = k.split("-")

                        calendar.set(Calendar.YEAR, Integer.parseInt(data[2]))
                        calendar.set(Calendar.MONTH, Integer.parseInt(data[1]) - 1)
                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[0]))
                        alDate.add(calendar)
                    }
                    var days = arrayOfNulls<Calendar>(alDate.size)
                    days = alDate.toTypedArray<Calendar?>()

                    dpd!!.selectableDays = days


                }
                dpd!!.show(fragmentManager, "Datepickerdialog")

            } else {
                Utils.showToast(this, "Please change your Start Date/Time")
            }

        } else if (TYPE == FOR_VENUE_FROM_TIMMINGS) {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                val jsonArray = obj.getJSONArray("result")
                alBookings = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)

                getTimmingsHashMap(alBookings, hashMap)
                Log.e("", "")


            }

        } else if (TYPE == FOR_VENUE_BOOK) {
            val obj = JSONObject(response.toString())
            if (obj.getString("Status").equals("Success", false)) {
                dialogRequest()
//                Utils.showToast(this, obj.getString("Message"))
//                startActivity(Intent(this, MainActivity::class.java))
            } else {

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

            spinnerData.getData(array[position], eventsList.get(position).event_id)
            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }


    private fun getTimmingsHashMap(arraylist: java.util.ArrayList<MySelectedTimeModel>, hashmap: HashMap<String, java.util.ArrayList<String>>) {
        for ((index, value) in arraylist.withIndex()) {
            if (hashmap.containsKey(value.availability_date)) {

                var al = ArrayList<String>()

                al = hashmap.get(value.availability_date)!!
                al.add(value.availability_from_time)
                hashmap.put(value.availability_date, al)


            } else {
                val al = ArrayList<String>()
                al.add(value.availability_from_time)
                hashmap.put(value.availability_date, al)
            }

        }


    }

    private fun getEndTimmingsHashMap(arraylist: java.util.ArrayList<MySelectedTimeModel>, hashmap: HashMap<String, java.util.ArrayList<String>>) {
        for ((index, value) in arraylist.withIndex()) {
            if (hashmap.containsKey(value.availability_date)) {

                var al = ArrayList<String>()
                al = hashmap.get(value.availability_date)!!
                al.add(value.availability_to_time)
                hashmap.put(value.availability_date, al)


            } else {
                val al = ArrayList<String>()
                if (index != 0 && value.availability_from_time.equals("00:00")) {
                    al.add(value.availability_from_time)
                }

                al.add(value.availability_to_time)


                hashmap.put(value.availability_date, al)
            }

        }


    }

    fun dialogRequest() {
        dialog = Dialog(this@BookVenueActivity, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setContentView(R.layout.dialog_book_request)
        dialog.setCancelable(false)
        val btnClose = dialog.findViewById(R.id.btnClose) as Button
        btnClose.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
        }



        dialog.show()
    }

    private fun addTimeMethod() {
        alTime.add("00:00")
        alTime.add("01:00")
        alTime.add("02:00")
        alTime.add("03:00")
        alTime.add("04:00")
        alTime.add("05:00")
        alTime.add("06:00")
        alTime.add("07:00")
        alTime.add("08:00")
        alTime.add("09:00")
        alTime.add("10:00")
        alTime.add("11:00")
        alTime.add("12:00")
        alTime.add("13:00")
        alTime.add("14:00")
        alTime.add("15:00")
        alTime.add("16:00")
        alTime.add("17:00")
        alTime.add("18:00")
        alTime.add("19:00")
        alTime.add("20:00")
        alTime.add("21:00")
        alTime.add("22:00")
        alTime.add("23:00")


    }
}

