package com.musicseque.artist.fragments;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.musicseque.BuildConfig;
import com.musicseque.Fonts.BoldNoyhr;
import com.musicseque.Fonts.Noyhr;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.models.ImageModel;
import com.musicseque.retrofit_interface.ImageUploadClass;
import com.musicseque.retrofit_interface.RetrofitAPI;
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
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileDetailFragment extends BaseFragment implements View.OnClickListener, MyInterface {
    private static final int FOR_AUDIO = 101;
    int API_TYPE;
    private static final int FOR_IMAGE = 100;
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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.ivCameraBackground)
    ImageView ivCameraBackground;
    @BindView(R.id.tvFollowersCount)
    Noyhr tvFollowersCount;
    @BindView(R.id.btnFollow)
    Noyhr btnFollow;
    @BindView(R.id.ivProfilePic)
    CircleImageView ivProfilePic;
    @BindView(R.id.pBar)
    ProgressBar pBar;

    @BindView(R.id.ivCameraProfilePic)
    ImageView ivCameraProfilePic;
    @BindView(R.id.tvUserID)
    Noyhr tvUserID;
    @BindView(R.id.rBar)
    RatingBar rBar;
    @BindView(R.id.tvReviews)
    Noyhr tvReviews;
    @BindView(R.id.tvUserNameDetail)
    BoldNoyhr tvUserName;
    @BindView(R.id.ivIndicator)
    ImageView ivIndicator;
    @BindView(R.id.tvUserType)
    BoldNoyhr tvUserType;
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

//    @BindView(R.id.frameLayout)
//    FrameLayout frameLayout;

    @BindView(R.id.sv)
    ScrollView sv;

    @BindView(R.id.cLayout)
    ConstraintLayout cLayout;
    ImageView img_right_icon;
    BoldNoyhr tv_title;
    private int IMAGE_FROM;
    // private ImageView iv_home, iv_profile, iv_feature, iv_chat, iv_settings;
    private Fragment fragment;
    ArrayList<ImageModel> arrayList = new ArrayList<>();
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_artist_profile, null);
        initOtherViews();
        initViews();
        listeners();

        showDefaultData();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserProfile();
        changeBackgroundColor(ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs_active), getResources().getString(R.string.txt_gigs), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
        changeFragment(new GigsFragment());   
    }

    private void showDefaultData() {
        if (getSharedPref().getString(Constants.VISIBILITY_STATUS, "Available").equalsIgnoreCase("Available")) {
            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.icon_green));
        } else if (getSharedPref().getString(Constants.VISIBILITY_STATUS, "Available").equalsIgnoreCase("Offline")) {
            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.icon_invisible));
        } else {
            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.icon_red));
        }


    }

    private void initOtherViews() {

        //  View views = view.findViewById(R.id.toolbarTop);
        // img_first_icon = (ImageView) views.findViewById(R.id.img_first_icon);
        img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        tv_title = (BoldNoyhr) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);
        img_right_icon.setVisibility(View.VISIBLE);
        tv_title.setText("Profile");


//        iv_home = (ImageView) view.findViewById(R.id.iv_home);
//        iv_profile = (ImageView) view.findViewById(R.id.iv_profile);
//        iv_feature = (ImageView) view.findViewById(R.id.iv_feature);
//        iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
//        iv_settings = (ImageView) view.findViewById(R.id.iv_settings);


        ButterKnife.bind(this, view);
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
        btnFollow.setVisibility(View.GONE);


    }

    private void listeners() {
      /*  iv_home.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        iv_feature.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        iv_settings.setOnClickListener(this);*/


        img_right_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), MainActivity.class).putExtra("profileTemp", true).putExtra("frag", "com.musicseque.artist.fragments.ProfileFragment"));


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
                tvBio.setVisibility(View.VISIBLE);
                ivIconDrop.setVisibility(View.GONE);
                ivIconUp.setVisibility(View.VISIBLE);
                break;
            case R.id.ivIconUp:
//visible
                tvBio.setVisibility(View.GONE);
                ivIconDrop.setVisibility(View.VISIBLE);
                ivIconUp.setVisibility(View.GONE);
                break;


            case R.id.llMusic:
                changeBackgroundColor(ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music_active), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
                changeFragment(new MusicFragment());
                //hitAPI(FOR_IMAGE);
                // changeFragment(new MusicFragment());
                break;
            case R.id.llVideo:
                changeBackgroundColor(ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos_active), getResources().getString(R.string.txt_video), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
               changeFragment(new VideoFragment());
                //hitAPI(FOR_IMAGE);
                break;

            case R.id.llPhotos:

                changeBackgroundColor(ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos_active), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
                hitAPI(FOR_IMAGE);


                break;
            case R.id.llGigs:
                changeBackgroundColor(ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs_active), getResources().getString(R.string.txt_gigs), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
                changeFragment(new GigsFragment());
                //hitAPI(FOR_IMAGE);
                break;
            case R.id.llCollaborators:
                changeBackgroundColor(ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators_active), getResources().getString(R.string.txt_collaborators), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs));
                changeFragment(new CollaboratorsFragment());
                //hitAPI(FOR_IMAGE);
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
        if (Utils.isNetworkConnected(getActivity())) {
            initializeLoader();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UserId", getSharedPref().getString(Constants.USER_ID, ""));
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, ProfileDetailFragment.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }

    }

    public void openDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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
        Utils.initializeAndShow(getActivity());
//        Utils.initializeProgressDialog(getActivity());
//        Utils.showProgressDialog();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)
                try {
                    Uri uri = data.getData();
//                    String filePath = Utils.getRealPathFromURIPath(uri, getActivity());
//                    File file = new File(filePath);
//                    // showImage(file);
//                    file = Utils.saveBitmapToFile(file);
                    String filePath = FileUtils.compressImage(uri.toString(), getActivity());
                    File file = new File(filePath);
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                    RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), getSharedPref().getString(Constants.USER_ID, ""));

                    if (IMAGE_FOR == FOR_BACKGROUND)
                        ImageUploadClass.imageUpload(fileToUpload, mUSerId,null, Constants.FOR_UPLOAD_ARTIST_COVER_PIC, ProfileDetailFragment.this);
                    else
                        ImageUploadClass.imageUpload(fileToUpload, mUSerId,null, Constants.FOR_UPLOAD_ARTIST_PROFILE_IMAGE, ProfileDetailFragment.this);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA) {
                Uri uri = Uri.parse(selectedImagePath);
//                String filePath = Utils.getRealPathFromURIPath(uri, getActivity());
//                File file = new File(filePath);
//                //  showImage(file);
//                file = Utils.saveBitmapToFile(file);
                String filePath = FileUtils.compressImage(uri.toString(), getActivity());
                File file = new File(filePath);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), getSharedPref().getString(Constants.USER_ID, ""));


                if (Utils.isNetworkConnected(getActivity())) {
                    initializeLoader();
                    if (IMAGE_FOR == FOR_BACKGROUND)
                        ImageUploadClass.imageUpload(fileToUpload, mUSerId,null,Constants.FOR_UPLOAD_ARTIST_COVER_PIC, ProfileDetailFragment.this);
                    else
                        ImageUploadClass.imageUpload(fileToUpload, mUSerId,null, Constants.FOR_UPLOAD_ARTIST_PROFILE_IMAGE, ProfileDetailFragment.this);

                } else {
                    Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
                }


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
                        tvUserID.setText(getSharedPref().getString(Constants.UNIQUE_CODE, ""));
                        // rBar.setRating(Float.parseFloat(obj.getString("Rating")));
                        // rBar.setNumStars(5);
//                        rBar.setIsIndicator(true);
//                          rBar.setRating(Float.parseFloat("4.0"));
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
                               pBar.setVisibility(View.GONE);

                            } else {
                                mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic");
                                Glide.with(getActivity())
                                        .load(mProfilePic)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                               pBar.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                               pBar.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(ivProfilePic);
                            }
                        } else {
                            mProfilePic = obj.getString("SocialImageUrl");
                            Glide.with(getActivity())
                                    .load(mProfilePic)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                           pBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            pBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(ivProfilePic);

                        }


                        if (getSharedPref().getString(Constants.LOGIN_TYPE, "Simple").equalsIgnoreCase("Simple")) {
                            ivCameraProfilePic.setVisibility(View.VISIBLE);
                        } else {
                            ivCameraProfilePic.setVisibility(View.GONE);

                        }


                    }
                    if (!obj.getString("BackgroundImage").equalsIgnoreCase("")) {
                        String mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage");
                       progressBar.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load(mCoverPic)
                                .listener(new RequestListener<Drawable>() {

                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(ivBackground);
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                    }
                    sv.fullScroll(ScrollView.FOCUS_UP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

            case Constants.FOR_ARTIST_UPLOADED_IMAGES:


                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    arrayList.clear();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            arrayList.add(new ImageModel(jsonObject.getString("ImgUrl"), jsonObject.getString("Image"), true,-1,false));

                            ImagesFragment fragment = new ImagesFragment();
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            bundle.putString("data", gson.toJson(arrayList));
                            fragment.setArguments(bundle);
                            changeFragment(fragment);
                        }

                    } else {
                        ImagesFragment fragment = new ImagesFragment();
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("data", "");
                        fragment.setArguments(bundle);
                        changeFragment(fragment);
                    }
                    //  arrayList.add(new ImageModel("", "", false));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


            case Constants.FOR_UPLOADED_AUDIO:
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    if (jsonArray.length() > 0) {

                    } else {
                        MusicFragment fragment = new MusicFragment();
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("data", "");
                        fragment.setArguments(bundle);
                        changeFragment(fragment);
                    }
                    //  arrayList.add(new ImageModel("", "", false));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case Constants.FOR_UPLOAD_ARTIST_PROFILE_IMAGE:
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        getEditor().putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).commit();
                        Utils.showToast(getActivity(), "Profile Pic uploaded successfully");
                        //Glide.with(getActivity()).load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).into(ivProfilePic);



                       pBar.setVisibility(View.VISIBLE);

                        Glide.with(this)
                                .load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                       pBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        pBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(ivProfilePic);

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
                        getEditor().putString(Constants.COVER_IMAGE, jsonObject1.getString("imageurl") + jsonObject1.getString("ImageName")).commit();
                       progressBar.setVisibility(View.VISIBLE);
                        Utils.showToast(getActivity(), "Cover Pic uploaded successfully");

                        Glide.with(this)
                                .load(jsonObject1.getString("imageurl") + jsonObject1.getString("ImageName"))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                     //   ivBackground.setVisibility(View.VISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                       // ivBackground.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                })
                                .into(ivBackground);

                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment).commit();

    }

//    @Override
//    public void onBackPressed() {
//        // finish();
//        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        finish();
//    }

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

                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeaccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (camera && readaccepted && writeaccepted) {
                        Utils.showToast(getActivity(), "Permission Granted, Now you can access storage and camera.");
                        if (IMAGE_FROM == 1) {
                            cameraIntent();
                        } else
                            galleryIntent();

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

    private void showImage(File file) {
        if (IMAGE_FOR == FOR_BACKGROUND)
            Glide.with(getActivity()).load(file).into(ivBackground);
        else
            Glide.with(getActivity()).load(file).into(ivProfilePic);
    }

    @Override
    public void onClick(View view) {
    }


    private void hitAPI(int TYPE) {
        if (TYPE == FOR_IMAGE) {
            if (Utils.isNetworkConnected(getActivity())) {
                initializeLoader();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserId", getSharedPref().getString(Constants.USER_ID, ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_ARTIST_UPLOADED_IMAGES, this);
            } else {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
            }

        } else if (TYPE == FOR_AUDIO) {
            if (Utils.isNetworkConnected(getActivity())) {
                initializeLoader();
                JSONObject jsonObject = new JSONObject();
                String requestBody = "";
                try {
                    jsonObject.put("UserId", getSharedPref().getString(Constants.USER_ID, ""));
                    jsonObject.put("FileType", "Audio");

                    requestBody = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                RetrofitAPI.callAPI(requestBody, Constants.FOR_UPLOADED_AUDIO, ProfileDetailFragment.this);
            }
        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }

    }
}
