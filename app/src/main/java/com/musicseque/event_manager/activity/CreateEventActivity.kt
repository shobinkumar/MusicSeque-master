package com.musicseque.event_manager.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.event_manager.adapter.EventAdapter
import com.musicseque.event_manager.model.CurrencyModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import com.musicseque.interfaces.DateTimeInterface
import kotlin.collections.ArrayList
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.event_manager.model.EventModel
import com.musicseque.models.CityModel
import com.musicseque.models.CountryModel
import com.musicseque.models.StateModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.*
import com.musicseque.utilities.SharedPref
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.toolbar_top.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import java.io.File
import java.util.*


class CreateEventActivity : BaseActivity(), View.OnClickListener, MyInterface, DateTimeInterface {


    private var mFromDateArtistCal: String = ""
    private lateinit var currencyArray: Array<String>
    private var mAddress: String = ""
    var uploadFile: MultipartBody.Part? = null
    private lateinit var eventsList: ArrayList<EventModel>
    lateinit private var newList: ArrayList<CurrencyModel>
    lateinit var arrGuestCount: Array<String?>
    private var mCurrencyId: String? = ""
    private var mCurrency: String? = ""
    var mWidthCode: Int = 0
    lateinit private var listPopupWindow: ListPopupWindow
    var mEventId: String = ""
    private lateinit var mCurrentDate: Date
    var DATE_TIME_FROM: Int? = null

    val FROM_DATE = 1
    val TO_DATE = 2;
    val FROM_TIME = 3;
    val TO_TIME = 4

    val FROM_DATE_VENUE = 5
    val TO_DATE_VENUE = 6;
    val FROM_TIME_VENUE = 7;
    val TO_TIME_VENUE = 8

    var mFromDateArtist: String = ""
    var mToDateArtist: String = ""
    var mFromTimeArtist: String = ""
    var mToTimeArtist: String = ""

    var mFromDateVenue: String = ""
    var mToDateVenue: String = ""
    var mFromTimeVenue: String = ""
    var mToTimeVenue: String = ""

    var mEventTypeId: String = ""
    var mCityId: String = ""
    var mStateId: String = ""
    var mCountryId: String = ""
    var mZipCode: String = ""


    var isEdit = false

    var countryAL = ArrayList<CountryModel>()
    var countryNameAL = ArrayList<String>()
    lateinit var arrCountryName: Array<String>


    private val stateNameAL = java.util.ArrayList<String>()
    private val stateAL = java.util.ArrayList<StateModel>()
    private lateinit var arrStateName: Array<String>


    lateinit var arrCityName: Array<String>
    var alCity = java.util.ArrayList<CityModel>()
    var alCityName = java.util.ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        initViews()
        listeners()
        getAPI(Constants.FOR_EVENT_TYPE_LIST, "")
    }

    private fun initViews() {

        mCurrentDate = KotlinUtils.getCurrentDate()
        arrGuestCount = arrayOf("0-50", "51-100", "101-200", "201-300", "300+")
        tv_title.text = "Create Event"
        img_right_icon.visibility = View.GONE




        recyclerEvent?.layoutManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager?
        try {
            mEventId = intent.getStringExtra("event_id")
            isEdit = intent.getBooleanExtra("isEdit", false)
            if (isEdit) {
                tv_title.text = "Edit Event"
            } else {
                rlStartDate.isEnabled = true
                rlStartTime.isEnabled = true
                rlEndDate.isEnabled = true
                rlEndTime.isEnabled = true
            }

        } catch (exp: Exception) {
            mEventId = ""
        }
        seekBarPrice.setOnSeekbarChangeListener { }
        seekBarPrice.setOnSeekbarChangeListener(OnSeekbarChangeListener { minValue -> tvBudgetPerGuest.setText(minValue.toString()) })
        rlAttendence.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener { mWidthCode = rlAttendence.getMeasuredWidth() })

    }


    private fun listeners() {


        img_first_icon.setOnClickListener(this)
        ivAddImage.setOnClickListener(this)
        iconDownEventType.setOnClickListener(this)
        iconUpEventType.setOnClickListener(this)
        rlStartDate.setOnClickListener(this)
        rlEndDate.setOnClickListener(this)
        rlStartTime.setOnClickListener(this)
        rlEndTime.setOnClickListener(this)

        rlStartDateVenue.setOnClickListener(this)
        rlEndDateVenue.setOnClickListener(this)
        rlStartTimeVenue.setOnClickListener(this)
        rlEndTimeVenue.setOnClickListener(this)
        rlBudgetGuestCurrency.setOnClickListener(this)
        tvSubmit.setOnClickListener(this)
        tvCountryCreateEvent.setOnClickListener(this)
        tvStateCreateEvent.setOnClickListener(this)
        tvCityCreateEvent.setOnClickListener(this)
    }

    private fun getAPI(value: Int, args: String) {

        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this)
            if (value == Constants.FOR_EVENT_TYPE_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_EVENT_TYPE_LIST, this)
            } else if (value == Constants.FOR_CURRENCY_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_CURRENCY_LIST, this)
            } else if (value == Constants.FOR_EVENT_CREATE_UPDATE) {
                RetrofitAPI.callAPI(args, Constants.FOR_EVENT_CREATE_UPDATE, this)
            } else if (value == Constants.FOR_EVENT_DETAIL) {
                val obj = JSONObject()
                obj.put("EventId", mEventId)
                RetrofitAPI.callAPI(obj.toString(), Constants.FOR_EVENT_DETAIL, this)
            } else if (value == Constants.FOR_COUNTRIES_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this@CreateEventActivity)

            } else if (value == FOR_STATE_LIST) {
                RetrofitAPI.callAPI(args, Constants.FOR_STATE_LIST, this@CreateEventActivity)

            } else if (value == Constants.FOR_CITY_LIST) {
                RetrofitAPI.callAPI(args, Constants.FOR_CITY_LIST, this@CreateEventActivity)
            }
        } else {
            Utils.showToast(this, getString(R.string.err_no_internet))
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {


            R.id.img_first_icon -> {
                finish()
            }
            R.id.ivAddImage -> {
                openDialog("event_image")
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


            R.id.rlStartDate, R.id.rlStartDateVenue -> {
                if (isEdit) {
                    Utils.showToast(this, "Can't edit date")
                } else {


                    if (view.id == R.id.rlStartDate) {
                        DATE_TIME_FROM = FROM_DATE

                    } else {
                        DATE_TIME_FROM = FROM_DATE_VENUE

                    }
                    val calc = Calendar.getInstance()
                    val mYear = calc.get(Calendar.YEAR)
                    val mDay = calc.get(Calendar.DAY_OF_MONTH)
                    val mMonth = calc.get(Calendar.MONTH)
                    KotlinUtils.setDate(this, this, mCurrentDate, 1)

                }


            }
            R.id.rlEndDate, R.id.rlEndDateVenue -> {
                if (isEdit) {
                    Utils.showToast(this, "Can't edit date")
                } else {
                    getTimmingsValue()



                    if (mFromDateArtist.equals("") || mFromDateArtist == null) {

                        showToast(resources.getString(R.string.err_event_start_date))
                    } else {

                        if (view.id == R.id.rlEndDate) {
                            DATE_TIME_FROM = TO_DATE

                        } else {
                            DATE_TIME_FROM = TO_DATE_VENUE
                        }
                        KotlinUtils.setDate(this, this, KotlinUtils.getDate(mFromDateArtistCal), 2)
                    }
                }


            }
            R.id.rlStartTime, R.id.rlStartTimeVenue -> {
                if (isEdit) {
                    Utils.showToast(this, "Can't edit time")
                } else {

                    getTimmingsValue()

                    if (mFromDateArtist.equals("") || mFromDateArtist == null) {
                        showToast(resources.getString(R.string.err_event_start_date))
                    } else {
                        if (view.id == R.id.rlStartTime) {
                            DATE_TIME_FROM = FROM_TIME
                        } else {
                            DATE_TIME_FROM = FROM_TIME_VENUE
                        }
                        val isEqual = KotlinUtils.compareCurrentDate(mFromDateArtistCal)
                        if (isEqual) {
                            val (mHour, mMinute) = KotlinUtils.getCurrentTime()
                            KotlinUtils.setTime(this, this, fragmentManager, mHour)
                        } else {
                            KotlinUtils.setTime(this, this, fragmentManager, 12)

                        }


                    }

                }


            }
            R.id.rlEndTime, R.id.rlEndTimeVenue -> {
                if (isEdit) {
                    Utils.showToast(this, "Can't edit time")
                } else {


                    getTimmingsValue()

                    if (mToDateArtist.equals("") || mToDateArtist == null) {
                        showToast(resources.getString(R.string.err_event_end_time))
                    } else {

                        if (view.id == R.id.rlEndTime) {
                            DATE_TIME_FROM = TO_TIME
                        } else {
                            DATE_TIME_FROM = TO_TIME_VENUE
                        }

                        val mHourString = mFromTimeArtist.split(":")[0]
                        val mHourInt = mHourString.toInt() + 1
                        KotlinUtils.setTime(this, this, fragmentManager, mHourInt)


                    }
                }


            }


            R.id.rlBudgetGuestCurrency -> {
                var list = ArrayList<String>()

                for ((index, value) in newList.withIndex()) {
                    list.add(newList.get(index).currencyType)
                }
                currencyArray = list.toTypedArray()


                if (currencyArray != null) {
                    showDropdown(currencyArray, tvCurrency, SpinnerData { mId, mName ->
                        mCurrency = mName
                        mCurrencyId = mId
                        tvCurrency.text = mCurrency
                    }, mWidthCode)
                }

            }
            R.id.tvSubmit -> {


                getEventsId()
                val mEventName = etEventName.text.toString()
                val mDescription = etEventDescription.text.toString()

                mFromDateArtist = tvStartDate.text.toString()
                mToDateArtist = tvEndDate.text.toString()
                mFromTimeArtist = tvStartTime.text.toString()
                mToTimeArtist = tvEndTime.text.toString()
                mZipCode = etZipCodeCreateEvent.text.toString()
                mAddress = etAddressCreateEvent.text.toString()
                val mAttendence = etAttendence.text.toString()
                val mCurrency = tvCurrency.text.toString()
                val mBudgetGuest = tvBudgetPerGuest.text.toString()
                if (KotlinUtils.checkEmpty(mEventName)) {
                    showToast(resources.getString(R.string.err_event_name))
                } else if (KotlinUtils.checkEmpty(mDescription)) {
                    showToast(resources.getString(R.string.err_event_desc))

                } else if (KotlinUtils.checkEmpty(mEventTypeId)) {
                    showToast(resources.getString(R.string.err_event_type))

                } else if (KotlinUtils.checkEmpty(mFromDateArtist)) {
                    showToast(resources.getString(R.string.err_event_start_date))
                } else if (KotlinUtils.checkEmpty(mToDateArtist)) {
                    showToast(resources.getString(R.string.err_event_end_date))

                } else if (KotlinUtils.checkEmpty(mFromTimeArtist)) {
                    showToast(resources.getString(R.string.err_event_start_time))
                } else if (KotlinUtils.checkEmpty(mToTimeArtist)) {
                    showToast(resources.getString(R.string.err_event_end_time))

                } else if (KotlinUtils.checkEmpty(mAttendence)) {
                    showToast(resources.getString(R.string.err_event_guest_count))
                } else if (mBudgetGuest.equals("0")) {
                    showToast(resources.getString(R.string.err_event_guest_budget_0))

                } else if (KotlinUtils.checkEmpty(mCountryId)) {
                    showToast(resources.getString(R.string.err_country))
                } else if (KotlinUtils.checkEmpty(mStateId)) {
                    showToast(resources.getString(R.string.err_state))
                } else if (KotlinUtils.checkEmpty(mCityId)) {
                    showToast(resources.getString(R.string.err_city))
                } else if (KotlinUtils.checkEmpty(mZipCode)) {
                    showToast(resources.getString(R.string.err_zip_code))
                } else {


                    val (mDate1, mDate2) = KotlinUtils.createEvent(mFromDateArtist, mToDateArtist)

                    val obj = JSONObject();
                    obj.put("EventId", mEventId)
                    obj.put("EventTitle", mEventName)
                    obj.put("EventDescription", mDescription)
                    obj.put("EventTypeId", mEventTypeId)

                    obj.put("EventDateFrom", mDate1)
                    obj.put("EventDateTo", mDate2)
                    obj.put("EventTimeFrom", mFromTimeVenue)
                    obj.put("EventTimeTo", mToTimeVenue)
                    obj.put("EventGatheringCapacity", mAttendence)
                    obj.put("EventChargesPayCurrencyId", mCurrencyId)
                    obj.put("EventBudget", mBudgetGuest)
                    obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
                    obj.put("EventCity", mCityId)
                    obj.put("EventState", mStateId)
                    obj.put("EventCountry", mCountryId)
                    obj.put("EventAddress", mAddress)
                    obj.put("EventPostalCode", mZipCode)
                    obj.put("ArtistBookingFromTime", mFromTimeArtist)
                    obj.put("ArtistBookingToTime", mToTimeArtist)
                    obj.put("VenueBookingFromTime", mFromTimeVenue)
                    obj.put("VenueBookingToTime", mToTimeVenue)



                    getAPI(FOR_EVENT_CREATE_UPDATE, obj.toString())

                }
            }
            R.id.tvCountryCreateEvent -> {
                showDropdown(arrCountryName, tvCountryCreateEvent, SpinnerData { mId, mName ->
                    mCountryId = mId
                    tvCountryCreateEvent.text = mName
                    mStateId = ""
                    mCityId = ""
                    tvStateCreateEvent.text = ""
                    tvCityCreateEvent.text = ""
                    alCity.clear()
                    alCityName.clear()
                    stateAL.clear()
                    stateNameAL.clear()

                    callStateAPI()

                }, 500)
            }
            R.id.tvStateCreateEvent -> {
                if (mCountryId.equals("")) {
                    Utils.showToast(this, "Please select country first")
                } else {
                    showDropdown(arrStateName, tvStateCreateEvent, SpinnerData { mId, mName ->
                        mStateId = mId
                        tvStateCreateEvent.text = mName
                        alCity.clear()
                        alCityName.clear()

                        mCityId = ""
                        tvCityCreateEvent.text = ""

                        callCityAPI()

                    }, 500)
                }
            }

            R.id.tvCityCreateEvent ->
                if (!mStateId.equals("")) {
                    showDropdown(arrCityName, tvCityCreateEvent, SpinnerData { mId, mName ->
                        mCityId = mId
                        tvCityCreateEvent.text = mName
                    }, 500)
                } else {
                    Utils.showToast(this, resources.getString(R.string.err_state))
                }

        }
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

    fun showToast(value: String) {
        Utils.showToast(this, value.toString())
    }


    override fun returnDateTime(mDateTime: String) {
        if (DATE_TIME_FROM == FROM_DATE || DATE_TIME_FROM == FROM_DATE_VENUE) {
            mFromDateArtistCal = mDateTime
            if (DATE_TIME_FROM == FROM_DATE)
                tvStartDate.text = KotlinUtils.monthToReadFormat(mDateTime)
            else
                tvStartDateVenue.text = KotlinUtils.monthToReadFormat(mDateTime)


        } else if (DATE_TIME_FROM == TO_DATE || DATE_TIME_FROM == TO_DATE_VENUE) {
            if (DATE_TIME_FROM == TO_DATE)
                tvEndDate.text = KotlinUtils.monthToReadFormat(mDateTime)
            else
                tvEndDateVenue.text = KotlinUtils.monthToReadFormat(mDateTime)


        } else if (DATE_TIME_FROM == FROM_TIME || DATE_TIME_FROM == FROM_TIME_VENUE) {

            if (DATE_TIME_FROM == FROM_TIME)
                tvStartTime.text = mDateTime
            else
                tvStartTimeVenue.text = mDateTime
        } else if (DATE_TIME_FROM == TO_TIME || DATE_TIME_FROM == TO_TIME_VENUE) {
            if (DATE_TIME_FROM == TO_TIME)
                tvEndTime.text = mDateTime
            else
                tvEndTimeVenue.text = mDateTime

        }
    }


    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            FOR_EVENT_TYPE_LIST -> {


                val jsonArray = JSONArray(response.toString())
                val gson = Gson()
                val listType = object : TypeToken<ArrayList<EventModel>>() {}.type
                eventsList = gson.fromJson<ArrayList<EventModel>>(jsonArray.toString(), listType)
                recyclerEvent.adapter = EventAdapter(this, eventsList)

                getAPI(FOR_COUNTRIES_LIST, "")
            }
            FOR_CURRENCY_LIST -> {
                val jsonArray = JSONArray(response.toString())
                val gson = Gson()
                val listType = object : TypeToken<ArrayList<CurrencyModel>>() {}.type
                newList = gson.fromJson<ArrayList<CurrencyModel>>(jsonArray.toString(), listType)

                if (!mEventId.equals(""))
                    getAPI(FOR_EVENT_DETAIL, "");
            }
            FOR_EVENT_DETAIL -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    val array = json.getJSONArray("result")
                    val jsonInner = array.getJSONObject(0)



                    mCurrencyId=jsonInner.getString("EventChargesPayCurrencyId")
                    mCountryId=jsonInner.getString("EventCountryId")
                    mStateId=jsonInner.getString("EventStateId")
                    mCityId=jsonInner.getString("EventCityId")


                    etEventName.setText(jsonInner.getString("EventTitle"))
                    etEventDescription.setText(jsonInner.getString("EventDescription"))
                    val arrayEvent = jsonInner.getString("EventTypeId").split(",")
                    for (eventId in arrayEvent) {
                        for (eventListAvailable in eventsList) {
                            if (eventListAvailable.id.equals(eventId)) {
                                eventListAvailable.isSelected = true
                                break
                                // eventsList.add(eventListAvailable)
                            }
                        }
                    }
                    recyclerEvent.adapter = EventAdapter(this, eventsList)




                    etAttendence.setText(jsonInner.getString("EventEstimatedGuest"))
                    val (mFromDateArtist, mToDateArtist) = KotlinUtils.dateFormatToReceive(jsonInner.getString("EventDateFrom"), jsonInner.getString("EventDateTo"))

                    tvStartDate.text = mFromDateArtist
                    tvEndDate.text = mToDateArtist
                    tvStartTime.text = jsonInner.getString("ArtistBookingFromTime")
                    tvEndTime.setText(jsonInner.getString("ArtistBookingToTime"))


                    tvStartDateVenue.text = mFromDateArtist
                    tvEndDateVenue.text = mToDateArtist
                    tvStartTimeVenue.text = jsonInner.getString("VenueBookingFromTime1")
                    tvEndTimeVenue.text = jsonInner.getString("VenueBookingToTime1")
                    etAttendence.setText(jsonInner.getString("EventEstimatedGuest"))


                    tvCurrency.text=jsonInner.getString("EventChargesPayCurrency")
                    tvCountryCreateEvent.text=jsonInner.getString("EventCountryName")
                    tvStateCreateEvent.text=jsonInner.getString("EventStateName")
                    tvCityCreateEvent.text=jsonInner.getString("EventCityName")
                    etAddressCreateEvent.setText(jsonInner.getString("EventAddress"))
                    etZipCodeCreateEvent.setText(jsonInner.getString("EventPostalCode"))


                    tvBudgetPerGuest.setText(jsonInner.getString("EventBudget"))
                    val budget = jsonInner.getString("EventBudget").toFloat()
                    //seekBarPrice.setMinValue(budget)



                    if (!jsonInner.getString("EventPromotionImg").equals("")) {
                        Glide.with(this).load(jsonInner.getString("EventPromotionImgPath") + jsonInner.getString("EventPromotionImg")).into(ivProfile)

                    } else {
                        Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfile)

                    }

                }

            }
            FOR_SAVE_UPDATE_EVENT_DETAIL -> {
                val json = JSONObject(response.toString())

                if (json.getString("Status").equals("Success")) {

                    mEventId = json.getString("EventId")
                    Utils.showToast(this, "Event Added successfully")

                    if (Utils.isNetworkConnected(this)) {

                        if (uploadFile != null) {
                            Utils.initializeAndShow(this)
                            val mEventIds = RequestBody.create(MediaType.parse("text/plain"), mEventId)
                            ImageUploadClass.imageUpload(uploadFile, mEventIds, null, FOR_UPLOAD_EVENT_PROFILE_IMAGE, this)

                        } else {
                            Utils.showToast(this, json.getString("Message"))
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Utils.showToast(this, resources.getString(R.string.err_no_internet))
                    }


                } else {
                    Utils.showToast(this, json.getString("Message"))

                }


            }
            FOR_UPLOAD_EVENT_PROFILE_IMAGE -> {
                val jsonObj = JSONObject(response.toString())
                if (jsonObj.getString("Status").equals("Success", true)) {
                    Utils.showToast(this, "Image uploaded successfully")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            FOR_COUNTRIES_LIST -> {
                try {
                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val model = CountryModel()
                        model.countryId = jsonObject.getString("CountryId")
                        model.countryName = jsonObject.getString("CountryName")
                        model.countryCode = jsonObject.getString("CountryCode")

                        countryNameAL.add(jsonObject.getString("CountryName"))
                        countryAL.add(model)
                    }

                    arrCountryName = countryNameAL.toTypedArray()

                    getAPI(FOR_CURRENCY_LIST, "")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            FOR_STATE_LIST -> {
                try {
                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val model = StateModel()
                        model.countryId = jsonObject.getString("CountryId")
                        model.stateId = jsonObject.getString("StateId")
                        model.stateName = jsonObject.getString("StateName")

                        stateNameAL.add(jsonObject.getString("StateName"))
                        stateAL.add(model)
                    }

                    arrStateName = stateNameAL.toTypedArray()
                    if (!mStateId.equals("", ignoreCase = true)) {
                        callCityAPI()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            Constants.FOR_CITY_LIST -> try {
                val jsonArray = JSONArray(response.toString())
                var i = 0
                while (i < jsonArray.length()) {
                    val jsonObjectState = jsonArray.getJSONObject(i)
                    val model = CityModel()
                    model.cityName = jsonObjectState.getString("CityName")
                    model.cityId = jsonObjectState.getString("CityId")
                    alCity.add(model)
                    alCityName.add(jsonObjectState.getString("CityName"))
                    i++
                }
                arrCityName = alCityName.toTypedArray()
            } catch (e: Exception) {
            }
        }
    }

    fun showDropdown(array: Array<String>, textView: TextView, spinnerData: SpinnerData, width: Int) {
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
            if (textView.id == R.id.tvCurrency) {
                spinnerData.getData(newList.get(position).id, newList.get(position).currency)
            } else if (textView.id == com.musicseque.R.id.tvCountryCreateEvent) {
                spinnerData.getData(countryAL.get(position).countryId, array[position])

            } else if (textView.id == com.musicseque.R.id.tvStateCreateEvent) {
                spinnerData.getData(stateAL.get(position).stateId, array[position])
            } else if (textView.id == com.musicseque.R.id.tvCityCreateEvent) {
                spinnerData.getData(alCity.get(position).cityId, array[position])
            }
            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
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
            getAPI(Constants.FOR_CITY_LIST, jsonObject1.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun callStateAPI() {
        try {
            val jsonObject1 = JSONObject()
            jsonObject1.put("CountryId", mCountryId)
            getAPI(Constants.FOR_STATE_LIST, jsonObject1.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun getTimmingsValue() {
        mFromDateArtist = tvStartDate.text.toString()
        mToDateArtist = tvEndDate.text.toString()
        mFromTimeArtist = tvStartTime.text.toString()
        mToTimeArtist = tvEndTime.text.toString()
        mFromDateVenue = tvStartDate.text.toString()
        mToDateVenue = tvEndDate.text.toString()
        mFromTimeVenue = tvStartTime.text.toString()
        mToTimeVenue = tvEndTime.text.toString()
    }
}