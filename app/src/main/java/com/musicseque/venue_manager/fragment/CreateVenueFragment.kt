package com.musicseque.venue_manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.musicseque.Fonts.BoldNoyhr
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.models.CountryModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.service.LocationService
import com.musicseque.service.LocationService.mLatitude
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_create_venue.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.musicseque.utilities.Constants.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class CreateVenueFragment : KotlinBaseFragment(), View.OnClickListener, MyInterface {
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
    var mVenueId=""

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
        hitAPI("countries_list")
    }

    private fun showDefaultData() {
        mVenueName = getSharedPref().getString(USER_NAME, "")
        mVenueEmail = getSharedPref().getString(EMAIL_ID, "")
        mCountryCode = getSharedPref().getString(Constants.COUNTRY_CODE, "")
        mVenueCountryId = getSharedPref().getString(Constants.COUNTRY_ID, "")
        mVenueCountry = getSharedPref().getString(Constants.COUNTRY_NAME, "")
        mVenuePhoneNumber = getSharedPref().getString(Constants.MOBILE_NUMBER, "")
        tvVenueEmail.text = mVenueEmail
        tvVenueName.text = mVenueName
        tvCountryCode.setText(mCountryCode)
        etMobileNumber.setText(mVenuePhoneNumber)
        tvVenueCountry.setText(mVenueCountry)
    }


    private fun initViews() {
        imgRight = activity?.findViewById<ImageView>(R.id.img_right_icon)
        tvHeading = activity?.findViewById<BoldNoyhr>(R.id.tvHeading)

        imgRight?.setVisibility(View.GONE)
        tvHeading?.setText("Profile")
    }

    private fun listeners() {
        ivVenueImage.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        tvCountryCode.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {




//visible




                R.id.ivVenueImage -> {
                checkPermissions("image", "com.musicseque.venue_manager.fragment.CreateVenueFragment",this)
            }
            R.id.tvCountryCode -> {
                showDropdown(SpinnerData { mData, mData1 ->
                    mVenueCountryId = mData
                    mVenueCountry = mData1
                    tvVenueCountry.setText(mData1)
                })
            }
            R.id.btnSubmit -> {
                mVenuePhoneNumber = etMobileNumber.text.toString()
                mVenueCity = etVenueCity.text.toString()
                mVenuePinCode = etVenuePinCode.text.toString()
                mVenueBio = etVenueDesc.text.toString()
                mVenueCapacity = etVenueCapacity.text.toString()
                mVenueAddress = etVenueAddress.text.toString()

                if (mVenuePhoneNumber.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_phone_empty))

                } else if (mVenuePhoneNumber.length < 10) {
                    Utils.showToast(activity, resources.getString(R.string.err_phone))
                } else if (mVenueAddress.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_address))

                } else if (mVenueCity.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_city))

                } else if (mVenuePinCode.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_postcode))

                } else if (mVenueBio.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_bio))

                } else if (mVenueCapacity.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_capacity))
                } else {

                    try {

                        val jsonBody = JSONObject()








//                        jsonBody.put("VenueId", getSharedPref().getString(USER_ID, ""))
//                        jsonBody.put("VenueName", mVenueName)
                        jsonBody.put("Phone", mVenuePhoneNumber)
                        jsonBody.put("EmailId", mVenueEmail)

                        jsonBody.put("City", mVenueCity)
                        jsonBody.put("PostCode", mVenuePinCode)
                        jsonBody.put("CountryId", mVenueCountryId)
                        jsonBody.put("Bio", mVenueBio)
                        jsonBody.put("UserId", getSharedPref().getString(USER_ID, ""))
//                        jsonBody.put("VenueTypeId", "")
                        jsonBody.put("VenueCapacity", mVenueCapacity)
//                        jsonBody.put("ProfileTypeId", "")
//                        jsonBody.put("VenueLatitude", mLatitude)
//                        jsonBody.put("VenueLongitude", LocationService.mLongitude)
//                        jsonBody.put("VenueWebsite", "")
                        jsonBody.put("DisplayName", mVenueName)
                        jsonBody.put("VenueAddress", mVenueAddress)
                        RetrofitAPI.callAPI(jsonBody.toString(), FOR_UPDATE_PROFILE, this)


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }


            }

        }
    }


    private fun hitAPI(type: String) {

        if (KotlinUtils.isNetConnected(activity!!.applicationContext)) {
            Utils.initializeAndShow(requireContext())
            if (type == "countries_list") {
                RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this)


            } else if (type == "profile") {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", getSharedPref().getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
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
                    hitAPI("profile")


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            FOR_USER_PROFILE -> {
                try {
                    val obj = JSONObject(response.toString())
                    if (obj.getString("Status").equals("Success",true)) {
                        if (obj.getString("ProfilePic").equals("")) {
                            pBar.visibility=View.GONE
                        } else {
                            Glide.with(this).load(obj.getString("ImgUrl") + obj.getString("ProfilePic")).into(ivVenueImage)
                            pBar.visibility=View.GONE

                        }



                        etVenueAddress.setText(obj.getString("VenueAddress"))
                        etVenueCity.setText(obj.getString("City"))
                        etVenueDesc.setText(obj.getString("Bio"))
                        etVenueCapacity.setText(obj.getString("VenueCapacity"))
                        etVenuePinCode.setText(obj.getString("PostCode"))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }
            FOR_UPDATE_PROFILE -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success",true)) {
                    getEditor().putString(Constants.IS_FIRST_LOGIN, "N").commit()
                    Utils.showToast(requireContext(), obj.getString("Message"))
                    startActivity(Intent(activity, MainActivity::class.java))

                } else {
                    Utils.showToast(requireContext(), obj.getString("Message"))

                }
            }
            FOR_UPLOAD_PROFILE_IMAGE -> {
                val obj=JSONObject(response.toString())
                if(obj.getString("Status").equals("Success",true))
                {
                    Utils.showToast(requireContext(),"Image saved successfully")
                }

            }


        }


    }

    public fun getImage(file: File) {
        Glide.with(this).load(file).into(ivVenueImage)
        if (Utils.isNetworkConnected(activity?.applicationContext)) {
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
            val mUSerId = RequestBody.create(MediaType.parse("text/plain"), sharedPref.getString(Constants.USER_ID, "")!!)
            ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, FOR_UPLOAD_PROFILE_IMAGE, this)
        } else {
            Utils.showToast(requireContext(), resources.getString(R.string.err_no_internet))
        }


    }


    fun showDropdown(spinnerData: SpinnerData) {
        listPopupWindow = ListPopupWindow(
                activity!!)
        listPopupWindow.setAdapter(ArrayAdapter(
                activity,
                R.layout.row_profile_spinner, arrCountryCode))
        listPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_black))
        listPopupWindow.setAnchorView(tvCountryCode)
        listPopupWindow.setWidth(KotlinUtils.getViewWidth(tvCountryCode))
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            spinnerData.getData(countryAL[position].countryId, countryAL[position].countryName)

            tvCountryCode.text = arrCountryCode[position]

            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }


}