package com.musicseque.start_up;


import android.content.DialogInterface;
import android.content.Intent;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


import com.musicseque.MainActivity;
import com.musicseque.MyApplication;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;


import com.musicseque.event_manager.activity.MainActivityEventManager;
import com.musicseque.service.LocationService;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.SharedPref;


public class SplashActivity extends BaseActivity {
//    SharedPref SharedPref;
//    SharedPref.Editor editor;
//    private RetrofitComponent retrofitComponent;
    LocationManager locationManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MyApplication.context = this;
        SharedPref.init(getApplicationContext());


        initOtherViews();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissions();
            }
        }, 1000);

    }


    private void checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!checkLocationPermission()) {
                requestLocationPermission();
            } else {

                checkGPS();
            }
        } else {
            checkGPS();
        }


    }


    private void initOtherViews() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                stopService(new Intent(this, LocationService.class));
                startService(new Intent(this, LocationService.class));

            } else if (resultCode == RESULT_CANCELED) {


                checkGPS();
            }
        }
    }


    void checkGPS() {
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
            showGPSDisabledAlertToUser();
        }
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
//        SharedPref = retrofitComponent.getShared();
//        editor = retrofitComponent.getEditor();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!checkLocationPermission()) {

            } else {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    stopService(new Intent(this, LocationService.class));

                    startService(new Intent(this, LocationService.class));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = null;
                            if (SharedPref.getBoolean(Constants.IS_LOGIN, false)) {
                                if (SharedPref.getString(Constants.PROFILE_TYPE, "").equalsIgnoreCase("Artist"))
                                    intent = new Intent(SplashActivity.this, MainActivity.class);
                                else if (SharedPref.getString(Constants.PROFILE_TYPE, "").equalsIgnoreCase("EventManager"))
                                    intent = new Intent(SplashActivity.this, MainActivityEventManager.class);
                                else if (SharedPref.getString(Constants.PROFILE_TYPE, "").equalsIgnoreCase("Venue Manager"))
                                    intent = new Intent(SplashActivity.this, MainActivity.class);
                            } else {
                                intent = new Intent(SplashActivity.this, LoginActivity.class);
                                intent.putExtra("isEmailVerified", true);

                            }
                            startActivity(intent);
                            finish();
                        }


                    }, 1000);
                }
            }
        } else {


            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                stopService(new Intent(this, LocationService.class));

                startService(new Intent(this, LocationService.class));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = null;
                        if (SharedPref.getBoolean(Constants.IS_LOGIN, false)) {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent.putExtra("isEmailVerified", true);

                        }
                        startActivity(intent);
                        finish();
                    }


                }, 1000);
            }


        }


    }
}