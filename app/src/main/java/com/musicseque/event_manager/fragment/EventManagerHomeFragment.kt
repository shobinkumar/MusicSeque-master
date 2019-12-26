//package com.musicseque.event_manager.fragment
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.Place
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment
//import com.musicseque.MainActivity
//import com.musicseque.R
//import com.musicseque.artist.activity.other_artist_activity.SearchArtistActivity
//import com.musicseque.artist.fragments.BaseFragment
//import com.musicseque.event_manager.activity.MainActivityEventManager
//import com.musicseque.service.LocationService
//import com.musicseque.utilities.Utils
//import com.musicseque.venue_manager.activity.SearchVenueActivity
//import kotlinx.android.synthetic.main.fragment_event_manager_home.*
//import java.util.*
//
//class EventManagerHomeFragment : BaseFragment(), View.OnClickListener {
//
//    lateinit var v: View
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val apiKey = getString(R.string.api_key)
//
//        if (!Places.isInitialized()) {
//            Places.initialize(activity!!, apiKey)
//        }
////        val placesClient = Places.createClient(activity!!)
////        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
////        autocompleteFragment!!.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))
////        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
////            @Override
////            public void onPlaceSelected(Place place) {
////                // TODO: Get info about the selected place.
////
////                etLoc.setText(place.getAddress());
////                Log.e("TAG", "Place: " + place.getName() + ", " + place.getId() + "," + place.getAddress() + "," + place.getLatLng());
////            }
////
////            @Override
////            public void onError(Status status) {
////                // TODO: Handle the error.
////                Log.i("TAG", "An error occurred: " + status);
////            }
////        });
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        v = inflater.inflate(R.layout.fragment_event_manager_home, container, false)
//
//
//        return v
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        initViews()
//        clickListener()
//
//    }
//
//    fun initViews() {
//
//
//        val tv_title = (activity as MainActivityEventManager?)!!.findViewById<View>(R.id.tvHeading) as TextView
//        val tvDone = (activity as MainActivityEventManager?)!!.findViewById<View>(R.id.tvDone) as TextView
//        tvDone.visibility = View.GONE
//        val img_right_icon = (activity as MainActivityEventManager?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
//        img_right_icon.visibility = View.GONE
//        tv_title.text = "Home"
//
//
//        try {
//            val address = Utils.getCompleteAddressString(LocationService.mLatitude.toDouble(), LocationService.mLongitude.toDouble(), activity)
//            etLocEventManager!!.setText(address.getAddressLine(0))
//        } catch (e: Exception) {
//        }
//    }
//
//    fun clickListener() {
//        ivArtistEventManager!!.setOnClickListener(this)
//        ivTalentEventManager!!.setOnClickListener(this)
//        ivEventManager!!.setOnClickListener(this)
//        ivVenueEventManager!!.setOnClickListener(this)
//
//        ivStoreEventManager!!.setOnClickListener(this)
//        ivGigsEventManager!!.setOnClickListener(this)
//
//
//        tvExploreEventManager!!.setOnClickListener(this)
//        //        etLoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
////            @Override
////            public void onFocusChange(View view, boolean b) {
////                if(b)
////                {
////                    etLoc.setCursorVisible(true);
////                    etLoc.setFocusable(true);
////                }
////                else
////                {
////                    etLoc.setCursorVisible(false);
////                    etLoc.setFocusable(false);
////                }
////            }
////        });
//    }
//
//    override fun onClick(view: View) {
//        if (view.id == R.id.ivArtistEventManager) {
//            startActivity(Intent(activity, SearchArtistActivity::class.java))
//        } else if (view.id == R.id.ivTalentEventManager) {
//            startActivity(Intent(activity, SearchVenueActivity::class.java))
//        } else if (view.id == R.id.ivEventManager) {
//            Toast.makeText(activity, "Disabled", Toast.LENGTH_SHORT).show()
//        } else if (view.id == R.id.ivVenueEventManager) {
//            startActivity(Intent(activity, SearchVenueActivity::class.java))
//        } else if (view.id == R.id.ivStoreEventManager) {
//            Toast.makeText(activity, "Disabled", Toast.LENGTH_SHORT).show()
//        } else if (view.id == R.id.ivGigsEventManager) {
//            Toast.makeText(activity, "Disabled", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//}
//
