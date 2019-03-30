package com.musicseque.dagger_data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.musicseque.utilities.Constants;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    Application mApplication;


    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }



    @Provides
    @ApplicationLevelScope
    Application getApplication(Application application) {
        mApplication = application;
        return mApplication;
    }

    @Provides
    @ApplicationLevelScope
    SharedPreferences getSharedPreferences() {
        return mApplication.getSharedPreferences("mypreference",
                Context.MODE_PRIVATE);
    }

    @ApplicationLevelScope
    @Provides
    public Retrofit retrofit(GsonConverterFactory gsonConverterFactory){
        return new Retrofit.Builder()
                .baseUrl(Constants.ROOT_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }



    @Provides
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}
