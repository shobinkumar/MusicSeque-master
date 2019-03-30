/*
package com.musicseque.artist.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.musicseque.BuildConfig;
import com.musicseque.Fonts.Noyhr;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.artist.fragments.ImagesFragment;
import com.musicseque.artist.fragments.MusicFragment;
import com.musicseque.artist.fragments.ProfileFragment;
import com.musicseque.fragments.HomeFragment;
import com.musicseque.fragments.SettingFragment;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.ImageUploadClass;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ArtistProfileActivity extends BaseActivity implements MyInterface, View.OnClickListener {

    final int PERMISSION_REQUEST_CODE = 10;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Uri muri;
    private String selectedImagePath = "";
    private File myDirectory;
    int IMAGE_FOR;
    int FOR_PROFILE = 1;
    int FOR_BACKGROUND = 2;


    @BindView(R.id.ivBackground)
    ImageView ivBackground;
    @BindView(R.id.ivCameraBackground)
    ImageView ivCameraBackground;
    @BindView(R.id.tvFollowersCount)
    Noyhr tvFollowersCount;
    @BindView(R.id.btnFollow)
    Noyhr btnFollow;
    @BindView(R.id.ivProfilePic)
    CircleImageView ivProfilePic;
    @BindView(R.id.ivCameraProfilePic)
    ImageView ivCameraProfilePic;
    @BindView(R.id.tvUserID)
    Noyhr tvUserID;
    @BindView(R.id.rBar)
    RatingBar rBar;
    @BindView(R.id.tvReviews)
    Noyhr tvReviews;
    @BindView(R.id.tvUserName)
    Noyhr tvUserName;
    @BindView(R.id.ivIndicator)
    ImageView ivIndicator;
    @BindView(R.id.tvUserType)
    Noyhr tvUserType;
    @BindView(R.id.tvUserLocation)
    Noyhr tvUserLocation;

    @BindView(R.id.tvGenre)
    Noyhr tvGenre;
    @BindView(R.id.tvExperience)
    Noyhr tvExperience;
    @BindView(R.id.tvMessage)
    Noyhr tvMessage;
    @BindView(R.id.tvAboutUser)
    Noyhr tvAboutUser;
    @BindView(R.id.ivIconUp)
    ImageView ivIconUp;
    @BindView(R.id.ivIconDrop)
    ImageView ivIconDrop;
    @BindView(R.id.tvBio)
    Noyhr tvBio;
    @BindView(R.id.ivMusic)
    ImageView ivMusic;
    @BindView(R.id.tvMusic)
    Noyhr tvMusic;
    @BindView(R.id.ivVideo)
    ImageView ivVideo;
    @BindView(R.id.tvVideo)
    Noyhr tvVideo;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvImage)
    Noyhr tvImage;
    @BindView(R.id.ivGigs)
    ImageView ivGigs;
    @BindView(R.id.tvGigs)
    Noyhr tvGigs;
    @BindView(R.id.ivCollaborators)
    ImageView ivCollaborators;
    @BindView(R.id.tvCollaborators)
    Noyhr tvCollaborators;


    ImageView img_first_icon, img_right_icon;
    Noyhr tv_title;
    private int IMAGE_FROM;
    private ImageView iv_home, iv_profile, iv_feature, iv_chat, iv_settings;
    private Fragment fragment;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_profile);
        initOtherViews();
        initViews();
        listeners();
        getUserProfile();
    }

    private void initOtherViews() {

        View view = findViewById(R.id.toolbarTop);
        img_first_icon = (ImageView) view.findViewById(R.id.img_first_icon);
        img_right_icon = (ImageView) view.findViewById(R.id.img_right_icon);
        tv_title = (Noyhr) view.findViewById(R.id.tv_title);
        img_right_icon.setVisibility(View.VISIBLE);
        img_first_icon.setVisibility(View.GONE);
        tv_title.setText("Profile");


        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);
        iv_feature = (ImageView) findViewById(R.id.iv_feature);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        iv_settings = (ImageView) findViewById(R.id.iv_settings);


        ButterKnife.bind(this);
        myDirectory = new File(Environment.getExternalStorageDirectory(), "MusicSegue");
        try {
            if (myDirectory.exists()) {
            } else {
                myDirectory.mkdir();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews() {
        changeBackgroundColor(ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music_active), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
        changeFragment(new MusicFragment());
        changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);

    }

    private void listeners() {
        iv_home.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        iv_feature.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        iv_settings.setOnClickListener(this);


        img_first_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_right_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }


    @OnClick({R.id.ivCameraBackground, R.id.btnFollow, R.id.ivCameraProfilePic, R.id.tvMessage, R.id.ivIconDrop, R.id.ivIconUp, R.id.llMusic, R.id.llVideo, R.id.llPhotos, R.id.llGigs, R.id.llCollaborators})
    public void setViewOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ivCameraBackground:
                IMAGE_FOR = FOR_BACKGROUND;
                openDialog();
                break;
            case R.id.btnFollow:

                break;
            case R.id.ivCameraProfilePic:
                IMAGE_FOR = FOR_PROFILE;
                openDialog();
                break;

            case R.id.tvMessage:
                break;
            case R.id.ivIconDrop:
                //hide
                tvBio.setVisibility(View.GONE);
                ivIconDrop.setVisibility(View.GONE);
                ivIconUp.setVisibility(View.VISIBLE);
                break;
            case R.id.ivIconUp:
//visible
                tvBio.setVisibility(View.VISIBLE);
                ivIconDrop.setVisibility(View.VISIBLE);
                ivIconUp.setVisibility(View.GONE);
                break;


            case R.id.llMusic:
                changeBackgroundColor(ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music_active), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
                changeFragment(new MusicFragment());
                break;
            case R.id.llVideo:
                changeBackgroundColor(ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos_active), getResources().getString(R.string.txt_video), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));

                break;

            case R.id.llPhotos:

                changeBackgroundColor(ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos_active), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
                changeFragment(new ImagesFragment());


                break;
            case R.id.llGigs:
                changeBackgroundColor(ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs_active), getResources().getString(R.string.txt_gigs), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));

                break;
            case R.id.llCollaborators:
                changeBackgroundColor(ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators_active), getResources().getString(R.string.txt_collaborators), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs));

                break;

        }
    }

    private void changeBackgroundColor(ImageView ivSelected, Noyhr tvSelected, Drawable selectedDrawable, String textSelected, ImageView iv1, Noyhr tv1, Drawable drawable1, String text1, ImageView iv2, Noyhr tv2, Drawable drawable2, String text2, ImageView iv3, Noyhr tv3, Drawable drawable3, String text3, ImageView iv4, Noyhr tv4, Drawable drawable4, String text4) {


        ivSelected.setImageDrawable(selectedDrawable);
        tvSelected.setText(textSelected);
        tvSelected.setTextColor(getResources().getColor(R.color.color_orange));

        iv1.setImageDrawable(drawable1);
        tv1.setText(text1);
        tv1.setTextColor(getResources().getColor(R.color.color_white));

        iv2.setImageDrawable(drawable2);
        tv2.setText(text2);
        tv2.setTextColor(getResources().getColor(R.color.color_white));

        iv3.setImageDrawable(drawable3);
        tv3.setText(text3);
        tv3.setTextColor(getResources().getColor(R.color.color_white));

        iv4.setImageDrawable(drawable4);
        tv4.setText(text4);
        tv4.setTextColor(getResources().getColor(R.color.color_white));


    }

    private void getUserProfile() {
        if (Utils.isNetworkConnected(ArtistProfileActivity.this)) {
            initializeLoader();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, ArtistProfileActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Utils.showToast(ArtistProfileActivity.this, getResources().getString(R.string.err_no_internet));
        }

    }

    public void openDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(ArtistProfileActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
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

    void initializeLoader() {
        Utils.initializeProgressDialog(ArtistProfileActivity.this);
        Utils.showProgressDialog();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)
                try {
                    Uri uri = data.getData();
                    String filePath = Utils.getRealPathFromURIPath(uri, ArtistProfileActivity.this);
                    File file = new File(filePath);
                    showImage(file);
                    file = Utils.saveBitmapToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                    RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString(Constants.USER_ID, ""));

                    if (IMAGE_FOR == FOR_BACKGROUND)
                        ImageUploadClass.imageUpload(fileToUpload, mUSerId, Constants.FOR_UPLOAD_ARTIST_COVER_PIC, ArtistProfileActivity.this);
                    else
                        ImageUploadClass.imageUpload(fileToUpload, mUSerId, Constants.FOR_UPLOAD_ARTIST_PROFILE_IMAGE, ArtistProfileActivity.this);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA) {
                Uri uri = Uri.parse(selectedImagePath);
                String filePath = Utils.getRealPathFromURIPath(uri, ArtistProfileActivity.this);
                File file = new File(filePath);
                showImage(file);
                file = Utils.saveBitmapToFile(file);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString(Constants.USER_ID, ""));

                if (IMAGE_FOR == FOR_BACKGROUND)
                    ImageUploadClass.imageUpload(fileToUpload, mUSerId, Constants.FOR_UPLOAD_ARTIST_COVER_PIC, ArtistProfileActivity.this);
                else
                    ImageUploadClass.imageUpload(fileToUpload, mUSerId, Constants.FOR_UPLOAD_ARTIST_PROFILE_IMAGE, ArtistProfileActivity.this);

            }

        }
    }

    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        switch (TYPE) {
            case Constants.FOR_USER_PROFILE:
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    if (obj.getString("Status").equalsIgnoreCase("Success")) {
                        tvUserName.setText(obj.getString("FirstName") + " " + obj.getString("LastName"));
                        tvUserType.setText(obj.getString("Expertise"));
                        tvUserLocation.setText(obj.getString("City") + ", " + obj.getString("CountryName"));
                        tvFollowersCount.setText(obj.getString("Followers"));
                        rBar.setRating(Float.parseFloat(obj.getString("Rating")));
                        rBar.setNumStars(5);
                        rBar.setIsIndicator(true);
                        rBar.setRating(Float.parseFloat("4.0"));
                        tvReviews.setText("(" + obj.getString("Reviews") + " reviews" + ")");
                        tvGenre.setText(obj.getString("GenreTypeName"));
                        tvExperience.setText(obj.getString("ExperienceYear"));
                        tvBio.setText(obj.getString("Bio"));
                        // tvAboutUser.setText("About " + obj.getString("FirstName") + " " + obj.getString("LastName"));
                        String mProfilePic = "";

                        if (obj.getString("SocialId").equalsIgnoreCase("")) {
                            mProfilePic = obj.getString("ProfilePic");
                            if (mProfilePic.equalsIgnoreCase("")) {

                                ivProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.icon_photo_upload_circle));

                            } else {
                                mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic");
                                Glide.with(ArtistProfileActivity.this)
                                        .load(mProfilePic)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                                return false;
                                            }
                                        })
                                        .into(ivProfilePic);
                            }
                        } else {
                            mProfilePic = obj.getString("SocialImageUrl");
                            Glide.with(ArtistProfileActivity.this)
                                    .load(mProfilePic)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            return false;
                                        }
                                    })
                                    .into(ivProfilePic);

                        }


                        if (sharedPreferences.getString(Constants.LOGIN_TYPE, "Simple").equalsIgnoreCase("Simple")) {
                            ivCameraProfilePic.setVisibility(View.VISIBLE);
                        } else {
                            ivCameraProfilePic.setVisibility(View.GONE);

                        }


                    }
                    if (!obj.getString("BackgroundImage").equalsIgnoreCase("")) {
                        String mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage");
                        Glide.with(ArtistProfileActivity.this)
                                .load(mCoverPic)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                        return false;
                                    }
                                })
                                .into(ivBackground);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


            case Constants.FOR_UPLOAD_ARTIST_PROFILE_IMAGE:
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        editor.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).commit();
                        Utils.showToast(ArtistProfileActivity.this, "Response " + response.toString());

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.FOR_UPLOAD_ARTIST_COVER_PIC:

                try {
                    JSONObject jsonObject1 = new JSONObject(response.toString());
                    if (jsonObject1.getString("Status").equalsIgnoreCase("Success")) {
                        editor.putString(Constants.COVER_IMAGE, jsonObject1.getString("imageurl") + jsonObject1.getString("ImageName")).commit();
                        //  Utils.showToast(ArtistProfileActivity.this, "Response " + response.raw().message());

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        // finish();
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ArtistProfileActivity.this, android.Manifest.permission.CAMERA);
        int result1 = ContextCompat.checkSelfPermission(ArtistProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(ArtistProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(ArtistProfileActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeaccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (camera && readaccepted && writeaccepted) {
                        Utils.showToast(ArtistProfileActivity.this, "Permission Granted, Now you can access storage and camera.");
                        if (IMAGE_FROM == 1) {
                            cameraIntent();
                        } else
                            galleryIntent();

                    } else {

                        Utils.showToast(ArtistProfileActivity.this, "Permission Denied, You cannot access storage and camera.");

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
        new AlertDialog.Builder(ArtistProfileActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void galleryIntent() {


        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("image/*");
        startActivityForResult(openGalleryIntent, SELECT_FILE);
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String image_name = "my_profile_" + System.currentTimeMillis() + ".jpeg";

            File photoUploadFile = new File(myDirectory, image_name);
            muri = Uri.fromFile(photoUploadFile);
            selectedImagePath = muri.getPath();
            Uri photoURI = FileProvider.getUriForFile(ArtistProfileActivity.this,
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

    private void showImage(File file) {
        if (IMAGE_FOR == FOR_BACKGROUND)
            Glide.with(ArtistProfileActivity.this).load(file).into(ivBackground);
        else
            Glide.with(ArtistProfileActivity.this).load(file).into(ivProfilePic);
    }

    @Override
    public void onClick(View view) {
        if (view == iv_home) {
            fragment = new HomeFragment();

            changeIconBottom(R.drawable.homeactive3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);


        } else if (view == iv_profile) {

            fragment = new ProfileFragment();

            changeIconBottom(R.drawable.home3, R.drawable.profileactive3, R.drawable.featured3, R.drawable.chat3, R.drawable.setting3, fragment);


        } else if (view == iv_feature) {

            fragment = new HomeFragment();

            changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featuredactive3, R.drawable.chat3, R.drawable.setting3, fragment);

        } else if (view == iv_chat) {

            fragment = new HomeFragment();

            changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chatactive3, R.drawable.setting3, fragment);

        } else if (view == iv_settings) {

            fragment = new SettingFragment();

            changeIconBottom(R.drawable.home3, R.drawable.profile3, R.drawable.featured3, R.drawable.chat3, R.drawable.settingactive3, fragment);

        }
    }

    private void changeIconBottom(int home, int profile, int featured, int chat, int setting, Fragment fragment) {


        iv_home.setImageResource(home);
        iv_profile.setImageResource(profile);
        iv_feature.setImageResource(featured);
        iv_chat.setImageResource(chat);
        iv_settings.setImageResource(setting);

        if (fragment != null)
            startActivity(new Intent(this, MainActivity.class).putExtra("frag", fragment.getClass().getName()));
//
//        replaceFragment(fragment);

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
*/
