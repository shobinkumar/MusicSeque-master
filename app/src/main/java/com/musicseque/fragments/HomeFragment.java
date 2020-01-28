package com.musicseque.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.artist.activity.other_artist_activity.SearchArtistActivity;
import com.musicseque.artist.activity.other_artist_activity.SearchBandActivity;
import com.musicseque.artist.fragments.BaseFragment;
import com.musicseque.event_manager.activity.SearchArtistActivityEventManager;
import com.musicseque.service.LocationService;
import com.musicseque.utilities.SharedPref;
import com.musicseque.utilities.Utils;
import com.musicseque.venue_manager.activity.SearchVenueActivity;

import java.util.Arrays;

import static com.musicseque.utilities.Constants.PROFILE_TYPE;


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView img_gigs, img_venue, img_event_manger, img_talent_manager, iv_featured, ivDrawer;
    private Context context;
    private LinearLayout line_settings;
    private TextView et_explore;
    Fragment fragment = null;


    ImageView ivArtist;


    ImageView ivEditLoc;

    EditText etLoc;

    ImageView ivBand;

    RelativeLayout rlSearch;


    ImageView ivSearch;

    View v;
    String mUserType = "";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String apiKey = getString(R.string.api_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), apiKey);
        }

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//
//                etLoc.setText(place.getAddress());
//                Log.e("TAG", "Place: " + place.getName() + ", " + place.getId() + "," + place.getAddress() + "," + place.getLatLng());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i("TAG", "An error occurred: " + status);
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
       initOtherViews();
        initialize();
        clickListner();


        return v;
    }

    private void initOtherViews() {

        TextView tv_title = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvHeading);
        TextView tvDone = (TextView) ((MainActivity) getActivity()).findViewById(R.id.tvDone);
        tvDone.setVisibility(View.GONE);
        ImageView img_right_icon = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.img_right_icon);
        img_right_icon.setVisibility(View.GONE);
        tv_title.setText("Home");
    }


    public void initialize() {
        mUserType = SharedPref.getString(PROFILE_TYPE, "");
        img_gigs = (ImageView) v.findViewById(R.id.img_gigs);
        img_venue = (ImageView) v.findViewById(R.id.img_venue);
        ivSearch = (ImageView) v.findViewById(R.id.ivSearch);
        ivArtist = (ImageView) v.findViewById(R.id.ivArtist);
        ivBand = (ImageView) v.findViewById(R.id.ivBand);
        ivEditLoc = (ImageView) v.findViewById(R.id.ivEditLoc);
        iv_featured = (ImageView) v.findViewById(R.id.iv_profile);
        img_event_manger = (ImageView) v.findViewById(R.id.img_event_manger);
        img_talent_manager = (ImageView) v.findViewById(R.id.img_talent_manager);
        et_explore = (TextView) v.findViewById(R.id.et_explore);
        etLoc = (EditText) v.findViewById(R.id.etLoc);

        rlSearch=(RelativeLayout) v.findViewById(R.id.rlSearch);
        try {
            Address address = Utils.getCompleteAddressString(Double.parseDouble(LocationService.mLatitude), Double.parseDouble(LocationService.mLongitude), getActivity());
            etLoc.setText(address.getAddressLine(0));
        } catch (Exception e) {

        }

    }

    public void clickListner() {
        img_gigs.setOnClickListener(this);
        img_venue.setOnClickListener(this);
        img_event_manger.setOnClickListener(this);
        img_talent_manager.setOnClickListener(this);
        et_explore.setOnClickListener(this);
        ivArtist.setOnClickListener(this);
        rlSearch.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivEditLoc.setOnClickListener(this);
        ivBand.setOnClickListener(this);
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
        if (view.getId() == R.id.ivArtist) {
            searchArtistMethod();

        } else if (view.getId() == R.id.rlSearch) {
            searchArtistMethod();
        } else if (view.getId() == R.id.ivSearch) {
            searchArtistMethod();
        } else if (view.getId() == R.id.ivEditLoc) {
            etLoc.setCursorVisible(true);
            etLoc.setFocusable(true);
        } else if (view.getId() == R.id.ivBand) {
            startActivity(new Intent(getActivity(), SearchBandActivity.class));
        }


    }





    private void searchArtistMethod() {

        if (mUserType.equalsIgnoreCase("Event Manager")) {
            startActivity(new Intent(getActivity(), SearchArtistActivityEventManager.class));

        } else {
            startActivity(new Intent(getActivity(), SearchArtistActivity.class));

        }
    }
}
