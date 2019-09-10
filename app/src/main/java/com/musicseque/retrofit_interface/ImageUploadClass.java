package com.musicseque.retrofit_interface;

import android.util.Log;

import com.musicseque.interfaces.MyInterface;
import com.musicseque.utilities.Constants;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.musicseque.utilities.Constants.FOR_UPLOAD_EVENT_PROFILE_IMAGE;

public class ImageUploadClass {
    static MyInterface commonInterface;

    public static void imageUpload(MultipartBody.Part fileToUpload, RequestBody mUSerId, RequestBody mMessage, int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;
        ImageUploadInterface api = RetrofitClientInstance.createService(ImageUploadInterface.class);
        Call<String> call = null;
        if (TYPE == Constants.FOR_UPLOAD_COVER_PIC) {
            call = api.uploadBackgroundImage(fileToUpload, mUSerId);
        } else if (TYPE == Constants.FOR_UPLOAD_PROFILE_IMAGE) {
            call = api.uploadProfilePic(fileToUpload, mUSerId);
        } else if (TYPE == Constants.FOR_REPORT_PROBLEM) {
            call = api.callReportMethod(fileToUpload, mUSerId, mMessage);
        } else if (TYPE == Constants.FOR_UPLOAD_ARTIST_AUDIO_VIDEO) {
            call = api.uploadArtistAudioVideo(fileToUpload, mUSerId, mMessage);
        } else if (TYPE == Constants.FOR_UPLOAD_BAND_PROFILE_IMAGE) {
            call = api.uploadBandProfilePic(fileToUpload, mUSerId);
        } else if (TYPE == Constants.FOR_UPLOAD_BAND_BACKGROUND_IMAGE) {
            call = api.uploadBandCoverPic(fileToUpload, mUSerId);
        }
        else if (TYPE == FOR_UPLOAD_EVENT_PROFILE_IMAGE) {
            call = api.uploadEventPic(fileToUpload, mUSerId);
        }
        callRetrofit(call, TYPE);
    }


    public static void fileUploadMultiple(ArrayList<MultipartBody.Part> fileToUpload, RequestBody mUSerId, RequestBody mMessage, int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;
        ImageUploadInterface api = RetrofitClientInstance.createService(ImageUploadInterface.class);
        Call<String> call = null;

        if (TYPE == Constants.FOR_UPLOAD_ARTIST_IMAGE) {
            call = api.uploadArtistImage(mUSerId, fileToUpload);
        }

        callRetrofit(call, TYPE);
    }


    public static <T> void callRetrofit(Call call, final int TYPE) {

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (TYPE == Constants.FOR_UPLOAD_COVER_PIC) {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("ARTIST_COVER_PIC", response.body().toString());


                } else if (TYPE == Constants.FOR_UPLOAD_PROFILE_IMAGE) {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("ARTIST_PROFILE_IMAGE", response.body().toString());


                } else if (TYPE == Constants.FOR_UPLOAD_ARTIST_IMAGE) {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("ARTIST_PROFILE_IMAGE", response.body().toString());
                } else if (TYPE == Constants.FOR_REPORT_PROBLEM) {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("FOR_REPORT_PROBLEM", response.body().toString());
                } else if (TYPE == Constants.FOR_UPLOAD_ARTIST_AUDIO_VIDEO) {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("FOR_UPLOAD_AUDIO_VIDEO", response.body().toString());
                } else if (TYPE == Constants.FOR_UPLOAD_BAND_PROFILE_IMAGE) {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("BAND_PROFILE_IMAGE", response.body().toString());
                } else if (TYPE == Constants.FOR_UPLOAD_BAND_BACKGROUND_IMAGE) {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("BAND_BACK_IMAGE", response.body().toString());
                }
                else if(TYPE==FOR_UPLOAD_EVENT_PROFILE_IMAGE)
                {
                    commonInterface.sendResponse(response.body(), TYPE);
                    Log.e("EVENT_PROFILE_IMAGE", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("Re", "");

            }
        });

    }

}
