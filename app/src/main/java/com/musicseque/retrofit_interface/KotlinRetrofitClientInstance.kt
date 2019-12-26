package com.musicseque.retrofit_interface

import com.musicseque.utilities.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class KotlinRetrofitClientInstance {


    companion object
    {
         var retrofit: Retrofit? = null
        private val BASE_URL = Constants.ROOT_URL
        private val BASE_URL1 ="http://htistestapi.htistelecom.in/api/"

        internal var okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS)
                .writeTimeout(60000, TimeUnit.SECONDS)
                .build()

        fun getRetrofitInstance(): Retrofit? {

            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build()
            }
            return retrofit
        }


        fun <S> createService(
                serviceClass: Class<S>): S? {
            return getRetrofitInstance()?.create(serviceClass)
        }
    }


}