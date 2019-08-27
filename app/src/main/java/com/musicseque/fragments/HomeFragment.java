package com.musicseque.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.artist.activity.other_artist_activity.SearchArtistActivity;
import com.musicseque.artist.activity.other_artist_activity.SearchBandActivity;
import com.musicseque.service.LocationService;
import com.musicseque.utilities.Utils;
import com.musicseque.venues.activity.SearchVenueActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




public class HomeFragment extends Fragment implements View.OnClickListener {
    private ImageView img_gigs, img_venue, img_event_manger, img_talent_manager, iv_featured, ivDrawer;
    private Context context;
    private LinearLayout line_settings;
    private TextView et_explore;
    Fragment fragment = null;

    @BindView(R.id.ivArtist)
    ImageView ivArtist;

    @BindView(R.id.ivEditLoc)
    ImageView ivEditLoc;
    @BindView(R.id.etLoc)
    EditText etLoc;

    @BindView(R.id.ivBand)
    ImageView ivBand;
    @BindView(R.id.rlSearch)
    RelativeLayout rlSearch;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        TextView tv_title = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);
        ImageView img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        img_right_icon.setVisibility(View.GONE);
        tv_title.setText("Home");
        initialize();
        clickListner();

        return v;
    }

    public void initialize() {
        img_gigs = (ImageView) v.findViewById(R.id.img_gigs);
        img_venue = (ImageView) v.findViewById(R.id.img_venue);

        iv_featured = (ImageView) v.findViewById(R.id.iv_profile);
        img_event_manger = (ImageView) v.findViewById(R.id.img_event_manger);
        img_talent_manager = (ImageView) v.findViewById(R.id.img_talent_manager);
        et_explore = (TextView) v.findViewById(R.id.et_explore);
        try
        {
            Address address = Utils.getCompleteAddressString(Double.parseDouble(LocationService.mLatitude), Double.parseDouble(LocationService.mLongitude), getActivity());
            etLoc.setText(address.getAddressLine(0));
        }
        catch(Exception e)
        {      

        }
        
    }

    public void clickListner() {


        img_gigs.setOnClickListener(this);
        img_venue.setOnClickListener(this);
        img_event_manger.setOnClickListener(this);
        img_talent_manager.setOnClickListener(this);
        et_explore.setOnClickListener(this);
//        etLoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b)
//                {
//                    etLoc.setCursorVisible(true);
//                    etLoc.setFocusable(true);
//                }
//                else
//                {
//                    etLoc.setCursorVisible(false);
//                    etLoc.setFocusable(false);
//                }
//            }
//        });



    }


    @Override
    public void onClick(View view) {
        if (view == img_gigs) {
            Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();

        } else if (view == img_venue) {
            startActivity(new Intent(getActivity(), SearchVenueActivity.class));
        } else if (view == img_event_manger) {
            Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();

        } else if (view == img_talent_manager) {
            Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();


        }


    }

    @OnClick({R.id.ivArtist, R.id.rlSearch,R.id.ivSearch,R.id.ivEditLoc,R.id.ivBand})
    public void click(View view) {
        if (view.getId() == R.id.ivArtist) {
            startActivity(new Intent(getActivity(), SearchArtistActivity.class));
        } else if (view.getId() == R.id.rlSearch) {
            startActivity(new Intent(getActivity(), SearchArtistActivity.class));
        }
        else if (view.getId() == R.id.ivSearch) {
            startActivity(new Intent(getActivity(), SearchArtistActivity.class));
        }
        else if(view.getId()==R.id.ivEditLoc)
        {
            etLoc.setCursorVisible(true);
            etLoc.setFocusable(true);
        }
        else if(view.getId()==R.id.ivBand)
        {
           startActivity(new Intent(getActivity(), SearchBandActivity.class));
        }

    }
}
