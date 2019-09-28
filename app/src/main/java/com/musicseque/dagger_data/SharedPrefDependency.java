//package com.musicseque.dagger_data;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import dagger.Module;
//import dagger.Provides;
//
//
//@Module
//public class SharedPrefDependency {
//
//Context context;
//    public SharedPrefDependency(Context context) {
//
//    this.context=context;
//    }
//
//
//    @ApplicationLevelScope
//    @Provides
//    public SharedPreferences.Editor getEditor(SharedPreferences sharedPreferences)
//    {
//        return sharedPreferences.edit();
//    }
//
//
//    @ApplicationLevelScope
//    @Provides
//    public SharedPreferences getShared() {
//
//        return context.getSharedPreferences("mypreference",
//                Context.MODE_PRIVATE);
//
//    }
//
//
//}
