package com.musicseque.retrofit_interface;

import android.util.Log;

import com.musicseque.interfaces.MyInterface;
import com.musicseque.utilities.Constants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitAPI {

    static MyInterface commonInterface;

    public static void callAPI(String params, int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;


        CommonInterface api = RetrofitClientInstance.createService(CommonInterface.class);


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

        }
        else if (TYPE == Constants.FOR_DELETE_ACCOUNT) {
            call = api.callDeleteAccountMethod(params);

        }
        callRetrofit(call, TYPE);


    }


    public static void callGetAPI(int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;
        CommonInterface api = RetrofitClientInstance.createService(CommonInterface.class);
        Call<String> call = null;
        if (TYPE == Constants.FOR_GENRE_LIST) {
            call = api.callGenreList();
        } else if (TYPE == Constants.FOR_COUNTRIES_LIST) {
            call = api.callCountryList();
        }
        callRetrofit(call, TYPE);

    }

    public static void uploadFile(MultipartBody.Part params, RequestBody file_name, RequestBody mUserId, int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;


        CommonInterface api = RetrofitClientInstance.createService(CommonInterface.class);


        Call<String> call = null;

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
                  //  Log.e("FOR_UPLOADED_AUDIO", response.body().toString());
                   commonInterface.sendResponse(response.body(), TYPE);
                   // commonInterface.sendResponse("",TYPE);
                } else if (TYPE == Constants.FOR_CHANGE_PASSWORD) {
                    Log.e("FOR_CHANGE_PASSWORD", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_UPDATE_STATUS) {
                    Log.e("FOR_UPDATE_STATUS", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_UPLOADED_VIDEO) {
                    Log.e("FOR_UPLOADED_VIDEO", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                }


                else if (TYPE == Constants.FOR_DELETE_ARTIST_IMAGES) {
                    Log.e("FOR_DELETE_IMAGES", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                } else if (TYPE == Constants.FOR_DELETE_ARTIST_MEDIA) {
                    Log.e("FOR_DELETE_MEDIA", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                }
                else if (TYPE == Constants.FOR_DELETE_ACCOUNT) {
                    Log.e("FOR_DELETE_ACCOUNT", response.body().toString());
                    commonInterface.sendResponse(response.body(), TYPE);

                }
//                else if (TYPE == Constants.FOR_SONGS_LIST) {
//                    Log.e("FOR_SONGS_LIST", response.body().toString());
//                    commonInterface.sendResponse(response.body(), TYPE);
//
//                }
//                else if (TYPE == Constants.FOR_ARTIST_UPLOADED_IMAGES) {
//                    Log.e("FOR_UPLOADED_IMAGES", response.body().toString());
//                    commonInterface.sendResponse(response.body(), TYPE);
//
//                } else if (TYPE == Constants.FOR_UPLOAD_ARTIST_IMAGE) {
//                    Log.e("FOR_UPLOAD_IMAGES", response.body().toString());
//                    commonInterface.sendResponse(response.body(), TYPE);
//
//                }
//                else if (TYPE == Constants.FOR_UPLOADED_AUDIO) {
//                    Log.e("FOR_UPLOADED_AUDIO", response.body().toString());
//                    commonInterface.sendResponse(response.body(), TYPE);
//
//                } else if (TYPE == Constants.FOR_UPLOAD_AUDIO) {
//                    Log.e("FOR_UPLOAD_AUDIO", response.body().toString());
//                    commonInterface.sendResponse(response.body(), TYPE);
//
//                }
//                else if (TYPE == Constants.FOR_UPLOADED_VIDEO) {
//                    Log.e("FOR_UPLOADED_VIDEO", response.body().toString());
//                    commonInterface.sendResponse(response.body(), TYPE);
//
//                } else if (TYPE == Constants.FOR_UPLOAD_VIDEO) {
//                    Log.e("FOR_UPLOAD_VIDEO", response.body().toString());
//                    commonInterface.sendResponse(response.body(), TYPE);
//
//                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("Re", "");

            }
        });

    }

}
