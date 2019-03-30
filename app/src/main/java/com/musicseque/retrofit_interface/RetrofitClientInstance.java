package com.musicseque.retrofit_interface;

import com.musicseque.utilities.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitClientInstance {

        private static Retrofit retrofit;
        private static final String BASE_URL =Constants.ROOT_URL;

    static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60000, TimeUnit.SECONDS)
            .readTimeout(60000, TimeUnit.SECONDS)
            .writeTimeout(60000, TimeUnit.SECONDS)
            .build();
        public static Retrofit getRetrofitInstance() {

            if (retrofit == null) {
                retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL)
                       .client(okHttpClient)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
            }
            return retrofit;
        }


        public static <S> S createService(
                Class<S> serviceClass) {
            return getRetrofitInstance().create(serviceClass);
        }


    }


