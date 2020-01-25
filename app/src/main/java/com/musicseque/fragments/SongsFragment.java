package com.musicseque.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musicseque.R;

import com.musicseque.artist.artist_adapters.SongsAdapter;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.KotlinBaseFragment;
import com.musicseque.utilities.Utils;

import org.json.JSONObject;

public class SongsFragment extends KotlinBaseFragment implements MyInterface {
    View view;
    RecyclerView recyclerSongs;
    RecyclerView.LayoutManager layoutManager;
    SongsAdapter songsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_songs, container, false);
        initOtherViews();
        initViews();
        listeners();
        hitAPI();
        return view;
    }


    private void initOtherViews() {

    }

    private void initViews() {
        recyclerSongs = (RecyclerView) view.findViewById(R.id.recyclerSongs);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerSongs.setLayoutManager(layoutManager);
    }

    private void listeners() {

    }

    private void hitAPI() {
        if (Utils.isNetworkConnected(getActivity())) {
            Utils.initializeAndShow(getActivity());
            try {
                JSONObject jsonBody = new JSONObject();
//                jsonBody.put("UserName", email);
//                jsonBody.put("Password", pasw);
//                jsonBody.put("FirstName", firstName);
//                jsonBody.put("LastName", lstName);
//                jsonBody.put("Phone", phone);
//                jsonBody.put("ProfileTypeId", profileType);
//                jsonBody.put("CountryCodeId", "0");
//                requestBody = jsonBody.toString();
//                Log.e("tag", "request is  " + requestBody);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  RetrofitAPI.callAPI(requestBody, Constants.FOR_SONGS_LIST,SongsFragment.this);
        } else {
            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
        }

    }

    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();

        if (TYPE == Constants.FOR_SONGS_LIST) {

        }
    }
}
