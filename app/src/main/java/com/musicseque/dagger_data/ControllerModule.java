package com.musicseque.dagger_data;


import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ControllerModule {

    Activity mActivity;

    public ControllerModule(Activity activity) {
        mActivity = activity;
    }


    @Provides
    @ActivityLevelScope
    Activity getActivty() {
        return mActivity;
    }


    @Provides
    @ActivityLevelScope
    Context getContext() {
        return mActivity;
    }


}
