package com.musicseque.venue_manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import com.bumptech.glide.Glide
import com.musicseque.Fonts.BoldNoyhr
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.SpinnerData
import com.musicseque.models.CountryModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.*
import kotlinx.android.synthetic.main.fragment_create_venue.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.musicseque.utilities.Constants.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception


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
    var mWidth = 0

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
        tvVenueCountry.setText(mVenueCountry)
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

    }

    private fun listeners() {
        ivVenueImage.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        tvVenueCountryCode.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        when (view.id) {
//            R.id.ivDrawer -> {
//                onClick(ivDrawer)
//            }
            R.id.ivVenueImage -> {
                checkPermissions("image", "com.musicseque.venue_manager.fragment.CreateVenueFragment", this)
            }
            R.id.tvVenueCountryCode -> {
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
                mCountryCode=tvVenueCountryCode.text.toString()
                mVenueCountry=tvVenueCountry.text.toString()



                 if(mCountryCode.equals("",true))
                {
                    Utils.showToast(activity, resources.getString(R.string.err_country_code))

                }
                else if (mVenuePhoneNumber.length < 10) {
                    Utils.showToast(activity, resources.getString(R.string.err_phone))
                } else if (mVenueAddress.equals("", ignoreCase = true)) {
                    Utils.showToast(activity,  resources.getString(R.string.err_address))

                } else if (mVenueCity.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_city))

                } else if (mVenuePinCode.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_postcode))

                } else if (mVenueBio.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_venue_bio))

                } else if (mVenueCapacity.equals("", ignoreCase = true)) {
                    Utils.showToast(activity, resources.getString(R.string.err_capacity))
                } else {

                    try {

                        val jsonBody = JSONObject()


//                        jsonBody.put("VenueId", SharedPref.getString(USER_ID, ""))
//                        jsonBody.put("VenueName", mVenueName)
                        jsonBody.put("Phone", mVenuePhoneNumber)
                        jsonBody.put("EmailId", mVenueEmail)

                        jsonBody.put("City", mVenueCity)
                        jsonBody.put("PostCode", mVenuePinCode)
                        jsonBody.put("CountryId", mVenueCountryId)
                        jsonBody.put("Bio", mVenueBio)
                        jsonBody.put("UserId", SharedPref.getString(USER_ID, ""))
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
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
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
                    if (obj.getString("Status").equals("Success", true)) {


                        mCountryCode = obj.getString("CountryCode")
                        mVenueCountryId = obj.getString("CountryId")
                        mVenueCountry = obj.getString("CountryName")
                        mVenuePhoneNumber = obj.getString("ContactNo")


                        SharedPref.putString(Constants.COUNTRY_CODE, obj.getString("CountryCode"))
                        SharedPref.putString(Constants.MOBILE_NUMBER, obj.getString("ContactNo"))
                        SharedPref.putString(Constants.COUNTRY_NAME, obj.getString("CountryName"))
                        SharedPref.putString(Constants.COUNTRY_ID, obj.getString("CountryId"))


                        tvVenueCountryCode.setText(obj.getString("CountryCode"));
                        etMobileNumber.setText(obj.getString("ContactNo"))
                        tvVenueCountry.setText(obj.getString("CountryName"))

                        etVenueAddress.setText(obj.getString("VenueAddress"))
                        etVenueCity.setText(obj.getString("City"))
                        etVenueDesc.setText(obj.getString("Bio"))
                        etVenueCapacity.setText(obj.getString("VenueCapacity"))
                        etVenuePinCode.setText(obj.getString("PostCode"))

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
                            val sUrl=obj.getString("SocialImageUrl")
                            if(sUrl.equals(""))
                                Glide.with(this).load(R.drawable.icon_img_dummy).into(ivVenueImage)

                            else
                            Glide.with(this).load(obj.getString("SocialImageUrl")).into(ivVenueImage)
                            pBar.visibility = View.GONE
                        }


                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
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


    fun showDropdown(spinnerData: SpinnerData) {
        listPopupWindow = ListPopupWindow(
                activity!!)
        listPopupWindow.setAdapter(ArrayAdapter(
                activity,
                R.layout.row_profile_spinner, arrCountryCode))
        listPopupWindow.setBackgroundDrawable(resources.getDrawable(R.drawable.rectangle_black))
        listPopupWindow.setAnchorView(tvVenueCountryCode)
        listPopupWindow.setWidth(mWidth)
        listPopupWindow.setHeight(400)
        listPopupWindow.setModal(true)
        listPopupWindow.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            spinnerData.getData(countryAL[position].countryId, countryAL[position].countryName)

            tvVenueCountryCode.text = arrCountryCode[position]

            listPopupWindow.dismiss()
        })
        listPopupWindow.show()
    }


}