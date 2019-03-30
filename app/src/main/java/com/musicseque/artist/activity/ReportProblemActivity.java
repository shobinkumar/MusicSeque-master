package com.musicseque.artist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.artist.fragments.ProfileDetailFragment;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.ImageUploadClass;
import com.musicseque.start_up.LoginActivity;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ReportProblemActivity extends BaseActivity implements MyInterface {
    @BindView(R.id.img_first_icon)
    ImageView img_first_icon;
    @BindView(R.id.img_right_icon)
    ImageView img_right_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rlAttachment)
    RelativeLayout rlAttachment;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvImageName)
    TextView tvImageName;

    @BindView(R.id.etMessage)
    EditText etMessage;

    String mEmail, mMessage;
    private MultipartBody.Part fileUpload;
    private RequestBody mID;
    private String mFileName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        ButterKnife.bind(this);
        tv_title.setText("Report A Problem");
        img_first_icon.setVisibility(View.VISIBLE);
        img_right_icon.setVisibility(View.GONE);

        tvEmail.setText(sharedPreferences.getString(Constants.EMAIL_ID, ""));

    }


    @OnClick({R.id.img_first_icon, R.id.btnSubmit, R.id.txt_attach})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_first_icon:
                finish();
                break;
            case R.id.btnSubmit:
                mEmail = tvEmail.getText().toString();
                mMessage = etMessage.getText().toString();
                if (mEmail.length() == 0) {
                    Utils.showToast(ReportProblemActivity.this, getResources().getString(R.string.err_email_id));
                } else if (!Utils.emailValidator(mEmail)) {
                    Utils.showToast(ReportProblemActivity.this, getResources().getString(R.string.err_email_invalid));
                } else if (mMessage.length() == 0) {
                    Utils.showToast(ReportProblemActivity.this, "Please enter the message");
                } else {
                    if (Utils.isNetworkConnected(ReportProblemActivity.this)) {
                        Utils.initializeAndShow(ReportProblemActivity.this);
                        hitAPI();
                    } else {
                        Utils.showToast(ReportProblemActivity.this, getResources().getString(R.string.err_no_internet));

                    }
                }

                break;
            case R.id.txt_attach:
                openDialog("report");
                break;
        }
    }

    private void hitAPI() {
        RequestBody mUSerId = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString(Constants.USER_ID, ""));
        RequestBody message = RequestBody.create(MediaType.parse("text/plain"), mMessage);
        ImageUploadClass.imageUpload(fileUpload, mUSerId, message, Constants.FOR_REPORT_PROBLEM, ReportProblemActivity.this);


    }

    public void getImageDetails(MultipartBody.Part fileToUpload, RequestBody mUSerId, String name) {
        fileUpload = fileToUpload;
        mFileName=name;
        tvImageName.setVisibility(View.VISIBLE);
        tvImageName.setText(name);
        // mID = mUSerId;
        // hitAPI();

    }

    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            if (jsonObject.getString("Status").equalsIgnoreCase("success")) {
                Utils.showToast(ReportProblemActivity.this, jsonObject.getString("Message"));
                Intent intent = new Intent(ReportProblemActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
            } else {
                Utils.showToast(ReportProblemActivity.this, jsonObject.getString("Message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
