package com.musicseque.artist.activity

import android.content.Intent
import android.os.Bundle
import android.view.View

import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_report_problem.*
import kotlinx.android.synthetic.main.toolbar_top.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class ReportProblemActivity : BaseActivity(), MyInterface, View.OnClickListener {

    private var mMessage: String = ""
    private var mEmail: String = ""
    private var fileUpload: MultipartBody.Part? = null
    private val mID: RequestBody? = null
    private var mFileName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_problem)
        initViews()
        listeners()

    }

    private fun initViews() {
        tv_title!!.text = "Report A Problem"
        img_first_icon!!.visibility = View.VISIBLE
        img_right_icon!!.visibility = View.GONE
        tvEmail!!.text = SharedPref.getString(Constants.EMAIL_ID, "")

    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        txt_attach.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> finish()
            R.id.btnSubmit -> {
                mEmail = tvEmail!!.text.toString()
                mMessage = etMessage!!.text.toString()
                if (mEmail!!.length == 0) {
                    Utils.showToast(this@ReportProblemActivity, resources.getString(R.string.err_email_id))
                } else if (!Utils.emailValidator(mEmail)) {
                    Utils.showToast(this@ReportProblemActivity, resources.getString(R.string.err_email_invalid))
                } else if (mMessage!!.length == 0) {
                    Utils.showToast(this@ReportProblemActivity, "Please enter the message")
                } else {
                    if (Utils.isNetworkConnected(this@ReportProblemActivity)) {
                        Utils.initializeAndShow(this@ReportProblemActivity)
                        hitAPI()
                    } else {
                        Utils.showToast(this@ReportProblemActivity, resources.getString(R.string.err_no_internet))
                    }
                }
            }
            R.id.txt_attach -> openDialog("report")
        }
    }

    private fun hitAPI() {
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, ""))
        val message = RequestBody.create(MediaType.parse("text/plain"), mMessage)
        ImageUploadClass.imageUpload(fileUpload, mUSerId, message, Constants.FOR_REPORT_PROBLEM, this@ReportProblemActivity)
    }

    fun getImageDetails(fileToUpload: MultipartBody.Part?, mUSerId: RequestBody?, name: String?) {
        fileUpload = fileToUpload
        mFileName = name
        tvImageName!!.visibility = View.VISIBLE
        tvImageName!!.text = name
        // mID = mUSerId;
// hitAPI();
    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        try {
            val jsonObject = JSONObject(response.toString())
            if (jsonObject.getString("Status").equals("success", ignoreCase = true)) {
                Utils.showToast(this@ReportProblemActivity, jsonObject.getString("Message"))
                val intent = Intent(this@ReportProblemActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Utils.showToast(this@ReportProblemActivity, jsonObject.getString("Message"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}