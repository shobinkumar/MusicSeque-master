package com.musicseque;

import android.app.Activity;
import android.app.Application;


public class MyApplication extends Application {

    private static MyApplication instance;
    public  static Activity context;




    @Override
    public void onCreate() {
        super.onCreate();






    }


    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


}
