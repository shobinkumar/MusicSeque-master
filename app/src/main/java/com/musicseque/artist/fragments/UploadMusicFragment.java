package com.musicseque.artist.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicseque.R;
import com.musicseque.artist.artist_adapters.UploadAudioAdapter;
import com.musicseque.artist.artist_models.UploadedMediaModel;
import com.musicseque.artist.retrofit_upload.ProgressRequestBody;
import com.musicseque.dagger_data.DaggerRetrofitComponent;
import com.musicseque.dagger_data.RetrofitComponent;
import com.musicseque.dagger_data.SharedPrefDependency;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.ImageUploadInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.retrofit_interface.KotlinRetrofitClientInstance;
import com.musicseque.retrofit_interface.RetrofitClientInstance;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.FileUtils;
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

import static android.app.Activity.RESULT_OK;

public class UploadMusicFragment extends BaseFragment implements MyInterface, ProgressRequestBody.UploadCallbacks {
    private static final int FOR_SELECT_AUDIO = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    View view;
    @BindView(R.id.recyclerAudio)
    RecyclerView recyclerAudio;

    @BindView(R.id.floatingButtonUploadMusic)
    FloatingActionButton floatingButton;

    public ProgressBar pBar;


    RecyclerView.LayoutManager layoutManager;
    UploadAudioAdapter adapter;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RetrofitComponent retrofitComponent;
    private ArrayList<UploadedMediaModel> uploadedAl = new ArrayList<>();
    private int mPercent = 0;

    UploadedMediaModel uploadedMediaModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_music, container, false);

        pBar = (ProgressBar) view.findViewById(R.id.pBar);


        ButterKnife.bind(this, view);
        initOtherViews();
        initViews();

        hitAPIs();
        return view;
    }

    private void initOtherViews() {
        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
        sharedPreferences = retrofitComponent.getShared();
        editor = retrofitComponent.getEditor();
    }

    private void initViews() {
        recyclerAudio = (RecyclerView) view.findViewById(R.id.recyclerAudio);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerAudio.setLayoutManager(layoutManager);


    }


    private void hitAPIs() {
        uploadedAl.clear();
        if (Utils.isNetworkConnected(getActivity())) {
          showDialog();
            String requestBody = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
                jsonObject.put("FileType", "Audio");

                requestBody = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            RetrofitAPI.callAPI(requestBody, Constants.FOR_UPLOADED_AUDIO, UploadMusicFragment.this);
        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }


    }


    @OnClick(R.id.floatingButtonUploadMusic)
    public void onClick(View view) {
        if (!checkPermission()) {
            requestPermission();
        } else {
            selectAudio();

        }


    }


    private void selectAudio() {
        adapter.stopPlaying();
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, FOR_SELECT_AUDIO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();


                Uri selectedFileUri = data.getData();
                String selectedFilePath = FileUtils.getPath(getActivity(), selectedFileUri);
                //  Log.i(TAG,"Selected File Path:" + selectedFilePath);


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
                                selectedFilePath
                        },
                        "");

                if (null == c) {
                    // ERROR
                }

                while (c.moveToNext()) {
                    c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    sArtist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                }


                UploadedMediaModel uploadedMediaModel = new UploadedMediaModel();
                uploadedMediaModel.setUploaded(false);
                uploadedMediaModel.setFileName(new File(selectedFilePath).getName());
                uploadedMediaModel.setArtist_name(sArtist);
                uploadedAl.add(0, uploadedMediaModel);
                // adapter.notifyDataSetChanged();
                adapter = new UploadAudioAdapter(getActivity(), uploadedAl, mPercent, UploadMusicFragment.this);
                recyclerAudio.setAdapter(adapter);
                uploadAudio(selectedFilePath, uploadedMediaModel);
            } else {
                hitAPIs();
            }
        }
    }

    private void uploadAudio(String mPath, UploadedMediaModel uploadedMediaModel) {
        RequestBody mUserId = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString(Constants.USER_ID, ""));
        RequestBody mTag = RequestBody.create(MultipartBody.FORM, "audio");
        File file = new File(mPath);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, "", this);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        ImageUploadInterface api = RetrofitClientInstance.createService(ImageUploadInterface.class);
        Call<String> request = api.uploadArtistAudioVideo(filePart, mUserId, mTag);

        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                            mPercent = 0;

                            hitAPIs();
//                            uploadedAl.get(0).setUploaded(true);
//                            uploadedAl.get(0).setFilePath(jsonObject.getString("fileurl"));
//                            uploadedAl.get(0).setFileName(jsonObject.getString("FileName"));
//                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        if (TYPE == Constants.FOR_UPLOADED_AUDIO) {
            try {
                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    uploadedAl = new Gson().fromJson(response.toString(), new TypeToken<ArrayList<UploadedMediaModel>>() {
                    }.getType());
                    adapter = new UploadAudioAdapter(getActivity(), uploadedAl, mPercent, UploadMusicFragment.this);
                    recyclerAudio.setAdapter(adapter);

                } else {

                }
                adapter = new UploadAudioAdapter(getActivity(), uploadedAl, mPercent, UploadMusicFragment.this);
                recyclerAudio.setAdapter(adapter);
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

    @Override
    public void onProgressUpdate(int percentage) {

        mPercent = percentage;
        Log.e("Percent", percentage + "");
        adapter = new UploadAudioAdapter(getActivity(), uploadedAl, mPercent, UploadMusicFragment.this);
        recyclerAudio.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onError() {
        Log.e("Error", "");
    }

    @Override
    public void onFinish() {
        Log.e("Finish", "");

    }


    public void deleteMusic(UploadedMediaModel uploadedMediaModel) {
        if (Utils.isNetworkConnected(getActivity())) {
          showDialog();
            uploadedMediaModels = uploadedMediaModel;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
                jsonObject.put("Id", uploadedMediaModel.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_DELETE_ARTIST_MEDIA, UploadMusicFragment.this);
        } else {
            Utils.showToast(getActivity(), getString(R.string.err_no_internet));
        }


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeaccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && readaccepted && writeaccepted) {
                        Utils.showToast(getActivity(), "Permission Granted, Now you can access storage and camera.");
                        selectAudio();
                    } else {

                        Utils.showToast(getActivity(), "Permission Denied, You cannot access storage and camera.");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopPlaying();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void showDialog() {
        Utils.initializeAndShow(getActivity());
    }

    public void dismissDialog() {
        Utils.hideProgressDialog();
    }
}



