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


        CommonInterfaceRetrofit api =RetrofitClientInstance.createService(CommonInterfaceRetrofit.class);


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

        }
        else if (TYPE == Constants.FOR_OTHER_BAND_LIST) {
            call = api.methodOtherBandList(params);

        }
        else if (TYPE == Constants.FOR_EVENT_MANAGER_DETAIL) {
            call = api.methodOtherBandList(params);

        }
        else if (TYPE == Constants.FOR_SEARCH_BAND_LIST) {
            call = api.methodBandListSearch(params);

        }


        else if (TYPE == Constants.FOR_LOGOUT) {
            call = api.methodForLogout(params);

        }
        else if (TYPE == Constants.FOR_TEST) {
            call = api.methodTest(params);

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
                if (TYPE == Constants.FOR_LOGIN) {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("FOR_LOGIN", response.body().toString());


                } else if (TYPE == Constants.FOR_FORGOT_PASSWORD) {
                    Log.e("FOR_FORGOT_PASSWORD", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_RESEND_OTP) {
                    Log.e("FOR_RESEND_OTP", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_SIGNUP) {
                    Log.e("FOR_SIGNUP", response.body().toString());

                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_ACCOUNT_EXISTS) {
                    Log.e("FOR_ACCOUNT_EXISTS", response.body().toString());

                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_SOCIAL_LOGIN) {
                    Log.e("FOR_SOCIAL_LOGIN", response.body().toString());

                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_RESET_PASSWORD) {
                    Log.e("FOR_RESET_PASSWORD", response.body().toString());

                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_VERIFY_ACCOUNT) {
                    Log.e("FOR_ACCOUNT_VERIFY", response.body().toString());

                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_GENRE_LIST) {
                    Log.e("FOR_GENRE_LIST", response.body().toString());

                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_COUNTRIES_LIST) {
                    Log.e("FOR_COUNTRY_LIST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_UPDATE_PROFILE) {
                    Log.e("FOR_UPDATE_PROFILE", response.body().toString());

                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_USER_PROFILE) {
                    Log.e("FOR_USER_PROFILE", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_ARTIST_UPLOADED_IMAGES) {
                    Log.e("FOR_UPLOADED_IMAGES", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_UPLOADED_AUDIO) {
                    commonInterface.sendResponse(response.body(), TYPE);
                } else if (TYPE == Constants.FOR_CHANGE_PASSWORD) {
                    Log.e("FOR_CHANGE_PASSWORD", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_UPDATE_STATUS) {
                    Log.e("FOR_UPDATE_STATUS", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_UPLOADED_VIDEO) {
                    Log.e("FOR_UPLOADED_VIDEO", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_DELETE_ARTIST_IMAGES) {
                    Log.e("FOR_DELETE_IMAGES", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_DELETE_ARTIST_MEDIA) {
                    Log.e("FOR_DELETE_MEDIA", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_DELETE_ACCOUNT) {
                    Log.e("FOR_DELETE_ACCOUNT", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.GET_ARTIST_LIST) {
                    Log.e("GET_ARTIST_LIST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.SEARCH_ARTIST) {
                    Log.e("SEARCH_ARTIST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_LAT_LNG) {
                    Log.e("FOR_LAT_LNG", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_FOLLOW_UNFOLLOW_ARTIST) {
                    Log.e("FOLLOW_UNFOLLOW_ARTIST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_OTHER_PROFILE) {
                    Log.e("FOR_OTHER_PROFILE", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_BAND_PROFILE) {
                    Log.e("FOR_BAND_PROFILE", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_UPDATE_BAND_PROFILE) {
                    Log.e("FOR_UPDATE_BAND_PROFILE", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_BAND_LIST) {
                    Log.e("FOR_BAND_LIST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_DELETE_BAND) {
                    Log.e("FOR_DELETE_BAND", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_SEARCH_BAND_MEMBER) {
                    Log.e("FOR_SEARCH_BAND_MEMBER", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_BAND_MEMBER_STATUS) {
                    Log.e("FOR_BAND_MEMBER_STATUS", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_ADD_BAND_MEMBER) {
                    Log.e("FOR_ADD_BAND_MEMBER", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_REMOVE_BAND_MEMBER) {
                    Log.e("FOR_REMOVE_BAND_MEMBER", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_NOTIFICATION) {
                    Log.e("FOR_NOTIFICATION", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_ACCEPT_BAND_REQUEST) {
                    Log.e("FOR_ACCEPT_BAND_REQUEST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_REJECT_BAND_REQUEST) {
                    Log.e("FOR_REJECT_BAND_REQUEST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                }
                else if (TYPE == Constants.FOR_OTHER_BAND_LIST) {
                    Log.e("FOR_OTHER_BAND_LIST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                }
                else if (TYPE == Constants.FOR_EVENT_MANAGER_DETAIL) {
                    Log.e("FOR_EVENT_MANAGER", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                }
                else if (TYPE == Constants.FOR_SEARCH_BAND_LIST) {
                    Log.e("FOR_SEARCH_BAND_LIST", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                }
                else if (TYPE == Constants.FOR_LOGOUT) {
                    Log.e("FOR_LOGOUT", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("Re", "");

            }
        });

    }


}