package com.musicseque.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.musicseque.MainActivity;
import com.musicseque.R;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private ImageView img_artist, img_gigs, img_venue, img_event_manger, img_talent_manager, iv_featured, img_search, ivDrawer;
    private Context context;
    private LinearLayout line_settings;
    private TextView et_explore;
    private RelativeLayout rel_search;
    Fragment fragment = null;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tv_title = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);
        ImageView img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        img_right_icon.setVisibility(View.GONE);
        tv_title.setText("Home");


        initialize();

        // initialization


        //

        clickListner();
        return v;
    }

    public void initialize() {

//        line_settings = (LinearLayout) findViewById(R.id.line_settings);

        img_artist = (ImageView) v.findViewById(R.id.img_artist);
        img_gigs = (ImageView) v.findViewById(R.id.img_gigs);
        img_venue = (ImageView) v.findViewById(R.id.img_venue);

        iv_featured = (ImageView) v.findViewById(R.id.iv_profile);
        img_event_manger = (ImageView) v.findViewById(R.id.img_event_manger);
        img_talent_manager = (ImageView) v.findViewById(R.id.img_talent_manager);
        et_explore = (TextView) v.findViewById(R.id.et_explore);
        rel_search = (RelativeLayout) v.findViewById(R.id.rel_search);
        img_search = (ImageView) v.findViewById(R.id.img_search);
    }

    public void clickListner() {

        img_artist.setOnClickListener(this);
        img_gigs.setOnClickListener(this);
        img_venue.setOnClickListener(this);
        img_event_manger.setOnClickListener(this);
        img_talent_manager.setOnClickListener(this);
//        iv_featured.setOnClickListener(this);
//        line_settings.setOnClickListener(this);
        et_explore.setOnClickListener(this);

        rel_search.setOnClickListener(this);
        img_search.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == img_artist) {
            Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();
        } else if (view == img_gigs) {
            Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();

        } else if (view == img_venue) {
            Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();

        } else if (view == img_event_manger) {
            Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();

        } else if (view == img_talent_manager) {
            Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();


        }

//        else if (view == rel_search) {
//
//            fragment = new FragSearchResults();
//            SwtichFragemnts.replaceFragment(fragment, getFragmentManager());
//        } else if (view == et_explore) {
//            fragment = new FragSearchResults();
//            SwtichFragemnts.replaceFragment(fragment, getFragmentManager());
//        } else if (view == img_search) {
//
//            fragment = new FragSearchResults();
//            SwtichFragemnts.replaceFragment(fragment, getFragmentManager());
//        }
    }
}
