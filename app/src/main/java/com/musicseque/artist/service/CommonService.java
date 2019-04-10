package com.musicseque.artist.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.musicseque.retrofit_interface.CommonInterface;
import com.musicseque.retrofit_interface.RetrofitClientInstance;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonService extends IntentService {


    public CommonService() {
        super("CommonService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String mAPI = intent.getStringExtra("API");
        String mParams = intent.getStringExtra("params");
        if (Utils.isNetworkConnected(this)) {
            if (mAPI.equalsIgnoreCase(Constants.UPDATE_STATUS_API))
                callAPI(mParams, Constants.FOR_UPDATE_STATUS);

            else if (mAPI.equalsIgnoreCase(Constants.UPLOAD_LAT_LNG_API)) {
                callAPI(mParams, Constants.FOR_LAT_LNG);

            }
        }


    }


    public void callAPI(String mParams, int TYPE) {
        CommonInterface api = RetrofitClientInstance.createService(CommonInterface.class);
        Call<String> call = null;
        if (TYPE == Constants.FOR_UPDATE_STATUS) {
            call = api.callUpdateStatusMethod(mParams);
        } else if (TYPE == Constants.FOR_LAT_LNG) {
            call = api.uploadLatLongMethod(mParams);

        }
        callMethod(call, TYPE);

    }

    private  <T> void callMethod(Call call, int type) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {


            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("Re", "");

            }
        });
    }


}
