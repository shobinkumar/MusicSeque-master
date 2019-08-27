package com.musicseque.artist.activity.other_artist_activity;


import android.os.Bundle;


import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicseque.R;
import com.musicseque.activities.BaseActivity;
import com.musicseque.artist.artist_adapters.SearchArtistAdapter;
import com.musicseque.artist.artist_models.ArtistModel;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;

public class SearchArtistActivity extends BaseActivity implements MyInterface {

    @BindView(R.id.recyclerArtist)
    RecyclerView recyclerArtist;

    RecyclerView.LayoutManager layoutManager;
    SearchArtistAdapter searchArtistAdapter;
    @BindString(R.string.err_no_internet)
    String no_internet;
    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.img_first_icon)
    ImageView img_first_icon;
    @BindView(R.id.img_right_icon)
    ImageView img_right_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tvNoRecord)
    TextView tvNoRecord;
    ArrayList<ArtistModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artist);
        ButterKnife.bind(this);
        initOtherViews();
        initViews();
    }

    private void initOtherViews() {
        tv_title.setText("Artist");
        img_first_icon.setVisibility(View.VISIBLE);
        img_right_icon.setVisibility(View.GONE);
    }

    private void initViews() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerArtist.setLayoutManager(layoutManager);
        hitAPI();

    }

    private void hitAPI() {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(SearchArtistActivity.this);
            JSONObject jsonObject = new JSONObject();
            try {
               // jsonObject.put("ProfileTypeId", "1");
                jsonObject.put("UserId",sharedPreferences.getString(Constants.USER_ID,""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RetrofitAPI.callAPI(jsonObject.toString(), Constants.GET_ARTIST_LIST, SearchArtistActivity.this);
        } else {
            Utils.showToast(this, no_internet);
        }


    }

    @Override
    public void sendResponse(Object response, int TYPE) {

        Utils.hideProgressDialog();
        switch (TYPE) {
            case Constants.GET_ARTIST_LIST:
//                searchArtistAdapter = new SearchArtistAdapter(SearchArtistActivityEventManager.this, arrayList,sharedPreferences.getString(Constants.USER_ID,""));
//                recyclerArtist.setAdapter(searchArtistAdapter);


                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        if (jsonArray.length() > 0) {
                            arrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<ArtistModel>>() {
                            }.getType());

                            searchArtistAdapter = new SearchArtistAdapter(SearchArtistActivity.this, arrayList,sharedPreferences.getString(Constants.USER_ID,""));
                            recyclerArtist.setAdapter(searchArtistAdapter);
                        }

                    }
                    else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.SEARCH_ARTIST:
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        tvNoRecord.setVisibility(View.GONE);
                        recyclerArtist.setVisibility(View.VISIBLE);

                        arrayList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if (jsonArray.length() > 0) {
                            arrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<ArtistModel>>() {
                            }.getType());
                            searchArtistAdapter = new SearchArtistAdapter(SearchArtistActivity.this, arrayList,sharedPreferences.getString(Constants.USER_ID,""));
                            recyclerArtist.setAdapter(searchArtistAdapter);
                        }

                    }
                    else
                    {
                        recyclerArtist.setVisibility(View.GONE);
                        tvNoRecord.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @OnTextChanged(R.id.etSearch)
    protected void onTextChanged(CharSequence text) {


    }

    @OnTextChanged(value = R.id.etSearch, callback = AFTER_TEXT_CHANGED)
    public void afterEditTextInput(Editable editable) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkConnected(SearchArtistActivity.this)) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("SearchTypeValue", editable.toString());
                        jsonObject.put("UserId",sharedPreferences.getString(Constants.USER_ID,""));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.SEARCH_ARTIST, SearchArtistActivity.this);

                } else {
                    Utils.showToast(SearchArtistActivity.this, no_internet);
                }
            }
        }, 1000);
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
