package com.musicseque.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferenceClass {

    private static MyPreferenceClass preferenceClass;
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    private MyPreferenceClass() {
    }

    ;


    public static MyPreferenceClass getInstance(Context context) {

        if (preferenceClass == null) {
            preferenceClass=new MyPreferenceClass();
           sharedPref = context.getSharedPreferences("mypreference",
                    Context.MODE_PRIVATE);
           editor=sharedPref.edit();
        }

        return preferenceClass;


    }


    public static void putString(String key,String value)
    {
        editor.putString(key,value).commit();
    }
    public static void putInt(String key,int value)
    {
        editor.putInt(key,value).commit();
    }
    public static void putBoolean(String key,boolean value)
    {
        editor.putBoolean(key,value).commit();
    }

    public static void getString(String key)
    {
        sharedPref.getString(key,"");
    }
    public static void getInt(String key)
    {
        sharedPref.getInt(key,0);
    }
    public static void getBoolean(String key)
    {
        sharedPref.getBoolean(key,false);
    }

}
