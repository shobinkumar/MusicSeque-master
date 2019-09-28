package com.musicseque.artist.activity.other_artist_activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.musicseque.Fonts.BoldNoyhr;
import com.musicseque.Fonts.Noyhr;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.artist.fragments.CollaboratorsFragment;
import com.musicseque.artist.fragments.GigsFragment;
import com.musicseque.artist.fragments.ImagesFragment;
import com.musicseque.artist.fragments.MusicFragment;
import com.musicseque.artist.fragments.VideoFragment;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.models.ImageModel;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.SharedPref;
import com.musicseque.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileActivity extends BaseActivity implements MyInterface {


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


    @BindView(R.id.sv)
    ScrollView sv;

    @BindView(R.id.cLayout)
    ConstraintLayout cLayout;

    ArrayList<ImageModel> arrayList = new ArrayList<>();
    View view;
    private static final int FOR_AUDIO = 101;
    private static final int FOR_IMAGE = 100;
    int mUSerId;

    @BindView(R.id.img_first_icon)
    ImageView img_first_icon;
    @BindView(R.id.img_right_icon)
    ImageView img_right_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;
    String mFollowerCount;

    boolean isFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_artist);
        initOtherViews();
        initViews();
        getUserProfile();
    }

    private void initOtherViews() {
        ButterKnife.bind(this);
        tv_title.setText("Profile");
        img_first_icon.setVisibility(View.VISIBLE);
        img_right_icon.setVisibility(View.GONE);
        mUSerId = getIntent().getIntExtra("id", 0);
    }

    private void initViews() {
        changeBackgroundColor(ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs_active), getResources().getString(R.string.txt_gigs), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
        changeFragment(new GigsFragment());
    }


    private void getUserProfile() {
        if (Utils.isNetworkConnected(this)) {
            showDialog();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ArtistUserId", mUSerId);
                jsonObject.put("LoginUserId", SharedPref.getString(Constants.USER_ID, ""));
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_OTHER_PROFILE, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Utils.showToast(this, getResources().getString(R.string.err_no_internet));
        }

    }

    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        switch (TYPE) {
            case Constants.FOR_OTHER_PROFILE:
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject obj = jsonArray.getJSONObject(0);


                        tvUserName.setText(obj.getString("FirstName") + " " + obj.getString("LastName"));
                        tvUserType.setText(obj.getString("Expertise"));
                        tvUserLocation.setText(obj.getString("City") + ", " + obj.getString("CountryName"));
                        tvFollowersCount.setText(obj.getString("Followers"));
                        int mCount = Integer.parseInt(obj.getString("Followers"));
                        mFollowerCount = mCount + "";
                        tvUserID.setText(SharedPref.getString(Constants.UNIQUE_CODE, ""));

                        tvReviews.setText("(" + obj.getString("Reviews") + " reviews" + ")");
                        tvGenre.setText(obj.getString("GenreTypeName"));
                        tvExperience.setText(obj.getString("ExperienceYear"));
                        tvBio.setText(obj.getString("Bio"));
                        tvUserID.setText(obj.getString("UniqueCode"));

                        if (obj.getString("NewStatus").equalsIgnoreCase("Available")) {
                            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.icon_green));
                        } else if (obj.getString("NewStatus").equalsIgnoreCase("Offline")) {
                            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.icon_invisible));
                        } else {
                            ivIndicator.setImageDrawable(getResources().getDrawable(R.drawable.icon_red));
                        }


                        if (obj.getInt("FollowersTag") == 1) {
                            isFollow = true;
                            btnFollow.setText("Following");
                        } else {
                            btnFollow.setText("Follow");
                            isFollow = false;
                        }


                        String mProfilePic = "";

                        if (obj.getString("SocialId").equalsIgnoreCase("")) {
                            mProfilePic = obj.getString("ProfilePic");
                            if (mProfilePic.equalsIgnoreCase("")) {

                                ivProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.icon_img_dummy));
                                pBar.setVisibility(View.GONE);

                            } else {
                                mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic");

                                Glide.with(this)
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
                            Glide.with(this)
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

                        if (!obj.getString("BackgroundImage").equalsIgnoreCase("")) {
                            String mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage");
                            progressBar.setVisibility(View.VISIBLE);
                            Glide.with(this)
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
                            Glide.with(this)
                                    .load(R.drawable.icon_img_dummy)
                                    .into(ivBackground);
                        }
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
                            arrayList.add(new ImageModel(jsonObject.getString("ImgUrl"), jsonObject.getString("Image"), true, -1, false));

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


            case Constants.FOR_FOLLOW_UNFOLLOW_ARTIST:
                try {
                    int mCount;
                    JSONObject obj = new JSONObject(response.toString());
                    if (obj.getString("Status").equalsIgnoreCase("Success")) {
                        if (obj.getInt("FollowerTag") == 1) {
                            isFollow = true;
                            btnFollow.setText("Following");
                            mCount = Integer.parseInt(mFollowerCount) + 1;
                            mFollowerCount = mCount + "";
                            tvFollowersCount.setText(mFollowerCount);
                        } else {
                            btnFollow.setText("Follow");
                            mCount = Integer.parseInt(mFollowerCount) - 1;
                            mFollowerCount = mCount + "";
                            tvFollowersCount.setText(mFollowerCount);
                            isFollow = false;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @OnClick({R.id.tvBand, R.id.img_first_icon, R.id.ivCameraBackground, R.id.btnFollow, R.id.ivCameraProfilePic, R.id.tvMessage, R.id.ivIconDrop, R.id.ivIconUp, R.id.llMusic, R.id.llVideo, R.id.llPhotos, R.id.llGigs, R.id.llCollaborators})
    public void setViewOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.img_first_icon:
                finish();
                break;
            case R.id.tvBand:
                startActivity(new Intent(this, ArtistBandListActivity.class).putExtra("id", mUSerId));
                break;
            case R.id.btnFollow:
                changeFollowStatus();
                if (Utils.isNetworkConnected(this)) {
                    showDialog();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ArtistId", mUSerId);
                        jsonObject.put("FollowerId", SharedPref.getString(Constants.USER_ID, ""));
                        if (isFollow) {
                            jsonObject.put("Status", "Y");
                            jsonObject.put("FollowerRemarks", "F");
                        } else {
                            jsonObject.put("Status", "N");
                            jsonObject.put("FollowerRemarks", "U");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_FOLLOW_UNFOLLOW_ARTIST, this);
                } else {
                    Utils.showToast(this, getResources().getString(R.string.err_no_internet));
                }


                break;
            case R.id.tvMessage:
                break;
            case R.id.ivIconDrop:


                tvBio.setVisibility(View.VISIBLE);
                ivIconDrop.setVisibility(View.GONE);
                ivIconUp.setVisibility(View.VISIBLE);
                break;
            case R.id.ivIconUp:
                tvBio.setVisibility(View.GONE);
                ivIconDrop.setVisibility(View.VISIBLE);
                ivIconUp.setVisibility(View.GONE);
                break;


            case R.id.llMusic:
                changeBackgroundColor(ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music_active), getResources().getString(R.string.txt_music), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
                changeFragment(new MusicFragment());

                break;
            case R.id.llVideo:
                changeBackgroundColor(ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos_active), getResources().getString(R.string.txt_video), ivMusic, tvMusic, getResources().getDrawable(R.drawable.icon_music), getResources().getString(R.string.txt_music), ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivGigs, tvGigs, getResources().getDrawable(R.drawable.icon_gigs), getResources().getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, getResources().getDrawable(R.drawable.icon_collaborators), getResources().getString(R.string.txt_collaborators));
                changeFragment(new VideoFragment());
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

    void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment).commit();

    }

    private void hitAPI(int TYPE) {
        if (TYPE == FOR_IMAGE) {
            if (Utils.isNetworkConnected(this)) {
                showDialog();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserId", mUSerId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_ARTIST_UPLOADED_IMAGES, this);
            } else {
                Utils.showToast(this, getResources().getString(R.string.err_no_internet));
            }

        } else if (TYPE == FOR_AUDIO) {
            if (Utils.isNetworkConnected(this)) {
                showDialog();
                JSONObject jsonObject = new JSONObject();
                String requestBody = "";
                try {
                    jsonObject.put("UserId", mUSerId);
                    jsonObject.put("FileType", "Audio");

                    requestBody = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                RetrofitAPI.callAPI(requestBody, Constants.FOR_UPLOADED_AUDIO, this);
            }
        } else {
            Utils.showToast(this, getResources().getString(R.string.err_no_internet));
        }

    }

    void changeFollowStatus() {
        String mStatus = btnFollow.getText().toString();
        if (mStatus.equalsIgnoreCase("Follow")) {
            isFollow = true;
            btnFollow.setText("Following");
        } else {
            btnFollow.setText("Follow");
            isFollow = false;
        }

    }

    void showDialog() {
        Utils.initializeAndShow(OtherProfileActivity.this);
    }

}

