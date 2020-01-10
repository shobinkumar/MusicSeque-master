package com.musicseque.venue_manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.musicseque.Fonts.*
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.interfaces.*
import com.musicseque.models.*
import com.musicseque.retrofit_interface.*
import com.musicseque.utilities.*
import org.json.*
import com.musicseque.utilities.Constants.*
import kotlinx.android.synthetic.main.fragment_create_venue.*
import kotlinx.android.synthetic.main.fragment_create_venue.etMobileNumber
import kotlinx.android.synthetic.main.fragment_create_venue.ivCamera
import kotlinx.android.synthetic.main.fragment_create_venue.pBar
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_music_lover.*
import okhttp3.*
import java.io.File
import java.lang.Exception


class VenueFormFragment : KotlinBaseFragment(), View.OnClickListener, MyInterface {
    private var imgRight: ImageView? = null
    private var tvHeading: BoldNoyhr? = null

    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var mCountryCode: String
    private lateinit var arrCountryCode: Array<String>
    private val countryAL = ArrayList<CountryModel>()
    private val countryCodeAL = ArrayList<String>()
    private val countryNameAL = ArrayList<String>()
    lateinit var v: View
    lateinit var mVenueEmail: String
    lateinit var mVenueName: String
    lateinit var mVenueAddress: String
    lateinit var mVenueCity: String
    lateinit var mVenuePinCode: String
    lateinit var mVenueCountry: String
    lateinit var mVenueBio: String
    lateinit var mVenueCapacity: String
    lateinit var mVenueCountryId: String
    lateinit var mVenuePhoneNumber: String
    lateinit var arrStateName: Array<String>
    var alState = java.util.ArrayList<StateModel>()
    var alStateName = java.util.ArrayList<String>()
    var mStateName = ""
    var mStateId = ""
    lateinit var arrCityName: Array<String>
    var alCity = java.util.ArrayList<CityModel>()
    var alCityName = java.util.ArrayList<String>()
    var mCityName = ""
    var mCityId = ""


    var mWidth = 0
    var mWidthFull = 0

    //var mVenueName
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_create_venue, null)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        showDefaultData()
        listeners()
        hitAPI(FOR_COUNTRIES_LIST, "")
    }

    private fun showDefaultData() {
        mVenueName = SharedPref.getString(USER_NAME, "")
        mVenueEmail = SharedPref.getString(EMAIL_ID, "")
        mCountryCode = SharedPref.getString(Constants.COUNTRY_CODE, "")
        mVenueCountryId = SharedPref.getString(Constants.COUNTRY_ID, "")
        mVenueCountry = SharedPref.getString(Constants.COUNTRY_NAME, "")
        mVenuePhoneNumber = SharedPref.getString(Constants.MOBILE_NUMBER, "")
        tvVenueEmail.text = mVenueEmail
        tvVenueName.text = mVenueName
        tvVenueCountryCode.setText(mCountryCode)
        etMobileNumber.setText(mVenuePhoneNumber)
        tvCountryVenueForm.setText(mVenueCountry)
    }


    private fun initViews() {
        imgRight = activity?.findViewById<ImageView>(R.id.img_right_icon)
        tvHeading = activity?.findViewById<BoldNoyhr>(R.id.tvHeading)
        val tvDone = (activity as MainActivity).findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE
        imgRight?.setVisibility(View.GONE)
        tvHeading?.setText("Profile")


        tvVenueCountryCode.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener
        {
            try {
                mWidth = tvVenueCountryCode.getMeasuredWidth()
            } catch (e: Exception) {

            }


        }
        )
        mWidthFull = 500

    }

    private fun listeners() {
        ivVenueImage.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        tvVenueCountryCode.setOnClickListener(this)
        tvStateVenueForm.setOnClickListener(this)
        tvCityVenueForm.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        when (view.id) {

            R.id.ivVenueImage -> {
                checkPermissions("image", "com.musicseque.venue_manager.fragment.VenueFormFragment", this)
            }
            R.id.tvVenueCountryCode -> {
                showDropdown(arrCountryCode, tvVenueCountryCode, SpinnerData { mId, mName ->
                    mVenueCountryId = mId
                    mVenueCountry = mName
                    tvCountryVenueForm.setText(mName)
                    mStateId = ""
                    mCityId = ""
                    tvStateVenueForm.text = ""
                    tvCityVenueForm.text = ""
                    alCity.clear()
                    alCityName.clear()
                    alState.clear()
                    alStateName.clear()

                    callStateAPI()
                }, mWidth)
            }
            R.id.tvStateVenueForm -> showDropdown(arrStateName, tvStateVenueForm, SpinnerData { mId, mName ->
                mStateId = mId
                mStateName = mName
                tvStateVenueForm.text = mName
                alCity.clear()
                alCityName.clear()

                mCityId = ""
                tvCityVenueForm.text = ""

                callCityAPI()
            }, mWidthFull)
            R.id.tvCityVenueForm ->
                if (!mStateId.equals("")) {
                    showDropdown(arrCityName, tvCityVenueForm, SpinnerData { mId, mName ->
                        mCityId = mId
                        mCityName = mName
                        tvCityVenueForm.text = mName
                    }, mWidthFull)
                } else {
                    Utils.showToast(activity, resources.getString(R.string.err_state))
                }


            R.id.btnSubmit -> {
                mVenuePhoneNumber = etMobileNumber.text.toString()
                mVenuePinCode = etVenuePinCode.text.toString()
                mVenueBio = etVenueDesc.text.toString()
                mVenueCapacity = etVenueCapacity.text.toString()
                mVenueAddress = etAddressVenueForm.text.toString()
                mCountryCode = tvVenueCountryCode.text.toString()
                mVenueCity = tvCityVenueForm.text.toString()


                if (mCountryCode.equals("", true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_country_code))

                } else if (mVenuePhoneNumber.length < 10) {
                    Utils.showToast(activity, resources.getString(R.string.err_phone))
                } else if (mStateId.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_state))
                } else if (mCityId.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_city))
                } else if (mVenueAddress.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_address))

                } else if (mVenuePinCode.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_postcode))

                } else if (mVenueBio.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_venue_bio))

                } else if (mVenueCapacity.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_capacity))
                } else {

                    try {

                        val jsonBody = JSONObject()
                        jsonBody.put("Phone", mVenuePhoneNumber)
                        jsonBody.put("EmailId", mVenueEmail)
                        jsonBody.put("City", mVenueCity)
                        jsonBody.put("PostCode", mVenuePinCode)
                        jsonBody.put("CountryId", mVenueCountryId)
                        jsonBody.put("Bio", mVenueBio)
                        jsonBody.put("UserId", SharedPref.getString(USER_ID, ""))
                        jsonBody.put("CityId", mCityId)
                        jsonBody.put("StateId", mStateId)
                        jsonBody.put("UserAddress", mVenueAddress)
                        jsonBody.put("VenueCapacity", mVenueCapacity)
                        jsonBody.put("DisplayName", mVenueName)
                        jsonBody.put("VenueAddress", mVenueAddress)
                        hitAPI(FOR_UPDATE_PROFILE, jsonBody.toString())


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }


            }

        }
    }


    private fun hitAPI(type: Int, args: String) {

        if (KotlinUtils.isNetConnected(activity!!.applicationContext)) {
            Utils.initializeAndShow(requireContext())
            if (type == FOR_COUNTRIES_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this)


            } else if (type == Constants.FOR_STATE_LIST) {
                RetrofitAPI.callAPI(args, Constants.FOR_STATE_LIST, this)
            } else if (type == Constants.FOR_CITY_LIST) {
                RetrofitAPI.callAPI(args, Constants.FOR_CITY_LIST, this)
            } else if (type == FOR_USER_PROFILE) {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else if (type == FOR_UPDATE_PROFILE) {
                RetrofitAPI.callAPI(args, FOR_UPDATE_PROFILE, this)
            }


        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()

        when (TYPE) {
            FOR_COUNTRIES_LIST -> {
                try {
                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val model = CountryModel()
                        model.setCountryId(jsonObject1.getString("CountryId"))
                        model.setCountryName(jsonObject1.getString("CountryName"))
                        model.setCountryCode(jsonObject1.getString("CountryCode"))

                        countryNameAL.add(jsonObject1.getString("CountryName"))
                        countryCodeAL.add(jsonObject1.getString("CountryCode"))
                        countryAL.add(model)
                    }


                    arrCountryCode = countryCodeAL.toTypedArray()
                    hitAPI(FOR_USER_PROFILE, "")


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

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


            FOR_USER_PROFILE -> {
                try {
                    val obj = JSONObject(response.toString())
                    if (obj.getString("Status").equals("Success", true)) {


                        mCountryCode = obj.getString("CountryCode")
                        mVenueCountryId = obj.getString("CountryId")
                        mVenueCountry = obj.getString("CountryName")
                        mVenuePhoneNumber = obj.getString("ContactNo")
                        mStateId = obj.getString("StateId")
                        mCityId = obj.getString("CityID")

                        SharedPref.putString(Constants.COUNTRY_CODE, obj.getString("CountryCode"))
                        SharedPref.putString(Constants.MOBILE_NUMBER, obj.getString("ContactNo"))
                        SharedPref.putString(Constants.COUNTRY_NAME, obj.getString("CountryName"))
                        SharedPref.putString(Constants.COUNTRY_ID, obj.getString("CountryId"))


                        tvVenueCountryCode.setText(obj.getString("CountryCode"));
                        etMobileNumber.setText(obj.getString("ContactNo"))
                        tvCountryVenueForm.setText(obj.getString("CountryName"))
                        tvStateVenueForm.text = obj.getString("StateName")
                        tvCityVenueForm.setText(obj.getString("CityName"))
                        etAddressVenueForm.setText(obj.getString("VenueAddress"))
                        etVenuePinCode.setText(obj.getString("PostCode"))

                        etVenueDesc.setText(obj.getString("Bio"))
                        etVenueCapacity.setText(obj.getString("VenueCapacity"))

                        if (obj.getString("SocialId").equals("")) {
                            ivCamera.visibility = View.VISIBLE
                            if (obj.getString("ProfilePic").equals("")) {
                                Glide.with(this).load(R.drawable.icon_img_dummy).into(ivVenueImage)

                                pBar.visibility = View.GONE
                            } else {
                                Glide.with(this).load(obj.getString("ImgUrl") + obj.getString("ProfilePic")).into(ivVenueImage)
                                pBar.visibility = View.GONE

                            }
                        } else {
                            ivCamera.visibility = View.GONE
                            val sUrl = obj.getString("SocialImageUrl")
                            if (sUrl.equals(""))
                                Glide.with(this).load(R.drawable.icon_img_dummy).into(ivVenueImage)
                            else
                                Glide.with(this).load(obj.getString("SocialImageUrl")).into(ivVenueImage)
                            pBar.visibility = View.GONE
                        }


                    }
                    callStateAPI()

                } catch (e: JSONException) {
                    e.printStackTrace()
                    callStateAPI()
                }


            }
            FOR_UPDATE_PROFILE -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", true)) {
                    SharedPref.putString(Constants.IS_FIRST_LOGIN, "N")
                    Utils.showToast(requireContext(), obj.getString("Message"))
                    startActivity(Intent(activity, MainActivity::class.java))

                } else {
                    Utils.showToast(requireContext(), obj.getString("Message"))

                }
            }
            FOR_UPLOAD_PROFILE_IMAGE -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", true)) {
                    Utils.showToast(requireContext(), "Image saved successfully")
                    SharedPref.putString(Constants.PROFILE_IMAGE, obj.getString("imageurl") + obj.getString("ImageName"))

                }

            }


        }


    }

    public fun getImage(file: File) {
        Glide.with(this).load(file).into(ivVenueImage)
        if (Utils.isNetworkConnected(activity?.applicationContext)) {
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
            val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, "")!!)
            ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, FOR_UPLOAD_PROFILE_IMAGE, this)
        } else {
            Utils.showToast(requireContext(), resources.getString(R.string.err_no_internet))
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
            } else if (textView.id == R.id.tvStateVenueForm) {
                spinnerData.getData(alState[position].stateId, array[position])
            } else if (textView.id == R.id.tvCityVenueForm) {
                spinnerData.getData(alCity[position].cityId, array[position])
            }
            textView.text = array[position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }

    private fun callStateAPI() {
        try {
            val jsonObject1 = JSONObject()
            jsonObject1.put("CountryId", mVenueCountryId)
            hitAPI(Constants.FOR_STATE_LIST, jsonObject1.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
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

}