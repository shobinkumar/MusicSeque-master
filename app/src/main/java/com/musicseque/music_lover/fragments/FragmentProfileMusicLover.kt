package com.musicseque.music_lover.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListPopupWindow
import android.widget.TextView
import com.bumptech.glide.Glide
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.models.CityModel
import com.musicseque.models.CountryModel
import com.musicseque.models.StateModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.*
import com.musicseque.utilities.Constants.FOR_COUNTRIES_LIST
import com.musicseque.utilities.Constants.FOR_UPDATE_PROFILE
import com.musicseque.utilities.Constants.FOR_USER_PROFILE
import com.musicseque.utilities.Constants.IS_FIRST_LOGIN
import kotlinx.android.synthetic.main.fragment_profile_music_lover.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

class FragmentProfileMusicLover : KotlinBaseFragment(), MyInterface, View.OnClickListener {
    lateinit var img_right_icon: ImageView
    private var mCount: Int = 0
    private var mWebsite: String = ""
    private var mBio: String = ""
    private var mPostCode: String = ""
    private var mCity: String = ""
    private var mEmail: String = ""
    private lateinit var listPopupWindow: ListPopupWindow
    private var mCountryId: String = ""
    private var mCountryName: String = ""
    private var mMobileNumber: String = ""
    private var mCountryCode: String = ""
    private var mWidthCode: Int = 0
    private var mWidthFullCode: Int = 500

  
    var mStateName = ""
    var mStateId = ""
  
    var mCityName = ""
    var mCityId = ""
    private var mAddress: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_music_lover, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()
        hitAPIs(Constants.FOR_COUNTRIES_LIST, "")
    }

    private fun listeners() {
        tvCountryCodeMusicLover.setOnClickListener(this)
        btnSubmitMusicLover.setOnClickListener(this)
        ivCameraMusicLover.setOnClickListener(this)
        tvStateProfileMusicLover.setOnClickListener(this)
        tvCityProfileMusicLover.setOnClickListener(this)
        etDescMusicLover.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                tvCountMusicLover.setText(editable.length.toString() + "/500")
                mCount = editable.length
            }
        })

    }

    private fun initViews() {

        mWidthCode = 200
        img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        img_right_icon.setVisibility(View.GONE)
        val tvDone = (activity as MainActivity?)!!.findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE
        var tv_title = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as TextView
        tv_title.text = "Profile"

    }

    private fun hitAPIs(type: Int, params: String) {

            if (type == FOR_COUNTRIES_LIST) {
                CountryStateCityClass.sendGetData(Constants.FOR_COUNTRIES_LIST, this@FragmentProfileMusicLover,activity!!)
            } else if (type == Constants.FOR_USER_PROFILE) {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                    APIHit.sendPostData(jsonObject.toString(), Constants.FOR_USER_PROFILE, this@FragmentProfileMusicLover,activity!!)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else if (type == Constants.FOR_STATE_LIST) {
                CountryStateCityClass.sendPostData(params, Constants.FOR_STATE_LIST, this,activity!!)
            } else if (type == Constants.FOR_CITY_LIST) {
                CountryStateCityClass.sendPostData(params, Constants.FOR_CITY_LIST, this,activity!!)
            } else if (type == FOR_UPDATE_PROFILE) {
               APIHit.sendPostData(params, FOR_UPDATE_PROFILE, this,activity!!)

            }

    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {

            FOR_COUNTRIES_LIST -> {
                try {

                    CountryStateCityClass.countriesDetail(response.toString())
                    hitAPIs(FOR_USER_PROFILE, "")
                } catch (e: Exception) {
                    Log.e("TAG", e.toString())

                }

            }


            Constants.FOR_STATE_LIST -> try {
                CountryStateCityClass.stateList(response.toString())
                if (!mStateId.equals("", ignoreCase = true)) {
                    callCityAPI()
                }
            } catch (e: Exception) {
            }
            Constants.FOR_CITY_LIST -> try {
                CountryStateCityClass.cityList(response.toString())
            } catch (e: Exception) {
            }

            FOR_USER_PROFILE -> {

                try {
                    val obj = JSONObject(response.toString())
                    if (obj.getString("Status").equals("Success", ignoreCase = true)) {


                        mCountryCode = obj.getString("CountryCode")
                        mMobileNumber = obj.getString("ContactNo")
                        mCountryName = obj.getString("CountryName")
                        mCountryId = obj.getString("CountryId")
                        mStateId = obj.getString("StateId")
                        mCityId = obj.getString("CityID")

                        SharedPref.putString(Constants.COUNTRY_CODE, mCountryCode)
                        SharedPref.putString(Constants.MOBILE_NUMBER, mMobileNumber)
                        SharedPref.putString(Constants.COUNTRY_NAME, mCountryName)


                        tvUserNameMusicLover.setText(obj.getString("FirstName") + " " + obj.getString("LastName"))
                        tvProfileTypeMusicLover.setText(obj.getString("ProfileTypeName"))
                        if (!obj.getString("CityName").equals("", ignoreCase = true))
                            tvCityCountryMusicLover.setText(obj.getString("CityName") + ", " + obj.getString("CountryName"))

                        etEmailMusicLover.setText(obj.getString("Email"))
                        tvCountryCodeMusicLover.setText(mCountryCode)
                        etMobileNumberMusicLover.setText(mMobileNumber)

                        tvCountryMusicLover.text = mCountryName
                        tvStateProfileMusicLover!!.text = obj.getString("StateName")
                        tvCityProfileMusicLover!!.text = obj.getString("CityName")
                        etAddressProfileMusicLover!!.setText(obj.getString("UserAddress"))
                        etPostalCodeMusicLover.setText(obj.getString("PostCode"))


                        etDescMusicLover.setText(obj.getString("Bio"))

                        etWebsiteMusicLover.setText(obj.getString("Website"))



                        if (obj.getString("SocialId") == "") {
                            ivCameraMusicLover.setVisibility(View.VISIBLE)
                            if (obj.getString("ProfilePic") == "") {
                                Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfileMusicLover)
                                pBarMusicLover.setVisibility(View.GONE)
                            } else {
                                Glide.with(this).load(obj.getString("ImgUrl") + obj.getString("ProfilePic")).into(ivProfileMusicLover)
                                pBarMusicLover.setVisibility(View.GONE)
                            }
                        } else {
                            ivCameraMusicLover.setVisibility(View.GONE)
                            val sUrl = obj.getString("SocialImageUrl")
                            if (sUrl == "") Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfileMusicLover) else Glide.with(this).load(obj.getString("SocialImageUrl")).into(ivProfileMusicLover)
                            pBarMusicLover.setVisibility(View.GONE)
                        }
                    }
                    callStateAPI()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callStateAPI()
                }


            }

            FOR_UPDATE_PROFILE -> {
                var jsonObject: JSONObject? = null


                SharedPref.putString(IS_FIRST_LOGIN, "N")


                try {
                    jsonObject = JSONObject(response.toString())
                    val tracking = jsonObject.getString("Status")
                    val Message = jsonObject.getString("Message")
                    Utils.showToast(requireContext(), Message)
                    if (tracking.equals("Success", ignoreCase = true)) {
                        startActivity(Intent(activity, MainActivity::class.java))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }


        }
    }

    fun showDropdown(array: Array<String?>, textView: TextView, spinnerData: SpinnerData, width: Int) {
        listPopupWindow = ListPopupWindow(
                activity)
        listPopupWindow.setAdapter(ArrayAdapter<Any?>(
                activity,
                R.layout.row_profile_spinner, array))
        listPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_black))
        listPopupWindow.setAnchorView(textView)
        listPopupWindow.setWidth(width)
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            if (textView.id == R.id.tvCountryCodeMusicLover) {
                spinnerData.getData(CountryStateCityClass.countryAL[position].countryId, CountryStateCityClass.countryAL[position].countryName)
            } else if (textView.id == R.id.tvStateProfileMusicLover) {
                spinnerData.getData(CountryStateCityClass.alState[position].stateId, array[position])
            } else if (textView.id == R.id.tvCityProfileMusicLover) {
                spinnerData.getData(CountryStateCityClass.alCity[position].cityId, array[position])
            }
            textView.text = array[position]
            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvCountryCodeMusicLover -> {
                showDropdown(CountryStateCityClass.arrCountryCode, tvCountryCodeMusicLover, SpinnerData { mId, mName ->
                    mCountryId = mId
                    mCountryName = mName
                    tvCountryMusicLover.text = mName
                    mStateId = ""
                    mCityId = ""
                    tvStateProfileMusicLover.text = ""
                    tvCityProfileMusicLover.text = ""
                    CountryStateCityClass.alCity.clear()
                    CountryStateCityClass.alCityName.clear()
                    CountryStateCityClass.alState.clear()
                    CountryStateCityClass.alStateName.clear()

                    callStateAPI()
                }, mWidthCode)
            }

            R.id.tvStateProfileMusicLover -> showDropdown(CountryStateCityClass.arrStateName, tvStateProfileMusicLover, SpinnerData { mId, mName ->
                mStateId = mId
                mStateName = mName
                tvStateProfileMusicLover.text = mName
                CountryStateCityClass.alCity.clear()
                CountryStateCityClass.alCityName.clear()

                mCityId = ""
                tvCityProfileMusicLover.text = ""

                callCityAPI()
            }, mWidthFullCode)
            R.id.tvCityProfileMusicLover ->
                if (!mStateId.equals("")) {
                    showDropdown(CountryStateCityClass.arrCityName, tvCityProfileMusicLover, SpinnerData { mId, mName ->
                        mCityId = mId
                        mCityName = mName
                        tvCityProfileMusicLover.text = mName
                    }, mWidthFullCode)
                } else {
                    Utils.showToast(activity, resources.getString(R.string.err_state))
                }





            R.id.ivCameraMusicLover -> {
                checkPermissions("image", "com.musicseque.music_lover.fragments.FragmentProfileMusicLover", this)


            }
            R.id.btnSubmitMusicLover -> {
                var requestBody = ""

                val jsonBody = JSONObject()

                mEmail = etEmailMusicLover.text.toString().trim { it <= ' ' }
                mMobileNumber = etMobileNumberMusicLover.getText().toString().trim()
                mPostCode = etPostalCodeMusicLover.text.toString().trim { it <= ' ' }
                mBio = etDescMusicLover.text.toString().trim { it <= ' ' }
                mWebsite = etWebsiteMusicLover.text.toString().trim { it <= ' ' }
                mAddress = etAddressProfileMusicLover.text.toString()

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
                        jsonBody.put("Genre", "0")
                        jsonBody.put("Website", mWebsite)
                        jsonBody.put("ExperienceId", "0")
                        requestBody = jsonBody.toString()
                        hitAPIs(FOR_UPDATE_PROFILE, requestBody)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }


            }
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


    fun getImage(file: File) {
        Glide.with(this).load(file).into(ivProfileMusicLover)
        if (Utils.isNetworkConnected(activity)) {
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
            val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, "")!!)
            ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_PROFILE_IMAGE, this)
        } else {
            Utils.showToast(requireContext(), resources.getString(R.string.err_no_internet))
        }


    }


}




