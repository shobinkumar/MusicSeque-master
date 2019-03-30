package com.musicseque.artist.service;

import android.app.IntentService;
import android.content.Intent;

import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

public class CommonService extends IntentService implements MyInterface {


    public CommonService() {
        super("CommonService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String mAPI = intent.getStringExtra("API");
        String mParams = intent.getStringExtra("params");
        if (Utils.isNetworkConnected(this)) {
            if (mAPI.equalsIgnoreCase(Constants.UPDATE_STATUS_API))
                RetrofitAPI.callAPI(mParams, Constants.FOR_UPDATE_STATUS, CommonService.this);

        }


    }

    @Override
    public void sendResponse(Object response, int TYPE) {
        if (TYPE == Constants.FOR_UPDATE_STATUS) {

        }
    }
}
