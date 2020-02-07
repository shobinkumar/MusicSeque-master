package com.musicseque.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.artist.activity.ChangePasswordActivity
import com.musicseque.artist.activity.ReportProblemActivity
import com.musicseque.artist.service.CommonService
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.start_up.LoginActivity
import com.musicseque.utilities.APIHit
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_settings.*
import org.json.JSONException
import org.json.JSONObject

class SettingFragment : Fragment(), View.OnClickListener, MyInterface {
    lateinit private var v: View
    private var tv_title: TextView? = null
    private val tvHeading: TextView? = null
    var img_right_icon: ImageView? = null
    var rltoolbar: RelativeLayout? = null

    var builder: AlertDialog.Builder? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_settings, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initOtherViews()
        initViews()
        listeners()
        if (SharedPref.getString(Constants.LOGIN_TYPE, "Simple").equals("Simple", ignoreCase = true)) {
            tvChangePassword!!.visibility = View.VISIBLE
        } else {
            tvChangePassword!!.visibility = View.GONE
        }
        if (SharedPref.getString(Constants.PROFILE_TYPE, "").equals("Venue Manager", ignoreCase = true)) {
            tvStatus!!.visibility = View.GONE
        } else {
            tvStatus!!.visibility = View.VISIBLE
        }
    }

    private fun initOtherViews() {
        img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        img_right_icon!!.visibility = View.GONE
        val tvDone = (activity as MainActivity?)!!.findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE
        //        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
//        SharedPref = retrofitComponent.getShared();
//        SharedPref = retrofitComponent.getSharedPref();
    }

    private fun initViews() {
        tv_title = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as TextView
        tv_title!!.text = "Settings"
    }

    private fun listeners() {
        tvLogout!!.setOnClickListener(this)
        tvStatus.setOnClickListener(this)
        tvChangePassword.setOnClickListener(this)
        tvPrivacyPolicy.setOnClickListener(this)
        tvReportProblem.setOnClickListener(this)
        tvTermsService.setOnClickListener(this)
        tvDeleteAccount.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.tvLogout -> {
                val jsonObject = JSONObject()
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))

                APIHit.sendPostData(jsonObject.toString(), Constants.FOR_LOGOUT, this@SettingFragment, activity!!)
                clearLoginCredentials()


            }


            R.id.tvStatus -> {
                val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val customView = layoutInflater.inflate(R.layout.dropdown_user_status, null)
                val tvAvailable = customView.findViewById<View>(R.id.tvAvailable) as TextView
                val tvDoNot = customView.findViewById<View>(R.id.tvDoNot) as TextView
                val tvInvisible = customView.findViewById<View>(R.id.tvInvisible) as TextView
                //instantiate popup window
                val popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                popupWindow.setBackgroundDrawable(BitmapDrawable())
                popupWindow.isOutsideTouchable = true
                popupWindow.showAsDropDown(tvStatus)
                // popupWindow.showAtLocation(tvStatus, Gravity.LEFT, locateView().left, locateView().left);
                tvAvailable.setOnClickListener {
                    SharedPref.putString(Constants.VISIBILITY_STATUS, "Available")
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
                    SharedPref.putString(Constants.VISIBILITY_STATUS, "Do_not_disturb")
                    popupWindow.dismiss()
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                        jsonObject.put("NewStatus", "Do_not_disturb")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    activity!!.startService(Intent(activity, CommonService::class.java).putExtra("API", Constants.UPDATE_STATUS_API).putExtra("params", jsonObject.toString()))
                    popupWindow.dismiss()
                }
                tvInvisible.setOnClickListener {
                    SharedPref.putString(Constants.VISIBILITY_STATUS, "Offline")
                    popupWindow.dismiss()
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                        jsonObject.put("NewStatus", "Offline")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    activity!!.startService(Intent(activity, CommonService::class.java).putExtra("API", Constants.UPDATE_STATUS_API).putExtra("params", jsonObject.toString()))
                    popupWindow.dismiss()
                }
            }
            R.id.tvChangePassword -> startActivity(Intent(activity, ChangePasswordActivity::class.java))
            R.id.tvPrivacyPolicy -> {
                //                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://musicsegue.com/Privacy%20Policy.html"))
                startActivity(browserIntent)
            }
            R.id.tvTermsService -> {
                //                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                val browserIntent1 = Intent(Intent.ACTION_VIEW, Uri.parse("http://musicsegue.com/TermsandConditions.html"))
                startActivity(browserIntent1)
            }
            R.id.tvReportProblem -> startActivity(Intent(activity, ReportProblemActivity::class.java))
            R.id.tvDeleteAccount -> {
                builder = AlertDialog.Builder(activity!!)
                builder!!.setMessage("Are you sure you want to delete your account?")
                        .setTitle("Music Segue")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { dialog, id ->
                            if (Utils.isNetworkConnected(activity)) {
                                try {
                                    Utils.initializeAndShow(activity)
                                    val str = JSONObject().put("UserId", SharedPref.getString(Constants.USER_ID, "")).toString()
                                    RetrofitAPI.callAPI(str, Constants.FOR_DELETE_ACCOUNT, this@SettingFragment)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            } else {
                                Utils.showToast(activity, resources.getString(R.string.err_no_internet))
                            }
                        }
                        .setNegativeButton("No") { dialog, id ->
                            //  Action for 'NO' Button
                            dialog.cancel()
                        }
                //Creating dialog box
                val alert = builder!!.create()
                //Setting the title manually
                alert.setTitle("Music Segue")
                alert.show()
            }
        }
    }


    fun clearLoginCredentials() {
        try {
            SharedPref.putBoolean(Constants.IS_LOGIN, false)
            SharedPref.putString(Constants.COUNTRY_CODE, "")
            SharedPref.putString(Constants.COUNTRY_ID, "")
            SharedPref.putString(Constants.COUNTRY_NAME, "")
            SharedPref.putString(Constants.MOBILE_NUMBER, "")
            SharedPref.putString(Constants.PROFILE_IMAGE, "")
            SharedPref.putString(Constants.USER_NAME, "")
            SharedPref.putString(Constants.USER_ID, "")
            SharedPref.putString(Constants.PROFILE_TYPE, "")
            SharedPref.putString(Constants.PROFILE_ID, "")
            // SharedPref.putString(Constants.EMAIL_ID, "");
            SharedPref.putString(Constants.PROFILE_IMAGE, "")
            SharedPref.putString(Constants.COUNTRY_NAME, "")
            SharedPref.putString(Constants.LOGIN_TYPE, "")
            SharedPref.putString(Constants.UNIQUE_CODE, "")
            SharedPref.putString(Constants.IS_FIRST_LOGIN, "")
        } catch (e: Exception) {
        }
    }

    fun locateView(): Rect? {
        val v: View? = tvStatus
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

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_DELETE_ACCOUNT -> try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                    clearLoginCredentials()
                    Utils.showToast(activity, jsonObject.getString("Message"))
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Constants.FOR_LOGOUT -> {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity!!.finish()
            }
        }
    }
}