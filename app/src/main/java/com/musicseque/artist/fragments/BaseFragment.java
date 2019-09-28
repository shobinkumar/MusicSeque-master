package com.musicseque.artist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.musicseque.BuildConfig;
import com.musicseque.event_manager.fragment.EventManagerDetailFragment;
import com.musicseque.utilities.FileUtils;
import com.musicseque.utilities.Utils;

import java.io.File;


public class BaseFragment extends Fragment {


    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int PERMISSION_REQUEST_CODE = 200;
    int VIDEO_FROM;
    private static final int FROM_CAMERA_VIDEO = 100;
    private static final int FROM_GALLERY_VIDEO = 101;
    private File myDirectory;
    private Uri muri;
    private String mVideoPath = "";
    String mFileType;
    UploadVideoFragment fragments;
    Fragment commonFragment;
    private String selectedImagePath;

//    public SharedPreferences      getSharedPref() {
//        RetrofitComponent retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
//        return retrofitComponent.getShared();
//    }
//
//
//    public SharedPreferences.Editor getEditor() {
//        RetrofitComponent retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
//        return retrofitComponent.getEditor();
//    }


    public void openDialog(String type, Fragment fragment) {

        myDirectory = new File(Environment.getExternalStorageDirectory(), "MusicSegue");
        try {
            if (myDirectory.exists()) {
            } else {
                myDirectory.mkdir();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String[] pictureDialogItems;
        mFileType = type;
        if (type.equalsIgnoreCase("video")) {
            fragments = (UploadVideoFragment) fragment;
            pictureDialogItems = new String[]{
                    "Select video from gallery",
                    "Capture video from camera"};
        } else {


            commonFragment = (EventManagerDetailFragment) fragment;

            pictureDialogItems = new String[]{
                    "Select photo from gallery",
                    "Capture photo from camera"};
        }


        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                VIDEO_FROM = FROM_GALLERY_VIDEO;
                                if (!checkPermission()) {
                                    requestPermission();
                                } else {
                                    if (type.equalsIgnoreCase("video"))
                                        galleryIntentVideo();
                                    else
                                        galleryIntentImage();

                                }
                                break;
                            case 1:
                                VIDEO_FROM = FROM_CAMERA_VIDEO;

                                if (!checkPermission()) {
                                    requestPermission();
                                } else {
                                    if (type.equalsIgnoreCase("video"))
                                        cameraIntentVideo();
                                    else
                                        cameraIntentPhoto();
                                }
                        }
                    }
                });
        pictureDialog.show();


    }


    private void galleryIntentVideo() {


        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("video/*");
        startActivityForResult(openGalleryIntent, SELECT_FILE);
    }

    private void galleryIntentImage() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, SELECT_FILE);

    }

    private void cameraIntentVideo() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        File mediaFile =
                new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/" + System.currentTimeMillis() + "_myvideo.mp4");
        mVideoPath = mediaFile.getAbsolutePath();

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        Uri videoUri = Uri.fromFile(mediaFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        startActivityForResult(intent, REQUEST_CAMERA);


    }

    void cameraIntentPhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String image_name = "my_profile_" + System.currentTimeMillis() + ".jpeg";

            File photoUploadFile = new File(myDirectory, image_name);
            muri = Uri.fromFile(photoUploadFile);
            selectedImagePath = muri.getPath();
            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoUploadFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI);
        } else {
            String image_name = "my_profile_" + System.currentTimeMillis() + ".jpeg";

            File photoUploadFile = new File(myDirectory, image_name);
            muri = Uri.fromFile(photoUploadFile);
            selectedImagePath = muri.getPath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, muri);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CAMERA);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)
                try {
                    if (mFileType.equalsIgnoreCase("video")) {
                        Uri selectedVideo = data.getData();
                        String[] filePathColumn = {MediaStore.Video.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                            mVideoPath = cursor.getString(columnIndex);

                            cursor.close();
                        }
                        sendDataToFragment();
                    } else {
                        Uri uri = data.getData();

                        String filePath = FileUtils.compressImage(uri.toString(), getActivity());
                        File file = new File(filePath);
                        sendImageToFragment(file);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA) {

                if (mFileType.equalsIgnoreCase("video"))
                    sendDataToFragment();
                else {
                    Uri uri = Uri.parse(selectedImagePath);
                    String filePath = FileUtils.compressImage(uri.toString(), getActivity());
                    File file = new File(filePath);
                    sendImageToFragment(file);
                }


            }

        }
    }

    private void sendImageToFragment(File file) {
        Fragment frag=(EventManagerDetailFragment)commonFragment;
       ((EventManagerDetailFragment) frag).getImageFile(file);
    }


    void sendDataToFragment() {

        File file = new File(mVideoPath);
        fragments.selectedPath(file.getAbsolutePath());
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
                        if (VIDEO_FROM == FROM_CAMERA_VIDEO) {
                            if (mFileType.equalsIgnoreCase("video"))
                                cameraIntentVideo();
                            else
                                cameraIntentPhoto();
                        } else
                        if (mFileType.equalsIgnoreCase("video"))
                            galleryIntentVideo();
                        else
                            galleryIntentImage();

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

}
