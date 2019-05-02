package com.musicseque.artist.band.band_fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.musicseque.BuildConfig;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.artist.service.CommonService;
import com.musicseque.dagger_data.DaggerRetrofitComponent;
import com.musicseque.dagger_data.RetrofitComponent;
import com.musicseque.dagger_data.SharedPrefDependency;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.interfaces.SpinnerData;
import com.musicseque.models.CountryModel;
import com.musicseque.models.GenreModel;
import com.musicseque.retrofit_interface.ImageUploadInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.FileUtils;
import com.musicseque.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class BandFormFragment extends Fragment implements View.OnClickListener, MyInterface {


    int IMAGE_FROM;

    @BindView(R.id.pBar)
    ProgressBar pBar;
    @BindView(R.id.ivCamera)
    ImageView ivCamera;
    @BindView(R.id.ivProfile)
    CircleImageView ivProfile;

    @BindView(R.id.ivAddImage)
    ImageView ivAddImage;
    @BindView(R.id.ivStatus)
    ImageView ivStatus;
    @BindView(R.id.tvCountryCode)
    TextView tvCountryCode;

    @BindView(R.id.tvExperience)
    TextView tvExperience;

    @BindView(R.id.tvGenre)
    TextView tvGenre;

    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvCount)
    TextView tvCount;

    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;

    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_city)
    EditText et_city;
    @BindView(R.id.et_postal_code)
    EditText et_postal_code;
    @BindView(R.id.et_desc)
    EditText et_desc;
    @BindView(R.id.et_website)
    EditText et_website;

    @BindView(R.id.etBandName)
    EditText etBandName;
    @BindView(R.id.btn_submit)
    Button btn_submit;


    private TextView tv_title;


    Dialog dialog;

    String mGenreId = "", mCountryId = "";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RetrofitComponent retrofitComponent;

    private View v;


    ArrayList<GenreModel> genreAL = new ArrayList<>();
    ArrayList<String> genreNameAL = new ArrayList<>();
    ArrayList<CountryModel> countryAL = new ArrayList<>();
    ArrayList<String> countryNameAL = new ArrayList<>();
    ArrayList<String> countryCodeAL = new ArrayList<>();


    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private File myDirectory;

    private Uri muri;
    private String selectedImagePath = "";


    private ListPopupWindow listPopupWindow;
    String[] arrCountryCode;
    String[] arrGenre;
    String[] arrExperience;
    String[] arrExpertise;


    int mCount;


    String mCountryCode = "", mExperience = "", mGenre = "", mGrade = "", mCertification = "", mCountryName = "", mMobileNumber = "", mExperienceId = "";
    int mWidthExp, mWidthCode;

    String STATUS_ACTIVE = "Available";
    private String STATUS_INVISIBLE = "Offline";
    private static final String STATUS_DO_NOT_DISTURB = "Do_not_disturb";
    int mStart = 0;
    ImageView img_right_icon;


    String mBandId="0";

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_band_profile, container, false);

        initOtherViews();
        initViews();
        listeners();
        hitAPIs();
        return v;
    }


    private void initOtherViews() {
        ButterKnife.bind(this, v);
        arrExperience = new String[]{"1 - 2 years", "3 - 5 years", "5 - 10 years", "10+ years"};
        arrExpertise = new String[]{"Lead Vocals", "Backing Vocals", "Songwriter", "Piano", "Synthesizer", "Drums", "Band", "Lead Guitar", "Bass Guitar", "Violin", "Harp", "Sitar", "Flute", "Clarinet", "Saxophone", "Trumpet"};

        mBandId =  getArguments().getString("band_id");


        img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        img_right_icon.setVisibility(View.GONE);


        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
        sharedPreferences = retrofitComponent.getShared();
        editor = retrofitComponent.getEditor();
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


        tvCountryCode.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidthCode = tvCountryCode.getMeasuredWidth();

            }
        });
        tvExperience.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidthExp = tvExperience.getMeasuredWidth();

            }
        });


        tv_title = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);


        tv_title.setText("Band Profile");

        et_desc.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void listeners() {
        btn_submit.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivAddImage.setOnClickListener(this);
        ivStatus.setOnClickListener(this);
        tvCountryCode.setOnClickListener(this);
        tvExperience.setOnClickListener(this);
        tvGenre.setOnClickListener(this);

        et_city.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        et_city.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStart = start + count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String capitalizedText;
                if (input.length() < 1)
                    capitalizedText = input;
                else if (input.length() > 1 && input.contains(" ")) {
                    String fstr = input.substring(0, input.lastIndexOf(" ") + 1);
                    if (fstr.length() == input.length()) {
                        capitalizedText = fstr;
                    } else {
                        String sstr = input.substring(input.lastIndexOf(" ") + 1);
                        sstr = sstr.substring(0, 1).toUpperCase() + sstr.substring(1);
                        capitalizedText = fstr + sstr;
                    }
                } else
                    capitalizedText = input.substring(0, 1).toUpperCase() + input.substring(1);

                if (!capitalizedText.equals(et_city.getText().toString())) {
                    et_city.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            et_city.setSelection(mStart);
                            et_city.removeTextChangedListener(this);
                        }
                    });
                    et_city.setText(capitalizedText);
                }
            }

        });


        et_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvCount.setText(editable.length() + "/500");
                mCount = editable.length();
            }
        });


    }

    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        switch (TYPE) {

            case Constants.FOR_UPDATE_BAND_PROFILE:
                JSONObject jsonObject = null;


                try {
                    jsonObject = new JSONObject(response.toString());
                    String tracking = jsonObject.getString("Status");
                    String Message = jsonObject.getString("Message");
                    Log.e("tag", "status is " + tracking);
                    if (tracking.equalsIgnoreCase("Success")) {
                        startActivity(new Intent(getActivity(), MainActivity.class));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.FOR_GENRE_LIST:
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        GenreModel model = new GenreModel();
                        model.setId(jsonObject2.getString("Id"));
                        model.setType(jsonObject2.getString("GenreType"));
                        genreNameAL.add(jsonObject2.getString("GenreType"));
                        genreAL.add(model);
                    }


                    getCountriesList();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.FOR_COUNTRIES_LIST:
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CountryModel model = new CountryModel();
                        model.setCountryId(jsonObject1.getString("CountryId"));
                        model.setCountryName(jsonObject1.getString("CountryName"));
                        model.setCountryCode(jsonObject1.getString("CountryCode"));

                        countryNameAL.add(jsonObject1.getString("CountryName"));
                        countryCodeAL.add(jsonObject1.getString("CountryCode"));
                        countryAL.add(model);
                    }


                    arrCountryCode = countryCodeAL.toArray(new String[countryCodeAL.size()]);
                    arrGenre = genreNameAL.toArray(new String[genreNameAL.size()]);
                    if (!mBandId.equalsIgnoreCase("0"))
                        getUserProfile();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.FOR_BAND_PROFILE:
                try {
                    JSONObject jsonObj = new JSONObject(response.toString());
                    if (jsonObj.getString("Status").equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jsonObj.getJSONArray("result");
                        JSONObject obj = jsonArray.getJSONObject(0);


                        if (obj.getString("BandImg").equalsIgnoreCase("")) {

                        } else {
                            Glide.with(BandFormFragment.this)
                                    .load(obj.getString("BandImgPath") + obj.getString("BandImg"))
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            pBar.setVisibility(View.GONE);
                                            ivCamera.setVisibility(View.VISIBLE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            pBar.setVisibility(View.GONE);
                                            ivCamera.setVisibility(View.VISIBLE);
                                            return false;
                                        }
                                    })
                                    .into(ivProfile);
                        }

                        if (obj.getString("NewStatus").equalsIgnoreCase(STATUS_ACTIVE)) {
                            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
                        } else if (obj.getString("NewStatus").equalsIgnoreCase(STATUS_INVISIBLE)) {
                            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_transparent));
                        } else {
                            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_red));
                        }


                        etBandName.setText(obj.getString("BandName"));
                        et_email.setText(obj.getString("BandEmail"));
                        tvCountryCode.setText(obj.getString("CountryCode"));
                        etMobileNumber.setText(obj.getString("BandContactNo"));
                        et_city.setText(obj.getString("BandCity"));
                        et_postal_code.setText(obj.getString("PostCode"));
                        tvCountry.setText(obj.getString("CountryName"));
                        et_desc.setText(obj.getString("Bio"));
                        tvGenre.setText(obj.getString("GenreTypeName"));
                        et_website.setText(obj.getString("BandWebsite"));

                        tvExperience.setText(obj.getString("ExperienceYear"));


                        mExperienceId = obj.getString("ExperienceId");
                        mGenreId = obj.getString("BandGenreId");
                        mCountryId = obj.getString("CountryId");

                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

        }


    }

    private void showDefaultData() {
        et_email.setText(sharedPreferences.getString(Constants.EMAIL_ID, ""));
//        tvUserName.setText(sharedPreferences.getString(Constants.USER_NAME, ""));
//        tvProfileType.setText(sharedPreferences.getString(Constants.PROFILE_TYPE, ""));
        if (sharedPreferences.getString(Constants.VISIBILITY_STATUS, STATUS_ACTIVE).equalsIgnoreCase(STATUS_ACTIVE)) {
            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));
        } else if (sharedPreferences.getString(Constants.VISIBILITY_STATUS, STATUS_ACTIVE).equalsIgnoreCase(STATUS_INVISIBLE)) {
            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_transparent));
        } else {
            ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_red));
        }


        mCountryCode = sharedPreferences.getString(Constants.COUNTRY_CODE, "");
        mCountryId = sharedPreferences.getString(Constants.COUNTRY_ID, "");
        mCountryName = sharedPreferences.getString(Constants.COUNTRY_NAME, "");
        mMobileNumber = sharedPreferences.getString(Constants.MOBILE_NUMBER, "");
        tvCountryCode.setText(mCountryCode);
        etMobileNumber.setText(mMobileNumber);
        tvCountry.setText(mCountryName);


        if (sharedPreferences.getString(Constants.LOGIN_TYPE, "Simple").equalsIgnoreCase("Simple")) {
            String mUrl = sharedPreferences.getString(Constants.PROFILE_IMAGE, "");
            if (mUrl.equalsIgnoreCase("")) {
                ivProfile.setVisibility(View.GONE);
                pBar.setVisibility(View.GONE);
                ivCamera.setVisibility(View.GONE);
                ivAddImage.setVisibility(View.VISIBLE);
                ivAddImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_photo_upload_circle));
                ivAddImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog();
                    }
                });
            } else {
                ivCamera.setVisibility(View.GONE);
                ivAddImage.setVisibility(View.GONE);
                ivProfile.setVisibility(View.VISIBLE);


                Glide.with(BandFormFragment.this)
                        .load(mUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                pBar.setVisibility(View.GONE);
                                ivCamera.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                pBar.setVisibility(View.GONE);
                                ivCamera.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(ivProfile);

                ivCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog();
                    }
                });


            }
        } else {
            String mUrl = sharedPreferences.getString(Constants.PROFILE_IMAGE, "");

            ivCamera.setVisibility(View.GONE);
            ivAddImage.setVisibility(View.GONE);

            Glide.with(BandFormFragment.this)
                    .load(mUrl)
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
                    .into(ivProfile);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivCamera:
                openDialog();
                break;

            case R.id.ivAddImage:
                openDialog();
                break;

            case R.id.btn_submit:
                apiUpdateProfile();

                break;
            case R.id.tvCountryCode:
                showDropdown(arrCountryCode, tvCountryCode, new SpinnerData() {
                    @Override
                    public void getData(String mData, String mData1) {
                        mCountryId = mData;
                        mCountryName = mData1;
                        tvCountry.setText(mData1);
                    }
                }, mWidthCode);
                break;

            case R.id.tvGenre:
                showDropdown(arrGenre, tvGenre, new SpinnerData() {
                    @Override
                    public void getData(String mData, String mData1) {
                        mGenre = mData;
                        mGenreId = mData1;
                    }
                }, mWidthExp);

                break;


            case R.id.tvExperience:
                showDropdown(arrExperience, tvExperience, new SpinnerData() {
                    @Override
                    public void getData(String mData, String mData1) {
                        mExperience = mData;
                        mExperienceId = mData1;
                    }
                }, mWidthExp);
                break;


            case R.id.ivStatus:
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.dropdown_user_status, null);
                TextView tvAvailable = (TextView) customView.findViewById(R.id.tvAvailable);
                TextView tvDoNot = (TextView) customView.findViewById(R.id.tvDoNot);
                TextView tvInvisible = (TextView) customView.findViewById(R.id.tvInvisible);


                //instantiate popup window
                final PopupWindow popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


                popupWindow.showAtLocation(ivStatus, Gravity.TOP | Gravity.LEFT, locateView().left, locateView().left);
                tvAvailable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_green));

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("LoginUserID", sharedPreferences.getString(Constants.USER_ID, ""));
                            jsonObject.put("NewStatus", "Available");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getActivity().startService(new Intent(getActivity(), CommonService.class).putExtra("API", Constants.UPDATE_BAND_AVAILABILITY_STATUS_API).putExtra("params", jsonObject.toString()));
                        popupWindow.dismiss();

                    }
                });
                tvDoNot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_red));
                        popupWindow.dismiss();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("LoginUserID", sharedPreferences.getString(Constants.USER_ID, ""));
                            jsonObject.put("NewStatus", "Do_not_disturb");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getActivity().startService(new Intent(getActivity(), CommonService.class).putExtra("API", Constants.UPDATE_BAND_AVAILABILITY_STATUS_API).putExtra("params", jsonObject.toString()));

                    }
                });
                tvInvisible.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.circle_transparent));
                        popupWindow.dismiss();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
                            jsonObject.put("NewStatus", "Offline");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getActivity().startService(new Intent(getActivity(), CommonService.class).putExtra("API", Constants.UPDATE_BAND_AVAILABILITY_STATUS_API).putExtra("params", jsonObject.toString()));

                    }
                });

                break;

        }

    }

    private void hitAPIs() {
        if (Utils.isNetworkConnected(getActivity())) {
            initializeLoader();
            RetrofitAPI.callGetAPI(Constants.FOR_GENRE_LIST, BandFormFragment.this);

        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }


    }

    private void getCountriesList() {
        if (Utils.isNetworkConnected(getActivity())) {
            initializeLoader();
            RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, BandFormFragment.this);

        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }

    }


    private void getUserProfile() {
        if (Utils.isNetworkConnected(getActivity())) {
            initializeLoader();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("BandId",mBandId);
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_BAND_PROFILE, BandFormFragment.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }

    }


    public void apiUpdateProfile() {

        String requestBody = "";
        if (Utils.isNetworkConnected(getActivity())) {
            JSONObject jsonBody = new JSONObject();
            String mEmail = "", mCity, mPostCode = "", mBio = "", mBandName = "";
            String mWebsite = "";

            mBandName = etBandName.getText().toString();
            mEmail = et_email.getText().toString().trim();
            //mCountryId = tvCountryCode.getText().toString().trim();
            mMobileNumber = etMobileNumber.getText().toString().trim();
            mCity = et_city.getText().toString().trim();
            mPostCode = et_postal_code.getText().toString().trim();
            mBio = et_desc.getText().toString().trim();
            mGenre = tvGenre.getText().toString().trim();
            mWebsite = et_website.getText().toString().trim();

            mExperience = tvExperience.getText().toString().trim();

            if (mBandName.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_band_name));

            } else if (mEmail.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_email_id));
            } else if (mCountryId.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_country_code));

            } else if (mMobileNumber.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_phone_empty));

            } else if (mMobileNumber.length() < 10) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_phone));
            } else if (mCity.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_city));

            } else if (mPostCode.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_postcode));

            } else if (mBio.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_bio));

            } else if (mCount < 10) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_bio_count));

            } else if (mGenre.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_genre));
            } else if (mExperience.equalsIgnoreCase("")) {
                Utils.showToast(getActivity(), getResources().getString(R.string.err_experience));
            } else {
                initializeLoader();


                try {
                    jsonBody.put("BandName", mBandName);
                    jsonBody.put("BandContactNo", mMobileNumber);
                    jsonBody.put("BandEmail", mEmail);
                    jsonBody.put("BandWebsite", mWebsite);
                    jsonBody.put("BandCity", mCity);
                    jsonBody.put("PostCode", mPostCode);
                    jsonBody.put("CountryId", mCountryId);
                    jsonBody.put("Bio", mBio);
                    jsonBody.put("BandGenreId", mGenreId);
                    jsonBody.put("ExperienceId", mExperienceId);
                    jsonBody.put("BandManagerId", sharedPreferences.getString(Constants.USER_ID, ""));
                    jsonBody.put("ProfileTypeId", "2");
                    jsonBody.put("BandId", mBandId);
                    requestBody = jsonBody.toString();
                    RetrofitAPI.callAPI(requestBody, Constants.FOR_UPDATE_BAND_PROFILE, BandFormFragment.this);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }


    }

    void initializeLoader() {
        Utils.initializeProgressDialog(getActivity());
        Utils.showProgressDialog();

    }


    public void openDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
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
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermission();
                                    }
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {

        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)
                try {
                    Uri uri = data.getData();


                    String filePath = FileUtils.compressImage(uri.toString(), getActivity());
                    File file = new File(filePath);
                    showImage(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                    RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString(Constants.USER_ID, ""));
                    uploadImages(fileToUpload, mUSerId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA) {
                Uri uri = Uri.parse(selectedImagePath);
                String filePath = FileUtils.compressImage(uri.toString(), getActivity());
                File file = new File(filePath);
                showImage(file);

                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString(Constants.USER_ID, ""));
                uploadImages(fileToUpload, mUSerId);
            }

        }
    }

    private void showImage(File file) {
        ivProfile.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.GONE);
        ivCamera.setVisibility(View.VISIBLE);
        ivAddImage.setVisibility(View.GONE);
        Glide.with(getActivity()).load(file).into(ivProfile);

    }


    void uploadImages(MultipartBody.Part fileToUpload, RequestBody mUSerId) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ROOT_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        ImageUploadInterface uploadImage = retrofit.create(ImageUploadInterface.class);
        Call<String> fileUpload = uploadImage.uploadBandProfilePic(fileToUpload, mUSerId);
        fileUpload.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        Utils.showToast(getActivity(), "Profile Pic uploaded successfully");
                        // Utils.showToast(getActivity(), "Response " + response.raw().message());

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("TAG", "Error " + t.getMessage());
            }
        });
    }

    public void showDropdown(final String[] array, final TextView textView, final SpinnerData spinnerData, final int width) {
        listPopupWindow = new ListPopupWindow(
                getActivity());
        listPopupWindow.setAdapter(new ArrayAdapter(
                getActivity(),
                R.layout.row_profile_spinner, array));
        listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_black));
        listPopupWindow.setAnchorView(textView);
        listPopupWindow.setWidth(width);
        listPopupWindow.setHeight(400);

        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (textView.getId() == R.id.tvCountryCode) {
                    spinnerData.getData(countryAL.get(position).getCountryId(), countryAL.get(position).getCountryName());
                } else if (textView.getId() == R.id.tvGenre) {
                    spinnerData.getData(array[position], genreAL.get(position).getId());

                } else if (textView.getId() == R.id.tvExperience) {
                    int pos = position + 1;
                    spinnerData.getData(array[position], pos + "");

                } else {
                    spinnerData.getData(array[position], "");
                }
                textView.setText(array[position]);

                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }


    public Rect locateView() {
        View v = ivStatus;
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }
}