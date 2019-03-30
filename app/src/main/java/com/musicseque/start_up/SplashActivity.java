package com.musicseque.start_up;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;


import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.dagger_data.DaggerRetrofitComponent;
import com.musicseque.dagger_data.RetrofitComponent;
import com.musicseque.dagger_data.SharedPrefDependency;

import com.musicseque.utilities.Constants;


public class SplashActivity extends BaseActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RetrofitComponent retrofitComponent;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);





        initOtherViews();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (sharedPreferences.getBoolean(Constants.IS_LOGIN, false)) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.putExtra("isEmailVerified",true);

                }
                startActivity(intent);
                finish();
            }


        }, 3000);

    }

//    private void hitAPI() throws JSONException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("siteUploadedId", "1145");
//        jsonObject.put("EmpId", "1");
//        jsonObject.put("ProjectId", "1");
//        jsonObject.put("SiteId", "1");
//        jsonObject.put("Latitude", "");
//        jsonObject.put("Longitude", "");
//        jsonObject.put("Message", "");
//        jsonObject.put("IsLocationOnOff", "");
//        jsonObject.put("OtherSite", "");
//        jsonObject.put("ActivityId", "1");
//        jsonObject.put("ActivityOther", "");
//        String data= jsonObject.toString();
//        CommonInterface apiService = RetrofitClientInstance.getRetrofitInstance().create(CommonInterface.class);
//        Call<String> call = apiService.StartDayData(data);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("Stat day",response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("Stat day",t.getLocalizedMessage().toString());
//
//            }
//        });
//
//    }

    private void initOtherViews() {
        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
        sharedPreferences = retrofitComponent.getShared();
        editor = retrofitComponent.getEditor();

    }

}
