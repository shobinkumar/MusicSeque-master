package com.musicseque.artist.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.musicseque.retrofit_interface.CommonInterfaceRetrofit;
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
            else if(mAPI.equalsIgnoreCase(Constants.UPDATE_BAND_AVAILABILITY_STATUS_API))
            {
                callAPI(mParams, Constants.FOR_BAND_VISIBILITY_STATUS);
            }
        }


    }


    public void callAPI(String mParams, int TYPE) {
        CommonInterfaceRetrofit api = RetrofitClientInstance.createService(CommonInterfaceRetrofit.class);
        Call<String> call = null;
        if (TYPE == Constants.FOR_UPDATE_STATUS) {
            call = api.callUpdateStatusMethod(mParams);
        } else if (TYPE == Constants.FOR_LAT_LNG) {
            call = api.uploadLatLongMethod(mParams);

        }
        else if(TYPE == Constants.FOR_BAND_VISIBILITY_STATUS)
        {
            call = api.callBandAvailabilityStatusMethod(mParams);
        }
        callMethod(call, TYPE);

    }

    private  <T> void callMethod(Call call, int type) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                Log.e("Re", "");

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("Re", "");

            }
        });
    }


}
