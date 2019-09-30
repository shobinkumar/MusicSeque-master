package com.musicseque.venue_manager.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.event_manager.adapter.EventListAdapter
import com.musicseque.event_manager.adapter.UpcomingEventAdapter
import com.musicseque.event_manager.model.EventListModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_VENUE_BOOK
import com.musicseque.utilities.Constants.VENUE_BOOK_API
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
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

class BookVenueActivity : BaseActivity(), View.OnClickListener, MyInterface, DatePickerDialog.OnDateSetListener {


    private var dpd: DatePickerDialog? = null
    internal var now = Calendar.getInstance()
    internal var addSixMonthsCalendar: Calendar = Calendar.getInstance()
    private var sDateSixMonths: String? = null
    internal lateinit var dateFormat: DateFormat
    internal lateinit var dateSixMonth: Date
    val FOR_START_TIME = 1
    val FOR_END_TIME = 1


    val FOR_START_DATE = false;


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

        tv_title.text = "Book Venue"
        img_right_icon.visibility = View.GONE

        rl.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener { mWidthCode = rl.getMeasuredWidth() })
        dateFormat = SimpleDateFormat("dd-MM-yyyy")


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

//                if (mEventName.equals("")) {
//                    Utils.showToast(this,resources.getString(R.string.err_event_name))
//                }
//                else if(mStartDate.equals(""))
//                {
//                    Utils.showToast(this,resources.getString(R.string.err_event_start_date))
//                }
//                else if(mStartTime.equals(""))
//                {
//                    Utils.showToast(this,resources.getString(R.string.err_event_start_time))
//                }
//                else if(mEndDate.equals(""))
//                {
//                    Utils.showToast(this,resources.getString(R.string.err_event_end_date))
//                }
//                else if(mEndTime.equals(""))
//                {
//                    Utils.showToast(this,resources.getString(R.string.err_event_end_time))
//
//                }
//                else
//                {
//                    val json = JSONObject()
//                    json.put("VenueId", "2596")
//                    json.put("EventId", "9")
//                    json.put("ArtistId", SharedPref.getString(Constants.USER_ID, ""))
//                    json.put("VenueBookingFrom", "10-10-2019")
//                    json.put("VenueBookingTo", "10-12-2019")
//                    json.put("VenueBookingFromTime", "10:00:00")
//                    json.put("VenueBookingToTime", "12:00:00")
//                    json.put("BookingType", "0")
//                    json.put("BookingStatus", "P")
//                    KotlinHitAPI.callAPI(json.toString(),FOR_VENUE_BOOK,this)
//                }

                val json = JSONObject()
                json.put("VenueId", "2596")
                json.put("EventId", "9")
                json.put("ArtistId", SharedPref.getString(Constants.USER_ID, ""))
                json.put("VenueBookingFrom", "10-10-2019")
                json.put("VenueBookingTo", "10-12-2019")
                json.put("VenueBookingFromTime", "10:00")
                json.put("VenueBookingToTime", "12:00")
                json.put("BookingType", "0")
                json.put("BookingStatus", "P")
                KotlinHitAPI.callAPI(json.toString(),FOR_VENUE_BOOK,this)


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
                    dpd?.show(fragmentManager, "Datepickerdialog")

                    val al = java.util.ArrayList<Calendar>()
//                    for (i in checkDatesAL.indices) {
//                        val calendar = Calendar.getInstance()
//                        calendar.set(Calendar.YEAR, Integer.parseInt(dateFunctions.getYear(checkDatesAL.get(i))))
//                        calendar.set(Calendar.MONTH, Integer.parseInt(dateFunctions.getMonth(checkDatesAL.get(i))) - 1)
//                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFunctions.getDay(checkDatesAL.get(i))))
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
                    val list = arrayListOf<String>()


                    val timeDialog = DialogTime(this, FOR_START_TIME, list, object : TimeInterface {
                        override fun getTime(time_str: String) {
                            tvStartTime.setText(time_str)
                        }
                    })
                    timeDialog.show()


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

                    val calMin = Calendar.getInstance()

                    val dateMin = dateFormat.parse(mStartDate)
                    calMin.time = dateMin

                    dpd!!.minDate = calMin
                    dpd!!.maxDate = addSixMonthsCalendar
                    dpd!!.show(fragmentManager, "Datepickerdialog")


//                    try {
//                        datesAvailableCheckOut = getBookingsAvailable.getCheckOutDates(sCheckInDate, sCheckInTime)
//                    } catch (e: ParseException) {
//                        e.printStackTrace()
//                    }
//
//                    val hs = HashSet<String>()
//                    hs.addAll(datesAvailableCheckOut)
//                    datesAvailableCheckOut.clear()
//                    datesAvailableCheckOut.addAll(hs)
//
//                    val dateCheckOut = datesAvailableCheckOut
//                    val newAL = java.util.ArrayList<Calendar>()
//                    if (dateCheckOut.size > 0) {
//                        for (i in dateCheckOut.indices) {
//                            val calendar = Calendar.getInstance()
//                            calendar.set(Calendar.YEAR, Integer.parseInt(dateFunctions.getYear(dateCheckOut.get(i))))
//                            calendar.set(Calendar.MONTH, Integer.parseInt(dateFunctions.getMonth(dateCheckOut.get(i))) - 1)
//                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFunctions.getDay(dateCheckOut.get(i))))
//                            newAL.add(calendar)
//                        }
//                        var day = arrayOfNulls<Calendar>(newAL.size)
//                        day = newAL.toTypedArray<Calendar>()
//
//                        dpd!!.setSelectableDays(day)
//                        dpd!!.show(fragmentManager, "Datepickerdialog")
//                    }


                }


            }
            R.id.tvEndTime -> {

            }
        }
    }

    private fun getDateTimeDetails() {
        mStartDate = tvStartDate.text.toString()
        mStartTime = tvStartTime.text.toString()
        mEndDate = tvEndDate.text.toString()
        mEndTime = tvEndTime.text.toString()

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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


        val sSelectedDateDialog = "$year-$sMonth-$sDay"
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
            } else {

            }
        }
    }

    fun convertToDate(sSelectedDate: String): Date? {
        var date: Date? = null
        try {
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
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


            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }


}