package com.musicseque.artist.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.musicseque.Fonts.CustomButton
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import org.json.JSONException
import org.json.JSONObject

class ChangePasswordActivity : BaseActivity(), MyInterface {
    @BindView(R.id.etCurrentPassword)
    var etCurrentPassword: EditText? = null
    @BindView(R.id.etNewPassword)
    var etNewPassword: EditText? = null
    @BindView(R.id.etConfirmNewPassword)
    var etConfirmNewPassword: EditText? = null
    @BindView(R.id.btnChangePassword)
    var btnChangePassword: CustomButton? = null
    @BindView(R.id.btnCancel)
    var btnCancel: CustomButton? = null
    @BindView(R.id.img_first_icon)
    var img_first_icon: ImageView? = null
    @BindView(R.id.img_right_icon)
    var img_right_icon: ImageView? = null
    @BindView(R.id.tv_title)
    var tv_title: TextView? = null
    var mCurrentPassword: String? = null
    var mNewPassword: String? = null
    var mConfirmNewPassword: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        ButterKnife.bind(this)
        tv_title!!.text = "Change Password"
        img_first_icon!!.visibility = View.VISIBLE
        img_right_icon!!.visibility = View.GONE
    }

    @OnClick(R.id.btnChangePassword, R.id.btnCancel, R.id.img_first_icon)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btnChangePassword -> {
                mCurrentPassword = etCurrentPassword!!.text.toString().trim { it <= ' ' }
                mNewPassword = etNewPassword!!.text.toString().trim { it <= ' ' }
                mConfirmNewPassword = etConfirmNewPassword!!.text.toString().trim { it <= ' ' }
                if (Utils.isNetworkConnected(this)) {
                    if (mCurrentPassword.equals("", ignoreCase = true)) {
                        Utils.showToast(this, getString(R.string.err_current_password))
                    } else if (!mCurrentPassword.equals(SharedPref.getString(Constants.PASSWORD, ""), ignoreCase = true)) {
                        Utils.showToast(this, getString(R.string.err_current_password_not_correct))
                    } else if (mNewPassword!!.length == 0) {
                        Utils.showToast(this, resources.getString(R.string.err_password_empty))
                    } else if (mNewPassword!!.length < 6 || !Utils.isValidPassword(mNewPassword)) {
                        Utils.showToast(this, resources.getString(R.string.err_password))
                    } else if (mConfirmNewPassword!!.length == 0) {
                        Utils.showToast(this, resources.getString(R.string.err_confirm_password_empty))
                    } else if (mNewPassword != mConfirmNewPassword) {
                        Utils.showToast(this, resources.getString(R.string.err_password_not_match))
                    } else {
                        Utils.initializeAndShow(this)
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                            jsonObject.put("Password", mNewPassword)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_CHANGE_PASSWORD, this)
                    }
                } else {
                }
            }
            R.id.btnCancel -> finish()
            R.id.img_first_icon -> finish()
        }
    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        try {
            val jsonObject = JSONObject(response.toString())
            if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                Utils.showToast(this, jsonObject.getString("Message"))
                SharedPref.putString(Constants.PASSWORD, mNewPassword)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Utils.showToast(this, jsonObject.getString("Message"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}