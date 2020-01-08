package com.musicseque.artist.fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable

import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.artist.service.CommonService
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.models.CityModel
import com.musicseque.models.CountryModel
import com.musicseque.models.GenreModel
import com.musicseque.models.StateModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.File
import java.util.*

class ProfileFragment : KotlinBaseFragment(), View.OnClickListener, MyInterface {

    var dialog: Dialog? = null
    var mGenreId = ""
    var mCountryId = ""
    private var v: View? = null
    var genreAL = ArrayList<GenreModel>()
    var genreNameAL = ArrayList<String>()
    var countryAL = ArrayList<CountryModel>()
    var countryNameAL = ArrayList<String>()
    var countryCodeAL = ArrayList<String>()
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var arrCountryCode: Array<String>
    lateinit var arrGenre: Array<String>
    lateinit var arrExperience: Array<String>
    lateinit var arrExpertise: Array<String>
    lateinit private var arrGrade: Array<String>
    lateinit private var arrCertification: Array<String>
    var mCount = 0
    lateinit var arrStateName: Array<String>
    var alState = ArrayList<StateModel>()
    var alStateName = ArrayList<String>()
    var mStateName = ""
    var mStateId = ""
    lateinit var arrCityName: Array<String>
    var alCity = ArrayList<CityModel>()
    var alCityName = ArrayList<String>()
    var mCityName = ""
    var mCityId = ""
    var mCountryCode = ""
    var mExperience = ""
    var mGenre = ""
    var mExpertise = ""
    var mGrade = ""
    var mCertification = ""
    var mCountryName = ""
    var mMobileNumber = ""
    var mExperienceId = ""
    var mAddress = ""
    var mWidthExp = 0
    var mWidthCode = 0
    var mWidth = 0
    var STATUS_ACTIVE = "Available"
    var STATUS_INVISIBLE = "Offline"
    var img_right_icon: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_profile, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initOtherViews()
        initViews()
        listeners()
        showDefaultData()
        hitAPIs(Constants.FOR_GENRE_LIST, "")
    }

    private fun initOtherViews() {
        arrExperience = arrayOf("1 - 2 years", "3 - 5 years", "5 - 10 years", "10+ years")
        arrExpertise = arrayOf("Lead Vocals", "Backing Vocals", "Songwriter", "Piano", "Synthesizer", "Drums", "Band", "Lead Guitar", "Bass Guitar", "Violin", "Harp", "Sitar", "Flute", "Clarinet", "Saxophone", "Trumpet")
        arrGrade = arrayOf("Prep", "Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5", "Grade 6", "Grade 7", "Grade 8", "Diploma", "Foundation", "Intermediate", "Advanced")
        arrCertification = arrayOf("ABSRM", "Trinity", "Rock n Pop", "Gigajam", "Others")
        img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        img_right_icon!!.visibility = View.GONE
        val tvDone = (activity as MainActivity?)!!.findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE

    }

    private fun initViews() {

//        tvCountryCode!!.viewTreeObserver.addOnGlobalLayoutListener { mWidthCode = tvCountryCode!!.measuredWidth }
//        tvExperience!!.viewTreeObserver.addOnGlobalLayoutListener { mWidthExp = tvExperience!!.measuredWidth }
        mWidthCode = 200
        mWidthExp = 500
        val tv_title = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as TextView
        tv_title!!.text = "Artist Profile"
        etDesc!!.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun listeners() {
        btn_submit!!.setOnClickListener(this)
        ivCamera!!.setOnClickListener(this)
        ivStatus!!.setOnClickListener(this)
        tvCountryCode!!.setOnClickListener(this)
        tvExperience!!.setOnClickListener(this)
        tvGenre!!.setOnClickListener(this)
        tvGrade!!.setOnClickListener(this)
        tvExpertise!!.setOnClickListener(this)
        tvCertification!!.setOnClickListener(this)
        tvState!!.setOnClickListener(this)
        tvCity!!.setOnClickListener(this)
        etDesc!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                tvCount!!.text = editable.length.toString() + "/500"
                mCount = editable.length
            }
        })
    }

    private fun showDefaultData() {
        etEmail!!.setText(SharedPref.getString(Constants.EMAIL_ID, ""))
        tvUserName!!.text = SharedPref.getString(Constants.USER_NAME, "")
        tvProfileType!!.text = SharedPref.getString(Constants.PROFILE_TYPE, "")
        if (SharedPref.getString(Constants.VISIBILITY_STATUS, STATUS_ACTIVE).equals(STATUS_ACTIVE, ignoreCase = true)) {
            ivStatus!!.setImageDrawable(resources.getDrawable(R.drawable.circle_green))
        } else if (SharedPref.getString(Constants.VISIBILITY_STATUS, STATUS_ACTIVE).equals(STATUS_INVISIBLE, ignoreCase = true)) {
            ivStatus!!.setImageDrawable(resources.getDrawable(R.drawable.circle_transparent))
        } else {
            ivStatus!!.setImageDrawable(resources.getDrawable(R.drawable.circle_red))
        }
        mCountryCode = SharedPref.getString(Constants.COUNTRY_CODE, "")
        mCountryId = SharedPref.getString(Constants.COUNTRY_ID, "")
        mCountryName = SharedPref.getString(Constants.COUNTRY_NAME, "")
        mMobileNumber = SharedPref.getString(Constants.MOBILE_NUMBER, "")
        tvCountryCode!!.text = mCountryCode
        etMobileNumber!!.setText(mMobileNumber)
        tvCountry!!.text = mCountryName
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivCamera -> checkPermissions("image", "com.musicseque.artist.fragments.ProfileFragment", this)

            R.id.btn_submit -> apiUpdateProfile()
            R.id.tvCountryCode -> showDropdown(arrCountryCode, tvCountryCode, SpinnerData { mId, mName ->
                mCountryId = mId
                mCountryName = mName
                tvCountry!!.text = mName
                mStateId = ""
                mCityId = ""
                tvState.text = ""
                tvCity.text = ""
                alCity.clear()
                alCityName.clear()
                alState.clear()
                alStateName.clear()

                callStateAPI()
            }, mWidthCode)
            R.id.tvGenre -> showDropdown(arrGenre, tvGenre, SpinnerData { mId, mName ->
                mGenre = mName
                mGenreId = mId
            }, mWidthExp)
            R.id.tvExpertise -> showDropdown(arrExpertise, tvExpertise, SpinnerData { mId, mName -> mExpertise = mName }, mWidthExp)
            R.id.tvExperience -> showDropdown(arrExperience, tvExperience, SpinnerData { mId, mName ->
                mExperience = mName
                mExperienceId = mId
            }, mWidthExp)
            R.id.tvCertification -> showDropdown(arrCertification, tvCertification, SpinnerData { mId, mName -> mCertification = mName }, mWidthExp)
            R.id.tvGrade -> showDropdown(arrGrade, tvGrade, SpinnerData { mId, mName -> mGrade = mName }, mWidthExp)
            R.id.tvState -> showDropdown(arrStateName, tvState, SpinnerData { mId, mName ->
                mStateId = mId
                mStateName = mName
                tvState.text = mName
                alCity.clear()
                alCityName.clear()

                mCityId = ""
                tvCity.text = ""

                callCityAPI()
            }, mWidthExp)



            R.id.tvCity ->
                if (!mStateId.equals("")) {
                    showDropdown(arrCityName, tvCity, SpinnerData { mId, mName ->
                        mCityId = mId
                        mCityName = mName
                        tvCity.text = mName
                    }, mWidthExp)
                } else {
                    Utils.showToast(activity, resources.getString(R.string.err_state))
                }

            R.id.ivStatus -> {
                val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val customView = layoutInflater.inflate(R.layout.dropdown_user_status, null)
                val tvAvailable = customView.findViewById<View>(R.id.tvAvailable) as TextView
                val tvDoNot = customView.findViewById<View>(R.id.tvDoNot) as TextView
                val tvInvisible = customView.findViewById<View>(R.id.tvInvisible) as TextView
                //instantiate popup window
                val popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                popupWindow.isOutsideTouchable = true
                popupWindow.showAtLocation(ivStatus, Gravity.TOP or Gravity.LEFT, locateView()!!.left, locateView()!!.left)
                tvAvailable.setOnClickListener {
                    ivStatus!!.setImageDrawable(resources.getDrawable(R.drawable.circle_green))
                    SharedPref.putString(Constants.VISIBILITY_STATUS, STATUS_ACTIVE)
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                        jsonObject.put("NewStatus", "Available")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    activity!!.startService(Intent(activity, CommonService::class.java).putExtra("API", Constants.UPDATE_STATUS_API).putExtra("params", jsonObject.toString()))
                    popupWindow.dismiss()
                }
                tvDoNot.setOnClickListener {
                    ivStatus!!.setImageDrawable(resources.getDrawable(R.drawable.circle_red))
                    SharedPref.putString(Constants.VISIBILITY_STATUS, STATUS_DO_NOT_DISTURB)
                    popupWindow.dismiss()
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                        jsonObject.put("NewStatus", "Do_not_disturb")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    activity!!.startService(Intent(activity, CommonService::class.java).putExtra("API", Constants.UPDATE_STATUS_API).putExtra("params", jsonObject.toString()))
                }
                tvInvisible.setOnClickListener {
                    ivStatus!!.setImageDrawable(resources.getDrawable(R.drawable.circle_transparent))
                    SharedPref.putString(Constants.VISIBILITY_STATUS, STATUS_INVISIBLE)
                    popupWindow.dismiss()
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                        jsonObject.put("NewStatus", "Offline")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    activity!!.startService(Intent(activity, CommonService::class.java).putExtra("API", Constants.UPDATE_STATUS_API).putExtra("params", jsonObject.toString()))
                }
            }
        }
    }

    private fun hitAPIs(type: Int, args: String) {
        if (Utils.isNetworkConnected(activity)) {
            initializeLoader()
            if (type == Constants.FOR_GENRE_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_GENRE_LIST, this@ProfileFragment)
            } else if (type == Constants.FOR_COUNTRIES_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this@ProfileFragment)
            } else if (type == Constants.FOR_STATE_LIST) {
                RetrofitAPI.callAPI(args, Constants.FOR_STATE_LIST, this@ProfileFragment)
            } else if (type == Constants.FOR_CITY_LIST) {
                RetrofitAPI.callAPI(args, Constants.FOR_CITY_LIST, this@ProfileFragment)
            } else if (type == Constants.FOR_USER_PROFILE) {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this@ProfileFragment)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else if (type == Constants.FOR_UPDATE_PROFILE) {
                RetrofitAPI.callAPI(args, Constants.FOR_UPDATE_PROFILE, this@ProfileFragment)
            }
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }
    }


    fun apiUpdateProfile() {
        var requestBody = ""
        if (Utils.isNetworkConnected(activity)) {
            val jsonBody = JSONObject()
            var mEmail = ""
            val mCity: String
            var mPostCode = ""
            var mBio = ""
            var mWebsite = ""
            mEmail = etEmail!!.text.toString().trim { it <= ' ' }
            //mCountryId = tvCountryCode.getText().toString().trim();
            mMobileNumber = etMobileNumber!!.text.toString().trim { it <= ' ' }
            mCity = tvCity!!.text.toString().trim { it <= ' ' }
            mPostCode = etPostalCode!!.text.toString().trim { it <= ' ' }
            mBio = etDesc!!.text.toString().trim { it <= ' ' }
            mGenre = tvGenre!!.text.toString().trim { it <= ' ' }
            mWebsite = etWebsite!!.text.toString().trim { it <= ' ' }
            mExpertise = tvExpertise!!.text.toString().trim { it <= ' ' }
            mExperience = tvExperience!!.text.toString().trim { it <= ' ' }
            mCountryCode = tvCountryCode!!.text.toString()
            mCountryName = tvCountry!!.text.toString()
            mAddress = etAddress!!.text.toString()
            //  mCertification = tvCertification.getText().toString().trim();
            mGrade = tvGrade!!.text.toString().trim { it <= ' ' }
            mCertification = if (mCertification.equals("Others", ignoreCase = true)) {
                etCertification!!.text.toString()
            } else tvCertification!!.text.toString()
            if (mEmail.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_email_id))
            } else if (mCountryCode.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_country_code))
            } else if (mMobileNumber.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_phone_empty))
            } else if (mMobileNumber.length < 10) {
                Utils.showToast(activity, resources.getString(R.string.err_phone))
            } else if (mStateId.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_state))
            } else if (mCity.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_city))
            } else if (mAddress.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_address))
            } else if (mPostCode.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_postcode))
            } else if (mBio.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_bio))
            } else if (mCount < 10) {
                Utils.showToast(activity, resources.getString(R.string.err_bio_count))
            } else if (mGenre.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_genre))
            } else if (mExpertise.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_expertise))
            } else if (mExperience.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_experience))
            } else {
                try {
                    jsonBody.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                    jsonBody.put("DisplayName", "")
                    jsonBody.put("Email", mEmail)
                    jsonBody.put("CountryId", mCountryId)
                    jsonBody.put("Phone", mMobileNumber)
                    jsonBody.put("CityId", mCityId)
                    jsonBody.put("StateId", mStateId)
                    jsonBody.put("UserAddress", mAddress)
                    jsonBody.put("PostCode", mPostCode)
                    jsonBody.put("Bio", mBio)
                    jsonBody.put("Genre", mGenreId)
                    jsonBody.put("Website", mWebsite)
                    jsonBody.put("Expertise", mExpertise)
                    jsonBody.put("ExperienceId", mExperienceId)
                    jsonBody.put("Certifications", mCertification)
                    jsonBody.put("Grade", mGrade)
                    requestBody = jsonBody.toString()
                    hitAPIs(Constants.FOR_UPDATE_PROFILE, requestBody)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }
    }

    fun initializeLoader() {
        Utils.initializeAndShow(activity)
    }


    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_UPDATE_PROFILE -> {
                var jsonObject: JSONObject? = null
                SharedPref.putString(Constants.IS_FIRST_LOGIN, "N")
                try {
                    jsonObject = JSONObject(response.toString())
                    val tracking = jsonObject.getString("Status")
                    val Message = jsonObject.getString("Message")
                    Utils.showToast(requireContext(), Message)
                    Log.e("tag", "status is $tracking")
                    if (tracking.equals("Success", ignoreCase = true)) {
                        startActivity(Intent(activity, MainActivity::class.java))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            Constants.FOR_UPLOAD_PROFILE_IMAGE -> {

                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        Utils.showToast(activity, "Profile Pic uploaded successfully")


                        pBar.visibility = View.VISIBLE

                        Glide.with(this)
                                .load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        pBar.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBar.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(ivProfile)
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))


                    } else {
                        pBar.visibility = View.GONE

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }
            Constants.FOR_GENRE_LIST -> try {
                val jsonArray = JSONArray(response.toString())
                var i = 0
                while (i < jsonArray.length()) {
                    val jsonObject2 = jsonArray.getJSONObject(i)
                    val model = GenreModel()
                    model.id = jsonObject2.getString("Id")
                    model.type = jsonObject2.getString("GenreType")
                    genreNameAL.add(jsonObject2.getString("GenreType"))
                    genreAL.add(model)
                    i++
                }
                hitAPIs(Constants.FOR_COUNTRIES_LIST, "")
                //getCountriesList();
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Constants.FOR_COUNTRIES_LIST -> try {
                val jsonArray = JSONArray(response.toString())
                var i = 0
                while (i < jsonArray.length()) {
                    val jsonObject1 = jsonArray.getJSONObject(i)
                    val model = CountryModel()
                    model.countryId = jsonObject1.getString("CountryId")
                    model.countryName = jsonObject1.getString("CountryName")
                    model.countryCode = jsonObject1.getString("CountryCode")
                    countryNameAL.add(jsonObject1.getString("CountryName"))
                    countryCodeAL.add(jsonObject1.getString("CountryCode"))
                    countryAL.add(model)
                    i++
                }
                arrCountryCode = countryCodeAL.toTypedArray()
                arrGenre = genreNameAL.toTypedArray()
                hitAPIs(Constants.FOR_USER_PROFILE, "")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Constants.FOR_USER_PROFILE -> try {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", ignoreCase = true)) {
                    SharedPref.putString(Constants.COUNTRY_CODE, obj.getString("CountryCode"))
                    SharedPref.putString(Constants.MOBILE_NUMBER, obj.getString("ContactNo"))
                    SharedPref.putString(Constants.COUNTRY_NAME, obj.getString("CountryName"))
                    SharedPref.putString(Constants.COUNTRY_ID, obj.getString("CountryId"))
                    mCountryCode = obj.getString("CountryCode")
                    mMobileNumber = obj.getString("ContactNo")
                    mCountryName = obj.getString("CountryName")
                    mCountryId = obj.getString("CountryId")
                    mExperienceId = obj.getString("ExperienceId")
                    mGenreId = obj.getString("Genre")
                    mStateId = obj.getString("StateId")
                    mCityId = obj.getString("CityID")
                    tvUserName!!.text = obj.getString("FirstName") + " " + obj.getString("LastName")
                    tvProfileType!!.text = obj.getString("ProfileTypeName")
                    if (!obj.getString("CityName").equals("", ignoreCase = true)) tvCityCountry!!.text = obj.getString("CityName") + ", " + obj.getString("CountryName")
                    etEmail!!.setText(obj.getString("Email"))
                    tvCountryCode!!.text = obj.getString("CountryCode")
                    etMobileNumber!!.setText(obj.getString("ContactNo"))
                    tvCountry!!.text = obj.getString("CountryName")
                    tvState!!.text = obj.getString("StateName")
                    tvCity!!.text = obj.getString("CityName")
                    etAddress!!.setText(obj.getString("UserAddress"))
                    etPostalCode!!.setText(obj.getString("PostCode"))
                    etDesc!!.setText(obj.getString("Bio"))
                    tvGenre!!.text = obj.getString("GenreTypeName")
                    etWebsite!!.setText(obj.getString("Website"))
                    tvExpertise!!.text = obj.getString("Expertise")
                    tvExperience!!.text = obj.getString("ExperienceYear")
                    val mCertification = obj.getString("Certifications")
                    if (mCertification.equals("", ignoreCase = true)) {
                        llCertification!!.visibility = View.GONE
                    } else if (!Arrays.asList<String>(*arrCertification).contains(mCertification)) {
                        tvCertification!!.text = "Others"
                        llCertification!!.visibility = View.VISIBLE
                        etCertification!!.setText(mCertification)
                    } else {
                        tvCertification!!.text = mCertification
                        llCertification!!.visibility = View.GONE
                    }
                    tvCertification!!.text = obj.getString("Certifications")
                    tvGrade!!.text = obj.getString("Grade")
                    if (obj.getString("SocialId") == "") {
                        ivCamera!!.visibility = View.VISIBLE
                        if (obj.getString("ProfilePic") == "") {
                            Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfile!!)
                            pBar!!.visibility = View.GONE
                        } else {
                            Glide.with(this).load(obj.getString("ImgUrl") + obj.getString("ProfilePic")).into(ivProfile!!)
                            pBar!!.visibility = View.GONE
                        }
                    } else {
                        ivCamera!!.visibility = View.GONE
                        val sUrl = obj.getString("SocialImageUrl")
                        if (sUrl == "") Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfile!!) else Glide.with(this).load(obj.getString("SocialImageUrl")).into(ivProfile!!)
                        pBar!!.visibility = View.GONE
                    }
                }
                callStateAPI()
            } catch (e: JSONException) {
                e.printStackTrace()
                callStateAPI()
            }
            Constants.FOR_STATE_LIST -> try {
                val jsonArray = JSONArray(response.toString())
                var i = 0
                while (i < jsonArray.length()) {
                    val jsonObjectState = jsonArray.getJSONObject(i)
                    val model = StateModel()
                    model.stateName = jsonObjectState.getString("StateName")
                    model.stateId = jsonObjectState.getString("StateId")
                    alState.add(model)
                    alStateName.add(jsonObjectState.getString("StateName"))
                    i++
                }
                arrStateName = alStateName.toTypedArray()
                if (!mStateId.equals("", ignoreCase = true)) {
                    callCityAPI()
                }
            } catch (e: Exception) {
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


    fun showDropdown(array: Array<String>, textView: TextView?, spinnerData: SpinnerData, width: Int) {
        listPopupWindow = ListPopupWindow(
                activity)
        listPopupWindow!!.setAdapter(ArrayAdapter<Any?>(
                activity,
                R.layout.row_profile_spinner, array))
        listPopupWindow!!.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_black))
        listPopupWindow!!.anchorView = textView
        listPopupWindow!!.width = width
        listPopupWindow!!.height = 400
        listPopupWindow!!.isModal = true
        listPopupWindow!!.setOnItemClickListener { parent, view, position, id ->
            if (textView!!.id == R.id.tvCountryCode) {
                spinnerData.getData(countryAL[position].countryId, countryAL[position].countryName)
            } else if (textView.id == R.id.tvGenre) {
                spinnerData.getData(genreAL[position].id, array[position])
            } else if (textView.id == R.id.tvExperience) {
                val pos = position + 1
                spinnerData.getData(pos.toString() + "", array[position])
            } else if (textView.id == R.id.tvState) {
                spinnerData.getData(alState[position].stateId, array[position])
            } else if (textView.id == R.id.tvCity) {
                spinnerData.getData(alCity[position].cityId, array[position])
            } else if (textView.id == R.id.tvCertification) {
                if (arrCertification[position].equals("Others", ignoreCase = true)) {
                    llCertification!!.visibility = View.VISIBLE
                } else {
                    llCertification!!.visibility = View.GONE
                    spinnerData.getData("", array[position])
                }
            } else {
                spinnerData.getData("", array[position])
            }
            textView.text = array[position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }


    fun locateView(): Rect? {
        val v: View? = ivStatus
        val loc_int = IntArray(2)
        if (v == null) return null
        try {
            v.getLocationOnScreen(loc_int)
        } catch (npe: NullPointerException) { //Happens when the view doesn't exist on screen anymore.
            return null
        }
        val location = Rect()
        location.left = loc_int[0]
        location.top = loc_int[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }

    fun getImageFile(file: File) {
        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, ""))
        if (Utils.isNetworkConnected(activity!!)) {
            initializeLoader()
            ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_PROFILE_IMAGE, this)
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }

    }

    private fun callStateAPI() {
        try {
            val jsonObject1 = JSONObject()
            jsonObject1.put("CountryId", mCountryId)
            hitAPIs(Constants.FOR_STATE_LIST, jsonObject1.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun callCityAPI() {
        try {
            val jsonObject1 = JSONObject()
            jsonObject1.put("StateId", mStateId)
            hitAPIs(Constants.FOR_CITY_LIST, jsonObject1.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    companion object {

        private const val STATUS_DO_NOT_DISTURB = "Do_not_disturb"
    }
}