package com.musicseque.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import com.musicseque.BuildConfig;
import com.musicseque.artist.activity.ReportProblemActivity;
import com.musicseque.artist.activity.UploadActivity;
import com.musicseque.photopicker.activity.PickImageActivity;
import com.musicseque.service.LocationService;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.FileUtils;
import com.musicseque.utilities.SharedPref;
import com.musicseque.utilities.Utils;
import com.musicseque.venue_manager.activity.BookVenueActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class BaseActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 102;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 103;
    //private static final int SELECT_FILE_MULTIPLE = 1000;
    private static final int SELECT_FILE_SINGLE = 1001;

    // public SharedPreferences sharedPreferences;
//    SharedPreferences pref;
//
//    public SharedPreferences.Editor editor;


    private int IMAGE_FROM;
    private File myDirectory;
    private Uri muri;
    private int REQUEST_CAMERA = 1;
    private String selectedImagePath;
    String activityName;
    ArrayList<MultipartBody.Part> al = new ArrayList<>();
    private ArrayList<String> pathList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
//        sharedPreferences = retrofitComponent.getShared();
//        editor = retrofitComponent.getEditor();
//        pref = SharedPref.getSharedPref();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeaccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (readaccepted && writeaccepted && cameraAccepted) {
                        Utils.showToast(BaseActivity.this, "Permission Granted, Now you can access storage and camera.");
                        if (IMAGE_FROM == 1) {
                            cameraIntent();
                        } else
                            galleryIntent();

                    } else {

                        Utils.showToast(BaseActivity.this, "Permission Denied, You cannot access storage and camera.");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                                    showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
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
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        Utils.showToast(BaseActivity.this, "Permission Granted, Now you can access location.");
                        startService(new Intent(this, LocationService.class));


                    } else {

                        Utils.showToast(BaseActivity.this, "Permission Denied, You cannot access location");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestLocationPermission();
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

    public void openDialog(String type) {
        al.clear();
        activityName = type;
        myDirectory = new File(Environment.getExternalStorageDirectory(), "MusicSegue");
        try {
            if (myDirectory.exists()) {
            } else {
                myDirectory.mkdir();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(BaseActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                IMAGE_FROM = 2;
                                if (!checkPermission()) {
                                    requestPermission();
                                } else {
                                    galleryIntent();

                                }
                                break;
                            case 1:
                                IMAGE_FROM = 1;

                                if (!checkPermission()) {
                                    requestPermission();
                                } else {
                                    cameraIntent();
                                }
                        }
                    }
                });
        pictureDialog.show();


    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(BaseActivity.this, android.Manifest.permission.CAMERA);
        int result1 = ContextCompat.checkSelfPermission(BaseActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(BaseActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {

        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }


    public boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestLocationPermission() {

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

    }

    private void galleryIntent() {

        if (activityName.equalsIgnoreCase("report") || activityName.equalsIgnoreCase("event_image")) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setType("image/*");
            startActivityForResult(openGalleryIntent, SELECT_FILE_SINGLE);
        } else {

            Intent mIntent = new Intent(this, PickImageActivity.class);
            mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 5);
            mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
            startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);

        }

    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String image_name = "my_profile_" + System.currentTimeMillis() + ".jpeg";

            File photoUploadFile = new File(myDirectory, image_name);
            muri = Uri.fromFile(photoUploadFile);
            selectedImagePath = muri.getPath();
            Uri photoURI = FileProvider.getUriForFile(BaseActivity.this,
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


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = BaseActivity.this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(BaseActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //  content://media/external/images/media/2849
            if (requestCode == SELECT_FILE_SINGLE) {
                try {
                    Uri uri = data.getData();
                    String filePath = getRealPathFromURIPath(uri, BaseActivity.this);
                    File file = new File(filePath);
                    file = Utils.saveBitmapToFile(file);

                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                    RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, ""));

                    if (activityName.equalsIgnoreCase("report") || activityName.equalsIgnoreCase("event_image")) {
                        forSingleImage(file, fileToUpload, mUSerId, file.getName());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                Uri uri = Uri.parse(selectedImagePath);
                String filePath = getRealPathFromURIPath(uri, BaseActivity.this);
                File file = new File(filePath);
                file = Utils.saveBitmapToFile(file);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, ""));
                al.add(fileToUpload);
                if (activityName.equalsIgnoreCase("report") || activityName.equalsIgnoreCase("event_image")) {
                    forSingleImage(file, fileToUpload, mUSerId, file.getName());
                } else {
                    callActivity(al, mUSerId);
                }

            } else if (requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
                pathList = data.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
                if (pathList != null && !this.pathList.isEmpty()) {
                    StringBuilder sb = new StringBuilder("");
                    for (int i = 0; i < pathList.size(); i++) {

                        String filePath = FileUtils.compressImage(pathList.get(i), this);
                        File file = new File(filePath);
                        file = Utils.saveBitmapToFile(file);

                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                        al.add(fileToUpload);
                    }
                    RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, ""));
                    callActivity(al, mUSerId);
                }
            }


        }

    }


    private void forSingleImage(File file, MultipartBody.Part fileToUpload, RequestBody mUSerId, String name) {
        if (activityName.equalsIgnoreCase("report")) {
            ReportProblemActivity report = (ReportProblemActivity) this;
            report.getImageDetails(fileToUpload, mUSerId, name);
        } else if (activityName.equalsIgnoreCase("event_image")) {
            BookVenueActivity report = (BookVenueActivity) this;
            report.getImage(file, fileToUpload, mUSerId, name);
        }


    }

    void callActivity(ArrayList<MultipartBody.Part> fileToUpload, RequestBody mUSerId) {

        UploadActivity uploadActivity = (UploadActivity) this;
        uploadActivity.uploadPic(fileToUpload, mUSerId);
    }


}