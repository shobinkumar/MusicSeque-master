package com.musicseque

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout.DrawerListener
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View

import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.musicseque.artist.activity.UploadActivity
import com.musicseque.artist.activity.other_artist_activity.SearchArtistActivity
import com.musicseque.artist.band.band_fragment.BandFormFragment
import com.musicseque.artist.band.band_fragment.BandListFragment
import com.musicseque.artist.fragments.EventsArtistFragment
import com.musicseque.artist.fragments.ProfileDetailFragment
import com.musicseque.artist.fragments.ProfileFragment
import com.musicseque.artist.other_band.fragments.OtherBandListFragment
import com.musicseque.artist.service.CommonService
import com.musicseque.event_manager.fragment.EventManagerDetailFragment
import com.musicseque.event_manager.fragment.EventManagerFormFragment
import com.musicseque.event_manager.fragment.EventStatusFragment
import com.musicseque.firebase_notification.NotificationActivity
import com.musicseque.fragments.HomeFragment
import com.musicseque.fragments.SettingFragment
import com.musicseque.interfaces.MyInterface
import com.musicseque.music_lover.fragments.FragmentProfileDetailMusicLover
import com.musicseque.music_lover.fragments.FragmentProfileMusicLover
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.service.LocationService
import com.musicseque.start_up.LoginActivity
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import com.musicseque.venue_manager.fragment.VenueBookingStatusFragment
import com.musicseque.venue_manager.fragment.VenueProfileDetailFragment
import com.musicseque.venue_manager.fragment.VenueTimmingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_items.*
import kotlinx.android.synthetic.main.drawer_items.ivProfile
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener, DrawerListener, MyInterface {


    lateinit private var fragment: Fragment
    var mLoginType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initOtherViews()
        initViews()
        clickListener()
        uploadLatLng()
        defaultValues()
        openDefaultFragment()
        showHideDrawerViews()
    }

    private fun initOtherViews() {

        SharedPref.putBoolean(Constants.IS_LOGIN, true)
        mLoginType = SharedPref.getString(Constants.PROFILE_TYPE, "")
    }

    private fun initViews() {

        llBand!!.visibility = View.GONE
    }

    private fun clickListener() {
        iv_home!!.setOnClickListener(this)
        iv_profile!!.setOnClickListener(this)
        iv_feature!!.setOnClickListener(this)
        iv_chat!!.setOnClickListener(this)
        iv_settings!!.setOnClickListener(this)
        ivDrawer!!.setOnClickListener(this)
        drawerLayout!!.setDrawerListener(this)
        ivUpArrow.setOnClickListener(this)
        llEventsInner.setOnClickListener(this)
        ivDownArrow.setOnClickListener(this)
        llBookingStatus.setOnClickListener(this)
        llTimmings.setOnClickListener(this)
        tvMyProfile.setOnClickListener(this)
        tvBandProfile.setOnClickListener(this)
        tvOtherBand.setOnClickListener(this)
        llHome.setOnClickListener(this)
        llActivity.setOnClickListener(this)
        llAlerts.setOnClickListener(this)
        llSchedule.setOnClickListener(this)
        llUpload.setOnClickListener(this)
        llBand.setOnClickListener(this)
        llSearch.setOnClickListener(this)
        llStats.setOnClickListener(this)
        llSettings.setOnClickListener(this)
        llLogout.setOnClickListener(this)


    }

    private fun uploadLatLng() {
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
        tvUserName!!.text = SharedPref.getString(Constants.USER_NAME, "")
        tvType!!.text = SharedPref.getString(Constants.PROFILE_TYPE, "")
        tvId!!.text = "ID : " + SharedPref.getString(Constants.UNIQUE_CODE, "")
        val mUrl = SharedPref.getString(Constants.PROFILE_IMAGE, "")
        if (mUrl.equals("", ignoreCase = true)) {
            Glide.with(this)
                    .load(R.drawable.icon_img_dummy)
                    .into(ivProfile!!)
        } else {
            Glide.with(this)
                    .load(mUrl)
                    .into(ivProfile!!)
        }
    }

    private fun openDefaultFragment() {
        fragment = HomeFragment()
        if (intent.getStringExtra("frag") == null) changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment) else {
            if (intent.getStringExtra("frag").equals("com.musicseque.fragments.HomeFragment", ignoreCase = true)) {
                fragment = HomeFragment()
                changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
            } else if (intent.getStringExtra("frag").equals("com.musicseque.artist.fragments.ProfileFragment", ignoreCase = true)) {
                val b = intent.getBooleanExtra("profileTemp", false)
                 if (b == null) {
                    if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true)) fragment =ProfileFragment() else fragment =ProfileDetailFragment()
                } else {
                     fragment =ProfileFragment()
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
            } else if (intent.getStringExtra("frag").equals("com.musicseque.venue_manager.fragment.CreateVenueFragment", ignoreCase = true)) {
                val b = intent.getBooleanExtra("profileTemp", false)
                if (b == null) {
                    if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true)) //fragment = new CreateVenueFragment();
                        Log.e("", "") else fragment = VenueProfileDetailFragment()
                } else { //  fragment = new CreateVenueFragment();
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
            } else if (intent.getStringExtra("frag").equals("com.musicseque.artist.fragments.BandFormFragment", ignoreCase = true)) {
                fragment = BandFormFragment()
                val args = Bundle()
                args.putString("band_id", intent.getStringExtra("band_id"))
                fragment.setArguments(args)
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
            } else if (intent.getStringExtra("frag").equals("com.musicseque.fragments.SettingFragment", ignoreCase = true)) {
                fragment = SettingFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.settingactive3, fragment)
            } else if (intent.getStringExtra("frag").equals("com.musicseque.music_lover.fragments.FragmentProfileMusicLover", ignoreCase = true)) {
                val b = intent.getBooleanExtra("profileTemp", false)
               if (b == null) {
                    if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true))
                        fragment = FragmentProfileMusicLover() else  fragment = ProfileDetailFragment()
                } else {
                   fragment = FragmentProfileMusicLover()
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
            }
            else if (intent.getStringExtra("frag").equals("com.musicseque.event_manager.fragment.EventManagerFormFragment", ignoreCase = true))
            {
                val b = intent.getBooleanExtra("profileTemp", false)
                if (b == null) {
                    if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true)) {
                        fragment=EventManagerFormFragment()
                    } else {
                        fragment=EventManagerDetailFragment()
                    }

                } else {
                    fragment= EventManagerDetailFragment()
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_home -> {
                fragment = HomeFragment()
                changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
            }
            R.id.iv_profile -> {
                if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true)) {
                    if (mLoginType.equals("Venue Manager", ignoreCase = true)) { //   fragment = new CreateVenueFragment();
                    } else if (mLoginType.equals("Artist", ignoreCase = true)) {
                        fragment = ProfileFragment()
                    } else if (mLoginType.equals("Music Lover", ignoreCase = true)) {
                        fragment = FragmentProfileMusicLover()
                    } else if (mLoginType.equals("Event Manager", ignoreCase = true)) {
                        fragment = EventManagerFormFragment()
                    }
                } else {
                    if (mLoginType.equals("Venue Manager", ignoreCase = true)) {
                        fragment = VenueProfileDetailFragment()
                    } else if (mLoginType.equals("Artist", ignoreCase = true)) {
                        fragment = ProfileDetailFragment()
                    } else if (mLoginType.equals("Music Lover", ignoreCase = true)) {
                        fragment = FragmentProfileDetailMusicLover()
                    } else if (mLoginType.equals("Event Manager", ignoreCase = true)) {
                        fragment = EventManagerDetailFragment()
                    }
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
            }
            R.id.iv_feature -> {
                fragment = HomeFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featuredactive3, R.drawable.chat3, R.drawable.setting3, fragment)
            }
            R.id.iv_chat -> {
                fragment = HomeFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chatactive3, R.drawable.setting3, fragment)
            }
            R.id.iv_settings -> {
                fragment = SettingFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.settingactive3, fragment)
            }
            R.id.ivDrawer -> { // If navigation drawer is not open yet open it else close it.
                if (!drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout!!.openDrawer(Gravity.START)
                    val mUrl = SharedPref.getString(Constants.PROFILE_IMAGE, "")
                    if (mUrl.equals("", ignoreCase = true)) {
                        Glide.with(this)
                                .load(R.drawable.icon_img_dummy)
                                .into(ivProfile!!)
                    } else {
                        Glide.with(this)
                                .load(mUrl)
                                .into(ivProfile!!)
                    }
                } else {
                    drawerLayout!!.closeDrawer(Gravity.END)
                }
            }
            R.id.ivUpArrow -> {
                llAllProfile!!.visibility = View.GONE
                ivUpArrow!!.visibility = View.GONE
                ivDownArrow!!.visibility = View.VISIBLE
            }
            R.id.ivDownArrow -> {
                llAllProfile!!.visibility = View.VISIBLE
                ivDownArrow!!.visibility = View.GONE
                ivUpArrow!!.visibility = View.VISIBLE
            }
            R.id.tvMyProfile -> {
                fragment = if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true)) {
                    ProfileFragment()
                } else {
                    ProfileDetailFragment()
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
                llAllProfile!!.visibility = View.GONE
                ivUpArrow!!.visibility = View.GONE
                ivDownArrow!!.visibility = View.VISIBLE
            }
            R.id.tvBandProfile -> {
                fragment = BandListFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
                llAllProfile!!.visibility = View.GONE
                ivUpArrow!!.visibility = View.GONE
                ivDownArrow!!.visibility = View.VISIBLE
            }
            R.id.tvOtherBand -> {
                fragment = OtherBandListFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
                llAllProfile!!.visibility = View.GONE
                ivUpArrow!!.visibility = View.GONE
                ivDownArrow!!.visibility = View.VISIBLE
            }
            R.id.llActivity -> {
            }
            R.id.llBookingStatus -> {
                fragment = VenueBookingStatusFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
            }
            R.id.llHome -> {
                fragment = HomeFragment()
                changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
                closeProfile()
            }
            R.id.llAlerts -> {
                startActivity(Intent(this, NotificationActivity::class.java))
                drawerLayout!!.closeDrawers()
                closeProfile()
            }
            R.id.llSchedule -> {
            }
            R.id.llTimmings -> {
                fragment = VenueTimmingsFragment()
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
            }
            R.id.llUpload -> {
                if (SharedPref.getString(Constants.PROFILE_TYPE, "").equals("Artist", ignoreCase = true)) {
                    startActivity(Intent(this, UploadActivity::class.java))
                } else if (mLoginType.equals("Venue Manager", ignoreCase = true)) {
                    Utils.showToast(this@MainActivity, "Currently working")
                    // startActivity(new Intent(this, UploadVenueMediaActivity.class));
                }
                drawerLayout!!.closeDrawers()
                closeProfile()
            }
            R.id.llBand -> {
            }
            R.id.llSearch -> {
                startActivity(Intent(this@MainActivity, SearchArtistActivity::class.java))
                closeProfile()
            }
            R.id.llStats -> {
            }
            R.id.llSettings -> {
                fragment = SettingFragment()
                replaceFragment(fragment)
                drawerLayout!!.closeDrawers()
                changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.settingactive3, fragment)
                closeProfile()
            }
            R.id.llLogout -> if (Utils.isNetworkConnected(this)) {
                Utils.initializeAndShow(this)
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_LOGOUT, this@MainActivity)
                clearLoginCredentials()
            } else {
                Utils.showToast(this, resources.getString(R.string.err_no_internet))
            }
            R.id.llEventsInner -> {
                tvHeading.setText("Events")
                if (mLoginType.equals("Event Manager", ignoreCase = true)) {
                    fragment = EventStatusFragment()
                    replaceFragment(fragment)
                    drawerLayout!!.closeDrawers()
                } else if (mLoginType.equals("Artist", ignoreCase = true)) {
                    fragment = EventsArtistFragment()
                    replaceFragment(fragment)
                    drawerLayout!!.closeDrawers()
                }
            }
        }


        closeProfile()
    }

    private fun changeIconBottom(home: Int, profile: Int, featured: Int, chat: Int, setting: Int, fragment: Fragment?) {
        iv_home!!.setImageResource(home)
        iv_profile!!.setImageResource(profile)
        iv_feature!!.setImageResource(featured)
        iv_chat!!.setImageResource(chat)
        iv_settings!!.setImageResource(setting)
        replaceFragment(fragment)
    }

    fun replaceFragment(fragment: Fragment?) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
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
            // SharedPref.putString(Constants.EMAIL_ID, "").commit();
            SharedPref.putString(Constants.PROFILE_IMAGE, "")
            SharedPref.putString(Constants.COUNTRY_NAME, "")
            SharedPref.putString(Constants.LOGIN_TYPE, "")
            SharedPref.putString(Constants.UNIQUE_CODE, "")
            SharedPref.putString(Constants.IS_FIRST_LOGIN, "")
        } catch (e: Exception) {
        }
    }

    fun closeProfile() {
        if (mLoginType.equals("Venue Manager", ignoreCase = true) || mLoginType.equals("Music Lover", ignoreCase = true) || mLoginType.equals("Event Manager", ignoreCase = true)) {
        } else if (mLoginType.equals("Artist", ignoreCase = true)) {
            llAllProfile!!.visibility = View.GONE
            ivUpArrow!!.visibility = View.GONE
            ivDownArrow!!.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(myreceiver, IntentFilter("com.musicseque.NotificationCount"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(myreceiver)
    }

    private fun showHideDrawerViews() {
        if (mLoginType.equals("Venue Manager", ignoreCase = true)) { // tvAddEvent.setVisibility(View.GONE);
            llAllProfile!!.visibility = View.GONE
            ivUpArrow!!.visibility = View.GONE
            ivDownArrow!!.visibility = View.GONE
            llTimmings!!.visibility = View.VISIBLE
            viewTimmings!!.visibility = View.VISIBLE
            llSchedule!!.visibility = View.GONE
            viewSchedule!!.visibility = View.GONE
            llBookingStatus!!.visibility = View.VISIBLE
            viewBookingStatus!!.visibility = View.VISIBLE
            llSchedule!!.visibility = View.VISIBLE
            viewSchedule!!.visibility = View.VISIBLE
            tvProfile!!.setOnClickListener {
                if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true)) { // fragment = new CreateVenueFragment();
                } else {
                    fragment = VenueProfileDetailFragment()
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
            }
        } else if (mLoginType.equals("Artist", ignoreCase = true)) { // tvAddEvent.setVisibility(View.VISIBLE);
            llAllProfile!!.visibility = View.GONE
            ivUpArrow!!.visibility = View.GONE
            ivDownArrow!!.visibility = View.GONE
            llTimmings!!.visibility = View.GONE
            viewTimmings!!.visibility = View.GONE
            llSchedule!!.visibility = View.VISIBLE
            viewSchedule!!.visibility = View.VISIBLE
            llBookingStatus!!.visibility = View.GONE
            viewBookingStatus!!.visibility = View.GONE
            llSchedule!!.visibility = View.GONE
            viewSchedule!!.visibility = View.GONE
        } else if (mLoginType.equals("Event Manager", ignoreCase = true)) {
            ivUpArrow!!.visibility = View.GONE
            ivDownArrow!!.visibility = View.GONE
            llAllProfile!!.visibility = View.GONE
            llBand!!.visibility = View.GONE
            llBookingStatus!!.visibility = View.GONE
            llTimmings!!.visibility = View.GONE
            tvProfile!!.setOnClickListener {
                fragment = if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true)) {
                    EventManagerFormFragment()
                } else {
                    EventManagerDetailFragment()
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
            }
        } else if (mLoginType.equals("Music Lover", ignoreCase = true)) {
            llAllProfile!!.visibility = View.GONE
            ivUpArrow!!.visibility = View.GONE
            ivDownArrow!!.visibility = View.GONE
            llTimmings!!.visibility = View.GONE
            viewTimmings!!.visibility = View.GONE
            llSchedule!!.visibility = View.GONE
            viewSchedule!!.visibility = View.GONE
            llBookingStatus!!.visibility = View.GONE
            viewBookingStatus!!.visibility = View.GONE
            llSchedule!!.visibility = View.GONE
            viewSchedule!!.visibility = View.GONE
            llEvents!!.visibility = View.GONE
            viewBookingStatus!!.visibility = View.GONE
            tvProfile!!.setOnClickListener {
                fragment = if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equals("Y", ignoreCase = true)) {
                    FragmentProfileMusicLover()
                } else {
                    FragmentProfileDetailMusicLover()
                }
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment)
                drawerLayout!!.closeDrawers()
            }
        }
    }

    var myreceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            rlNotificationCount!!.visibility = View.VISIBLE
            tvNotificationCount!!.text = intent.getStringExtra("notification_count")
        }
    }

    override fun onDrawerSlide(view: View, v: Float) {
        if (!drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.openDrawer(Gravity.START)
            val mUrl = SharedPref.getString(Constants.PROFILE_IMAGE, "")
            if (mUrl.equals("", ignoreCase = true)) {
                Glide.with(this)
                        .load(R.drawable.icon_img_dummy)
                        .into(ivProfile!!)
            } else {
                Glide.with(this)
                        .load(mUrl)
                        .into(ivProfile!!)
            }
        } else {
            drawerLayout!!.closeDrawers()
        }
    }

    override fun onDrawerOpened(view: View) {
        Log.e("", "")
    }

    override fun onDrawerClosed(view: View) {
        Log.e("", "")
    }

    override fun onDrawerStateChanged(i: Int) {}
    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        if (TYPE == Constants.FOR_LOGOUT) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}