package com.musicseque.retrofit_interface;

import android.util.Log;

import com.musicseque.interfaces.MyInterface;
import com.musicseque.utilities.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitAPI {

    static MyInterface commonInterface;

    public static void callAPI(String params, int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;


        CommonInterfaceRetrofit api = RetrofitClientInstance.createService(CommonInterfaceRetrofit.class);


        Call<String> call = null;

        if (TYPE == Constants.FOR_LOGIN) {
            call = api.callLogin(params);
        } else if (TYPE == Constants.FOR_FORGOT_PASSWORD) {
            call = api.callEmailVerify(params);
        } else if (TYPE == Constants.FOR_RESEND_OTP) {
            call = api.callResendOTP(params);
        } else if (TYPE == Constants.FOR_SIGNUP) {
            call = api.callSignUp(params);
        } else if (TYPE == Constants.FOR_ACCOUNT_EXISTS) {
            call = api.callAccountExistingAPI("application/json", params);
        } else if (TYPE == Constants.FOR_SOCIAL_LOGIN) {
            call = api.callSocialLogin(params);
        } else if (TYPE == Constants.FOR_RESET_PASSWORD) {
            call = api.callResetPassword(params);
        } else if (TYPE == Constants.FOR_VERIFY_ACCOUNT) {
            call = api.callVerifyAccount(params);
        } else if (TYPE == Constants.FOR_UPDATE_PROFILE) {
            call = api.callUserInfoUpdate(params);
        } else if (TYPE == Constants.FOR_USER_PROFILE) {
            call = api.callUserProfile(params);
        } else if (TYPE == Constants.FOR_ARTIST_UPLOADED_IMAGES) {
            call = api.callArtistUploadedImages(params);
        } else if (TYPE == Constants.FOR_UPLOADED_AUDIO) {
            call = api.callArtistUploadedAudioAndVideo(params);
        } else if (TYPE == Constants.FOR_CHANGE_PASSWORD) {
            call = api.callChangePasswordMethod(params);
        } else if (TYPE == Constants.FOR_UPDATE_STATUS) {
            call = api.callUpdateStatusMethod(params);
        } else if (TYPE == Constants.FOR_UPLOADED_VIDEO) {
            call = api.callArtistUploadedAudioAndVideo(params);

        } else if (TYPE == Constants.FOR_DELETE_ARTIST_IMAGES) {
            call = api.callDeleteArtistImagesMethod(params);

        } else if (TYPE == Constants.FOR_DELETE_ARTIST_MEDIA) {
            call = api.callDeleteArtistMediaMethod(params);

        } else if (TYPE == Constants.FOR_DELETE_ACCOUNT) {
            call = api.callDeleteAccountMethod(params);

        } else if (TYPE == Constants.GET_ARTIST_LIST) {
            call = api.getArtistListMethod(params);

        } else if (TYPE == Constants.SEARCH_ARTIST) {
            call = api.getSearchArtistListMethod(params);

        } else if (TYPE == Constants.FOR_LAT_LNG) {
            call = api.uploadLatLongMethod(params);

        } else if (TYPE == Constants.FOR_FOLLOW_UNFOLLOW_ARTIST) {
            call = api.followUnfollowArtist(params);

        } else if (TYPE == Constants.FOR_OTHER_PROFILE) {
            call = api.getOtherProfile(params);

        } else if (TYPE == Constants.FOR_BAND_PROFILE) {
            call = api.getBandProfile(params);

        } else if (TYPE == Constants.FOR_UPDATE_BAND_PROFILE) {
            call = api.updateBandProfile(params);

        } else if (TYPE == Constants.FOR_BAND_LIST) {
            call = api.getBandList(params);

        } else if (TYPE == Constants.FOR_DELETE_BAND) {
            call = api.deleteBand(params);

        } else if (TYPE == Constants.FOR_BAND_MEMBER_STATUS) {
            call = api.bandMemberStatus(params);

        } else if (TYPE == Constants.FOR_SEARCH_BAND_MEMBER) {
            call = api.searchBandMember(params);

        } else if (TYPE == Constants.FOR_ADD_BAND_MEMBER) {
            call = api.bandMemberAdd(params);

        } else if (TYPE == Constants.FOR_REMOVE_BAND_MEMBER) {
            call = api.bandMemberRemove(params);

        } else if (TYPE == Constants.FOR_NOTIFICATION) {
            call = api.methodNotification(params);

        } else if (TYPE == Constants.FOR_ACCEPT_BAND_REQUEST) {
            call = api.methodAcceptBandRequest(params);

        } else if (TYPE == Constants.FOR_REJECT_BAND_REQUEST) {
            call = api.methodRejectBandRequest(params);

        } else if (TYPE == Constants.FOR_OTHER_BAND_LIST) {
            call = api.methodOtherBandList(params);

        } else if (TYPE == Constants.FOR_EVENT_MANAGER_DETAIL) {
            call = api.methodOtherBandList(params);

        } else if (TYPE == Constants.FOR_SEARCH_BAND_LIST) {
            call = api.methodBandListSearch(params);

        } else if (TYPE == Constants.FOR_SEARCH_ARTIST_EVENT_MANAGER) {
            call = api.methodSearchArtistEventManager(params);

        } else if (TYPE == Constants.FOR_LOGOUT) {
            call = api.methodForLogout(params);

        } else if (TYPE == Constants.FOR_TEST) {
            call = api.methodTest(params);

        } else if (TYPE == Constants.FOR_STATE_LIST) {
            call = api.methodForStateList(params);

        } else if (TYPE == Constants.FOR_CITY_LIST) {
            call = api.methodForCityList(params);

        } else if (TYPE == Constants.FOR_SEND_REQ_ARTIST) {
            call = api.methodForSendArtistReq(params);

        }

        callRetrofit(call, TYPE);


    }


    public static void callGetAPI(int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;
        CommonInterfaceRetrofit api = RetrofitClientInstance.createService(CommonInterfaceRetrofit.class);
        Call<String> call = null;
        if (TYPE == Constants.FOR_GENRE_LIST) {
            call = api.callGenreList();
        } else if (TYPE == Constants.FOR_COUNTRIES_LIST) {
            call = api.callCountryList();
        }
        callRetrofit(call, TYPE);

    }


    public static <T> void callRetrofit(Call call, final int TYPE) {

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                commonInterface.sendResponse(response.body(), TYPE);


            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("Re", "");
                commonInterface.sendResponse("", TYPE);
            }
        });

    }


}