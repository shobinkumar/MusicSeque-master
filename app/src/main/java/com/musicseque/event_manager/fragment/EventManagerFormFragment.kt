package com.musicseque.event_manager.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.*
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import com.musicseque.utilities.Utils.initializeProgressDialog
import kotlinx.android.synthetic.main.fragment_band_profile.*
import kotlinx.android.synthetic.main.fragment_event_manager_form.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.ArrayList


class EventManagerFormFragment : KotlinBaseFragment(), MyInterface, View.OnClickListener {


    private var mAddress: String = ""
    private lateinit var arrCountryCode: Array<String>
    private lateinit var mExpArray: Array<String>
   // lateinit var mCountryCodeArray: Array<String>
    var mCountryCode: String = ""
    var mMobileNumber: String = ""
    var mExperienceId: String = ""
    var mExperience: String = ""
    var mCountryName: String = ""
    var mCountryId: String = ""
    val countryNameList = arrayListOf<String>()
    val countryCodeList = arrayListOf<String>()
    val countryList = arrayListOf<CountryModel>()
    var mCount: Int = 0
    var mStart = 0
    var mWidthExp: Int = 0
    var mWidthCode: Int = 0

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


    lateinit var v: View
    lateinit var listPopupWindow: ListPopupWindow
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_event_manager_form, null)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        listeners()
        changeListeners()
        getAPI(FOR_COUNTRIES_LIST, "")
    }


    private fun initViews() {
        val img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        img_right_icon.setVisibility(View.GONE)
        val tvDone = (activity as MainActivity?)!!.findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE
        val tvHeading = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as TextView
        tvHeading.text = "Profile"
        mExpArray = getResources().getStringArray(R.array.array_exp)


//        tvCountryCodeFormEventManager.viewTreeObserver.addOnGlobalLayoutListener {
//            mWidthCode = tvCountryCodeFormEventManager.measuredWidth
//        }
        mWidthCode = 200
        mWidthExp = 500
        // tvExperienceFormEventManager.viewTreeObserver.addOnGlobalLayoutListener { mWidthExp = tvExperienceFormEventManager.measuredWidth }
    }

    private fun listeners() {
        btnSubmitFormEventManager.setOnClickListener(this)
        ivCameraFormEventManager.setOnClickListener(this)
        ivStatusFormEventManager.setOnClickListener(this)
        tvCountryCodeFormEventManager.setOnClickListener(this)
        tvExperienceFormEventManager.setOnClickListener(this)
        tvStateFormEventManager.setOnClickListener(this)
        tvCityFormEventManager.setOnClickListener(this)


    }

    private fun changeListeners() {


        etDescFormEventManager.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                tvCountFormEventManager.text = editable.length.toString() + "/500"
                mCount = editable.length
            }
        })
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.tvCountryCodeFormEventManager -> {
                showDropdown(arrCountryCode, tvCountryCodeFormEventManager, SpinnerData { mId, mName ->
                    mCountryId = mId
                    mCountryName = mName
                    tvCountryFormEventManager.text = mName

                    mStateId = ""
                    mCityId = ""
                    tvStateFormEventManager.text = ""
                    tvCityFormEventManager.text = ""
                    alCity.clear()
                    alCityName.clear()
                    alState.clear()
                    alStateName.clear()

                    callStateAPI()
                }, mWidthCode)
            }


            R.id.tvStateFormEventManager -> showDropdown(arrStateName, tvStateFormEventManager, SpinnerData { mId, mName ->
                mStateId = mId
                mStateName = mName
                tvStateFormEventManager.text = mName
                alCity.clear()
                alCityName.clear()

                mCityId = ""
                tvCityFormEventManager.text = ""

                callCityAPI()
            }, mWidthExp)
            R.id.tvCityFormEventManager ->
                if (!mStateId.equals("")) {
                    showDropdown(arrCityName, tvCityFormEventManager, SpinnerData { mId, mName ->
                        mCityId = mId
                        mCityName = mName
                        tvCityFormEventManager.text = mName
                    }, mWidthExp)
                } else {
                    Utils.showToast(activity, resources.getString(R.string.err_state))
                }





            R.id.tvExperienceFormEventManager -> {
                showDropdown(mExpArray, tvExperienceFormEventManager, SpinnerData { mId, mName ->
                    mExperience = mName
                    mExperienceId = mId
                }, mWidthExp)
            }

            R.id.ivCameraFormEventManager -> {
                checkPermissions("image", "com.musicseque.event_manager.fragment.EventManagerFormFragment", this)

            }




            R.id.btnSubmitFormEventManager -> {


                var requestBody = ""

                val jsonBody = JSONObject()
                var mEmail = ""
                var mPostCode = ""
                var mBio = ""

                mEmail = etEmailFormEventManager.text.toString()
                mMobileNumber = etMobileNumberFormEventManager.text.toString()
                mPostCode = etPostalCodeFormEventManager.text.toString()
                mBio = etDescFormEventManager.text.toString()
                mExperience = tvExperienceFormEventManager.text.toString()
                mAddress = etAddressFormEventManager.text.toString()


                if (mEmail.equals("", ignoreCase = true)) {
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

                } else if (mExperience.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_experience))
                } else {
                    // showDialog()
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
                        jsonBody.put("Genre", "")
                        jsonBody.put("Website", "")
                        jsonBody.put("Expertise", "")
                        jsonBody.put("ExperienceId", mExperienceId)
                        jsonBody.put("Certifications", "")
                        jsonBody.put("Grade", "")


                        requestBody = jsonBody.toString()
                        getAPI(FOR_UPDATE_PROFILE, requestBody)
                        // RetrofitAPI.callAPI(requestBody, Constants.FOR_UPDATE_PROFILE, this)


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }


            }
        }
    }

    private fun getAPI(TYPE: Int, str: String) {

        if (Utils.isNetworkConnected(activity)) {
            showDialog()
            if (TYPE == FOR_COUNTRIES_LIST) {
                RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this)
            } else if (TYPE == Constants.FOR_STATE_LIST) {
                RetrofitAPI.callAPI(str, Constants.FOR_STATE_LIST, this)
            } else if (TYPE == Constants.FOR_CITY_LIST) {
                RetrofitAPI.callAPI(str, Constants.FOR_CITY_LIST, this)
            } else if (TYPE == FOR_USER_PROFILE) {
                val jsonObject = JSONObject()
                jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
            } else if (TYPE == FOR_UPDATE_PROFILE) {
                RetrofitAPI.callAPI(str, Constants.FOR_UPDATE_PROFILE, this)
            }

        } else {
            Utils.showToast(activity, getString(R.string.err_no_internet))
        }

    }


    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_COUNTRIES_LIST -> {
                try {
                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val model = CountryModel()
                        model.countryId = jsonObject1.getString("CountryId")
                        model.countryName = jsonObject1.getString("CountryName")
                        model.countryCode = jsonObject1.getString("CountryCode")

                        countryNameList.add(jsonObject1.getString("CountryName"))
                        countryCodeList.add(jsonObject1.getString("CountryCode"))
                        countryList.add(model)
                    }

                    arrCountryCode = countryCodeList.toTypedArray()


                    getAPI(FOR_USER_PROFILE, "")


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

                    val obj: JSONObject = JSONObject(response.toString())
                    if (obj.getString("Status").equals("Success", true))
                    {
                        tvUserNameFormEventManager.text = obj.getString("FirstName") + " " + obj.getString("LastName")
                        tvProfileTypeFormEventManager.text = obj.getString("ProfileTypeName")

                        if (!obj.getString("CityName").equals("", ignoreCase = true))
                            tvCityCountryFormEventManager.setText(obj.getString("CityName") + ", " + obj.getString("CountryName"))

                        etEmailFormEventManager?.setText(obj.getString("Email"))
                        tvCountryCodeFormEventManager.text = obj.getString("CountryCode")
                        etMobileNumberFormEventManager.setText(obj.getString("ContactNo"))
                        etDescFormEventManager.setText(obj.getString("Bio"))
                        tvExperienceFormEventManager.text = obj.getString("ExperienceYear")

                        tvCountryFormEventManager.text = obj.getString("CountryName")
                        tvStateFormEventManager.text = obj.getString("StateName")
                        tvCityFormEventManager!!.text = obj.getString("CityName")
                        etAddressFormEventManager.setText(obj.getString("UserAddress"))
                        etPostalCodeFormEventManager.setText(obj.getString("PostCode"))


                        SharedPref.putString(COUNTRY_CODE, obj.getString("CountryCode"))
                        SharedPref.putString(MOBILE_NUMBER, obj.getString("ContactNo"))
                        SharedPref.putString(COUNTRY_NAME, obj.getString("CountryName"))
                        SharedPref.putString(COUNTRY_ID, obj.getString("CountryId"))

                        mCountryCode = obj.getString("CountryCode")
                        mMobileNumber = obj.getString("ContactNo")
                        mCountryName = obj.getString("CountryName")
                        mCountryId = obj.getString("CountryId")
                        mExperienceId = obj.getString("ExperienceId")
                        mStateId = obj.getString("StateId")
                        mCityId = obj.getString("CityID")


                        if (obj.getString("SocialId") == "") {
                            ivCameraFormEventManager.setVisibility(View.VISIBLE)
                            if (obj.getString("ProfilePic") == "") {
                                Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfileFormEventManager)
                                pBarFormEventManager.setVisibility(View.GONE)
                            } else {
                                Glide.with(this).load(obj.getString("ImgUrl") + obj.getString("ProfilePic")).into(ivProfileFormEventManager)
                                pBarFormEventManager.setVisibility(View.GONE)
                            }
                        } else {
                            ivCameraFormEventManager.setVisibility(View.GONE)
                            val sUrl = obj.getString("SocialImageUrl")
                            if (sUrl == "") Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfileFormEventManager) else Glide.with(this).load(obj.getString("SocialImageUrl")).into(ivProfileFormEventManager)
                            pBarFormEventManager.setVisibility(View.GONE)
                        }


                    } else {

                    }

                    callStateAPI()
                } catch (exc: Exception) {
                    callStateAPI()
                }
            }
            FOR_UPDATE_PROFILE -> {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", true)) {
                    SharedPref.putString(IS_FIRST_LOGIN, "N")
                    startActivity(Intent(activity, MainActivity::class.java))
                } else {
                    Utils.showToast(activity, jsonObject.getString("Message"))
                }
            }
            FOR_UPLOAD_PROFILE_IMAGE -> {
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                        Utils.showToast(activity, "Profile Pic uploaded successfully")
                    } else {
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
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

    private fun callCityAPI() {
        try {
            val jsonObject1 = JSONObject()
            jsonObject1.put("StateId", mStateId)
            getAPI(Constants.FOR_CITY_LIST, jsonObject1.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun showDropdown(array: Array<String>, textView: TextView, spinnerData: SpinnerData, width: Int) {
        listPopupWindow = ListPopupWindow(
                activity!!)
        listPopupWindow.setAdapter(ArrayAdapter(
                activity,
                R.layout.row_profile_spinner, array))
        listPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_black))
        listPopupWindow.setAnchorView(textView)
        listPopupWindow.setWidth(width)
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            if (textView.id == R.id.tvCountryCodeFormEventManager) {
                spinnerData.getData(countryList.get(position).getCountryId(), countryList.get(position).getCountryName())
            } else if (textView.id == R.id.tvExperienceFormEventManager) {
                val pos = position + 1
                spinnerData.getData(pos.toString() + "", array[position])

            } else if (textView.id == R.id.tvStateFormEventManager) {
                spinnerData.getData(alState[position].stateId, array[position])
            } else if (textView.id == R.id.tvCityFormEventManager) {
                spinnerData.getData(alCity[position].cityId, array[position])
            } else {
                spinnerData.getData("", array[position])
            }
            textView.text = array[position]

            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }

    private fun showDialog() {
        initializeProgressDialog(activity)
    }

    fun getImage(file: File) {
        Glide.with(this).load(file).into(ivProfileFormEventManager)
        if (Utils.isNetworkConnected(activity)) {
            showDialog()
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
            val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, "")!!)
            ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_PROFILE_IMAGE, this)
        } else {
            Utils.showToast(requireContext(), resources.getString(R.string.err_no_internet))
        }


    }
}



