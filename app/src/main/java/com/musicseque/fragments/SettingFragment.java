package com.musicseque.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.musicseque.artist.service.CommonService;
import com.musicseque.dagger_data.DaggerRetrofitComponent;
import com.musicseque.dagger_data.RetrofitComponent;
import com.musicseque.dagger_data.SharedPrefDependency;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.start_up.LoginActivity;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class SettingFragment extends Fragment implements View.OnClickListener, MyInterface {
    private TextView tv_logout, tv_title, tvHeading;

    ImageView img_right_icon;
    RelativeLayout rltoolbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RetrofitComponent retrofitComponent;
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
        if (sharedPreferences.getString(Constants.LOGIN_TYPE, "Simple").equalsIgnoreCase("Simple")) {
            tvChangePassword.setVisibility(View.VISIBLE);
        } else {
            tvChangePassword.setVisibility(View.GONE);
        }

        return v;
    }

    private void initOtherViews() {

        img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        img_right_icon.setVisibility(View.GONE);

        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
        sharedPreferences = retrofitComponent.getShared();
        editor = retrofitComponent.getEditor();
    }

    private void initViews() {
        tv_logout = (TextView) v.findViewById(R.id.tv_logout);
        tv_title = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);
        tv_title.setText("Settings");
    }

    private void listeners() {
        tv_logout.setOnClickListener(this);
    }


    @OnClick({R.id.tvStatus, R.id.tvChangePassword, R.id.tvPrivacyPolicy, R.id.tvReportProblem, R.id.tvTermsService,R.id.tvDeleteAccount})
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
                        editor.putString(Constants.VISIBILITY_STATUS, "Available").commit();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
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
                        editor.putString(Constants.VISIBILITY_STATUS, "Do_not_disturb").commit();
                        popupWindow.dismiss();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
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
                        editor.putString(Constants.VISIBILITY_STATUS, "Offline").commit();
                        popupWindow.dismiss();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
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
                                        String str = new JSONObject().put("UserId", sharedPreferences.getString(Constants.USER_ID, "")).toString();
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
                clearLoginCredentials();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;

        }
    }


    void clearLoginCredentials() {
        try {
            editor.putBoolean(Constants.IS_LOGIN, false).commit();
            editor.putString(Constants.COUNTRY_CODE, "").commit();
            editor.putString(Constants.COUNTRY_ID, "").commit();
            editor.putString(Constants.COUNTRY_NAME, "").commit();
            editor.putString(Constants.MOBILE_NUMBER, "").commit();
            editor.putString(Constants.PROFILE_IMAGE, "").commit();
            editor.putString(Constants.USER_NAME, "").commit();
            editor.putString(Constants.USER_ID, "").commit();
            editor.putString(Constants.PROFILE_TYPE, "").commit();
            editor.putString(Constants.PROFILE_ID, "").commit();
            // editor.putString(Constants.EMAIL_ID, "").commit();
            editor.putString(Constants.PROFILE_IMAGE, "").commit();
            editor.putString(Constants.COUNTRY_NAME, "").commit();
            editor.putString(Constants.LOGIN_TYPE, "").commit();

            editor.putString(Constants.UNIQUE_CODE, "").commit();
            editor.putString(Constants.IS_FIRST_LOGIN, "").commit();


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
                    JSONObject jsonObject=new JSONObject(response.toString());
                    if(jsonObject.getString("Status").equalsIgnoreCase("Success"))
                    {
                        clearLoginCredentials();
                        Utils.showToast(getActivity(),jsonObject.getString("Message"));
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                   break;
        }
    }
}
