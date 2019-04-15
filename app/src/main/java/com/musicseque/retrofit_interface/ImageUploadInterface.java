package com.musicseque.retrofit_interface;

import com.musicseque.utilities.Constants;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageUploadInterface {
    @Multipart
    @POST(Constants.UPLOAD_ARTIST_IMAGE_API)
    Call<String> uploadProfilePic(@Part MultipartBody.Part file, @Part("Id") RequestBody name);

    @Multipart
    @POST(Constants.UPLOAD_ARTIST_BACKGROUND_IMAGE_API)
    Call<String> uploadBackgroundImage(@Part MultipartBody.Part file, @Part("Id") RequestBody name);


    @Multipart
    @POST(Constants.UPLOAD_ARTIST_IMAGES_API)
    Call<String> uploadArtistImage(@Part("UserId") RequestBody name,@Part ArrayList<MultipartBody.Part> file);


    @Multipart
    @POST(Constants.UPLOAD_MEDIA_API)
    Call<String> uploadArtistAudioVideo(@Part MultipartBody.Part file, @Part("UserId") RequestBody name, @Part("Tag") RequestBody tag);


    @Multipart
    @POST(Constants.REPORT_ISSUE_API)
    Call<String> callReportMethod(@Part MultipartBody.Part file, @Part("UserId") RequestBody name, @Part("Message") RequestBody tag);


}
