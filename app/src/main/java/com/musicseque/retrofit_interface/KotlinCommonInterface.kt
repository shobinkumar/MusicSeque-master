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
    @POST(Constants.VENUE_ADD_UPDATE_PROFILE_API)
    abstract fun methodForVenueProfileSaveUpdate(@Body params:String): Call<String>


    @Headers("Content-Type: application/json")
    @POST(Constants.GET_EVENTS_LIST_API)
    abstract fun getEventsListMethod(@Body params:String): Call<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.DELETE_EVENT_API)
    abstract fun methodForDeleteEvent(@Body params: String): Call<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.GET_VENUE_TIMMINGS_API)
    abstract fun methodForGetVenueTimmings(@Body params: String): Call<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.VENUE_BOOK_API)
    abstract fun methodForVenueBook(@Body params: String): Call<String>

      @Headers("Content-Type: application/json")
    @POST(Constants.VENUE_TO_TIMMINGS_API)
    abstract fun methodForVenueToTimmings(@Body params: String): Call<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.VENUE_FROM_TIMMINGS_API)
    abstract fun methodForVenueFromTimmings(@Body params: String): Call<String>


    @Headers("Content-Type: application/json")
    @POST(Constants.VENUE_SUBMIT_TIMMINGS_API)
    abstract fun methodForSubmitVenueTimmings(@Body params: String): Call<String>


    @Headers("Content-Type: application/json")
    @POST(Constants.VENUE_BOOKED_PENDING_TIMMINGS_API)
    abstract fun methodForVenueBookedPendingTimmings(@Body params: String): Call<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.VENUE_ACCEPT_REJECT_API)
    abstract fun methodAcceptRejectRequest(@Body params: String): Call<String>

    @Headers("Content-Type: application/json")
    @POST(Constants.EVENT_STATUS_API)
    abstract fun methodEventStatus(@Body params: String): Call<String>


 @Headers("Content-Type: application/json")
 @POST(Constants.TEST_API)
 abstract fun methodTest(@Body params: String): Call<String>


}