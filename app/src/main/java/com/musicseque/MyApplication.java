package com.musicseque;

import android.app.Activity;
import android.app.Application;

import com.musicseque.dagger_data.ApplicationComponent;


public class MyApplication extends Application {

    private static MyApplication instance;
    private ApplicationComponent appComponent;
    public  static Activity context;




    @Override
    public void onCreate() {
        super.onCreate();






    }

    public ApplicationComponent getAppComponent() {

        return appComponent;
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


}
