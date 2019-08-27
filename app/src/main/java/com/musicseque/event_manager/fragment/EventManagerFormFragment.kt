package com.musicseque.event_manager.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.TextView
import com.google.gson.JsonObject
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.artist.fragments.BaseFragment
import com.musicseque.event_manager.activity.MainActivityEventManager
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.models.CountryModel
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_UPDATE_PROFILE
import com.musicseque.utilities.Constants.FOR_USER_PROFILE
import com.musicseque.utilities.Utils
import com.musicseque.utilities.Utils.initializeProgressDialog
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import kotlinx.android.synthetic.main.fragment_event_manager_form.*


class EventManagerFormFragment : BaseFragment(), MyInterface, View.OnClickListener {


    lateinit var mMobileNumber: String
    lateinit var mExperienceId: String
    lateinit var mExperience: String
    lateinit var mCountryName: String
    lateinit var mCountryId: String
    val countryNameList = arrayListOf<String>()
    val countryCodeList = arrayListOf<String>()
    val countryList = arrayListOf<CountryModel>()
    internal var mCount: Int = 0
    internal var mStart = 0
    internal var mWidthExp: Int = 0
    internal var mWidthCode: Int = 0
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
        countriesAPI()
    }


    private fun initViews() {
        tvCountryCode.viewTreeObserver.addOnGlobalLayoutListener {
            mWidthCode = tvCountryCode.measuredWidth
        }
        tvExperience.viewTreeObserver.addOnGlobalLayoutListener { mWidthExp = tvExperience.measuredWidth }
    }

    private fun listeners() {
        btn_submit.setOnClickListener(this)
        ivCamera.setOnClickListener(this)
        ivStatus.setOnClickListener(this)
        tvCountryCode.setOnClickListener(this)
        tvExperience.setOnClickListener(this)


    }

    private fun changeListeners() {
        et_city.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        et_city.addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mStart = start + count
            }

            override fun afterTextChanged(s: Editable) {
                val input = s.toString()
                val capitalizedText: String
                if (input.length < 1)
                    capitalizedText = input
                else if (input.length > 1 && input.contains(" ")) {
                    val fstr = input.substring(0, input.lastIndexOf(" ") + 1)
                    if (fstr.length == input.length) {
                        capitalizedText = fstr
                    } else {
                        var sstr = input.substring(input.lastIndexOf(" ") + 1)
                        sstr = sstr.substring(0, 1).toUpperCase() + sstr.substring(1)
                        capitalizedText = fstr + sstr
                    }
                } else
                    capitalizedText = input.substring(0, 1).toUpperCase() + input.substring(1)

                if (capitalizedText != et_city.text.toString()) {
                    et_city.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                        }

                        override fun afterTextChanged(s: Editable) {
                            et_city.setSelection(mStart)
                            et_city.removeTextChangedListener(this)
                        }
                    })
                    et_city.setText(capitalizedText)
                }
            }

        })


        et_desc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                tvCount.text = editable.length.toString() + "/500"
                mCount = editable.length
            }
        })
    }

    override fun onClick(view: View?) {
        var mExpArray = getResources().getStringArray(R.array.array_exp)
        val mCountryCodeArray = arrayOfNulls<String>(countryCodeList.size)
        countryCodeList.toArray(mCountryCodeArray)
        when (view!!.id) {
            R.id.tvCountryCode -> {
                showDropdown(mCountryCodeArray, tvCountryCode, SpinnerData { mData, mData1 ->
                    mCountryId = mData
                    mCountryName = mData1
                    tvCountry.text = mData1
                }, mWidthCode)
            }
            R.id.tvExperience -> {
                showDropdown(mExpArray, tvExperience, SpinnerData { mData, mData1 ->
                    mExperience = mData
                    mExperienceId = mData1
                }, mWidthExp)
            }
            R.id.btn_submit -> {


                var requestBody = ""
                if (Utils.isNetworkConnected(activity!!)) {
                    val jsonBody = JSONObject()
                    var mEmail = ""
                    val mCity: String
                    var mPostCode = ""
                    var mBio = ""

                    mEmail = et_email.text.toString()
                    mMobileNumber = etMobileNumber.text.toString()
                    mCity = et_city.text.toString()
                    mPostCode = et_postal_code.text.toString()
                    mBio = et_desc.text.toString()
                    mExperience = tvExperience.text.toString()



                    if (mEmail.equals("", ignoreCase = true)) {
                        Utils.showToast(activity, resources.getString(R.string.err_email_id))
                    } else if (mCountryId.equals("", ignoreCase = true)) {
                        Utils.showToast(activity, resources.getString(R.string.err_country_code))

                    } else if (mMobileNumber.equals("", ignoreCase = true)) {
                        Utils.showToast(activity, resources.getString(R.string.err_phone_empty))

                    } else if (mMobileNumber.length < 10) {
                        Utils.showToast(activity, resources.getString(R.string.err_phone))
                    } else if (mCity.equals("", ignoreCase = true)) {
                        Utils.showToast(activity, resources.getString(R.string.err_city))

                    } else if (mPostCode.equals("", ignoreCase = true)) {
                        Utils.showToast(activity, resources.getString(R.string.err_postcode))

                    } else if (mBio.equals("", ignoreCase = true)) {
                        Utils.showToast(activity, resources.getString(R.string.err_bio))

                    } else if (mCount < 10) {
                        Utils.showToast(activity, resources.getString(R.string.err_bio_count))

                    } else if (mExperience.equals("", ignoreCase = true)) {
                        Utils.showToast(activity, resources.getString(R.string.err_experience))
                    } else {
                        showDialog()
                        try {
                            jsonBody.put("UserId", sharedPref.getString(Constants.USER_ID, ""))
                            jsonBody.put("DisplayName", "")
                            jsonBody.put("Email", mEmail)
                            jsonBody.put("CountryId", mCountryId)
                            jsonBody.put("Phone", mMobileNumber)
                            jsonBody.put("City", mCity)
                            jsonBody.put("PostCode", mPostCode)
                            jsonBody.put("Bio", mBio)
                            jsonBody.put("Genre", "")
                            jsonBody.put("Website", "")
                            jsonBody.put("Expertise", "")
                            jsonBody.put("ExperienceId", mExperienceId)
                            jsonBody.put("Certifications", "")
                            jsonBody.put("Grade", "")


                            requestBody = jsonBody.toString()
                            RetrofitAPI.callAPI(requestBody, Constants.FOR_UPDATE_PROFILE, this)


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }


                } else {
                    Utils.showToast(activity, resources.getString(R.string.err_no_internet))
                }


            }
        }
    }

    private fun countriesAPI() {

        if (Utils.isNetworkConnected(activity)) {
            showDialog()
            RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this)
        } else {
            Utils.showToast(activity, getString(R.string.err_no_internet))
        }

    }

    private fun profileAPI() {
        if (Utils.isNetworkConnected(activity)) {
            showDialog()
            try {
                val jsonObject = JSONObject()
                jsonObject.put("UserId", sharedPref.getString(Constants.USER_ID, ""))
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
            } catch (e: JSONException) {
                e.printStackTrace()
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




                    profileAPI()


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            FOR_USER_PROFILE -> {
                try {

                    val obj: JSONObject = JSONObject(response.toString())
                    if (obj.getString("Status").equals("Success", true)) {
                        tvUserName.text = obj.getString("FirstName") + " " + obj.getString("LastName")
                        tvProfileType.text = obj.getString("ProfileTypeName")

                        if (!obj.getString("City").equals("", ignoreCase = true))
                            tvCityCountry.setText(obj.getString("City") + ", " + obj.getString("CountryName"))

                        et_email?.setText(obj.getString("Email"))
                        tvCountryCode.text = obj.getString("CountryCode")
                        etMobileNumber.setText(obj.getString("ContactNo"))
                        et_city.setText(obj.getString("City"))
                        et_postal_code.setText(obj.getString("PostCode"))
                        tvCountry.text = obj.getString("CountryName")
                        et_desc.setText(obj.getString("Bio"))
                        tvExperience.text = obj.getString("ExperienceYear")

                    } else {

                    }
                } catch (exc: Exception) {

                }
            }
            FOR_UPDATE_PROFILE -> {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", true)) {
                    startActivity(Intent(activity, MainActivityEventManager::class.java))
                } else {
                    Utils.showToast(activity, jsonObject.getString("Message"))
                }
            }
        }
    }

    fun showDropdown(array: Array<String?>, textView: TextView, spinnerData: SpinnerData, width: Int) {
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
            if (textView.id == R.id.tvCountryCode) {
                spinnerData.getData(countryList.get(position).getCountryId(), countryList.get(position).getCountryName())
            } else if (textView.id == R.id.tvExperience) {
                val pos = position + 1
                spinnerData.getData(array[position], pos.toString() + "")

            } else {
                spinnerData.getData(array[position], "")
            }
            textView.text = array[position]

            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }

    private fun showDialog() {
        initializeProgressDialog(activity)
    }

}



