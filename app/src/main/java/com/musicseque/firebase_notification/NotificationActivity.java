package com.musicseque.firebase_notification;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.adapters.NotificationAdapter;
import com.musicseque.interfaces.CommonInterface;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.models.NotificationModel;
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

public class NotificationActivity extends BaseActivity implements MyInterface, CommonInterface {

    @BindView(R.id.recyclerNotification)
    RecyclerView recyclerNotification;
    @BindView(R.id.tvNoNotification)
    TextView tvNoNotification;

    @BindView(R.id.img_first_icon)
    ImageView img_first_icon;
    @BindView(R.id.img_right_icon)
    ImageView img_right_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;

    ArrayList<NotificationModel> arrayList = new ArrayList();
    NotificationAdapter notificationAdapter;

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
        tv_title.setText("Notifications");
        img_first_icon.setVisibility(View.VISIBLE);
        img_right_icon.setVisibility(View.GONE);

    }

    private void initViews() {
        recyclerNotification.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void hitAPI() {
        if (Utils.isNetworkConnected(this)) {
            showDialog();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("LoggedInArtistId", SharedPref.getString(Constants.USER_ID, ""));
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
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        if (TYPE == Constants.FOR_NOTIFICATION) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("Status").equalsIgnoreCase("Success")) {
                    recyclerNotification.setVisibility(View.VISIBLE);
                    tvNoNotification.setVisibility(View.GONE);
                    JSONArray jsonArray = object.getJSONArray("result");
                    arrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<NotificationModel>>() {
                    }.getType());
                    notificationAdapter = new NotificationAdapter(NotificationActivity.this, arrayList, NotificationActivity.this);
                    recyclerNotification.setAdapter(notificationAdapter);
                } else {
                    recyclerNotification.setVisibility(View.GONE);
                    tvNoNotification.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (TYPE == Constants.FOR_ACCEPT_BAND_REQUEST) {

            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("Status").equalsIgnoreCase("Success")) {
                    Utils.showToast(NotificationActivity.this, object.getString("MesOutput"));
                    hitAPI();
                } else {
                    Utils.showToast(NotificationActivity.this, object.getString("MesOutput"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (TYPE == Constants.FOR_REJECT_BAND_REQUEST) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("Status").equalsIgnoreCase("Success")) {
                    Utils.showToast(NotificationActivity.this, object.getString("MesOutput"));
                    hitAPI();
                } else {
                    Utils.showToast(NotificationActivity.this, object.getString("MesOutput"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void commonMethod(Object obj, String status) {
        NotificationModel model = (NotificationModel) obj;
        if (status.equalsIgnoreCase("accept")) {


            JSONObject object = new JSONObject();
            try {
                object.put("LoggedInArtistId", SharedPref.getString(Constants.USER_ID, ""));
                object.put("BandId", model.getBandId());
                object.put("BandManagerId", model.getBandManagerId());
                object.put("RequestStatus", "Y");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RetrofitAPI.callAPI(object.toString(), Constants.FOR_ACCEPT_BAND_REQUEST, NotificationActivity.this);

        } else if (status.equalsIgnoreCase("reject")) {


            JSONObject object = new JSONObject();

            try {
                object.put("LoggedInArtistId", SharedPref.getString(Constants.USER_ID, ""));
                object.put("BandId", model.getBandId());
                object.put("BandManagerId", model.getBandManagerId());
                object.put("RequestStatus", "N");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RetrofitAPI.callAPI(object.toString(), Constants.FOR_REJECT_BAND_REQUEST, NotificationActivity.this);

        }
    }

    @OnClick({R.id.img_first_icon})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_first_icon:
                finish();
                break;
        }
    }
}
