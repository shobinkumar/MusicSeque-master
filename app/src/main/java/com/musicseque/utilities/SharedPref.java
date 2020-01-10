package com.musicseque.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sourav on 21/11/16.
 */

public class SharedPref
{
    private static SharedPreferences mSharedPref;


    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences("mypreference", Activity.MODE_PRIVATE);
    }


    public static SharedPreferences getSharedPref()
    {
      return   mSharedPref;
    }

    public static SharedPreferences.Editor getEditor()
    {
        return  mSharedPref.edit();
    }


    public static String getString(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

//    public static boolean readString(String key, boolean defValue) {
//        return mSharedPref.getBoolean(key, defValue);
//    }
//
//    public static void writeString(String key, boolean value) {
//        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
//        prefsEditor.putBoolean(key, value);
//        prefsEditor.commit();
//    }

    public static Integer getInt(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void putInt(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value).commit();
    }
}
