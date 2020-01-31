package com.musicseque.venue_manager.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.event_manager.adapter.EventAdapter
import com.musicseque.event_manager.model.CurrencyModel
import com.musicseque.event_manager.model.EventModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.models.CityModel
import com.musicseque.models.CountryModel
import com.musicseque.models.StateModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.*
import com.musicseque.utilities.Constants.*
import com.musicseque.venue_manager.model.MySelectedTimeModel
import com.musicseque.venue_manager.others.DialogTime
import com.musicseque.venue_manager.others.TimeInterface
import com.musicseque.venue_manager.others.ToDialogTime
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_book_venue.*
import kotlinx.android.synthetic.main.activity_book_venue.etAttendence
import kotlinx.android.synthetic.main.activity_book_venue.etEventDescription
import kotlinx.android.synthetic.main.activity_book_venue.etEventName
import kotlinx.android.synthetic.main.activity_book_venue.iconDownEventType
import kotlinx.android.synthetic.main.activity_book_venue.iconUpEventType
import kotlinx.android.synthetic.main.activity_book_venue.ivAddImage
import kotlinx.android.synthetic.main.activity_book_venue.ivProfile
import kotlinx.android.synthetic.main.activity_book_venue.recyclerEvent
import kotlinx.android.synthetic.main.activity_book_venue.rlAttendence
import kotlinx.android.synthetic.main.activity_book_venue.rlBudgetGuestCurrency
import kotlinx.android.synthetic.main.activity_book_venue.rlStartTime
import kotlinx.android.synthetic.main.activity_book_venue.seekBarPrice
import kotlinx.android.synthetic.main.activity_book_venue.tvBudgetPerGuest
import kotlinx.android.synthetic.main.activity_book_venue.tvCurrency
import kotlinx.android.synthetic.main.activity_book_venue.tvEndDate
import kotlinx.android.synthetic.main.activity_book_venue.tvEndTime
import kotlinx.android.synthetic.main.activity_book_venue.tvStartDate
import kotlinx.android.synthetic.main.activity_book_venue.tvStartTime
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.toolbar_top.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BookVenueActivity : BaseActivity(), View.OnClickListener, MyInterface, DatePickerDialog.OnDateSetListener {

    private var mCountryId: String = ""
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


    private lateinit var eventsList: ArrayList<EventModel>
    lateinit private var newList: ArrayList<CurrencyModel>
    var mEventTypeId: String = ""
    private var mCurrencyId: String? = ""
    private var mCurrency: String? = ""
    var uploadFile: MultipartBody.Part? = null

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
    var mSelectedDate: String = ""


    var mStateId = ""

    var mCityId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_venue)
        initViews()
        listeners()
        getSixMonthDate()
        addTimeMethod()
        hitAPI(FOR_EVENT_TYPE_LIST, "")
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



        recyclerEvent?.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        seekBarPrice.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                tvBudgetPerGuest.setText(progress.toString())
                etBudgetPerGuest.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })


        rlAttendence.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener { mWidthCode = rlAttendence.getMeasuredWidth() })



        rlStartTime.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener { mWidthCode = rlStartTime.getMeasuredWidth() })
        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        timeFormat = SimpleDateFormat("HH:mm")
        mSelectedDate = intent.getStringExtra("selected_date")

        mStartDate = mSelectedDate;
        tvStartDate.text = mSelectedDate
        tvVenueName.text = intent.getStringExtra("venue_name")

    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        rlBudgetGuestCurrency.setOnClickListener(this)
        // tvEventName.setOnClickListener(this)
        tvStartDate.setOnClickListener(this)
        tvStartTime.setOnClickListener(this)
        tvEndDate.setOnClickListener(this)
        tvEndTime.setOnClickListener(this)
        tvSendRequest.setOnClickListener(this)
        ivAddImage.setOnClickListener(this)
        iconDownEventType.setOnClickListener(this)
        iconUpEventType.setOnClickListener(this)
        tvStateBookVenue!!.setOnClickListener(this)
        tvCityBookVenue!!.setOnClickListener(this)
        tvCountryBookVenue.setOnClickListener(this)



        etBudgetPerGuest.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(str: Editable?) {
                if (str?.length == 0) {
                    tvBudgetPerGuest.text = 0.toString()
                    seekBarPrice.setProgress(0)
                } else {
                    tvBudgetPerGuest.text = str.toString()
                    var value = str.toString()
                    if (str!!.length > 1) {
                        if (str.startsWith("0")) {
                            value = str.substring(1)
                        }

                    }

                    seekBarPrice.setProgress(Integer.parseInt(value))
                    etBudgetPerGuest.setSelection(value.length)


                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(str: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })

        // eventArrow.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.tvCountryBookVenue -> {
                showDropdown(CountryStateCityClass.arrCountryName, tvCountryBookVenue, SpinnerData { mId, mName ->
                    mCountryId = mId
                    tvCountryCreateEvent.text = mName
                    mStateId = ""
                    mCityId = ""
                    tvStateCreateEvent.text = ""
                    tvCityCreateEvent.text = ""
                    CountryStateCityClass.alCity.clear()
                    CountryStateCityClass.alCityName.clear()
                    CountryStateCityClass.alState.clear()
                    CountryStateCityClass.alStateName.clear()

                    callStateAPI()

                }, 500)
            }

            R.id.tvStateBookVenue -> {
                if (mCountryId.equals("")) {
                    Utils.showToast(this, "Please select country first")
                } else {
                    showDropdown(CountryStateCityClass.arrStateName, tvStateCreateEvent, SpinnerData { mId, mName ->
                        mStateId = mId
                        tvStateCreateEvent.text = mName
                        CountryStateCityClass.alCity.clear()
                        CountryStateCityClass.alCityName.clear()

                        mCityId = ""
                        tvCityCreateEvent.text = ""

                        callCityAPI()

                    }, 500)
                }
            }
            R.id.tvCityBookVenue -> {
                if (!mStateId.equals("")) {
                    showDropdown(CountryStateCityClass.arrCityName, tvCityCreateEvent, SpinnerData { mId, mName ->
                        mCityId = mId
                        tvCityCreateEvent.text = mName
                    }, 500)
                } else {
                    Utils.showToast(this, resources.getString(R.string.err_state))
                }
            }
            R.id.iconDownEventType -> {
                recyclerEvent.setVisibility(View.VISIBLE)
                iconDownEventType.setVisibility(View.GONE)
                iconUpEventType.setVisibility(View.VISIBLE)
            }
            R.id.iconUpEventType -> {
                recyclerEvent.setVisibility(View.GONE)
                iconDownEventType.setVisibility(View.VISIBLE)
                iconUpEventType.setVisibility(View.GONE)
            }


            R.id.img_first_icon -> {
                finish()
            }
            R.id.rlBudgetGuestCurrency -> {
                var list = ArrayList<String>()
                // var items: Array<String> = arrayOf()

                for ((index, value) in newList.withIndex()) {
                    list.add(newList.get(index).currencyType)
                }
                val currencyArray = arrayOfNulls<String>(list.size)
                list.toArray(currencyArray)

                if (currencyArray != null) {
                    showDropdown(currencyArray!!, tvCurrency, SpinnerData { mId, mName ->
                        mCurrency = mName
                        mCurrencyId = mId
                        tvCurrency.text = mCurrency
                    }, mWidthCode)
                }

            }
            R.id.ivAddImage -> {
                openDialog("event_image")
            }
            R.id.tvSendRequest -> {
                getEventsId()
                val mEventName = etEventName.text.toString()
                val mDescription = etEventDescription.text.toString()

                getDateTimeDetails()

                val mAttendence = etAttendence.text.toString()
                val mCurrency = tvCurrency.text.toString()
                val mBudgetGuest = tvBudgetPerGuest.text.toString()
                if (KotlinUtils.checkEmpty(mEventName)) {
                    Utils.showToast(this, resources.getString(R.string.err_event_name))
                } else if (KotlinUtils.checkEmpty(mDescription)) {
                    Utils.showToast(this, resources.getString(R.string.err_event_desc))

                } else if (KotlinUtils.checkEmpty(mEventTypeId)) {
                    Utils.showToast(this, resources.getString(R.string.err_event_type))

                } else if (KotlinUtils.checkEmpty(mStartDate)) {
                    Utils.showToast(this, resources.getString(R.string.err_event_start_date))
                } else if (KotlinUtils.checkEmpty(mEndDate)) {
                    Utils.showToast(this, resources.getString(R.string.err_event_end_date))

                } else if (KotlinUtils.checkEmpty(mStartTime)) {
                    Utils.showToast(this, resources.getString(R.string.err_event_start_time))
                } else if (KotlinUtils.checkEmpty(mEndTime)) {
                    Utils.showToast(this, resources.getString(R.string.err_event_end_time))

                } else if (KotlinUtils.checkEmpty(mAttendence)) {
                    Utils.showToast(this, resources.getString(R.string.err_event_guest_count))
                } else if (KotlinUtils.checkEmpty(mCurrency)) {
                    Utils.showToast(this, resources.getString(R.string.err_currency))
                } else if (mBudgetGuest.equals("0")) {
                    Utils.showToast(this, resources.getString(R.string.err_event_guest_budget_0))

                } else {


                    //val (mDate1, mDate2) = KotlinUtils.dateFormatToSend(mStartDate, mEndDate)

                    val obj = JSONObject();
                    obj.put("EventId", mEventId)
                    obj.put("EventTitle", mEventName)
                    obj.put("EventDescription", mDescription)
                    obj.put("EventTypeId", mEventTypeId)

                    obj.put("EventDateFrom", mStartDate)
                    obj.put("EventDateTo", mEndDate)
                    obj.put("EventTimeFrom", mStartTime)
                    obj.put("EventTimeTo", mEndTime)
                    obj.put("EventGatheringCapacity", mAttendence)
                    obj.put("EventChargesPayCurrencyId", mCurrencyId)
                    obj.put("EventBudget", mBudgetGuest)
                    obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
                    obj.put("EventVenueId", mVenueId)

                    hitAPI(FOR_SAVE_UPDATE_EVENT_DETAIL, obj.toString())


                }
            }

            R.id.tvStartDate -> {

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


                            var al = ArrayList<String>()

                            if (hashMap.containsKey(tvStartDate.text.toString())) {
                                al = hashMap.get(tvStartDate.text.toString())!!
                                if (al != null && al.size > 0) {
                                    for (time in alTime) {
                                        val dTimeList = hourFormat.parse(time)

                                        if (dTimeList.before(dCurrentTime)) {

                                            if (al!!.contains(time)) {

                                            } else {
                                                al.add(time)
                                            }
                                        } else {
                                            break
                                        }


                                    }

                                } else {


                                }

                            } else {

                                for (time in alTime) {
                                    val dTimeList = hourFormat.parse(time)

                                    if (dTimeList.before(dCurrentTime)) {


                                        al.add(time)

                                    }


                                }


                            }


                            val timeDialog = DialogTime(this, FOR_START_TIME, al, object : TimeInterface {
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
            if (type == Constants.FOR_COUNTRIES_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this)
            } else if (type == Constants.FOR_STATE_LIST) {
                RetrofitAPI.callAPI(str, Constants.FOR_STATE_LIST, this)
            } else if (type == Constants.FOR_CITY_LIST) {
                RetrofitAPI.callAPI(str, Constants.FOR_CITY_LIST, this)
            } else if (type == FOR_VENUE_FROM_TIMMINGS) {
                val json = JSONObject()
                json.put("VenueId", mVenueId)
                // json.put("BookingAsOnDate", "01-01-1900")
                RetrofitAPI.callAPI(json.toString(), Constants.FOR_VENUE_FROM_TIMMINGS, this)
            } else if (type == FOR_VENUE_TO_TIMMINGS) {
                val json = JSONObject()
                json.put("VenueId", mVenueId)
                json.put("FromDate", mStartDate)
                json.put("FromTime", mStartTime)

                RetrofitAPI.callAPI(json.toString(), Constants.FOR_VENUE_TO_TIMMINGS, this)

            } else if (type == FOR_VENUE_BOOK) {
                RetrofitAPI.callAPI(str, FOR_VENUE_BOOK, this)
            } else if (type == FOR_EVENT_TYPE_LIST) {
                RetrofitAPI.callGetAPI(FOR_EVENT_TYPE_LIST, this)
            } else if (type == FOR_CURRENCY_LIST) {
                RetrofitAPI.callGetAPI(FOR_CURRENCY_LIST, this)
            } else if (type == FOR_SAVE_UPDATE_EVENT_DETAIL) {
                RetrofitAPI.callAPI(str, FOR_SAVE_UPDATE_EVENT_DETAIL, this)
            }

        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {

            FOR_EVENT_TYPE_LIST -> {
                val jsonArray = JSONArray(response.toString())
                val gson = Gson()
                val listType = object : TypeToken<ArrayList<EventModel>>() {}.type
                eventsList = Gson().fromJson<ArrayList<EventModel>>(jsonArray.toString(), listType)
                recyclerEvent.adapter = EventAdapter(this, eventsList)
                hitAPI(FOR_CURRENCY_LIST, "")
            }
            FOR_CURRENCY_LIST -> {
                val jsonArray = JSONArray(response.toString())
                val gson = Gson()
                val listType = object : TypeToken<ArrayList<CurrencyModel>>() {}.type
                newList = gson.fromJson<ArrayList<CurrencyModel>>(jsonArray.toString(), listType)

                hitAPI(FOR_VENUE_FROM_TIMMINGS, "")
            }
            FOR_VENUE_FROM_TIMMINGS -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", false)) {
                    val jsonArray = obj.getJSONArray("result")
                    alBookings = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)

                    getTimmingsHashMap(alBookings, hashMap)
                    Log.e("", "")


                }
                hitAPI(FOR_COUNTRIES_LIST,"")
            }
            FOR_VENUE_TO_TIMMINGS -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", false)) {
                    val jsonArray = obj.getJSONArray("result")
                    alBookingsEnd = Gson().fromJson<java.util.ArrayList<MySelectedTimeModel>>(jsonArray.toString(), object : TypeToken<java.util.ArrayList<MySelectedTimeModel>>() {}.type)
                    getEndTimmingsHashMap(alBookingsEnd, endHashMap)
                    Log.e("", "")


                    val alDate = java.util.ArrayList<Calendar>()
                    if (alBookingsEnd.size > 0) {
                        //getEnabledDays()
                        val hashMapSorted = endHashMap.toList().sortedBy { (key, _) -> key }.toMap()

                        for ((k, v) in hashMapSorted) {


                            val calendar = Calendar.getInstance()
                            val data = k.split("-")

                            calendar.set(Calendar.YEAR, Integer.parseInt(data[2]))
                            calendar.set(Calendar.MONTH, Integer.parseInt(data[1]) - 1)
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[0]))
                            if (mStartDate.equals(k)) {
                                alDate.add(calendar)
                            }

                        }
                        var days = arrayOfNulls<Calendar>(alDate.size)
                        days = alDate.toTypedArray<Calendar?>()

                        dpd!!.selectableDays = days


                    }
                    if (alDate.size > 0)
                        dpd!!.show(fragmentManager, "Datepickerdialog")
                    else
                        Utils.showToast(this, "Please change your Start Date/Time")

                } else {
                    Utils.showToast(this, "Please change your Start Date/Time")
                }
            }
            FOR_VENUE_BOOK -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", false)) {
                    if (Utils.isNetworkConnected(this)) {

                        if (uploadFile != null) {
                            Utils.initializeAndShow(this)
                            val mEventIds = RequestBody.create(MediaType.parse("text/plain"), mEventId)
                            ImageUploadClass.imageUpload(uploadFile, mEventIds, null, FOR_UPLOAD_EVENT_PROFILE_IMAGE, this)

                        } else {

                        }
                    } else {
                        Utils.showToast(this, resources.getString(R.string.err_no_internet))
                    }

                    dialogRequest()

                } else {

                }
            }
            FOR_SAVE_UPDATE_EVENT_DETAIL -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", false)) {
                    mEventId = obj.getString("EventId")
                    if (Utils.isNetworkConnected(this)) {

                        if (uploadFile != null) {
                            Utils.initializeAndShow(this)
                            val mEventIds = RequestBody.create(MediaType.parse("text/plain"), mEventId)
                            ImageUploadClass.imageUpload(uploadFile, mEventIds, null, FOR_UPLOAD_EVENT_PROFILE_IMAGE, this)

                        } else {
                            dialogRequest()
                        }
                    } else {
                        Utils.showToast(this, resources.getString(R.string.err_no_internet))
                    }

                } else {

                }
            }
            FOR_UPLOAD_EVENT_PROFILE_IMAGE -> {
                val jsonObj = JSONObject(response.toString())
                if (jsonObj.getString("Status").equals("Success", true)) {
                    dialogRequest()
                }
            }
            Constants.FOR_COUNTRIES_LIST -> {
                CountryStateCityClass.countriesDetail(response.toString())

            }
            Constants.FOR_STATE_LIST -> {
                CountryStateCityClass.stateList(response.toString())


            }
            Constants.FOR_CITY_LIST -> {
                CountryStateCityClass.cityList(response.toString())
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
                com.musicseque.R.layout.row_profile_spinner, array))
        listPopupWindow.setBackgroundDrawable(resources.getDrawable(com.musicseque.R.drawable.rectangle_black))
        listPopupWindow.setAnchorView(textView)
        listPopupWindow.setWidth(width)
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            if (textView.id == com.musicseque.R.id.tvCurrency) {
                spinnerData.getData(newList.get(position).id, newList.get(position).currency)
            } else if (textView.id == com.musicseque.R.id.etAttendence) {
                spinnerData.getData("", array[position])

            } else if (textView.id == R.id.tvStateBookVenue) {
                spinnerData.getData(CountryStateCityClass.alState[position].stateId, array[position])
            } else if (textView.id == R.id.tvCityBookVenue) {
                spinnerData.getData(CountryStateCityClass.alCity[position].cityId, array[position])
            } else if (textView.id == R.id.tvCountryBookVenue) {
                spinnerData.getData(CountryStateCityClass.alCity[position].cityId, array[position])
            }
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
        dialog = Dialog(this@BookVenueActivity)
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

    private fun getEventsId() {
        for (item in eventsList) {
            if (item.isSelected) {
                mEventTypeId = mEventTypeId + "," + item.id

            }

        }
        if (mEventTypeId.equals(""))
            mEventTypeId = ""
        else
            mEventTypeId = mEventTypeId.substring(1, mEventTypeId.length)

    }

    public fun getImage(file: File, fileToUpload: MultipartBody.Part, mUSerId: RequestBody, name: String) {
        Glide.with(this).load(file).into(ivProfile)
        uploadFile = fileToUpload
        if (!mEventId.equals("", true)) {
            Utils.initializeAndShow(this)
            val mEventIds = RequestBody.create(MediaType.parse("text/plain"), mEventId)
            ImageUploadClass.imageUpload(uploadFile, mEventIds, null, FOR_UPLOAD_EVENT_PROFILE_IMAGE, this)
            uploadFile = null
        }


    }

    private fun callCityAPI() {
        try {
            val jsonObject1 = JSONObject()
            jsonObject1.put("StateId", mStateId)
            hitAPI(Constants.FOR_CITY_LIST, jsonObject1.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun callStateAPI() {
        try {
            val jsonObject1 = JSONObject()
            jsonObject1.put("CountryId", mCountryId)
            hitAPI(Constants.FOR_STATE_LIST, jsonObject1.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}

