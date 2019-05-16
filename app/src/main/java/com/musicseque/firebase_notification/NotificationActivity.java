package com.musicseque.firebase_notification;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.artist.activity.OtherProfileActivity;
import com.musicseque.artist.band.band_model.BandMemberStatusModel;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.interfaces.RemoveMemberInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends BaseActivity implements MyInterface, RemoveMemberInterface {

    @BindView(R.id.recyclerNotification)
    RecyclerView recyclerNotification;
    @BindView(R.id.tvNoNotification)
    TextView tvNoNotification;

    int API_TYPE;
    int FOR_NOTIFICATIONS;
    int FOR_ACCEPT;
    int FOR_REJECT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initOtherViews();
        initViews();
        hitAPI();

    }


    private void initOtherViews() {
        ButterKnife.bind(this);

    }

    private void initViews() {
        recyclerNotification.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void hitAPI() {
        if (Utils.isNetworkConnected(this)) {
            API_TYPE = FOR_NOTIFICATIONS;
            showDialog();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("LoginUserId", sharedPreferences.getString(Constants.USER_ID, ""));
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_NOTIFICATION, NotificationActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Utils.showToast(this, getResources().getString(R.string.err_no_internet));
        }
    }

    void showDialog() {
        Utils.initializeAndShow(NotificationActivity.this);
    }


    @Override
    public void sendMemberId(@NotNull String id) {

        if (id.equalsIgnoreCase("accept")) {
            API_TYPE = FOR_ACCEPT;

            JSONObject object = new JSONObject();
            try {
                object.put("user_id", "");
                object.put("band_id", "");
                object.put("owner_id", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RetrofitAPI.callAPI(object.toString(), Constants.FOR_ACCEPT_BAND_REQUEST, NotificationActivity.this);

        } else if (id.equalsIgnoreCase("reject")) {
            API_TYPE = FOR_REJECT;

            JSONObject object = new JSONObject();

            try {
                object.put("user_id", "");
                object.put("band_id", "");
                object.put("owner_id", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RetrofitAPI.callAPI(object.toString(), Constants.FOR_REJECT_BAND_REQUEST, NotificationActivity.this);

        }
    }

    @Override
    public void addOrRemoveMember(@NotNull String id, @NotNull String type, @NotNull BandMemberStatusModel data, int pos) {

    }


    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        if (TYPE == FOR_NOTIFICATIONS) {

        } else if (TYPE == FOR_ACCEPT) {

        } else if (TYPE == FOR_REJECT) {

        }


    }

}
