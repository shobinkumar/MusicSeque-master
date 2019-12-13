package com.musicseque.start_up

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView

import com.musicseque.Fonts.NoyhrEditText
import com.musicseque.R

import com.musicseque.interfaces.MyInterface
import com.musicseque.models.CountryModel
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


class SignupActivity : Activity(), View.OnClickListener, MyInterface, TextWatcher {

    private var requestBody = ""
    private var firstName = ""
    private var lstName = ""
    private var email = ""
    private var phone = ""
    private var pasw = ""
    private var confPasw = ""
    private var proType: String? = null
     lateinit var radioBtn: CheckBox
   lateinit  var et_firstName: NoyhrEditText
    lateinit var et_lastname: EditText
    private var et_email: EditText? = null
    private var et_phone: EditText? = null
    private var et_pasw: EditText? = null
    private var et_conf_pasw: EditText? = null
    private var pro_type: TextView? = null
    private var tv_sign_up: TextView? = null
    internal var userType = arrayOf("Artist", "Talent Manager", "Event Manager", "Music Lover", "Venue", "Store")
    private var rel8: RelativeLayout? = null
    private var list: ListView? = null
    private var profileType = ""
    lateinit var ivBack: ImageView
     lateinit var tvName: TextView
    lateinit var tvTerms: TextView
    private var prevString: String? = null
    internal var mCountryCode = ""

    private var spinnerCountryCode: Spinner? = null
    private val countryCodeAL = ArrayList<String>()
    private val countryAL = ArrayList<CountryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initOtherViews()
        initViews()
        clickListeners()
        getCountriesList()
        Utils.setTypefaces(radioBtn, this@SignupActivity)
    }


    private fun initOtherViews() {

    }

    private fun initViews() {

        ivBack = findViewById<View>(R.id.ivBack) as ImageView
        tvName = findViewById<View>(R.id.tvName) as TextView

        et_firstName = findViewById<View>(R.id.et_firstName) as NoyhrEditText
        et_lastname = findViewById<View>(R.id.et_lastname) as EditText
        et_email = findViewById<View>(R.id.et_email) as EditText
        spinnerCountryCode = findViewById<View>(R.id.spinnerCountryCode) as Spinner

        et_phone = findViewById<View>(R.id.et_phone) as EditText
        et_pasw = findViewById<View>(R.id.et_pasw) as EditText
        et_conf_pasw = findViewById<View>(R.id.et_conf_pasw) as EditText
        pro_type = findViewById<View>(R.id.pro_type) as TextView
        tv_sign_up = findViewById<View>(R.id.tv_sign_up) as TextView
        tvTerms = findViewById<View>(R.id.tvTerms) as TextView

        list = findViewById<View>(R.id.list2) as ListView
        rel8 = findViewById<View>(R.id.rel8) as RelativeLayout
        radioBtn = findViewById<View>(R.id.radioBtn) as CheckBox


        tvTerms.setLinkTextColor(Color.WHITE) // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        val normalLinkClickSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://musicsegue.com/TermsandConditions.html"))
                startActivity(browserIntent)
            }

        }

        val noUnderLineClickSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://musicsegue.com/Privacy%20Policy.html"))
                startActivity(browserIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = Color.WHITE // specific color for this link
            }
        }



        makeLinks(tvTerms, arrayOf("Terms of Use", "Privacy Policy"), arrayOf(normalLinkClickSpan, noUnderLineClickSpan))


        tvName.text = "Create Account"


        et_firstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                var string = s.toString()

                if (string == prevString) {
                    return
                } else if (string.length == 0)
                    return
                else if (string.length == 1) {
                    prevString = string.toUpperCase()
                    et_firstName.setText(string.toUpperCase())
                    et_firstName.setSelection(string.length)
                } else if (string.length > 0 && string[string.length - 2] == ' ') {
                    string = string.substring(0, string.length - 1) + Character.toUpperCase(string[string.length - 1])
                    prevString = string
                    et_firstName.setText(string)
                    et_firstName.setSelection(string.length)
                }// if the last entered character is after a space
                // 1st character
            }
        })


        val adapter = ArrayAdapter(this, R.layout.spinner_item, userType)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        list!!.adapter = adapter


        list!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, l ->
            if (userType[pos] == "Artist" || userType[pos] == "Venue") {
                pro_type!!.text = userType[pos]
                list!!.visibility = View.GONE
                if (pos == 0)
                    profileType = (pos + 1).toString()
                else
                    profileType = (pos + 2).toString()
            } else {
                Utils.showToast(this@SignupActivity, "You can select only artist and venue for now")
            }
        }


    }

    private fun clickListeners() {
        tv_sign_up!!.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        rel8!!.setOnClickListener(this)
        et_firstName.addTextChangedListener(this)
        et_lastname!!.addTextChangedListener(this)

        et_email!!.filters = arrayOf<InputFilter>(object : InputFilter.AllCaps() {
            override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
                return source.toString().toLowerCase()
            }
        })

    }

    private fun getCountriesList() {
        if (Utils.isNetworkConnected(this@SignupActivity)) {
            initializeLoader()
            RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, this@SignupActivity)

        } else {
            Utils.showToast(this@SignupActivity, resources.getString(R.string.err_no_internet))
        }

    }

    override fun onClick(view: View) {
        if (view === tv_sign_up) {

            firstName = et_firstName.text.toString()
            lstName = et_lastname!!.text.toString()
            email = et_email!!.text.toString().trim { it <= ' ' }
            phone = et_phone!!.text.toString()
            pasw = et_pasw!!.text.toString().trim { it <= ' ' }
            confPasw = et_conf_pasw!!.text.toString().trim { it <= ' ' }
            proType = pro_type!!.text.toString()

            //            if (firstName.equals("") || lstName.equals("") || email.equals("") || phone.equals("") || pasw.equals("") || confPasw.equals("")) {
            //
            //                Utils.showToast(this, getResources().getString(R.string.err_fields_empty));
            //            }
            //
            //            else

            if (firstName.equals("", ignoreCase = true)) {
                Utils.showToast(this, resources.getString(R.string.err_first_name))

            } else if (lstName.equals("", ignoreCase = true)) {
                Utils.showToast(this, resources.getString(R.string.err_last_name))

            } else if (email.equals("", ignoreCase = true)) {
                Utils.showToast(this, resources.getString(R.string.err_email_id))

            } else if (!Utils.emailValidator(email)) {
                Utils.showToast(this, resources.getString(R.string.err_email_invalid))

            } else if (phone.length == 0) {
                Utils.showToast(this, resources.getString(R.string.err_phone_empty))

            } else if (phone.length < 10) {
                Utils.showToast(this, resources.getString(R.string.err_phone))

            } else if (pasw.length == 0) {
                Utils.showToast(this, resources.getString(R.string.err_password_empty))
            } else if (pasw.length < 6 || !Utils.isValidPassword(pasw)) {
                Utils.showToast(this@SignupActivity, resources.getString(R.string.err_password))
            } else if (confPasw.length == 0) {
                Utils.showToast(this, resources.getString(R.string.err_confirm_password_empty))


            } else if (pasw != confPasw) {
                Utils.showToast(this, resources.getString(R.string.err_password_not_match))


            } else if (!radioBtn.isChecked) {
                Utils.showToast(this@SignupActivity, resources.getString(R.string.err_terms))
            } else if (profileType.equals("", ignoreCase = true)) {
                Utils.showToast(this, resources.getString(R.string.err_user_type))

            } else {


                hitAPI()

            }
        } else if (view === rel8) {
            if (list!!.visibility == View.VISIBLE) {
                // Its visible
                list!!.visibility = View.GONE

            } else {
                // Either gone or invisible
                list!!.visibility = View.VISIBLE
            }
        } else if (view === ivBack) {
            finish()
        }

    }

    private fun hitAPI() {
        if (Utils.isNetworkConnected(this@SignupActivity)) {
            initializeLoader()
            try {
                SharedPref.putString(Constants.EMAIL_ID, email)
                val jsonBody = JSONObject()
                jsonBody.put("UserName", email)
                jsonBody.put("Password", pasw)
                jsonBody.put("FirstName", firstName)
                jsonBody.put("LastName", lstName)
                jsonBody.put("Phone", phone)
                jsonBody.put("ProfileTypeId", profileType)
                jsonBody.put("CountryId", mCountryCode)
                jsonBody.put("OTPType", "Email verification")
                //  jsonBody.put("SocialImageUrl", "");
                requestBody = jsonBody.toString()
                Log.e("tag", "request is  $requestBody")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            RetrofitAPI.callAPI(requestBody, Constants.FOR_SIGNUP, this@SignupActivity)
        } else {
            Utils.showToast(this@SignupActivity, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        try {

            if (TYPE == Constants.FOR_SIGNUP) {
                val obj = JSONObject(response.toString())
                val tracking = obj.getString("Status")
                val Message = obj.getString("Message")
                Log.e("tag", "status is $tracking")
                if (tracking == "Success") {

                    Utils.showToast(this@SignupActivity, resources.getString(R.string.txt_please_verify))
                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    intent.putExtra("isEmailVerified", false)
                    startActivity(intent)
                    finish()

                } else {
                    Utils.showToast(this@SignupActivity, "" + Message)
                }
            } else if (TYPE == Constants.FOR_COUNTRIES_LIST) {
                try {
                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val model = CountryModel()
                        model.countryId = jsonObject.getString("CountryId")
                        model.countryName = jsonObject.getString("CountryName")
                        model.countryCode = jsonObject.getString("CountryCode")

                        countryCodeAL.add(jsonObject.getString("CountryCode"))
                        countryAL.add(model)
                    }


                    val arrCountryCode = countryCodeAL.toTypedArray()

                    //
                    //                    ArrayAdapter<String> adapterCode =
                    //                            new ArrayAdapter<String>(SignupActivity.this, R.layout.spinner_item_country_code, arrCountryCode);
                    //                    spinnerCountryCode.setAdapter(adapterCode);

                    val adapterCode = ArrayAdapter(this@SignupActivity, R.layout.spinner_item_country_code, arrCountryCode)
                    spinnerCountryCode!!.adapter = adapterCode
                    spinnerCountryCode!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            mCountryCode = countryAL[position].countryId
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {

                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        } catch (e: JSONException) {
            e.printStackTrace()

        }


    }


    internal fun initializeLoader() {
        Utils.initializeAndShow(this@SignupActivity)

    }


    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {

        if (et_firstName.hasFocus()) {
            makeFirstLetterCapital(s.toString(), et_firstName)
        } else if (et_lastname!!.hasFocus()) {
            makeFirstLetterCapital(s.toString(), et_lastname)
        }

    }

    internal fun makeFirstLetterCapital(str: String, editText: EditText) {
        var string = str
        Log.d("STRING", "$string $prevString")
        if (string == prevString) {
            return
        } else if (string.length == 0)
            return
        else if (string.length == 1) {
            prevString = string.toUpperCase()
            editText.setText(string.toUpperCase())
            editText.setSelection(string.length)
        } else if (string.length > 0 && string[string.length - 2] == ' ') {
            string = string.substring(0, string.length - 1) + Character.toUpperCase(string[string.length - 1])
            prevString = string
            editText.setText(string)
            editText.setSelection(string.length)
        }// if the last entered character is after a space
        // 1st character
    }


    fun makeLinks(textView: TextView, links: Array<String>, clickableSpans: Array<ClickableSpan>) {
        val spannableString = SpannableString(textView.text)
        for (i in links.indices) {
            val clickableSpan = clickableSpans[i]
            val link = links[i]

            val startIndexOfLink = textView.text.toString().indexOf(link)
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textView.highlightColor = Color.TRANSPARENT // prevent TextView change background when highlight
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }


}
