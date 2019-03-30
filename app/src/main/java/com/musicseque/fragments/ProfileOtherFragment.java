package com.musicseque.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musicseque.R;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileOtherFragment extends Fragment implements View.OnClickListener, MyInterface {
    View v;
    TextView tvProfile, tvEvents, tv_title, tv_first_name, tv_lst_name, tv_email, tv_phone, tv_city, tv_postCode, tv_country, tv_bio;
    TextView tvExperience, tvTalent, tvSkills, tvCertification, tvWebsite, tv_Genre, tvCollaborators1, tvCollaborators2, tvCollaborators3;
    TextView tvMusicVideos1, tvMusicVideos2, tvMusicVideos3;
    private TextView tvUserName, tvUserSpeciality, tvUserLocation, tvUserId;
    CircleImageView ivUserImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile_other, container, false);
        initOtherViews();
        initViews();
        listeners();
        hitAPI();

        return v;
    }



    private void initOtherViews() {

    }

    private void initViews() {


        tvProfile = (TextView) v.findViewById(R.id.tvProfile);
        tvEvents = (TextView) v.findViewById(R.id.tvEvents);
        tv_title = (TextView) v.findViewById(R.id.tv_title);

        ivUserImage=(CircleImageView)v.findViewById(R.id.ivUserImage);
        tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvUserSpeciality = (TextView) v.findViewById(R.id.tvUserSpeciality);
        tvUserLocation = (TextView) v.findViewById(R.id.tvUserLocation);
        tvUserId = (TextView) v.findViewById(R.id.tvUserId);


        tv_first_name = (TextView) v.findViewById(R.id.tv_first_name);
        tv_lst_name = (TextView) v.findViewById(R.id.tv_lst_name);
        tv_email = (TextView) v.findViewById(R.id.tv_email);
        tv_phone = (TextView) v.findViewById(R.id.tv_phone);
        tv_city = (TextView) v.findViewById(R.id.tv_city);
        tv_postCode = (TextView) v.findViewById(R.id.tv_postCode);
        tv_country = (TextView) v.findViewById(R.id.tv_country);
        tv_bio = (TextView) v.findViewById(R.id.tv_bio);
        tvExperience = (TextView) v.findViewById(R.id.tvExperience);
        tvTalent = (TextView) v.findViewById(R.id.tvTalent);
        tvSkills = (TextView) v.findViewById(R.id.tvSkills);
        tvCertification = (TextView) v.findViewById(R.id.tvCertification);
        tvWebsite = (TextView) v.findViewById(R.id.tvWebsite);
        tv_Genre = (TextView) v.findViewById(R.id.tv_Genre);

        tvCollaborators1 = (TextView) v.findViewById(R.id.tvCollaborators1);
        tvCollaborators2 = (TextView) v.findViewById(R.id.tvCollaborators2);
        tvCollaborators3 = (TextView) v.findViewById(R.id.tvCollaborators3);
        tvMusicVideos1 = (TextView) v.findViewById(R.id.tvMusicVideos1);
        tvMusicVideos2 = (TextView) v.findViewById(R.id.tvMusicVideos2);
        tvMusicVideos3 = (TextView) v.findViewById(R.id.tvMusicVideos3);


    }

    private void listeners() {
        tvProfile.setOnClickListener(this);
        tvEvents.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvProfile:
                break;
            case R.id.tvEvents:
                break;

        }
    }


    private void hitAPI() {
    if(Utils.isNetworkConnected(getActivity()))
    {
        Utils.initializeAndShow(getActivity());
        RetrofitAPI.callGetAPI(Constants.FOR_OTHER_PROFILE, ProfileOtherFragment.this);
    }
    else
    {
        Utils.showToast(getActivity(),getResources().getString(R.string.err_no_internet));
    }

    }


    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        if(TYPE==Constants.FOR_OTHER_PROFILE)
        {
            try {
                JSONObject obj = new JSONObject(response.toString());
                String DisplayName = obj.getString("DisplayName");
                String FirstName = obj.getString("FirstName");
                String LastName = obj.getString("LastName");
                String City = obj.getString("City");
                String PostCode = obj.getString("PostCode");
                String Bio = obj.getString("Bio");
                String Genre = obj.getString("Genre");
                String CountryName = obj.getString("CountryName");
                String ArtistType = obj.getString("ArtistType");
                String Email = obj.getString("Email");
                String Website = obj.getString("Website");
                String ContactNo = obj.getString("ContactNo");
                String GenreTypeName = obj.getString("GenreTypeName");

                //tv_display_name.setText(DisplayName);
                tv_first_name.setText(FirstName);
                tv_lst_name.setText(LastName);
                tv_email.setText(Email);
                tv_phone.setText(ContactNo);
                tv_city.setText(City);
                tv_postCode.setText(PostCode);
                tv_country.setText(CountryName);
                tvWebsite.setText(Website);
                tv_bio.setText(Bio);
                tv_Genre.setText(GenreTypeName);
               // tv_artist_type.setText(ArtistType);

            } catch (JSONException e) {
                e.printStackTrace();


            }

        }
    }
}
