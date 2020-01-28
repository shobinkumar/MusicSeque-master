package com.musicseque.retrofit_interface;


import com.musicseque.utilities.Constants;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface CommonInterfaceRetrofit {


    @Headers("Content-Type: application/json")
    @POST(Constants.API_LOGIN)
    Call<String> callLogin(@Body String jsonObject);


    @Headers("Content-Type: application/json")
    @POST(Constants.EMAIL_VERYFIY)
    Call<String> callEmailVerify(@Body String jsonObject);

    @Headers("Content-Type: application/json")
    @POST(Constants.RESEND_OTP_API)
    Call<String> callResendOTP(@Body String jsonObject);


    @Headers("Content-Type: application/json")
    @POST(Constants.API_SIGN_UP)
    Call<String> callSignUp(@Body String jsonObject);

    @Headers("Content-Type: application/json")
    @POST(Constants.ACCOUNT_EXISTS_API)
    Call<String> callAccountExistingAPI(@Header("accept") String type, @Body String jsonObject);

    @Headers("Content-Type: application/json")
    @POST(Constants.RESET_PASSWORD_API)
    Call<String> callResetPassword(@Body String jsonObject);

    @Headers("Content-Type: application/json")
    @POST(Constants.LOGIN_SOCIAL_API)
    Call<String> callSocialLogin(@Body String jsonObject);


    @Headers("Content-Type: application/json")
    @POST(Constants.ACCOUNT_VERIFY_API)
    Call<String> callVerifyAccount(@Body String jsonObject);


    @Headers("Content-Type: application/json")
    @POST(Constants.UPDATE_USER_INFO_API)
    Call<String> callUserInfoUpdate(@Body String jsonObject);

    @Headers("Content-Type: application/json")
    @POST(Constants.RETRIEVE_USER_PROFILE_API)
    Call<String> callUserProfile(@Body String jsonObject);


    @Headers("Content-Type: application/json")
    @POST(Constants.RETRIEVE_ARTIST_UPLOADED_IMAGES_API)
    Call<String> callArtistUploadedImages(@Body String params);


// @Multipart
// @Headers("Content-Type: application/json")
// @POST(Constants.UPDATE_PROFILE)
// Call<String> callUploadImages(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("Id") RequestBody id);


    @GET(Constants.GENRE_LIST_API)
    Call<String> callGenreList();

    @GET(Constants.COUNTRY_LIST_API)
    Call<String> callCountryList();

    @Headers("Content-Type: application/json")
    @POST(Constants.GET_UPLOADED_MEDIA_API)
    Call<String> callArtistUploadedAudioAndVideo(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.CHANGE_PASSWORD_API)
    Call<String> callChangePasswordMethod(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.UPDATE_STATUS_API)
    Call<String> callUpdateStatusMethod(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.DELETE_ARTIST_IMAGES_API)
    Call<String> callDeleteArtistImagesMethod(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.DELETE_ARTIST_MEDIA_FILES_API)
    Call<String> callDeleteArtistMediaMethod(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.DELETE_ACCOUNT_API)
    Call<String> callDeleteAccountMethod(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.GET_ARTIST_LIST_API)
    Call<String> getArtistListMethod(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.SEARCH_ARTIST_LIST_API)
    Call<String> getSearchArtistListMethod(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.UPLOAD_LAT_LNG_API)
    Call<String> uploadLatLongMethod(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.FOLLOW_UNFOLLOW_ARTIST_API)
    Call<String> followUnfollowArtist(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.OTHER_PROFILE_API)
    Call<String> getOtherProfile(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.GET_BAND_DETAIL_API)
    Call<String> getBandProfile(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.UPDATE_BAND_PROFILE_API)
    Call<String> updateBandProfile(@Body String params);


    //For Band
    @Headers("Content-Type: application/json")
    @POST(Constants.UPDATE_BAND_AVAILABILITY_STATUS_API)
    Call<String> callBandAvailabilityStatusMethod(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.GET_BAND_LIST_API)
    Call<String> getBandList(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.DELETE_BAND_API)
    Call<String> deleteBand(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.SEARCH_BAND_MEMBER_API)
    Call<String> searchBandMember(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.BAND_MEMBER_STATUS_API)
    Call<String> bandMemberStatus(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.BAND_MEMBER_REMOVE_API)
    Call<String> bandMemberRemove(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.BAND_MEMBER_ADD_API)
    Call<String> bandMemberAdd(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.NOTIFICATION_API)
    Call<String> methodNotification(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.ACCEPT_REJECT_BAND_REQUEST_API)
    Call<String> methodAcceptBandRequest(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.ACCEPT_REJECT_BAND_REQUEST_API)
    Call<String> methodRejectBandRequest(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.OTHER_BAND_LIST_API)
    Call<String> methodOtherBandList(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.SEARCH_BAND_LIST_API)
    Call<String> methodBandListSearch(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.LOGOUT_API)
    Call<String> methodForLogout(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.TEST_API)
    Call<String>  methodTest(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(Constants.STATE_LIST_API)
    Call<String>  methodForStateList(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.CITY_LIST_API)
    Call<String>  methodForCityList(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.SEARCH_ARTIST_EVENT_MANAGER_API)
    Call<String>  methodSearchArtistEventManager(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.SEND_REQ_TO_ARTIST_FOR_EVENT_API)
    Call<String>  methodForSendArtistReq(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(Constants.ARTIST_EVENT_ACCEPT_REJECT_API)
    Call<String>  methodForAcceptRejectEventReq(@Body String params);

}