package com.musicseque;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.musicseque.artist.activity.SearchArtistActivity;
import com.musicseque.artist.activity.UploadActivity;
import com.musicseque.artist.fragments.BandFormFragment;
import com.musicseque.artist.fragments.BandListFragment;
import com.musicseque.artist.fragments.ProfileDetailFragment;
import com.musicseque.artist.fragments.ProfileFragment;
import com.musicseque.artist.service.CommonService;
import com.musicseque.dagger_data.DaggerRetrofitComponent;
import com.musicseque.dagger_data.RetrofitComponent;
import com.musicseque.dagger_data.SharedPrefDependency;
import com.musicseque.fragments.HomeFragment;
import com.musicseque.fragments.SettingFragment;
import com.musicseque.service.LocationService;
import com.musicseque.start_up.LoginActivity;
import com.musicseque.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_home, iv_profile, iv_feature, iv_chat, iv_settings;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RetrofitComponent retrofitComponent;


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

    @BindView(R.id.llSchedule)
    LinearLayout llSchedule;
    @BindView(R.id.tvSchedule)
    TextView tvSchedule;
    @BindView(R.id.ivSchedule)
    ImageView ivSchedule;

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

    @BindView(R.id.drawerLayout)
    DrawerLayout navDrawer;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initOtherViews();
        initViews();
        clickListener();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
            jsonObject.put("Latitude", LocationService.mLatitude);
            jsonObject.put("Longitude", LocationService.mLongitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startService(new Intent(this, CommonService.class).putExtra("API", Constants.UPLOAD_LAT_LNG_API).putExtra("params", jsonObject.toString()));

        tvUserName.setText(sharedPreferences.getString(Constants.USER_NAME, ""));
        tvType.setText(sharedPreferences.getString(Constants.PROFILE_TYPE, ""));
        tvId.setText("ID : " + sharedPreferences.getString(Constants.UNIQUE_CODE, ""));
        String mUrl = sharedPreferences.getString(Constants.PROFILE_IMAGE, "");
        if (mUrl.equalsIgnoreCase("")) {
            Glide.with(this)
                    .load(R.drawable.dummy_profile)
                    .into(ivProfile);
        } else {
            Glide.with(this)
                    .load(mUrl)
                    .into(ivProfile);
        }


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
                    if (sharedPreferences.getString(Constants.IS_FIRST_LOGIN, "").equalsIgnoreCase("Y"))
                        fragment = new ProfileFragment();
                    else
                        fragment = new ProfileDetailFragment();
                } else {

                    fragment = new ProfileFragment();

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


    private void initOtherViews() {

        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
        sharedPreferences = retrofitComponent.getShared();
        editor = retrofitComponent.getEditor();
        editor.putBoolean(Constants.IS_LOGIN, true).commit();


    }

    private void initViews() {
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        iv_feature = (ImageView) findViewById(R.id.iv_feature);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        iv_settings = (ImageView) findViewById(R.id.iv_settings);
        ivDrawer = (ImageView) findViewById(R.id.ivDrawer);

    }

    private void clickListener() {
        iv_home.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        iv_feature.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        iv_settings.setOnClickListener(this);
        ivDrawer.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == iv_home) {
            fragment = new HomeFragment();

            changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);


        } else if (view == iv_profile) {

            if (sharedPreferences.getString(Constants.IS_FIRST_LOGIN, "").equalsIgnoreCase("Y"))
                fragment = new ProfileFragment();
            else
                fragment = new ProfileDetailFragment();

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
                String mUrl = sharedPreferences.getString(Constants.PROFILE_IMAGE, "");
                if (mUrl.equalsIgnoreCase("")) {
                    Glide.with(this)
                            .load(R.drawable.dummy_profile)
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

        }

    }


    @OnClick({R.id.tvMyProfile, R.id.tvBandProfile, R.id.ivUpArrow, R.id.ivDownArrow, R.id.llActivity, R.id.llHome, R.id.llAlerts, R.id.llSchedule, R.id.llUpload, R.id.llBand, R.id.llSearch, R.id.llStats, R.id.llSettings, R.id.llLogout})
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
                changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);

                if (sharedPreferences.getString(Constants.IS_FIRST_LOGIN, "").equalsIgnoreCase("Y"))
                    fragment = new ProfileFragment();
                else
                    fragment = new ProfileDetailFragment();

                replaceFragment(fragment);
                navDrawer.closeDrawers();
                break;
            case R.id.tvBandProfile:
                fragment = new BandListFragment();
                replaceFragment(fragment);
                navDrawer.closeDrawers();
                break;
            case R.id.llActivity:
                break;
            case R.id.llHome:
                changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);

                fragment = new HomeFragment();
                replaceFragment(fragment);
                navDrawer.closeDrawers();
                break;

            case R.id.llAlerts:
                break;
            case R.id.llSchedule:
                break;
            case R.id.llUpload:
                startActivity(new Intent(this, UploadActivity.class));
                navDrawer.closeDrawers();

                break;
            case R.id.llBand:
                break;
            case R.id.llSearch:
                startActivity(new Intent(MainActivity.this, SearchArtistActivity.class));

                break;
            case R.id.llStats:
                break;
            case R.id.llSettings:
                fragment = new SettingFragment();
                replaceFragment(fragment);
                navDrawer.closeDrawers();
                changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.settingactive3, fragment);

                break;
            case R.id.llLogout:
                clearLoginCredentials();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    void clearLoginCredentials() {
        try {
            editor.putBoolean(Constants.IS_LOGIN, false).commit();
            editor.putString(Constants.COUNTRY_CODE, "").commit();
            editor.putString(Constants.COUNTRY_ID, "").commit();
            editor.putString(Constants.COUNTRY_NAME, "").commit();
            editor.putString(Constants.MOBILE_NUMBER, "").commit();
            editor.putString(Constants.PROFILE_IMAGE, "").commit();
            editor.putString(Constants.USER_NAME, "").commit();
            editor.putString(Constants.USER_ID, "").commit();
            editor.putString(Constants.PROFILE_TYPE, "").commit();
            editor.putString(Constants.PROFILE_ID, "").commit();
            // editor.putString(Constants.EMAIL_ID, "").commit();
            editor.putString(Constants.PROFILE_IMAGE, "").commit();
            editor.putString(Constants.COUNTRY_NAME, "").commit();
            editor.putString(Constants.LOGIN_TYPE, "").commit();

            editor.putString(Constants.UNIQUE_CODE, "").commit();
            editor.putString(Constants.IS_FIRST_LOGIN, "").commit();

        } catch (Exception e) {

        }


    }
}
