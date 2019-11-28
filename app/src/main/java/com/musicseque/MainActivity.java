package com.musicseque;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.musicseque.artist.activity.other_artist_activity.SearchArtistActivity;
import com.musicseque.artist.activity.UploadActivity;
import com.musicseque.artist.band.band_fragment.BandFormFragment;
import com.musicseque.artist.band.band_fragment.BandListFragment;
import com.musicseque.artist.fragments.ProfileDetailFragment;
import com.musicseque.artist.fragments.ProfileFragment;
import com.musicseque.artist.fragments.UploadPhotoFragment;
import com.musicseque.artist.other_band.fragments.OtherBandListFragment;
import com.musicseque.artist.service.CommonService;

import com.musicseque.event_manager.activity.CreateEventActivity;
import com.musicseque.event_manager.activity.EventsListActivity;
import com.musicseque.firebase_notification.NotificationActivity;
import com.musicseque.fragments.HomeFragment;
import com.musicseque.fragments.SettingFragment;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.service.LocationService;
import com.musicseque.start_up.LoginActivity;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.SharedPref;
import com.musicseque.utilities.Utils;
import com.musicseque.venue_manager.fragment.VenueBookingStatusFragment;
import com.musicseque.venue_manager.fragment.CreateVenueFragment;
import com.musicseque.venue_manager.fragment.VenueProfileDetailFragment;
import com.musicseque.venue_manager.fragment.VenueTimmingsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.musicseque.utilities.Constants.FOR_LOGOUT;
import static com.musicseque.utilities.Constants.PROFILE_TYPE;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, DrawerLayout.DrawerListener, MyInterface {

    private ImageView iv_home, iv_profile, iv_feature, iv_chat, iv_settings;


    //    SharedPref SharedPref;
//    SharedPref.SharedPref SharedPref;
//    private RetrofitComponent retrofitComponent;
    @BindView(R.id.tvAddEvent)
    TextView tvAddEvent;

    @BindView(R.id.llActivity)
    LinearLayout llActivity;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.ivActivity)
    ImageView ivActivity;

    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.ivHome)
    ImageView ivHome;

    @BindView(R.id.llProfile)
    LinearLayout llProfile;
    @BindView(R.id.tvProfile)
    TextView tvProfile;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;

    @BindView(R.id.llAllProfile)
    LinearLayout llAllProfile;
    @BindView(R.id.tvMyProfile)
    TextView tvMyProfile;
    @BindView(R.id.tvBandProfile)
    TextView tvBandProfile;
    @BindView(R.id.tvOtherBand)
    TextView tvOtherBand;
    @BindView(R.id.ivUpArrow)
    ImageView ivUpArrow;
    @BindView(R.id.ivDownArrow)
    ImageView ivDownArrow;

    @BindView(R.id.llAlerts)
    LinearLayout llAlerts;
    @BindView(R.id.tvAlerts)
    TextView tvAlerts;
    @BindView(R.id.ivAlerts)
    ImageView ivAlerts;
    @BindView(R.id.rlNotificationCount)
    RelativeLayout rlNotificationCount;
    @BindView(R.id.tvNotificationCount)
    TextView tvNotificationCount;


    @BindView(R.id.llBookingStatus)
    LinearLayout llBookingStatus;
    @BindView(R.id.tvBookingStatus)
    TextView tvBookingStatus;
    @BindView(R.id.ivBookingStatus)
    ImageView ivBookingStatus;
    @BindView(R.id.viewBookingStatus)
    View viewBookingStatus;


    @BindView(R.id.llSchedule)
    LinearLayout llSchedule;
    @BindView(R.id.tvSchedule)
    TextView tvSchedule;
    @BindView(R.id.ivSchedule)
    ImageView ivSchedule;
    @BindView(R.id.viewSchedule)
    View viewSchedule;


    @BindView(R.id.llTimmings)
    LinearLayout llTimmings;
    @BindView(R.id.tvTimmings)
    TextView tvTimmings;
    @BindView(R.id.ivTimmings)
    ImageView ivTimmings;
    @BindView(R.id.viewTimmings)
    View viewTimmings;


    @BindView(R.id.llUpload)
    LinearLayout llUpload;
    @BindView(R.id.tvUpload)
    TextView tvUpload;
    @BindView(R.id.ivUpload)
    ImageView ivUpload;

    @BindView(R.id.llBand)
    LinearLayout llBand;
    @BindView(R.id.tvBand)
    TextView tvBand;
    @BindView(R.id.ivBandMember)
    ImageView ivBandMember;

    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;

    @BindView(R.id.llStats)
    LinearLayout llStats;
    @BindView(R.id.tvStats)
    TextView tvStats;
    @BindView(R.id.ivStats)
    ImageView ivStats;

    @BindView(R.id.llSettings)
    LinearLayout llSettings;
    @BindView(R.id.tvSettings)
    TextView tvSettings;
    @BindView(R.id.ivSettings)
    ImageView ivSettings;

    @BindView(R.id.llLogout)
    LinearLayout llLogout;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.ivLogout)
    ImageView ivLogout;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @BindView(R.id.ivDrawer)
    ImageView ivDrawer;


    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvType)
    TextView tvType;

    @BindView(R.id.tvId)
    TextView tvId;


    @BindView(R.id.llAllEvents)
    LinearLayout llAllEvents;
    @BindView(R.id.ivDownArrowEvents)
    ImageView ivDownArrowEvents;
    @BindView(R.id.ivUpArrowEvents)
    ImageView ivUpArrowEvents;


    @BindView(R.id.drawerLayout)
    DrawerLayout navDrawer;
    private Fragment fragment;


    String mLoginType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initOtherViews();
        initViews();
        clickListener();
        uploadLatLng();
        defaultValues();
        openDefaultFragment();
        showHideDrawerViews();


    }


    private void initOtherViews() {

//        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
//        SharedPref = retrofitComponent.getShared();
//        SharedPref = retrofitComponent.getSharedPref();
        SharedPref.putBoolean(Constants.IS_LOGIN, true);
        mLoginType = SharedPref.getString(PROFILE_TYPE, "");


    }

    private void initViews() {
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        iv_feature = (ImageView) findViewById(R.id.iv_feature);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        iv_settings = (ImageView) findViewById(R.id.iv_settings);
        ivDrawer = (ImageView) findViewById(R.id.ivDrawer);
        llBand.setVisibility(View.GONE);

    }

    private void clickListener() {
        iv_home.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        iv_feature.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        iv_settings.setOnClickListener(this);
        ivDrawer.setOnClickListener(this);
        navDrawer.setDrawerListener(this);

    }

    private void uploadLatLng() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""));
            jsonObject.put("Latitude", LocationService.mLatitude);
            jsonObject.put("Longitude", LocationService.mLongitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startService(new Intent(this, CommonService.class).putExtra("API", Constants.UPLOAD_LAT_LNG_API).putExtra("params", jsonObject.toString()));

    }

    private void defaultValues() {
        tvUserName.setText(SharedPref.getString(Constants.USER_NAME, ""));
        tvType.setText(SharedPref.getString(Constants.PROFILE_TYPE, ""));
        tvId.setText("ID : " + SharedPref.getString(Constants.UNIQUE_CODE, ""));
        String mUrl = SharedPref.getString(Constants.PROFILE_IMAGE, "");
        if (mUrl.equalsIgnoreCase("")) {
            Glide.with(this)
                    .load(R.drawable.icon_img_dummy)
                    .into(ivProfile);
        } else {
            Glide.with(this)
                    .load(mUrl)
                    .into(ivProfile);
        }


    }

    private void openDefaultFragment() {


        fragment = new HomeFragment();
        if (getIntent().getStringExtra("frag") == null)
            changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
        else {


            if (getIntent().getStringExtra("frag").equalsIgnoreCase("com.musicseque.fragments.HomeFragment")) {
                fragment = new HomeFragment();
                changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);

            } else if (getIntent().getStringExtra("frag").equalsIgnoreCase("com.musicseque.artist.fragments.ProfileFragment")) {
                Boolean b = getIntent().getBooleanExtra("profileTemp", false);

                if (b == null) {
                    if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equalsIgnoreCase("Y"))
                        fragment = new ProfileFragment();
                    else
                        fragment = new ProfileDetailFragment();
                } else {

                    fragment = new ProfileFragment();

                }


                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);

            } else if (getIntent().getStringExtra("frag").equalsIgnoreCase("com.musicseque.venue_manager.fragment.CreateVenueFragment")) {
                Boolean b = getIntent().getBooleanExtra("profileTemp", false);

                if (b == null) {
                    if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equalsIgnoreCase("Y"))
                        fragment = new CreateVenueFragment();
                    else
                        fragment = new VenueProfileDetailFragment();
                } else {

                    fragment = new CreateVenueFragment();

                }


                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);

            } else if (getIntent().getStringExtra("frag").equalsIgnoreCase("com.musicseque.artist.fragments.BandFormFragment")) {


                fragment = new BandFormFragment();
                Bundle args = new Bundle();
                args.putString("band_id", getIntent().getStringExtra("band_id"));
                fragment.setArguments(args);
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
            } else if (getIntent().getStringExtra("frag").equalsIgnoreCase("com.musicseque.fragments.SettingFragment")) {
                fragment = new SettingFragment();

                changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.settingactive3, fragment);
            }

        }


    }

    @Override
    public void onClick(View view) {
        if (view == iv_home) {
            fragment = new HomeFragment();

            changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);


        } else if (view == iv_profile) {

            if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equalsIgnoreCase("Y")) {
                if (mLoginType.equalsIgnoreCase("Venue Manager")) {
                    fragment = new CreateVenueFragment();
                } else {
                    fragment = new ProfileFragment();
                }
            } else {
                if (mLoginType.equalsIgnoreCase("Venue Manager")) {
                    fragment = new VenueProfileDetailFragment();
                } else {
                    fragment = new ProfileDetailFragment();
                }
            }


            changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);


        } else if (view == iv_feature) {

            fragment = new HomeFragment();

            changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featuredactive3, R.drawable.chat3, R.drawable.setting3, fragment);

        } else if (view == iv_chat) {

            fragment = new HomeFragment();

            changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chatactive3, R.drawable.setting3, fragment);

        } else if (view == iv_settings) {

            fragment = new SettingFragment();

            changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.settingactive3, fragment);

        } else if (view == ivDrawer) {

            // If navigation drawer is not open yet open it else close it.
            if (!navDrawer.isDrawerOpen(GravityCompat.START)) {
                navDrawer.openDrawer(Gravity.START);
                String mUrl = SharedPref.getString(Constants.PROFILE_IMAGE, "");
                if (mUrl.equalsIgnoreCase("")) {
                    Glide.with(this)
                            .load(R.drawable.icon_img_dummy)
                            .into(ivProfile);
                } else {
                    Glide.with(this)
                            .load(mUrl)
                            .into(ivProfile);
                }
            } else {
                navDrawer.closeDrawer(Gravity.END);
            }
        }
        closeProfile();

    }

    private void changeIconBottom(int home, int profile, int featured, int chat, int setting, Fragment fragment) {


        iv_home.setImageResource(home);
        iv_profile.setImageResource(profile);
        iv_feature.setImageResource(featured);
        iv_chat.setImageResource(chat);
        iv_settings.setImageResource(setting);
        replaceFragment(fragment);

    }

    public void replaceFragment(Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }

    }


    @OnClick({R.id.llBookingStatus, R.id.llTimmings, R.id.ivDownArrowEvents, R.id.ivUpArrowEvents, R.id.tvAddEvent, R.id.tvPastEvents, R.id.tvUpcomingEvent, R.id.tvMyProfile, R.id.tvBandProfile, R.id.tvOtherBand, R.id.ivUpArrow, R.id.ivDownArrow, R.id.llActivity, R.id.llHome, R.id.llAlerts, R.id.llSchedule, R.id.llUpload, R.id.llBand, R.id.llSearch, R.id.llStats, R.id.llSettings, R.id.llLogout})
    public void onClicks(View view) {
        switch (view.getId()) {
            case R.id.ivUpArrow:
                llAllProfile.setVisibility(View.GONE);
                ivUpArrow.setVisibility(View.GONE);
                ivDownArrow.setVisibility(View.VISIBLE);

                break;
            case R.id.ivDownArrow:
                llAllProfile.setVisibility(View.VISIBLE);
                ivDownArrow.setVisibility(View.GONE);
                ivUpArrow.setVisibility(View.VISIBLE);
                break;
            case R.id.tvMyProfile:


                if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equalsIgnoreCase("Y")) {
                    fragment = new ProfileFragment();
                } else {
                    fragment = new ProfileDetailFragment();
                }


                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
                navDrawer.closeDrawers();
                llAllProfile.setVisibility(View.GONE);
                ivUpArrow.setVisibility(View.GONE);
                ivDownArrow.setVisibility(View.VISIBLE);
                break;
            case R.id.tvBandProfile:
                fragment = new BandListFragment();
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
                navDrawer.closeDrawers();
                llAllProfile.setVisibility(View.GONE);
                ivUpArrow.setVisibility(View.GONE);
                ivDownArrow.setVisibility(View.VISIBLE);
                break;
            case R.id.tvOtherBand:
                fragment = new OtherBandListFragment();
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
                navDrawer.closeDrawers();
                llAllProfile.setVisibility(View.GONE);
                ivUpArrow.setVisibility(View.GONE);
                ivDownArrow.setVisibility(View.VISIBLE);
                break;
            case R.id.llActivity:
                break;
            case R.id.llBookingStatus:
                fragment = new VenueBookingStatusFragment();
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
                navDrawer.closeDrawers();
                break;
            case R.id.llHome:


                fragment = new HomeFragment();
                changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
                navDrawer.closeDrawers();
                closeProfile();
                break;

            case R.id.llAlerts:
                startActivity(new Intent(this, NotificationActivity.class));
                navDrawer.closeDrawers();
                closeProfile();
                break;
            case R.id.llSchedule:
                break;


            case R.id.llTimmings:
                fragment = new VenueTimmingsFragment();
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
                navDrawer.closeDrawers();
                break;

            case R.id.llUpload:
                if (SharedPref.getString(Constants.PROFILE_TYPE, "").equalsIgnoreCase("Artist")) {
                    startActivity(new Intent(this, UploadActivity.class));
                } else if (mLoginType.equalsIgnoreCase("Venue Manager")) {
                    Utils.showToast(MainActivity.this, "Currently working");
                    // startActivity(new Intent(this, UploadVenueMediaActivity.class));
                }


                navDrawer.closeDrawers();
                closeProfile();

                break;
            case R.id.llBand:
                break;
            case R.id.llSearch:
                startActivity(new Intent(MainActivity.this, SearchArtistActivity.class));
                closeProfile();
                break;
            case R.id.llStats:
                break;
            case R.id.llSettings:
                fragment = new SettingFragment();
                replaceFragment(fragment);
                navDrawer.closeDrawers();
                changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.settingactive3, fragment);
                closeProfile();
                break;
            case R.id.llLogout:

                if (Utils.isNetworkConnected(this)) {
                    Utils.initializeAndShow(this);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_LOGOUT, MainActivity.this);
                    clearLoginCredentials();
                } else {
                    Utils.showToast(this, getResources().getString(R.string.err_no_internet));
                }


                break;
            case R.id.ivDownArrowEvents:
                llAllEvents.setVisibility(View.VISIBLE);
                ivDownArrowEvents.setVisibility(View.GONE);
                ivUpArrowEvents.setVisibility(View.VISIBLE);


                break;
            case R.id.ivUpArrowEvents:
                llAllEvents.setVisibility(View.GONE);
                ivDownArrowEvents.setVisibility(View.VISIBLE);
                ivUpArrowEvents.setVisibility(View.GONE);

                break;

//            case R.id.tvAddEvent:
//                startActivity(new Intent(this, CreateEventActivity.class));
//                navDrawer.closeDrawers();
//
//                break;
            case R.id.tvUpcomingEvent:
                if (mLoginType.equalsIgnoreCase("Venue Manager")) {
                    Utils.showToast(MainActivity.this, "Coming Soon");
                } else {
                    startActivity(new Intent(this, EventsListActivity.class).putExtra("type", 2));
                    navDrawer.closeDrawers();
                }


                break;

            case R.id.tvPastEvents:

                if (mLoginType.equalsIgnoreCase("Venue Manager")) {
                    Utils.showToast(MainActivity.this, "Coming Soon");
                } else {
                    startActivity(new Intent(this, EventsListActivity.class).putExtra("type", 1));
                    navDrawer.closeDrawers();
                }


                break;


        }

    }

    void clearLoginCredentials() {
        try {
            SharedPref.putBoolean(Constants.IS_LOGIN, false);
            SharedPref.putString(Constants.COUNTRY_CODE, "");
            SharedPref.putString(Constants.COUNTRY_ID, "");
            SharedPref.putString(Constants.COUNTRY_NAME, "");
            SharedPref.putString(Constants.MOBILE_NUMBER, "");
            SharedPref.putString(Constants.PROFILE_IMAGE, "");
            SharedPref.putString(Constants.USER_NAME, "");
            SharedPref.putString(Constants.USER_ID, "");
            SharedPref.putString(Constants.PROFILE_TYPE, "");
            SharedPref.putString(Constants.PROFILE_ID, "");
            // SharedPref.putString(Constants.EMAIL_ID, "").commit();
            SharedPref.putString(Constants.PROFILE_IMAGE, "");
            SharedPref.putString(Constants.COUNTRY_NAME, "");
            SharedPref.putString(Constants.LOGIN_TYPE, "");

            SharedPref.putString(Constants.UNIQUE_CODE, "");
            SharedPref.putString(Constants.IS_FIRST_LOGIN, "");


        } catch (Exception e) {

        }


    }

    void closeProfile() {
        if (mLoginType.equalsIgnoreCase("Venue Manager")) {

        } else {
            llAllProfile.setVisibility(View.GONE);
            ivUpArrow.setVisibility(View.GONE);
            ivDownArrow.setVisibility(View.VISIBLE);
        }
        ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myreceiver, new IntentFilter("com.musicseque.NotificationCount"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myreceiver);
    }

    private void showHideDrawerViews() {
        if (mLoginType.equalsIgnoreCase("Venue Manager")) {
           // tvAddEvent.setVisibility(View.GONE);
            llAllProfile.setVisibility(View.GONE);
            ivUpArrow.setVisibility(View.GONE);
            ivDownArrow.setVisibility(View.GONE);
            llTimmings.setVisibility(View.VISIBLE);
            viewTimmings.setVisibility(View.VISIBLE);
            llSchedule.setVisibility(View.GONE);
            viewSchedule.setVisibility(View.GONE);
            llBookingStatus.setVisibility(View.VISIBLE);
            viewBookingStatus.setVisibility(View.VISIBLE);
            llSchedule.setVisibility(View.VISIBLE);
            viewSchedule.setVisibility(View.VISIBLE);
            tvProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SharedPref.getString(Constants.IS_FIRST_LOGIN, "").equalsIgnoreCase("Y")) {
                        fragment = new CreateVenueFragment();

                    } else {
                        fragment = new VenueProfileDetailFragment();

                    }
                    changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);
                    navDrawer.closeDrawers();
                }
            });
        } else {
           // tvAddEvent.setVisibility(View.VISIBLE);
            llAllProfile.setVisibility(View.GONE);
            ivUpArrow.setVisibility(View.GONE);
            ivDownArrow.setVisibility(View.VISIBLE);
            llTimmings.setVisibility(View.GONE);
            viewTimmings.setVisibility(View.GONE);
            llSchedule.setVisibility(View.VISIBLE);
            viewSchedule.setVisibility(View.VISIBLE);
            llBookingStatus.setVisibility(View.GONE);
            viewBookingStatus.setVisibility(View.GONE);
            llSchedule.setVisibility(View.GONE);
            viewSchedule.setVisibility(View.GONE);

        }


    }

    BroadcastReceiver myreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            rlNotificationCount.setVisibility(View.VISIBLE);
            tvNotificationCount.setText(intent.getStringExtra("notification_count"));
        }
    };

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {
        if (!navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.openDrawer(Gravity.START);
            String mUrl = SharedPref.getString(Constants.PROFILE_IMAGE, "");
            if (mUrl.equalsIgnoreCase("")) {
                Glide.with(this)
                        .load(R.drawable.icon_img_dummy)
                        .into(ivProfile);
            } else {
                Glide.with(this)
                        .load(mUrl)
                        .into(ivProfile);
            }
        } else {
            navDrawer.closeDrawers();
        }


    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
        Log.e("", "");

    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        Log.e("", "");

    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        if (TYPE == FOR_LOGOUT) {


            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

    }
}
