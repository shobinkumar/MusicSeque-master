package com.musicseque.dagger_data;


import android.content.SharedPreferences;

import dagger.Component;
import retrofit2.Retrofit;

@Component(modules = {RetrofitDependency.class,SharedPrefDependency.class})
@ApplicationLevelScope
public interface RetrofitComponent {
    Retrofit getRetrofit();
    SharedPreferences getShared();
    SharedPreferences.Editor getEditor();



}
