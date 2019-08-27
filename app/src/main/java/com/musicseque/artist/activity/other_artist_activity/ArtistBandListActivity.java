package com.musicseque.artist.activity.other_artist_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.artist.artist_adapters.ArtistBandListAdapter;
import com.musicseque.artist.artist_models.BandDataModel;
import com.musicseque.artist.band.band_adapter.BandListAdapter;
import com.musicseque.artist.band.band_fragment.BandListFragment;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistBandListActivity extends BaseActivity implements MyInterface {
    @BindView(R.id.recyclerBand)
    RecyclerView recyclerBand;

    @BindView(R.id.tvNoBand)
    TextView tvNoBand;


    @BindView(R.id.img_first_icon)
    ImageView img_first_icon;
    @BindView(R.id.img_right_icon)
    ImageView img_right_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;

    int mUserId;

    RecyclerView.LayoutManager layoutManager;
    BandListAdapter adapter;
    ArrayList<BandDataModel> alBand = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_bands);
        ButterKnife.bind(this);
        initOtherViews();
        initViews();
        listeners();
    }

    private void listeners() {
        img_first_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {

        recyclerBand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        hitAPIs("get_list");
    }

    private void initOtherViews() {

        mUserId = getIntent().getIntExtra("id",0);
        tv_title.setText("Bands");
        img_first_icon.setVisibility(View.VISIBLE);
        img_right_icon.setVisibility(View.GONE);

    }

    private void hitAPIs(String type) {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this);
            try {

                if (type.equalsIgnoreCase("get_list")) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("LoginUserId", mUserId+"");
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_BAND_LIST, this);
                }


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
            case Constants.FOR_BAND_LIST:
                try {
                    JSONObject object = new JSONObject(response.toString());
                    if (object.getString("Status").equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = object.getJSONArray("result");
                        alBand = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BandDataModel>>() {
                        }.getType());
                        recyclerBand.setVisibility(View.VISIBLE);
                        tvNoBand.setVisibility(View.GONE);
                        recyclerBand.setAdapter(new ArtistBandListAdapter(this, alBand));
                    } else {
                        recyclerBand.setVisibility(View.GONE);
                        tvNoBand.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


        }
    }
}
