package com.musicseque.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.artist.activity.ChangePasswordActivity;
import com.musicseque.artist.activity.PrivacyPolicyActivity;
import com.musicseque.artist.activity.ReportProblemActivity;
import com.musicseque.artist.fragments.ProfileFragment;
import com.musicseque.artist.service.CommonService;

import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.start_up.LoginActivity;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.SharedPref;
import com.musicseque.utilities.Utils;
import com.musicseque.venue_manager.fragment.CreateVenueFragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.musicseque.utilities.Constants.PROFILE_TYPE;


public class SettingFragment extends Fragment implements View.OnClickListener, MyInterface {
    private TextView tv_logout, tv_title, tvHeading;

    ImageView img_right_icon;
    RelativeLayout rltoolbar;
    //    SharedPref SharedPref;
//    SharedPref.SharedPref SharedPref;
//    private RetrofitComponent retrofitComponent;
    View v;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.tvPrivacyPolicy)
    TextView tvPrivacyPolicy;

    @BindView(R.id.tvChangePassword)
    TextView tvChangePassword;

    @BindView(R.id.tvTermsService)
    TextView tvTermsService;


    AlertDialog.Builder builder;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, v);
        initOtherViews();
        initViews();
        listeners();
        if (SharedPref.getString(Constants.LOGIN_TYPE, "Simple").equalsIgnoreCase("Simple")) {
            tvChangePassword.setVisibility(View.VISIBLE);
        } else {
            tvChangePassword.setVisibility(View.GONE);
        }

        if (SharedPref.getString(PROFILE_TYPE, "").equalsIgnoreCase("Venue Manager")) {
            tvStatus.setVisibility(View.GONE);
        } else {
            tvStatus.setVisibility(View.VISIBLE);

        }

        return v;
    }

    private void initOtherViews() {

        img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        img_right_icon.setVisibility(View.GONE);
        TextView tvDone = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvDone);
        tvDone.setVisibility(View.GONE);
//        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
//        SharedPref = retrofitComponent.getShared();
//        SharedPref = retrofitComponent.getSharedPref();
    }

    private void initViews() {
        tv_logout = (TextView) v.findViewById(R.id.tv_logout);
        tv_title = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);
        tv_title.setText("Settings");
    }

    private void listeners() {
        tv_logout.setOnClickListener(this);
    }


    @OnClick({R.id.tvStatus, R.id.tvChangePassword, R.id.tvPrivacyPolicy, R.id.tvReportProblem, R.id.tvTermsService, R.id.tvDeleteAccount})
    public void click(View view) {

        switch (view.getId()) {
            case R.id.tvStatus:
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.dropdown_user_status, null);
                TextView tvAvailable = (TextView) customView.findViewById(R.id.tvAvailable);
                TextView tvDoNot = (TextView) customView.findViewById(R.id.tvDoNot);
                TextView tvInvisible = (TextView) customView.findViewById(R.id.tvInvisible);


                //instantiate popup window
                final PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(tvStatus);

                // popupWindow.showAtLocation(tvStatus, Gravity.LEFT, locateView().left, locateView().left);
                tvAvailable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPref.putString(Constants.VISIBILITY_STATUS, "Available");

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""));
                            jsonObject.put("NewStatus", "Available");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getActivity().startService(new Intent(getActivity(), CommonService.class).putExtra("API", Constants.UPDATE_STATUS_API).putExtra("params", jsonObject.toString()));

                        popupWindow.dismiss();

                    }
                });
                tvDoNot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPref.putString(Constants.VISIBILITY_STATUS, "Do_not_disturb");
                        popupWindow.dismiss();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""));
                            jsonObject.put("NewStatus", "Do_not_disturb");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getActivity().startService(new Intent(getActivity(), CommonService.class).putExtra("API", Constants.UPDATE_STATUS_API).putExtra("params", jsonObject.toString()));

                        popupWindow.dismiss();
                    }
                });
                tvInvisible.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPref.putString(Constants.VISIBILITY_STATUS, "Offline");
                        popupWindow.dismiss();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""));
                            jsonObject.put("NewStatus", "Offline");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getActivity().startService(new Intent(getActivity(), CommonService.class).putExtra("API", Constants.UPDATE_STATUS_API).putExtra("params", jsonObject.toString()));

                        popupWindow.dismiss();
                    }
                });


                break;
            case R.id.tvChangePassword:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.tvPrivacyPolicy:
//                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://musicsegue.com/Privacy%20Policy.html"));
                startActivity(browserIntent);
                break;
            case R.id.tvTermsService:
//                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://musicsegue.com/TermsandConditions.html"));
                startActivity(browserIntent1);
                break;
            case R.id.tvReportProblem:
                startActivity(new Intent(getActivity(), ReportProblemActivity.class));
                break;
            case R.id.tvDeleteAccount:
                builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete your account?")
                        .setTitle("Music Segue")

                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Utils.isNetworkConnected(getActivity())) {

                                    try {
                                        Utils.initializeAndShow(getActivity());
                                        String str = new JSONObject().put("UserId", SharedPref.getString(Constants.USER_ID, "")).toString();
                                        RetrofitAPI.callAPI(str, Constants.FOR_DELETE_ACCOUNT, SettingFragment.this);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Music Segue");
                alert.show();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:

                if (Utils.isNetworkConnected(getActivity())) {
                    Utils.initializeAndShow(getActivity());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_LOGOUT, SettingFragment.this);
                    clearLoginCredentials();
                } else {
                    Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
                }
                break;

        }
    }


    void clearLoginCredentials() {
        try {
            SharedPref.putBoolean(Constants.IS_LOGIN, false);
            SharedPref.putString(Constants.COUNTRY_CODE, "");
            SharedPref.putString(Constants.COUNTRY_ID, "");
            SharedPref.putString(Constants.COUNTRY_NAME, "");
            SharedPref.putString(Constants.MOBILE_NUMBER, "");
            SharedPref.putString(Constants.PROFILE_IMAGE, "");
            SharedPref.putString(Constants.USER_NAME, "");
            SharedPref.putString(Constants.USER_ID, "");
            SharedPref.putString(Constants.PROFILE_TYPE, "");
            SharedPref.putString(Constants.PROFILE_ID, "");
            // SharedPref.putString(Constants.EMAIL_ID, "");
            SharedPref.putString(Constants.PROFILE_IMAGE, "");
            SharedPref.putString(Constants.COUNTRY_NAME, "");
            SharedPref.putString(Constants.LOGIN_TYPE, "");

            SharedPref.putString(Constants.UNIQUE_CODE, "");
            SharedPref.putString(Constants.IS_FIRST_LOGIN, "");


        } catch (Exception e) {

        }


    }

    public Rect locateView() {
        View v = tvStatus;
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

    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        switch (TYPE) {
            case Constants.FOR_DELETE_ACCOUNT:
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        clearLoginCredentials();
                        Utils.showToast(getActivity(), jsonObject.getString("Message"));
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case Constants.FOR_LOGOUT:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

                break;
        }
    }
}
