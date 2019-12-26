package com.musicseque.start_up


import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.widget.Toast


import com.musicseque.MainActivity
import com.musicseque.MyApplication
import com.musicseque.R
import com.musicseque.activities.BaseActivity


import com.musicseque.service.LocationService
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref


class SplashActivity : BaseActivity() {
    //    SharedPref SharedPref;
    //    SharedPref.Editor editor;
    //    private RetrofitComponent retrofitComponent;
    lateinit var locationManager: LocationManager


    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        MyApplication.context = this
        SharedPref.init(this)


        initOtherViews()
        Handler().postDelayed({ checkPermissions() }, 1000)

    }


    private fun checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!checkLocationPermission()) {
                requestLocationPermission()
            } else {

                checkGPS()
            }
        } else {
            checkGPS()
        }


    }


    private fun initOtherViews() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                stopService(Intent(this, LocationService::class.java))
                startService(Intent(this, LocationService::class.java))

            } else if (resultCode == Activity.RESULT_CANCELED) {


                checkGPS()
            }
        }
    }


    internal fun checkGPS() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //            startService(new Intent(this, LocationService.class));
            //
            //            new Handler().postDelayed(new Runnable() {
            //                @Override
            //                public void run() {
            //                    Intent intent = null;
            //                    if (SharedPref.getBoolean(Constants.IS_LOGIN, false)) {
            //                        intent = new Intent(SplashActivity.this, MainActivity.class);
            //                    } else {
            //                        intent = new Intent(SplashActivity.this, LoginActivity.class);
            //                        intent.putExtra("isEmailVerified", true);
            //
            //                    }
            //                    startActivity(intent);
            //                    finish();
            //                }
            //
            //
            //            }, 1000);
        } else {
            showGPSDisabledAlertToUser()
        }
    }


    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS"
                ) { dialog, id ->
                    val callGPSSettingIntent = Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(callGPSSettingIntent)
                }
        alertDialogBuilder.setNegativeButton("Cancel"
        ) { dialog, id -> dialog.cancel() }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    override fun onResume() {
        super.onResume()
        //        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
        //        SharedPref = retrofitComponent.getShared();
        //        editor = retrofitComponent.getEditor();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!checkLocationPermission()) {

            } else {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    stopService(Intent(this, LocationService::class.java))

                    startService(Intent(this, LocationService::class.java))

                    Handler().postDelayed({
                        var intent: Intent? = null
                        if (SharedPref.getBoolean(Constants.IS_LOGIN, false)) {
                            if (SharedPref.getString(Constants.PROFILE_TYPE, "").equals("Artist", ignoreCase = true))
                                intent = Intent(this@SplashActivity, MainActivity::class.java)
                            else if (SharedPref.getString(Constants.PROFILE_TYPE, "").equals("Event Manager", ignoreCase = true))
                                intent = Intent(this@SplashActivity, MainActivity::class.java)
                            else if (SharedPref.getString(Constants.PROFILE_TYPE, "").equals("Venue Manager", ignoreCase = true))
                                intent = Intent(this@SplashActivity, MainActivity::class.java)
                            else if (SharedPref.getString(Constants.PROFILE_TYPE, "").equals("Music Lover", ignoreCase = true))
                                intent = Intent(this@SplashActivity, MainActivity::class.java)
                        } else {
                            intent = Intent(this@SplashActivity, LoginActivity::class.java)
                            intent.putExtra("isEmailVerified", true)

                        }
                        startActivity(intent)
                        finish()
                    }, 1000)
                }
            }
        } else {


            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                stopService(Intent(this, LocationService::class.java))

                startService(Intent(this, LocationService::class.java))

                Handler().postDelayed({
                    var intent: Intent? = null
                    if (SharedPref.getBoolean(Constants.IS_LOGIN, false)) {
                        intent = Intent(this@SplashActivity, MainActivity::class.java)
                    } else {
                        intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        intent!!.putExtra("isEmailVerified", true)

                    }
                    startActivity(intent)
                    finish()
                }, 1000)
            }


        }


    }
}