package com.musicseque.artist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicseque.Fonts.CustomButton;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements MyInterface {

    @BindView(R.id.etCurrentPassword)
    EditText etCurrentPassword;


    @BindView(R.id.etNewPassword)
    EditText etNewPassword;

    @BindView(R.id.etConfirmNewPassword)
    EditText etConfirmNewPassword;

    @BindView(R.id.btnChangePassword)
    CustomButton btnChangePassword;

    @BindView(R.id.btnCancel)
    CustomButton btnCancel;


    @BindView(R.id.img_first_icon)
    ImageView img_first_icon;
    @BindView(R.id.img_right_icon)
    ImageView img_right_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;

    String mCurrentPassword, mNewPassword, mConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        tv_title.setText("Change Password");
        img_first_icon.setVisibility(View.VISIBLE);
        img_right_icon.setVisibility(View.GONE);
    }


    @OnClick({R.id.btnChangePassword, R.id.btnCancel, R.id.img_first_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChangePassword:
                mCurrentPassword = etCurrentPassword.getText().toString().trim();
                mNewPassword = etNewPassword.getText().toString().trim();
                mConfirmNewPassword = etConfirmNewPassword.getText().toString().trim();

                if (Utils.isNetworkConnected(this)) {
                    if (mCurrentPassword.equalsIgnoreCase("")) {
                        Utils.showToast(this, getString(R.string.err_current_password));
                    } else if (!mCurrentPassword.equalsIgnoreCase(sharedPreferences.getString(Constants.PASSWORD, ""))) {
                        Utils.showToast(this, getString(R.string.err_current_password_not_correct));
                    } else if (mNewPassword.length() == 0) {
                        Utils.showToast(this, getResources().getString(R.string.err_password_empty));
                    } else if (mNewPassword.length() < 6 || !Utils.isValidPassword(mNewPassword)) {
                        Utils.showToast(this, getResources().getString(R.string.err_password));
                    } else if (mConfirmNewPassword.length() == 0) {
                        Utils.showToast(this, getResources().getString(R.string.err_confirm_password_empty));

                    } else if (!mNewPassword.equals(mConfirmNewPassword)) {
                        Utils.showToast(this, getResources().getString(R.string.err_password_not_match));
                    } else {
                        Utils.initializeAndShow(this);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
                            jsonObject.put("Password", mNewPassword);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_CHANGE_PASSWORD, this);

                    }
                } else {

                }


                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.img_first_icon:
                finish();
                break;
        }
    }


    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                Utils.showToast(this, jsonObject.getString("Message"));
                editor.putString(Constants.PASSWORD, mNewPassword).commit();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Utils.showToast(this, jsonObject.getString("Message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
