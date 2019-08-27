package com.musicseque.retrofit_interface

import com.musicseque.utilities.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

public interface KotlinCommonInterface {

    @Headers("Content-Type: application/json")
    @POST(Constants.EVENT_SAVE_UPDATE_DETAIL_API)
     fun methodEventSaveUpdateDetail(@Body params:String): Call<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.EVENT_DETAIL_API)
    fun methodEventDetail(@Body params:String): Call<String>


    @Headers("Content-Type: application/json")
    @GET(Constants.EVENT_TYPE_LIST_API)
   abstract fun methodEvents(): Call<String>

    @Headers("Content-Type: application/json")
    @GET(Constants.CURRENCY_LIST_API)
    abstract fun methodCurrency(): Call<String>



    @Headers("Content-Type: application/json")
    @POST(Constants.VENUE_LIST_API)
    abstract fun methodForVenueList(@Body params:String): Call<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.VENUE_PROFILE_API)
    abstract fun methodForVenueProfile(@Body params:String): Call<String>



}