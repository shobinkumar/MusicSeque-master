package com.musicseque.venue_manager.activity

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_other_venue_profile.*
import kotlinx.android.synthetic.main.toolbar_top.*

import org.json.JSONException
import org.json.JSONObject

class  OtherVenueProfileActivity : BaseActivity(), MyInterface, View.OnClickListener {
    override fun onClick(view: View) {
        when (view.id) {


            R.id.ivIconDrop -> {
                tvBio.visibility = View.VISIBLE
                ivIconDrop.visibility = View.GONE
                ivIconUp.visibility = View.VISIBLE
            }
            //hide

            R.id.ivIconUp -> {
                tvBio.visibility = View.GONE
                ivIconDrop.visibility = View.VISIBLE
                ivIconUp.visibility = View.GONE
            }
            R.id.img_first_icon -> {
                finish()
            }
            R.id.tvAvailability->
            {
                val intent= Intent(this,CheckVenueAvailabilityActivity::class.java).putExtra("venue_id",mVenueId)
                startActivity(intent)
            }
        }
    }

    lateinit var mVenueId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_venue_profile)
        initViews()
        listeners()












    }

    private fun initViews() {
        tv_title.setText("Profile")
        img_right_icon.visibility = View.GONE
        img_first_icon.visibility = View.VISIBLE
        mVenueId = intent.getStringExtra("venue_id")
        hitAPI("profile")


    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        ivIconDrop.setOnClickListener(this)
        ivIconUp.setOnClickListener(this)
        tvAvailability.setOnClickListener(this)

    }

    fun hitAPI(type: String) {

        if (KotlinUtils.isNetConnected(this)) {
            Utils.initializeAndShow(this)
            if (type.equals("profile")) {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", mVenueId)
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }

    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        val jsonObj = JSONObject(response.toString())
        if (jsonObj.getString("Status").equals("Success", true)) {
            when (TYPE) {


                Constants.FOR_USER_PROFILE -> {
                    try {
                        tvUserNameDetail.setText(jsonObj.getString("FirstName") + " " + jsonObj.getString("LastName"))
                        tvUserID.text = SharedPref.getString(Constants.UNIQUE_CODE, "")
                        tvUserLocation.setText(jsonObj.getString("City") + ", " + jsonObj.getString("CountryName"))
                        tvReviews.text = "(" + jsonObj.getString("Reviews") + " reviews" + ")"
                        tvCapacity.text = jsonObj.getString("VenueCapacity")
                        tvFollowersCount.text = jsonObj.getString("Followers")
                        tvBio.text = jsonObj.getString("Bio")
                        tvUserID.text=jsonObj.getString("UniqueCode")

                        if (jsonObj.getString("ProfilePic").equals("", true)) {
                            Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfilePic)
                        } else {
                            Glide.with(this).load(jsonObj.getString("ImgUrl") + jsonObj.getString("ProfilePic")).into(ivProfilePic)

                        }

                        if (jsonObj.getString("BackgroundImage").equals("", true)) {
                            progressBar.visibility = View.GONE
                            Glide.with(this).load(jsonObj.getString("BackgroundImageUrl") + jsonObj.getString("BackgroundImage")).into(ivBackground)

                            R.drawable.icon_img_dummy

                        } else {
                            Glide.with(this).load(R.drawable.icon_img_dummy).into(ivBackground)
                            progressBar.visibility = View.GONE

                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }


                }


            }
        }


    }

}