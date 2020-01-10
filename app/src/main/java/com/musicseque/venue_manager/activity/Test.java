//package com.musicseque.venue_manager.activity
//
//import android.os.Bundle
//import android.view.View
//import android.view.ViewTreeObserver
//import android.widget.*
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.musicseque.R
//import com.musicseque.activities.BaseActivity
//import com.musicseque.event_manager.model.EventListModel
//import com.musicseque.interfaces.MyInterface
//import com.musicseque.interfaces.SpinnerData
//import com.musicseque.retrofit_interface.KotlinHitAPI
//import com.musicseque.utilities.Constants
//import com.musicseque.utilities.Constants.*
//import com.musicseque.utilities.SharedPref
//import com.musicseque.utilities.Utils
//import com.musicseque.venue_manager.model.MySelectedTimeModel
//import com.musicseque.venue_manager.others.DialogTime
//import com.musicseque.venue_manager.others.TimeInterface
//import com.musicseque.venue_manager.others.ToDialogTime
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
//import kotlinx.android.synthetic.main.activity_book_venue.*
//import kotlinx.android.synthetic.main.activity_book_venue.tvEndDate
//import kotlinx.android.synthetic.main.activity_book_venue.tvEndTime
//import kotlinx.android.synthetic.main.activity_book_venue.tvStartDate
//import kotlinx.android.synthetic.main.activity_book_venue.tvStartTime
//import kotlinx.android.synthetic.main.frag_profile_other.*
//import kotlinx.android.synthetic.main.toolbar_top.*
//import org.json.JSONObject
//import java.text.DateFormat
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.ArrayList
//import kotlin.collections.HashMap
//
//class BookVenueActivity : BaseActivity(), View.OnClickListener, MyInterface, DatePickerDialog.OnDateSetListener {
//
//
//private var timeAL = ArrayList<String>()
//private lateinit var timeFormat: SimpleDateFormat
//private var dpd: DatePickerDialog? = null
//        internal var now = Calendar.getInstance()
//        internal var addSixMonthsCalendar: Calendar = Calendar.getInstance()
//private var sDateSixMonths: String? = null
//        internal lateinit var dateFormat: DateFormat
//        internal lateinit var dateSixMonth: Date
//        val FOR_START_TIME = 1
//        val FOR_END_TIME = 2
//
//        lateinit private var alBookings: java.util.ArrayList<MySelectedTimeModel>
//    lateinit private var alBookingsEnd: java.util.ArrayList<MySelectedTimeModel>
//
//    val hashMap = HashMap<String, ArrayList<String>>()
//
//
//        var hashMapSorted = HashMap<String, ArrayList<String>>()
//        val listStartDate = ArrayList<String>()
//        val listEndDate = ArrayList<String>()
//
//
//        var FOR_START_DATE = false;
//        val monthOldFormat = SimpleDateFormat("dd-MMM-yyyy")
//        val monthNewFormat = SimpleDateFormat("dd-MM-yyyy")
//
//
//private lateinit var eventsList: ArrayList<EventListModel>
//    var mStartDate = ""
//            var mStartTime = ""
//            var mEndDate = ""
//            var mEndTime = ""
//            var mEventName = ""
//            var mEventId = ""
//            var mVenueId = ""
//
//
//            lateinit var listPopupWindow: ListPopupWindow
//            var mWidthCode: Int = 0
//
//
//            internal var endHashMap = HashMap<String, java.util.ArrayList<String>>()
//
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_book_venue)
//        initViews()
//        listeners()
//        getSixMonthDate()
//        hitAPI("get_events_list")
//        }
//
//private fun getSixMonthDate() {
//        addSixMonthsCalendar.add(Calendar.MONTH, 2)
//        sDateSixMonths = dateFormat.format(addSixMonthsCalendar.getTime())
//        dateSixMonth = dateFormat.parse(sDateSixMonths)
//        }
//
//
//private fun initViews() {
//
//        mVenueId = intent.getStringExtra("venue_id")
//        tv_title.text = "Book Venue"
//        img_right_icon.visibility = View.GONE
//
//        rl.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener { mWidthCode = rl.getMeasuredWidth() })
//        dateFormat = SimpleDateFormat("dd-MM-yyyy")
//        timeFormat = SimpleDateFormat("HH:mm")
//
//
//        }
//
//private fun listeners() {
//        img_first_icon.setOnClickListener(this)
//        tvEventName.setOnClickListener(this)
//        tvStartDate.setOnClickListener(this)
//        tvStartTime.setOnClickListener(this)
//        tvEndDate.setOnClickListener(this)
//        tvEndTime.setOnClickListener(this)
//        tvSendRequest.setOnClickListener(this)
//        }
//
//        override fun onClick(view: View) {
//        when (view.id) {
//        R.id.img_first_icon -> {
//        finish()
//        }
//        R.id.tvSendRequest -> {
//
//        if (mEventName.equals("")) {
//        Utils.showToast(this, resources.getString(R.string.err_event_name))
//        } else if (mStartDate.equals("")) {
//        Utils.showToast(this, resources.getString(R.string.err_event_start_date))
//        } else if (mStartTime.equals("")) {
//        Utils.showToast(this, resources.getString(R.string.err_event_start_time))
//        } else if (mEndDate.equals("")) {
//        Utils.showToast(this, resources.getString(R.string.err_event_end_date))
//        } else if (mEndTime.equals("")) {
//        Utils.showToast(this, resources.getString(R.string.err_event_end_time))
//
//        } else {
//        val json = JSONObject()
//        json.put("VenueId", "2596")
//        json.put("EventId", "9")
//        json.put("ArtistId", SharedPref.getString(Constants.USER_ID, ""))
//        json.put("VenueBookingFrom", "10-10-2019")
//        json.put("VenueBookingTo", "10-12-2019")
//        json.put("VenueBookingFromTime", "10:00:00")
//        json.put("VenueBookingToTime", "12:00:00")
//        json.put("BookingType", "0")
//        json.put("BookingStatus", "P")
//        KotlinHitAPI.callAPI(json.toString(), FOR_VENUE_BOOK, this)
//        }
//
//
//        }
//        R.id.tvEventName -> {
//        var list = ArrayList<String>()
//
//        for ((index, value) in eventsList.withIndex()) {
//        list.add(eventsList.get(index).event_title)
//        }
//        val eventArray = arrayOfNulls<String>(list.size)
//        list.toArray(eventArray)
//
//        if (eventArray != null) {
//        showDropdown(eventArray, tvEventName, SpinnerData { mData, mData1 ->
//        mEventName = mData
//        mEventId = mData1
//        tvEventName.text = mEventName
//        }, mWidthCode)
//        }
//
//        }
//        R.id.tvStartDate -> {
//        if (mEventName.equals("")) {
//        Utils.showToast(this, "Please select Event name")
//        } else {
//        FOR_START_DATE = true
//        dpd = null
//        if (dpd == null) {
//        dpd = DatePickerDialog.newInstance(
//        this,
//        now.get(Calendar.YEAR),
//        now.get(Calendar.MONTH),
//        now.get(Calendar.DAY_OF_MONTH)
//        )
//        }
//        dpd?.minDate = now
//        dpd?.maxDate = addSixMonthsCalendar
//
//        //  hitAPI("from_time")
//
//
//        }
//
//
//        }
//        R.id.tvStartTime -> {
//        getDateTimeDetails()
//        if (mStartDate.equals(""))
//        Utils.showToast(this, resources.getString(R.string.err_event_start_date))
//        else {
//        if (timeAL.size > 0) {
//        val timeDialog = DialogTime(this, FOR_START_TIME, timeAL, object : TimeInterface {
//        override fun getTime(time_str: String) {
//        tvStartTime.setText(time_str)
//        }
//        })
//        timeDialog?.show()
//        } else {
//        val timeDialog = DialogTime(this, FOR_START_TIME, hashMap.get(tvStartDate.text.toString()), object : TimeInterface {
//        override fun getTime(time_str: String) {
//        tvStartTime.setText(time_str)
//        }
//        })
//        timeDialog?.show()
//        }
//
//        }
//
//        }
//        R.id.tvEndDate -> {
//        FOR_START_DATE = false
//        getDateTimeDetails()
//        dpd = null
//        if (dpd == null) {
//        dpd = DatePickerDialog.newInstance(
//        this,
//        now.get(Calendar.YEAR),
//        now.get(Calendar.MONTH),
//        now.get(Calendar.DAY_OF_MONTH)
//        )
//        }
//
//        if (mStartDate.equals("", ignoreCase = true))
//        Utils.showToast(this, resources.getString(R.string.err_event_start_date))
//        else if (mStartTime.equals("", ignoreCase = true))
//        Utils.showToast(this, resources.getString(R.string.err_event_start_time))
//        else {
//        hashMapSorted.clear()
//        listEndDate.clear()
//        hitAPI("to_time")
//
//
//        }
//
//
//        }
//        R.id.tvEndTime -> {
//        getDateTimeDetails()
//        if (mStartDate.equals(""))
//        Utils.showToast(this, resources.getString(R.string.err_event_start_date))
//        else if (mStartTime.equals("", ignoreCase = true))
//        Utils.showToast(this, resources.getString(R.string.err_event_start_date))
//        else if (mEndDate.equals("", ignoreCase = true))
//        Utils.showToast(this, resources.getString(R.string.err_event_end_date))
//        else {
//
//        val timeDialog = ToDialogTime(this, FOR_END_TIME, endHashMap.get(tvEndDate.text.toString()), object : TimeInterface {
//        override fun getTime(time_str: String) {
//        tvEndTime.setText(time_str)
//        }
//        })
//        timeDialog?.show()
//        }
//
//
//        }
//        }
//        }
//
//private fun getDisabledDays() {
//        val hashMapSorted = hashMap.toList().sortedBy { (key, _) -> key }.toMap()
//        for ((k, v) in hashMapSorted) {
//        if (v.size == 24) {
//        listStartDate.add(k)
//        }
//
//        }
//        }
//
//
//private fun getDateTimeDetails() {
//        mStartDate = tvStartDate.text.toString()
//        mStartTime = tvStartTime.text.toString()
//        mEndDate = tvEndDate.text.toString()
//        mEndTime = tvEndTime.text.toString()
//
//        }
//
//
//        override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
//        var dateCheckOut: Date? = null
//        var sMonth = ""
//        var sDay = ""
//        val month = monthOfYear + 1
//        if (month < 10)
//        sMonth = "0$month"
//        else
//        sMonth = month.toString() + ""
//        if (dayOfMonth < 10)
//        sDay = "0$dayOfMonth"
//        else
//        sDay = dayOfMonth.toString() + ""
//
//
//        val sSelectedDateDialog = "$sDay-$sMonth-$year"
//        val dateSelected = convertToDate(sSelectedDateDialog)
//
//
//        if (FOR_START_DATE) {
//        tvStartDate.setText(sSelectedDateDialog)
//
//        // dateCheckOut = getBookingsAvailable.getCheckOutDate(sCheckOutDate)
//
//        tvStartTime.setText("")
//        tvEndDate.setText("")
//        tvEndTime.setText("")
//
//
//        } else {
//        tvEndDate.setText(sSelectedDateDialog)
//
//        }
//
//        }
//
//private fun hitAPI(type: String) {
//        if (Utils.isNetworkConnected(this)) {
//        if (type.equals("get_events_list")) {
//        val obj = JSONObject()
//        obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
//        obj.put("EventStatus", "2")
//        KotlinHitAPI.callAPI(obj.toString(), Constants.FOR_SHOW_EVENTS_LIST, this)
//
//        } else if (type.equals("get_timmings")) {
//        val json = JSONObject()
//        json.put("VenueId", mVenueId)
//        json.put("BookingAsOnDate", "01-01-1900")
//        KotlinHitAPI.callAPI(json.toString(), Constants.FOR_VENUE_TIMMINGS, this)
//        } else if (type.equals("to_time")) {
//        val json = JSONObject()
//        json.put("VenueId", mVenueId)
//        json.put("FromDate", mStartDate)
//        json.put("FromTime", mStartTime)
//
//        KotlinHitAPI.callAPI(json.toString(), Constants.FOR_VENUE_TO_TIMMINGS, this)
//
//        }
//        else if (type.equals("from_time")) {
//        val json = JSONObject()
//        json.put("VenueId", mVenueId)
//        json.put("FromDate", mStartDate)
//        json.put("FromTime", mStartTime)
//
//        KotlinHitAPI.callAPI(json.toString(), Constants.FOR_VENUE_FROM_TIMMINGS, this)
//
//        }
//        } else {
//        Utils.showToast(this, resources.getString(R.string.err_no_internet))
//        }
//
//
//        }
//
//        override fun sendResponse(response: Any?, TYPE: Int) {
//        Utils.hideProgressDialog()
//
//        if (TYPE == Constants.FOR_SHOW_EVENTS_LIST) {
//        val obj = JSONObject(response.toString())
//        if (obj.getString("Status").equals("Success", false)) {
//        val arr = obj.getJSONArray("result")
//        val listType = object : TypeToken<ArrayList<EventListModel>>() {}.type
//        eventsList = Gson().fromJson<ArrayList<EventListModel>>(arr.toString(), listType)
//        }
//        hitAPI("get_timmings")
//        } else if (TYPE == FOR_VENUE_TIMMINGS) {
//        val obj = JSONObject(response.toString())
//        if (obj.getString("Status").equals("Success", false)) {
//        val jsonArray = obj.getJSONArray("result")
//        alBookings = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)
//
//        getTimmingsHashMap(alBookings, hashMap)
//
//
//        }
//        } else if (TYPE == FOR_VENUE_TO_TIMMINGS) {
//        val obj = JSONObject(response.toString())
//        if (obj.getString("Status").equals("Success", false)) {
//        val jsonArray = obj.getJSONArray("result")
//        alBookingsEnd = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)
//        getEndTimmingsHashMap(alBookingsEnd, endHashMap)
//
//
//
//        if (alBookingsEnd.size > 0) {
//        //getEnabledDays()
//        val hashMapSorted = endHashMap.toList().sortedBy { (key, _) -> key }.toMap()
//        val alDate = java.util.ArrayList<Calendar>()
//        for ((k, v) in hashMapSorted) {
//
//
//        val calendar = Calendar.getInstance()
//        val data = k.split("-")
//
//        calendar.set(Calendar.YEAR, Integer.parseInt(data[2]))
//        calendar.set(Calendar.MONTH, Integer.parseInt(data[1]) - 1)
//        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[0]))
//        alDate.add(calendar)
//        }
//        var days = arrayOfNulls<Calendar>(alDate.size)
//        days = alDate.toTypedArray<Calendar?>()
//
//        dpd!!.selectableDays = days
//        dpd!!.show(fragmentManager, "Datepickerdialog")
//
//        }
//
//
//        } else {
//        Utils.showToast(this, "Please change your Start Date/Time")
//        }
//        }
//        }
//
//
//        fun convertToDate(sSelectedDate: String): Date? {
//        var date: Date? = null
//        try {
//        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
//        date = simpleDateFormat.parse(sSelectedDate)
//        } catch (e: ParseException) {
//        e.printStackTrace()
//        }
//
//        return date
//
//        }
//
//        fun showDropdown(array: Array<String?>, textView: TextView, spinnerData: SpinnerData, width: Int) {
//        listPopupWindow = ListPopupWindow(this)
//        listPopupWindow.setAdapter(ArrayAdapter(
//        this,
//        R.layout.row_profile_spinner, array))
//        listPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_black))
//        listPopupWindow.setAnchorView(textView)
//        listPopupWindow.setWidth(width)
//        listPopupWindow.setHeight(400)
//        listPopupWindow.setModal(true)
//        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
//
//        spinnerData.getData(array[position], position.toString())
//        listPopupWindow.dismiss()
//        })
//        listPopupWindow.show()
//        }
//
//
//private fun getTimmingsHashMap(arraylist: java.util.ArrayList<MySelectedTimeModel>, hashmap: HashMap<String, java.util.ArrayList<String>>) {
//        for ((index, value) in arraylist.withIndex()) {
//        if (hashmap.containsKey(value.venue_booking_date)) {
//
//        var al = ArrayList<String>()
//
//        al = hashmap.get(value.venue_booking_date)!!
//        al.add(value.booking_time)
//        hashmap.put(value.venue_booking_date, al)
//
//
//        } else {
//        val al = ArrayList<String>()
//        al.add(value.booking_time)
//        hashmap.put(value.venue_booking_date, al)
//        }
//
//        }
//
//
//        }
//
//private fun getEndTimmingsHashMap(arraylist: java.util.ArrayList<MySelectedTimeModel>, hashmap: HashMap<String, java.util.ArrayList<String>>) {
//        for ((index, value) in arraylist.withIndex()) {
//        if (hashmap.containsKey(value.availability_date)) {
//
//        var al = ArrayList<String>()
//        al = hashmap.get(value.availability_date)!!
//        al.add(value.availability_to_time)
//        hashmap.put(value.availability_date, al)
//
//
//        } else {
//        val al = ArrayList<String>()
//        al.add(value.availability_to_time)
//        hashmap.put(value.availability_date, al)
//        }
//
//        }
//
//
//        }
//
//
////        val hashMapSorted = hashMap.toList().sortedBy { (key, _) -> key }.toMap()
//
//
//        }
//
