package com.musicseque.event_manager.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.activities.BaseActivity
import com.musicseque.event_manager.adapter.EventAdapter
import com.musicseque.event_manager.model.CurrencyModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants.*
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_create_event.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import com.musicseque.interfaces.DateTimeInterface
import com.musicseque.utilities.Constants
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.event_manager.model.EventModel
import com.musicseque.fragments.HomeFragment
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.utilities.SharedPref
import kotlinx.android.synthetic.main.row_event_list.view.*
import kotlinx.android.synthetic.main.toolbar_top.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class CreateEventActivity : BaseActivity(), View.OnClickListener, MyInterface, DateTimeInterface {


    var uploadFile: MultipartBody.Part? = null
    private lateinit var eventsList: ArrayList<EventModel>
    private var mAttendenceCount: String? = null
    lateinit private var newList: ArrayList<CurrencyModel>
    lateinit var arrGuestCount: Array<String?>
    private var mCurrencyId: String? = ""
    private var mCurrency: String? = ""
    var mWidthCode: Int = 0
    lateinit private var listPopupWindow: ListPopupWindow
    lateinit var eventArray: Array<EventModel>
    var mEventId: String = ""
    var DATE_TIME_FROM: Int? = null

    val FROM_DATE = 1
    val TO_DATE = 2;
    val FROM_TIME = 3;
    val TO_TIME = 4

    var mFromDate: String = ""
    var mToDate: String = ""
    var mFromTime: String = ""
    var mToTime: String = ""
    var mEventTypeId: String = ""
    var isEdit=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        initViews()
        listeners()
        getAPI("events", "")
    }

    private fun initViews() {
        arrGuestCount = arrayOf("0-50", "51-100", "101-200", "201-300", "300+")
        tv_title.text = "Create Event"
        img_right_icon.visibility = View.GONE




        recyclerEvent?.layoutManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager?
        try {
            mEventId = intent.getStringExtra("event_id")
            isEdit=intent.getBooleanExtra("isEdit",false)
            if(isEdit)
                tv_title.text = "Edit Event"
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
        // rlAttendence.setOnClickListener(this)
        rlBudgetGuestCurrency.setOnClickListener(this)
        tvSubmit.setOnClickListener(this)
    }

    private fun getAPI(value: String, args: String) {

        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this)
            if (value.equals("events")) {
                KotlinHitAPI.callGetAPI(FOR_EVENT_TYPE_LIST, this)
            } else if (value.equals("currency")) {
                KotlinHitAPI.callGetAPI(FOR_CURRENCY_LIST, this)
            } else if (value.equals("submit")) {
                KotlinHitAPI.callAPI(args, FOR_SAVE_UPDATE_EVENT_DETAIL, this)
            } else if (value.equals("event_detail")) {
                val obj = JSONObject()
                obj.put("EventId", mEventId)
                KotlinHitAPI.callAPI(obj.toString(), FOR_EVENT_DETAIL, this)
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


            R.id.rlStartDate -> {
                DATE_TIME_FROM = FROM_DATE
                KotlinUtils.setDate(this, this)

            }
            R.id.rlEndDate -> {
                mFromDate = tvStartDate.text.toString()
                DATE_TIME_FROM = TO_DATE
                if (mFromDate.equals("") || mFromDate == null) {

                    showToast(resources.getString(R.string.err_event_start_date))
                } else {
                    KotlinUtils.setDate(this, this)
                }


            }
            R.id.rlStartTime -> {
                DATE_TIME_FROM = FROM_TIME
                mFromDate = tvStartDate.text.toString()
                if (mFromDate.equals("") || mFromDate == null) {
                    showToast(resources.getString(R.string.err_event_start_date))
                } else {
                    KotlinUtils.setTime(this, this)
                }


            }
            R.id.rlEndTime -> {
                DATE_TIME_FROM = TO_TIME

                mToDate = tvEndDate.text.toString()
                if (mToDate.equals("") || mToDate == null) {
                    showToast(resources.getString(R.string.err_event_end_time))
                } else {
                    KotlinUtils.setTime(this, this)
                }


            }


//            R.id.rlAttendence -> {
//
//                showDropdown(arrGuestCount, Attendence, SpinnerData { mData, mData1 ->
//                    mAttendenceCount = mData
//                    Attendence.text = mAttendenceCount
//                }, mWidthCode)
//            }
            R.id.rlBudgetGuestCurrency -> {
                var list = ArrayList<String>()
                // var items: Array<String> = arrayOf()

                for ((index, value) in newList.withIndex()) {
                    list.add(newList.get(index).currencyType)
                }
                val currencyArray = arrayOfNulls<String>(list.size)
                list.toArray(currencyArray)

                if (currencyArray != null) {
                    showDropdown(currencyArray, tvCurrency, SpinnerData { mData, mData1 ->
                        mCurrency = mData
                        mCurrencyId = mData1
                        tvCurrency.text = mCurrency
                    }, mWidthCode)
                }

            }
            R.id.tvSubmit -> {


                getEventsId()
                val mEventName = etEventName.text.toString()
                val mDescription = etEventDescription.text.toString()

                mFromDate = tvStartDate.text.toString()
                mToDate = tvEndDate.text.toString()
                mFromTime = tvStartTime.text.toString()
                mToTime = tvEndTime.text.toString()

                val mAttendence = etAttendence.text.toString()
                val mCurrency = tvCurrency.text.toString()
                val mBudgetGuest = tvBudgetPerGuest.text.toString()
                if (KotlinUtils.checkEmpty(mEventName)) {
                    showToast(resources.getString(R.string.err_event_name))
                } else if (KotlinUtils.checkEmpty(mDescription)) {
                    showToast(resources.getString(R.string.err_event_desc))

                } else if (KotlinUtils.checkEmpty(mEventTypeId)) {
                    showToast(resources.getString(R.string.err_event_type))

                } else if (KotlinUtils.checkEmpty(mFromDate)) {
                    showToast(resources.getString(R.string.err_event_start_date))
                } else if (KotlinUtils.checkEmpty(mToDate)) {
                    showToast(resources.getString(R.string.err_event_end_date))

                } else if (KotlinUtils.checkEmpty(mFromTime)) {
                    showToast(resources.getString(R.string.err_event_start_time))
                } else if (KotlinUtils.checkEmpty(mToTime)) {
                    showToast(resources.getString(R.string.err_event_end_time))

                } else if (KotlinUtils.checkEmpty(mAttendence)) {
                    showToast(resources.getString(R.string.err_event_guest_count))
                } else if (mBudgetGuest.equals("0")) {
                    showToast(resources.getString(R.string.err_event_guest_budget_0))

                } else {


                    val (mDate1, mDate2) = KotlinUtils.dateFormatToSend(mFromDate, mToDate)

                    val obj = JSONObject();
                    obj.put("EventId", mEventId)
                    obj.put("EventTitle", mEventName)
                    obj.put("EventDescription", mDescription)
                    obj.put("EventTypeId", mEventTypeId)

                    obj.put("EventDateFrom", mDate1)
                    obj.put("EventDateTo", mDate2)
                    obj.put("EventTimeFrom", mFromTime)
                    obj.put("EventTimeTo", mToTime)
                    obj.put("EventGatheringCapacity", mAttendence)
                    obj.put("EventChargesPayCurrencyId", mCurrencyId)
                    obj.put("EventBudget", mBudgetGuest)
                    obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
                    getAPI("submit", obj.toString())

                }
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
        if (DATE_TIME_FROM == FROM_DATE) {


            if (KotlinUtils.compareCurrentDate(mDateTime))
                tvStartDate.text = KotlinUtils.monthToReadFormat(mDateTime)
            else
                Utils.showToast(this, resources.getString(R.string.err_current_date))
        } else if (DATE_TIME_FROM == TO_DATE) {

            val newFormat = SimpleDateFormat("dd/MM/yyyy")
            val oldFormat = SimpleDateFormat("dd/MMM/yyyy")


            val isOK = KotlinUtils.compareDates(newFormat.format(oldFormat.parse(mFromDate)), mDateTime)
            if (isOK) {
                tvEndDate.text = KotlinUtils.monthToReadFormat(mDateTime)
            } else {
                showToast(resources.getString(R.string.err_to_date_before))
            }


        } else if (DATE_TIME_FROM == FROM_TIME) {

            tvStartTime.text = mDateTime

        } else if (DATE_TIME_FROM == TO_TIME) {
            mFromTime = tvStartTime.text.toString()
            mFromDate = tvStartDate.text.toString()
            mToDate = tvEndDate.text.toString()
            val newFormat = SimpleDateFormat("dd/MM/yyyy")
            val oldFormat = SimpleDateFormat("dd/MMM/yyyy")
            val isDateEqual = KotlinUtils.isDateEqual(newFormat.format(oldFormat.parse(mFromDate)), newFormat.format(oldFormat.parse(mToDate)))

            if (isDateEqual) {
                val isOK = KotlinUtils.compareTimes(mFromTime, mDateTime)
                if (isOK) {
                    tvEndTime.text = mDateTime
                } else {
                    showToast(resources.getString(R.string.err_to_time_before))
                }
            } else {
                tvEndTime.text = mDateTime
            }


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
                getAPI("currency", "")
            }
            FOR_CURRENCY_LIST -> {
                val jsonArray = JSONArray(response.toString())
                val gson = Gson()
                val listType = object : TypeToken<ArrayList<CurrencyModel>>() {}.type
                newList = gson.fromJson<ArrayList<CurrencyModel>>(jsonArray.toString(), listType)

                if (!mEventId.equals(""))
                    getAPI("event_detail", "");
            }
            FOR_EVENT_DETAIL -> {
                val json = JSONObject(response.toString())
                if (json.getString("Status").equals("Success", true)) {
                    val array = json.getJSONArray("result")
                    val jsonInner = array.getJSONObject(0)
                    etEventName.setText(jsonInner.getString("EventTitle"))
                    etEventDescription.setText(jsonInner.getString("EventDescription"))
                    etAttendence.setText(jsonInner.getString("EventEstimatedGuest"))
                    if(jsonInner.getString("VenueName").equals(""))
                    {
                        val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(jsonInner.getString("EventDateFrom"), jsonInner.getString("EventDateTo"))

                        tvStartDate.setText(mFromDate)
                        tvEndDate.text = mToDate
                        tvStartTime.setText(jsonInner.getString("EventTimeFrom"))
                        tvEndTime.setText(jsonInner.getString("EventTimeTo"))
                    }
                    else
                    {
                        val (mFromDate, mToDate) = KotlinUtils.dateFormatToReceive(jsonInner.getString("VenueBookedFromDate"), jsonInner.getString("VenueBookedToDate"))

                        tvStartDate.setText(mFromDate)
                        tvEndDate.text = mToDate
                        tvStartTime.setText(jsonInner.getString("VenueBookingFromTime"))
                        tvEndTime.setText(jsonInner.getString("VenueBookingToTime"))
                    }

                    tvBudgetPerGuest.setText(jsonInner.getString("EventBudget"))
                    val budget = jsonInner.getString("EventBudget").toFloat()
                    //seekBarPrice.setMinValue(budget)
                    mCurrency = jsonInner.getString("EventChargesPayCurrency")
                    mCurrencyId = jsonInner.getString("EventChargesPayCurrencyId")
                    tvCurrency.setText(mCurrency)

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
                    Log.e("", "")
                    if (!jsonInner.getString("EventPromotionImg").equals("")) {
                        Glide.with(this).load(jsonInner.getString("EventPromotionImgPath") + jsonInner.getString("EventPromotionImg")).into(ivProfile)

                    }
                    else{
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
                    }

                    else {
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
        }
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
                spinnerData.getData(newList.get(position).currency, newList.get(position).id)
            } else if (textView.id == com.musicseque.R.id.etAttendence) {
                spinnerData.getData(array[position], "")

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
            uploadFile=null
        }


    }


}