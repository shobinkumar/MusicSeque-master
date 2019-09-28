package com.musicseque.event_manager.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.bumptech.glide.Glide
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.band.band_fragment.BandFormFragment
import com.musicseque.artist.fragments.ProfileDetailFragment
import com.musicseque.artist.fragments.ProfileFragment
import com.musicseque.artist.service.CommonService
import com.musicseque.event_manager.fragment.EventManagerDetailFragment
import com.musicseque.event_manager.fragment.EventManagerFormFragment
import com.musicseque.event_manager.fragment.EventManagerHomeFragment
import com.musicseque.fragments.HomeFragment
import com.musicseque.fragments.SettingFragment
import com.musicseque.service.LocationService
import com.musicseque.start_up.LoginActivity
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import org.json.JSONException
import org.json.JSONObject
import kotlinx.android.synthetic.main.activity_main_event_manager.*
import kotlinx.android.synthetic.main.drawer_item_event_manager.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.ivProfile

class MainActivityEventManager : BaseActivity(), View.OnClickListener {


    lateinit var fragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_event_manager)
        listeners()
        uploadLocation()
        defaultValues()
        openDefaultFragment()
    }

    private fun listeners() {
    tvAddEvent.setOnClickListener(this)

    }

    private fun uploadLocation() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
            jsonObject.put("Latitude", LocationService.mLatitude)
            jsonObject.put("Longitude", LocationService.mLongitude)
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        startService(Intent(this, CommonService::class.java).putExtra("API", Constants.UPLOAD_LAT_LNG_API).putExtra("params", jsonObject.toString()))

    }

    private fun defaultValues() {
        tvUserName.setText(SharedPref.getString(Constants.USER_NAME, ""))
        tvType.setText(SharedPref.getString(Constants.PROFILE_TYPE, ""))
        tvId.setText("ID : " + SharedPref.getString(Constants.UNIQUE_CODE, "")!!)
        val mUrl = SharedPref.getString(Constants.PROFILE_IMAGE, "")
        if (mUrl!!.equals("", ignoreCase = true)) {
            Glide.with(this)
                    .load(R.drawable.icon_img_dummy)
                    .into(ivProfile)
        } else {
            Glide.with(this)
                    .load(mUrl)
                    .into(ivProfile)
        }


    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.llActivity -> {

            }
            R.id.llHome -> {

            }
            R.id.llProfile -> {
                fragment = EventManagerFormFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)

            }
            R.id.llAlerts -> {

            }
            R.id.llUpload -> {

            }
            R.id.ivDownArrowEvents -> {
                llAllEvents.visibility = View.VISIBLE
                ivDownArrowEvents.visibility = View.GONE
                ivUpArrowEvents.visibility = View.VISIBLE


            }
            R.id.ivUpArrowEvents -> {
                llAllEvents.visibility = View.GONE
                ivDownArrowEvents.visibility = View.VISIBLE
                ivUpArrowEvents.visibility = View.GONE
            }
            R.id.llSettings -> {

            }
            R.id.tvAddEvent -> {
              //  val intent = Intent()
                startActivity(Intent(this, CreateEventActivity::class.java))
             //   startActivity(intent)

            }
            R.id.tvUpcomingEvent -> {

            }
            R.id.tvPastEvents -> {

            }
            R.id.llLogout -> {
                clearLoginCredentials()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun openDefaultFragment() {


        fragment = EventManagerHomeFragment()
        if (intent.getStringExtra("frag") == null)
            changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
        else {


            if (intent.getStringExtra("frag").equals("com.musicseque.event_manager.fragment.EventManagerHomeFragment", ignoreCase = true)) {
                fragment = HomeFragment()
                changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)

            }
            else if (intent.getStringExtra("frag").equals("com.musicseque.event_manager.fragment.EventManagerFormFragment", ignoreCase = true)) {

                fragment = EventManagerFormFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)

            }

        }


    }

    private fun changeIconBottom(home: Int, profile: Int, featured: Int, chat: Int, setting: Int, fragment: Fragment) {


        iv_home.setImageResource(home)
        iv_profile.setImageResource(profile)
        iv_feature.setImageResource(featured)
        iv_chat.setImageResource(chat)
        iv_settings.setImageResource(setting)
        replaceFragment(fragment)

    }

    fun replaceFragment(fragment: Fragment) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun clearLoginCredentials() {
        try {
            editor.putBoolean(Constants.IS_LOGIN, false).commit()
            editor.putString(Constants.COUNTRY_CODE, "").commit()
            editor.putString(Constants.COUNTRY_ID, "").commit()
            editor.putString(Constants.COUNTRY_NAME, "").commit()
            editor.putString(Constants.MOBILE_NUMBER, "").commit()
            editor.putString(Constants.PROFILE_IMAGE, "").commit()
            editor.putString(Constants.USER_NAME, "").commit()
            editor.putString(Constants.USER_ID, "").commit()
            editor.putString(Constants.PROFILE_TYPE, "").commit()
            editor.putString(Constants.PROFILE_ID, "").commit()
            // editor.putString(Constants.EMAIL_ID, "").commit();
            editor.putString(Constants.PROFILE_IMAGE, "").commit()
            editor.putString(Constants.COUNTRY_NAME, "").commit()
            editor.putString(Constants.LOGIN_TYPE, "").commit()

            editor.putString(Constants.UNIQUE_CODE, "").commit()
            editor.putString(Constants.IS_FIRST_LOGIN, "").commit()


        } catch (e: Exception) {

        }


    }
}