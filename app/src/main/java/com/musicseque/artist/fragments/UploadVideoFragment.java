package com.musicseque.artist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicseque.R;
import com.musicseque.artist.artist_adapters.UploadVideoAdapter;
import com.musicseque.artist.artist_models.UploadedMediaModel;
import com.musicseque.artist.retrofit_upload.ProgressRequestBody;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.ImageUploadInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.retrofit_interface.KotlinRetrofitClientInstance;
import com.musicseque.retrofit_interface.RetrofitClientInstance;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.SharedPref;
import com.musicseque.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadVideoFragment extends BaseFragment implements MyInterface, ProgressRequestBody.UploadCallbacks {


    View view;
    @BindView(R.id.recyclerVideo)
    RecyclerView recyclerVideo;

    @BindView(R.id.floatingButtonUploadVideo)
    FloatingActionButton floatingButtonUploadVideo;

    RecyclerView.LayoutManager layoutManager;
    UploadVideoAdapter adapter;


//    SharedPref SharedPref;
//    SharedPref.Editor editor;
//    private RetrofitComponent retrofitComponent;
    private ArrayList<UploadedMediaModel> uploadedAl = new ArrayList<>();

    private int mPercent = 0;
    private UploadedMediaModel uploadedMediaModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_video, container, false);
        ButterKnife.bind(this, view);
        initOtherViews();
        initViews();

        hitAPIs();
        return view;
    }

    private void initOtherViews() {
//        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
//        SharedPref = retrofitComponent.getShared();
//        editor = retrofitComponent.getEditor();
    }

    private void initViews() {
        recyclerVideo = (RecyclerView) view.findViewById(R.id.recyclerVideo);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerVideo.setLayoutManager(layoutManager);


    }


    private void hitAPIs() {
        if (Utils.isNetworkConnected(getActivity())) {
            showDialog();
            String requestBody = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""));
                jsonObject.put("FileType", "video");

                requestBody = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            RetrofitAPI.callAPI(requestBody, Constants.FOR_UPLOADED_VIDEO, UploadVideoFragment.this);
        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));

        }


    }


    @OnClick(R.id.floatingButtonUploadVideo)
    public void onClick(View view) {
        openDialog("video", UploadVideoFragment.this);
    }


    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        if (TYPE == Constants.FOR_UPLOADED_VIDEO) {
            try {
                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    uploadedAl = new Gson().fromJson(response.toString(), new TypeToken<ArrayList<UploadedMediaModel>>() {
                    }.getType());


                } else {

                }
                adapter = new UploadVideoAdapter(getActivity(), uploadedAl, mPercent, UploadVideoFragment.this);
                recyclerVideo.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (TYPE == Constants.FOR_DELETE_ARTIST_MEDIA) {
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                    uploadedAl.remove(uploadedMediaModels);
                    adapter.notifyDataSetChanged();
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectedPath(String mPath) {

        String sArtist = "";

        Cursor c = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TRACK,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.YEAR
                },
                MediaStore.Audio.Media.DATA + " = ?",
                new String[]{
                        mPath
                },
                "");

        if (null == c) {
            // ERROR
        }

        while (c.moveToNext()) {
            ;
            sArtist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));

        }


        UploadedMediaModel uploadedMediaModel = new UploadedMediaModel();
        uploadedMediaModel.setUploaded(false);
        uploadedMediaModel.setArtist_name(sArtist);
        uploadedMediaModel.setFileName(new File(mPath).getName());
        uploadedAl.add(0, uploadedMediaModel);
        adapter.notifyDataSetChanged();


        RequestBody mUserId = RequestBody.create(MultipartBody.FORM, SharedPref.getString(Constants.USER_ID, ""));
        RequestBody mTag = RequestBody.create(MultipartBody.FORM, "video");
        File file = new File(mPath);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, "", this);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        ImageUploadInterface api = RetrofitClientInstance.createService(ImageUploadInterface.class);
        Call<String> request = api.uploadArtistAudioVideo(filePart, mUserId, mTag);

        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    mPercent = 0;
                    hitAPIs();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error", t.getLocalizedMessage());
                /* we can also stop our progress update here, although I have not check if the onError is being called when the file could not be downloaded, so I will just use this as a backup plan just incase the onError did not get called. So I can stop the progress right here. */
            }
        });
    }

    @Override
    public void onProgressUpdate(int percentage) {
        mPercent = percentage;
        Log.e("Percent", percentage + "");
        adapter = new UploadVideoAdapter(getActivity(), uploadedAl, mPercent, UploadVideoFragment.this);
        recyclerVideo.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    public void deleteVideo(UploadedMediaModel uploadedMediaModel) {
        if (Utils.isNetworkConnected(getActivity())) {
            showDialog();
            uploadedMediaModels = uploadedMediaModel;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""));
                jsonObject.put("Id", uploadedMediaModel.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_DELETE_ARTIST_MEDIA, UploadVideoFragment.this);
        } else {
            Utils.showToast(getActivity(), getString(R.string.err_no_internet));
        }


    }

    public void showDialog() {
        Utils.initializeAndShow(getActivity());
    }
}

