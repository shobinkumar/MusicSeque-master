package com.musicseque.artist.band.band_fragment

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
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
import com.musicseque.utilities.Constants.FOR_BAND_PROFILE
import com.musicseque.utilities.Constants.FOR_COUNTRIES_LIST
import com.musicseque.utilities.Constants.FOR_GENRE_LIST
import com.musicseque.utilities.Constants.FOR_UPDATE_BAND_PROFILE
import kotlinx.android.synthetic.main.fragment_band_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

import java.util.*

class BandFormFragment : KotlinBaseFragment(), View.OnClickListener, MyInterface {
    private var tv_title: TextView? = null
    var mGenreId = ""
    var mCountryId = ""
    private var v: View? = null
    var genreAL = ArrayList<GenreModel>()
    var genreNameAL = ArrayList<String>()
    var countryAL = ArrayList<CountryModel>()
    var countryNameAL = ArrayList<String>()
    var countryCodeAL = ArrayList<String>()
    lateinit var listPopupWindow: ListPopupWindow
    lateinit var arrCountryCode: Array<String>
    lateinit var arrGenre: Array<String>
    lateinit var arrExperience: Array<String>
    lateinit var arrExpertise: Array<String>
    var mCount = 0
    var mCountryCode = ""
    var mExperience = ""
    var mGenre = ""
    var mCountryName = ""
    var mMobileNumber = ""
    var mExperienceId = ""
    var mAddress = ""
    var mWidthExp = 0
    var mWidthCode = 0
    var STATUS_ACTIVE = "Available"
    private val STATUS_INVISIBLE = "Offline"
    var mBandId = "0"
    lateinit var arrCityName: Array<String>
    var alCity = ArrayList<CityModel>()
    var alCityName = ArrayList<String>()
    var mCityName = ""
    var mCityId = ""
    lateinit var arrStateName: Array<String>
    var alState = ArrayList<StateModel>()
    var alStateName = ArrayList<String>()
    var mStateName = ""
    var mStateId = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_band_profile, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initOtherViews()
        initViews()
        listeners()
        hitAPIs(FOR_GENRE_LIST, "")
    }

    private fun initOtherViews() {
        arrExperience = arrayOf("1 - 2 years", "3 - 5 years", "5 - 10 years", "10+ years")
        arrExpertise = arrayOf("Lead Vocals", "Backing Vocals", "Songwriter", "Piano", "Synthesizer", "Drums", "Band", "Lead Guitar", "Bass Guitar", "Violin", "Harp", "Sitar", "Flute", "Clarinet", "Saxophone", "Trumpet")
        mBandId = arguments!!.getString("band_id")
        val img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        img_right_icon!!.visibility = View.GONE
        val tvDone = (activity as MainActivity?)!!.findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE


    }

    private fun initViews() {
        tvCountryCodeBandProfile.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener { mWidthCode = tvCountryCodeBandProfile.getMeasuredWidth() })
        tvExperienceBandProfile.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener { mWidthExp = tvExperienceBandProfile.getMeasuredWidth() })
        tv_title = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as TextView
        tv_title!!.text = "Band Profile"
        etDescBandProfile.setMovementMethod(ScrollingMovementMethod.getInstance())
        etEmailBandProfile.setText(SharedPref.getString(Constants.EMAIL_ID, ""))
    }

    private fun listeners() {
        btnSubmitBandProfile.setOnClickListener(this)
        ivCameraBandProfile.setOnClickListener(this)
        tvCountryCodeBandProfile.setOnClickListener(this)
        tvExperienceBandProfile.setOnClickListener(this)
        tvGenreBandProfile.setOnClickListener(this)
        tvStateBandProfile.setOnClickListener(this)
        tvCityBandProfile.setOnClickListener(this)
        etDescBandProfile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                tvCountBandProfile.setText(editable.length.toString() + "/500")
                mCount = editable.length
            }
        })
    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_UPDATE_BAND_PROFILE -> {
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = JSONObject(response.toString())
                    val tracking = jsonObject.getString("Status")
                    val Message = jsonObject.getString("Message")
                    Log.e("tag", "status is $tracking")
                    if (tracking.equals("Success", ignoreCase = true)) {
                        startActivity(Intent(activity, MainActivity::class.java))
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
                hitAPIs(FOR_COUNTRIES_LIST, "")
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
                if (!mBandId.equals("0", ignoreCase = true)) hitAPIs(FOR_BAND_PROFILE, "")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Constants.FOR_BAND_PROFILE ->
                try {
                    val jsonObj = JSONObject(response.toString())
                    if (jsonObj.getString("Status").equals("Success", ignoreCase = true)) {
                        val jsonArray = jsonObj.getJSONArray("result")
                        val obj = jsonArray.getJSONObject(0)
                        if (obj.getString("BandImg").equals("", ignoreCase = true)) {
                            Glide.with(this@BandFormFragment)
                                    .load(R.drawable.icon_img_dummy)
                                    .into(ivProfileBandProfile)
                        } else {
                            Glide.with(this@BandFormFragment)
                                    .load(obj.getString("BandImgPath") + obj.getString("BandImg"))
                                    .listener(object : RequestListener<Drawable?> {
                                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                            pBarBandProfile.setVisibility(View.GONE)
                                            ivCameraBandProfile.setVisibility(View.VISIBLE)
                                            return false
                                        }

                                        override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                            pBarBandProfile.setVisibility(View.GONE)
                                            ivCameraBandProfile.setVisibility(View.VISIBLE)
                                            return false
                                        }
                                    })
                                    .into(ivProfileBandProfile)
                        }
                        if (obj.getString("NewStatus").equals(STATUS_ACTIVE, ignoreCase = true)) {
                            ivStatusBandProfile.setImageDrawable(resources.getDrawable(R.drawable.circle_green))
                        } else if (obj.getString("NewStatus").equals(STATUS_INVISIBLE, ignoreCase = true)) {
                            ivStatusBandProfile.setImageDrawable(resources.getDrawable(R.drawable.circle_transparent))
                        } else {
                            ivStatusBandProfile.setImageDrawable(resources.getDrawable(R.drawable.circle_red))
                        }
                        etBandNameBandProfile.setText(obj.getString("BandName"))
                        //etEmailBandProfile.setText(obj.getString("BandEmail"));
                        tvCountryCodeBandProfile.setText(obj.getString("CountryCode"))
                        etMobileNumberBandProfile.setText(obj.getString("BandContactNo"))
                        tvStateBandProfile.text = obj.getString("BandState")
                        tvCityBandProfile.setText(obj.getString("BandCity"))
                        etAddressBandProfile.setText(obj.getString("BandAddress"))

                        etPostalCodeBandProfile.setText(obj.getString("PostCode"))
                        tvCountryBandProfile.setText(obj.getString("CountryName"))
                        etDescBandProfile.setText(obj.getString("Bio"))
                        tvGenreBandProfile.setText(obj.getString("GenreTypeName"))
                        etWebsiteBandProfile.setText(obj.getString("BandWebsite"))
                        tvExperienceBandProfile.setText(obj.getString("ExperienceYear"))
                        mExperienceId = obj.getString("ExperienceId")
                        mGenreId = obj.getString("BandGenreId")
                        mCountryId = obj.getString("CountryId")
                        mStateId = obj.getString("StateId")
                        mCityId = obj.getString("CityId")


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
            Constants.FOR_UPLOAD_BAND_PROFILE_IMAGE -> {
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        Utils.showToast(activity, "Profile Pic uploaded successfully")


                        pBarBandProfile.visibility = View.VISIBLE

                        Glide.with(this)
                                .load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        pBarBandProfile.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBarBandProfile.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(ivProfileBandProfile)
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))


                    } else {
                        pBarBandProfile.visibility = View.GONE

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivCameraBandProfile -> checkPermissions("image", "com.musicseque.artist.band.band_fragment.BandFormFragment", this)
            R.id.btnSubmitBandProfile -> apiUpdateProfile()
            R.id.tvCountryCodeBandProfile ->
                showDropdown(arrCountryCode, tvCountryCodeBandProfile, SpinnerData { mId, mName ->
                    mCountryId = mId
                    mCountryName = mName
                    tvCountryBandProfile.setText(mName)

                    alState.clear()
                    alStateName.clear()

                    alCity.clear()
                    alCityName.clear()
                    mStateId = ""
                    mCityId = ""
                    tvStateBandProfile.text = ""
                    tvCityBandProfile.text = ""



                    callStateAPI()

                }, mWidthCode)

            R.id.tvStateBandProfile -> showDropdown(arrStateName, tvStateBandProfile, SpinnerData { mId, mName ->
                mStateId = mId
                mStateName = mName
                tvStateBandProfile.text=mName


                alCity.clear()
                alCityName.clear()
                mCityId = ""
                tvCityBandProfile.text = ""
                callCityAPI()

            }, mWidthExp)
            R.id.tvCityBandProfile ->
                if (!mStateId.equals("")) {
                    showDropdown(arrCityName, tvCityBandProfile, SpinnerData { mId, mName ->
                        mCityId = mId
                        mCityName = mName
                        tvCityBandProfile.text=mName
                    }, mWidthExp)
                } else {
                    Utils.showToast(activity, resources.getString(R.string.err_state))
                }




            R.id.tvGenreBandProfile -> showDropdown(arrGenre, tvGenreBandProfile, SpinnerData { mId, mName ->
                mGenre = mName
                mGenreId = mId
            }, mWidthExp)
            R.id.tvExperienceBandProfile -> showDropdown(arrExperience, tvExperienceBandProfile, SpinnerData { mId, mName ->
                mExperience = mName
                mExperienceId = mId
            }, mWidthExp)
            R.id.ivStatusBandProfile -> {
                val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val customView = layoutInflater.inflate(R.layout.dropdown_user_status, null)
                val tvAvailable = customView.findViewById<View>(R.id.tvAvailable) as TextView
                val tvDoNot = customView.findViewById<View>(R.id.tvDoNot) as TextView
                val tvInvisible = customView.findViewById<View>(R.id.tvInvisible) as TextView
                //instantiate popup window
                val popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                popupWindow.isOutsideTouchable = true
                popupWindow.showAtLocation(ivStatusBandProfile, Gravity.TOP or Gravity.LEFT, locateView()!!.left, locateView()!!.left)
                tvAvailable.setOnClickListener {
                    ivStatusBandProfile.setImageDrawable(resources.getDrawable(R.drawable.circle_green))
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("BandId", mBandId)
                        jsonObject.put("NewStatus", "Available")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    activity!!.startService(Intent(activity, CommonService::class.java).putExtra("API", Constants.UPDATE_BAND_AVAILABILITY_STATUS_API).putExtra("params", jsonObject.toString()))
                    popupWindow.dismiss()
                }
                tvDoNot.setOnClickListener {
                    ivStatusBandProfile.setImageDrawable(resources.getDrawable(R.drawable.circle_red))
                    popupWindow.dismiss()
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("BandId", mBandId)
                        jsonObject.put("NewStatus", "Do_not_disturb")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    activity!!.startService(Intent(activity, CommonService::class.java).putExtra("API", Constants.UPDATE_BAND_AVAILABILITY_STATUS_API).putExtra("params", jsonObject.toString()))
                }
                tvInvisible.setOnClickListener {
                    ivStatusBandProfile.setImageDrawable(resources.getDrawable(R.drawable.circle_transparent))
                    popupWindow.dismiss()
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("BandId", mBandId)
                        jsonObject.put("NewStatus", "Offline")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    activity!!.startService(Intent(activity, CommonService::class.java).putExtra("API", Constants.UPDATE_BAND_AVAILABILITY_STATUS_API).putExtra("params", jsonObject.toString()))
                }
            }
        }
    }

    private fun hitAPIs(type: Int, args: String) {
        if (Utils.isNetworkConnected(activity)) {
            initializeLoader()

            if (type == FOR_GENRE_LIST)
                RetrofitAPI.callGetAPI(Constants.FOR_GENRE_LIST, this@BandFormFragment)
            else if (type == FOR_COUNTRIES_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this@BandFormFragment)
            } else if (type == Constants.FOR_BAND_PROFILE) {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("BandId", mBandId)
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_BAND_PROFILE, this@BandFormFragment)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else if (type == Constants.FOR_STATE_LIST) {
                RetrofitAPI.callAPI(args, Constants.FOR_STATE_LIST, this@BandFormFragment)
            } else if (type == Constants.FOR_CITY_LIST) {
                RetrofitAPI.callAPI(args, Constants.FOR_CITY_LIST, this@BandFormFragment)
            }
            else if(type== FOR_UPDATE_BAND_PROFILE)
            {
                RetrofitAPI.callAPI(args, Constants.FOR_UPDATE_BAND_PROFILE, this@BandFormFragment)
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
            var mPostCode = ""
            var mBio = ""
            var mBandName = ""
            var mWebsite = ""
            mBandName = etBandNameBandProfile.getText().toString()
            mEmail = etEmailBandProfile.getText().toString().trim()
            mMobileNumber = etMobileNumberBandProfile.getText().toString().trim()
            mAddress = etAddressBandProfile.text.toString()
            mPostCode = etPostalCodeBandProfile.getText().toString().trim()
            mBio = etDescBandProfile.getText().toString().trim()
            mGenre = tvGenreBandProfile.getText().toString().trim()
            mWebsite = etWebsiteBandProfile.getText().toString().trim()
            mExperience = tvExperienceBandProfile.getText().toString().trim()
            if (mBandName.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_band_name))
            } else if (mEmail.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_email_id))
            } else if (mCountryId.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_country_code))
            } else if (mMobileNumber.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_phone_empty))
            } else if (mMobileNumber.length < 10) {
                Utils.showToast(activity, resources.getString(R.string.err_phone))
            } else if (mStateId.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_state))
            } else if (mCityId.equals("", ignoreCase = true)) {
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
            } else if (mExperience.equals("", ignoreCase = true)) {
                Utils.showToast(activity, resources.getString(R.string.err_experience))
            } else {

                try {
                    jsonBody.put("BandName", mBandName)
                    jsonBody.put("BandContactNo", mMobileNumber)
                    jsonBody.put("BandEmail", mEmail)
                    jsonBody.put("BandWebsite", mWebsite)
                    jsonBody.put("CityId", mCityId)
                    jsonBody.put("StateId", mStateId)
                    jsonBody.put("PostCode", mPostCode)
                    jsonBody.put("CountryId", mCountryId)
                    jsonBody.put("Bio", mBio)
                    jsonBody.put("BandGenreId", mGenreId)
                    jsonBody.put("ExperienceId", mExperienceId)
                    jsonBody.put("BandManagerId", SharedPref.getString(Constants.USER_ID, ""))
                    jsonBody.put("ProfileTypeId", "2")
                    jsonBody.put("BandId", mBandId)
                    jsonBody.put("BandAddress", mAddress)
                    requestBody = jsonBody.toString()
                    hitAPIs(FOR_UPDATE_BAND_PROFILE,requestBody)

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


    fun showDropdown(array: Array<String>, textView: TextView, spinnerData: SpinnerData, width: Int) {
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
            if (textView.id == R.id.tvCountryCodeBandProfile) {
                spinnerData.getData(countryAL[position].countryId, countryAL[position].countryName)
            } else if (textView.id == R.id.tvGenreBandProfile) {
                spinnerData.getData( genreAL[position].id,array[position])
            } else if (textView.id == R.id.tvExperienceBandProfile) {
                val pos = position + 1
                spinnerData.getData(pos.toString() + "",array[position])
            }

            else if (textView.id == R.id.tvStateBandProfile) {
                spinnerData.getData(alState[position].stateId, array[position])
            } else if (textView.id == R.id.tvCityBandProfile) {
                spinnerData.getData(alCity[position].cityId, array[position])
            }
            else {
                spinnerData.getData(array[position], "")
            }
            textView.text = array[position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }

    fun locateView(): Rect? {
        val v: View = ivStatusBandProfile
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

    companion object {

        private const val STATUS_DO_NOT_DISTURB = "Do_not_disturb"
    }

    fun getImageFile(file: File) {
        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), mBandId)
        if (Utils.isNetworkConnected(activity!!)) {
            initializeLoader()
            ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_BAND_PROFILE_IMAGE, this)
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
}