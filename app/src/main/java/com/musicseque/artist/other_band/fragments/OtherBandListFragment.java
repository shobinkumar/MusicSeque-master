package com.musicseque.artist.other_band.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.artist.band.band_adapter.BandListAdapter;
import com.musicseque.artist.artist_models.BandDataModel;
import com.musicseque.artist.band.band_fragment.BandFormFragment;
import com.musicseque.artist.fragments.BaseFragment;
import com.musicseque.artist.other_band.adapters.OtherBandListAdapter;
import com.musicseque.interfaces.MyInterface;
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

public class OtherBandListFragment extends BaseFragment implements MyInterface {
    View view;
    @BindView(R.id.recyclerBand)
    RecyclerView recyclerBand;

    @BindView(R.id.tvNoBand)
    TextView tvNoBand;


    TextView tvTitle;

    RecyclerView.LayoutManager layoutManager;
    BandListAdapter adapter;
    ArrayList<BandDataModel> alBand = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other_band_list, null);
        initOtherViews();
        initViews();
        return view;
    }

    private void initViews() {
        tvTitle.setText("Bands");
        recyclerBand.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        hitAPIs("get_list");
    }

    private void initOtherViews() {
        ButterKnife.bind(this, view);
        tvTitle = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);
        ImageView img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        img_right_icon.setVisibility(View.GONE);

    }

    private void hitAPIs(String type) {
        if (Utils.isNetworkConnected(getActivity())) {
            Utils.initializeAndShow(getActivity());
            try {

                if (type.equalsIgnoreCase("get_list")) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("LoggedInArtistId", SharedPref.getString(Constants.USER_ID, ""));
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_OTHER_BAND_LIST, OtherBandListFragment.this);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }

    }


    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        switch (TYPE) {
            case Constants.FOR_OTHER_BAND_LIST:
                try {
                    JSONObject object = new JSONObject(response.toString());
                    if (object.getString("Status").equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = object.getJSONArray("result");
                        alBand = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BandDataModel>>() {
                        }.getType());
                        tvNoBand.setVisibility(View.GONE);
                        recyclerBand.setVisibility(View.VISIBLE);

                        recyclerBand.setAdapter(new OtherBandListAdapter(getActivity(), alBand));
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
