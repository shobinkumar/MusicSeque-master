package com.musicseque.artist.other_band.fragments;

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
import com.musicseque.artist.band.band_fragment.BandMemberStatusFragment;
import com.musicseque.artist.fragments.BaseFragment;
import com.musicseque.artist.fragments.GigsFragment;
import com.musicseque.artist.fragments.MusicFragment;
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

public class OtherBandProfileDetailFragment extends BaseFragment implements View.OnClickListener, MyInterface {
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


    @BindView(R.id.tvFollowersCount)
    Noyhr tvFollowersCount;

    @BindView(R.id.ivProfilePic)
    CircleImageView ivProfilePic;
    @BindView(R.id.pBar)
    ProgressBar pBar;


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
    //    @BindView(R.id.tvUserType)
//    BoldNoyhr tvUserType;
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
    @BindView(R.id.ivMember)
    ImageView ivMember;
    @BindView(R.id.tvMember)
    Noyhr tvMember;


    @BindView(R.id.sv)
    ScrollView sv;

    @BindView(R.id.cLayout)
    ConstraintLayout cLayout;
    ImageView img_right_icon;
    BoldNoyhr tv_title;
    private int IMAGE_FROM;
    private Fragment fragment;
    ArrayList<ImageModel> arrayList = new ArrayList<>();
    View view;
    private String mBandId,mBandManagerId;


    boolean isPicAPIHit = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_other_band_profile_detail, null);
        initOtherViews();
        initViews();
        listeners();


        return view;
    }


    private void initOtherViews() {

        mBandId = getArguments().getString("band_id");
        mBandManagerId = getArguments().getString("manager_id");
        img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        tv_title = (BoldNoyhr) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);
        img_right_icon.setVisibility(View.GONE);
        tv_title.setText("Band Profile");


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

        img_right_icon.setVisibility(View.GONE);
    }

    private void listeners() {


    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPicAPIHit) {
            getBandProfile();
            changeBackgroundColor(ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs_active), getResources().getString(R.string.txt_gigs), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivMember, tvMember, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_members));
            changeFragment(new GigsFragment());
        }

    }

    @OnClick({R.id.tvMessage, R.id.ivIconDrop, R.id.ivIconUp, R.id.llMusic, R.id.llVideo, R.id.llPhotos, R.id.llGigs, R.id.llMember})
    public void setViewOnClickEvent(View view) {
        switch (view.getId()) {


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
                changeBackgroundColor(ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music_active), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivMember, tvMember, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_members));

                break;
            case R.id.llVideo:
                changeBackgroundColor(ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos_active), getResources().getString(R.string.txt_video), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivMember, tvMember, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_members));

                break;

            case R.id.llPhotos:

                changeBackgroundColor(ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos_active), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivMember, tvMember, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_members));


                break;
            case R.id.llGigs:
                changeBackgroundColor(ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs_active), getResources().getString(R.string.txt_gigs), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivMember, tvMember, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_members));
                // changeFragment(new GigsFragment());
                //hitAPI(FOR_IMAGE);
                break;
            case R.id.llMember:
                changeBackgroundColor(ivMember, tvMember, getResources().getDrawable(R.drawable.icon_collaborators_active), getResources().getString(R.string.txt_members), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs));

                Bundle bundle = new Bundle();
                bundle.putString("band_id", mBandId);
                bundle.putString("manager_id", mBandManagerId);
                OtherBandMembersFragment fragment = new OtherBandMembersFragment();
                fragment.setArguments(bundle);

                changeFragment(fragment);

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

    private void getBandProfile() {
        if (Utils.isNetworkConnected(getActivity())) {
            initializeLoader();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("BandId", mBandId);
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_BAND_PROFILE, OtherBandProfileDetailFragment.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }

    }



    void initializeLoader() {
        Utils.initializeAndShow(getActivity());
//        Utils.initializeProgressDialog(getActivity());
//        Utils.showProgressDialog();

    }


    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        switch (TYPE) {
            case Constants.FOR_BAND_PROFILE:
                try {
                    JSONObject object = new JSONObject(response.toString());
                    if (object.getString("Status").equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = object.getJSONArray("result");
                        JSONObject obj = jsonArray.getJSONObject(0);


                        tvUserName.setText(obj.getString("BandName"));
                        tvUserLocation.setText(obj.getString("BandCity") + ", " + obj.getString("CountryName"));
                        tvFollowersCount.setText(obj.getString("Followers"));
                        tvUserID.setText(obj.getString("UniqueCode"));
                        tvReviews.setText("(" + obj.getString("Reviews") + " reviews" + ")");
                        tvGenre.setText(obj.getString("GenreTypeName"));
                        tvExperience.setText(obj.getString("ExperienceYear"));
                        tvBio.setText(obj.getString("Bio"));
                        String mProfilePic = "";


                        mProfilePic = obj.getString("BandImg");
                        if (mProfilePic.equalsIgnoreCase("")) {

                            ivProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.icon_img_dummy));
                            pBar.setVisibility(View.GONE);

                        } else {
                            mProfilePic = obj.getString("BandImgPath") + obj.getString("BandImg");
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


                        if (!obj.getString("BandBackgroundImg").equalsIgnoreCase("")) {
                            String mCoverPic = obj.getString("BandBackgroundImgPath") + obj.getString("BandBackgroundImg");
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
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }


                        if (obj.getString("NewStatus").equalsIgnoreCase("Available")) {
                            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.greenindicator));
                        } else if (obj.getString("NewStatus").equalsIgnoreCase("Offline")) {
                            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.icon_invisible));
                        } else {
                            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.icon_red));
                        }
                    } else {
                        progressBar.setVisibility(View.GONE
                        );
                    }


                    sv.fullScroll(ScrollView.FOCUS_UP);

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









    @Override
    public void onClick(View view) {
    }


}
