//package com.musicseque.demo;
//
//
//
//
//import android.app.Activity;
//
//import android.os.Bundle;
//
//import android.view.View;
//import android.widget.AdapterView;
//
//import android.widget.AutoCompleteTextView;
//
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.AutocompletePrediction;
//import com.google.android.gms.location.places.GeoDataClient;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.PlaceBufferResponse;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.RuntimeRemoteException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.musicseque.MainActivity;
//import com.musicseque.R;
//
//public class GooglePlacesAutocompleteActivity extends Activity {
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main1);
//        placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
//
//        placeAutocompleteFragment.setFilter(autocompleteFilter);
//
//        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                Toast.makeText(getApplicationContext(),place.getName().toString(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Status status) {
//                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }
//
////    private AdapterView.OnItemClickListener mAutocompleteClickListener
////            = new AdapterView.OnItemClickListener() {
////        @Override
////        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////            /*
////             Retrieve the place ID of the selected item from the Adapter.
////             The adapter stores each Place suggestion in a AutocompletePrediction from which we
////             read the place ID and title.
////              */
////            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
////            final String placeId = item.getPlaceId();
////
////
////            /*
////             Issue a request to the Places Geo Data Client to retrieve a Place object with
////             additional details about the place.
////              */
////            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
////            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
////        }
////    };
////
////    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
////            = new OnCompleteListener<PlaceBufferResponse>() {
////        @Override
////        public void onComplete(Task<PlaceBufferResponse> task) {
////            try {
////                PlaceBufferResponse places = task.getResult();
////
////                // Get the Place object from the buffer.
////                final Place place = places.get(0);
////                addressTv.setText(place.getAddress().toString());
////                locationDataTv.setText("Latitude : "+String.valueOf(place.getLatLng().latitude)+"\n Longitude : "+String.valueOf(place.getLatLng().longitude));
////                places.release();
////            } catch (RuntimeRemoteException e) {
////                // Request did not complete successfully
////                return;
////            }
////        }
////    };
//}