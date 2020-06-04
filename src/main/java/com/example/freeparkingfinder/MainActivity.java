//Map manipulation and location tracker implementation from
//https://github.com/AbbasHassan/PlacesProject
package com.example.freeparkingfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*-------------By Aram-------------
--------------14/03/2020---------*/

//Map tab


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap; //map object
    private FusedLocationProviderClient mFusedLocationProviderClient; //fetches current loc of device
    private PlacesClient placesClient; //loads parking places suggestions
    private List<AutocompletePrediction> predictionList; //predict user target loc

    private Location mLastKnownLocation; //stores last known loc
    private LocationCallback locationCallback; //updates user request if last notification null

    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private RippleBackground ripple_Bg;

    private int searchRadius = 1000;
    private int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrefManagerSettings prefManagerSettings = new PrefManagerSettings(this);
        if (searchRadius != -1) //if initial value of searchRadius is not already set by user the default value is used
        {
            searchRadius = prefManagerSettings.getRadius();
        }
        if (DEFAULT_ZOOM != -1)
        {
            DEFAULT_ZOOM = prefManagerSettings.getZoom();
        }


        materialSearchBar = findViewById(R.id.searchBar);
        Button findParking = findViewById(R.id.find_Parking);
        ripple_Bg = findViewById(R.id.rippleBg);

        SupportMapFragment mapFragment = (SupportMapFragment)//store map in mapFragment variable
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView();

        String apikey = "Enter API-KEY";

        if (!Places.isInitialized())
        {
            Places.initialize(getApplicationContext(), apikey);
        }

        //enables location finder
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled)
            {

            }

            @Override
            public void onSearchConfirmed(CharSequence text)
            {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode)
            {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION)
                {
                    //opening or closing a navigation drawer
                }
                else if (buttonCode == MaterialSearchBar.BUTTON_BACK)
                {
                    materialSearchBar.closeSearch();
                }

            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            //provide predictions for users and offer them as suggestions
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                FindAutocompletePredictionsRequest predictionsRequest =
                        FindAutocompletePredictionsRequest.builder()
                                .setTypeFilter(TypeFilter.ADDRESS).setSessionToken(token).setQuery(s.toString()).build();

                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task)
                    {
                        if (task.isSuccessful())
                        {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null)
                            {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++)
                                {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }

                                materialSearchBar.updateLastSuggestions(suggestionsList);

                                if (!materialSearchBar.isSuggestionsVisible())
                                {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        }
                        else
                        {
                            Log.i ("mytag", "prediction on fetching task unsuccessful");
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }

        });


        //retrieve lat and lon of the location the user clicks
        //retrieves placeID first and sends to google places which returns lat and lon
        //then moves camera to that location
        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v)
            {
                if (position >= predictionList.size())
                {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                materialSearchBar.clearSuggestions();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                    //hide keyboard after user clicks on location
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                String placeId = selectedPrediction.getPlaceId();//pass placeID to google to get lat and lon
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                //pass placeId and placeFields to fetchPlaceRequest which is then passed to placesClient to fetch lat/lon
                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse)
                    {
                        Place place = fetchPlaceResponse.getPlace();
                        LatLng latLngOfPlace = place.getLatLng();
                        if (latLngOfPlace != null)
                        {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        if (e instanceof ApiException)
                        {
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode = apiException.getStatusCode();
                        }
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v)
            {

            }
        });

        //when user clicks on find parking
        findParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //starts ripple effect on position marker
                ripple_Bg.startRippleAnimation();

                mMap.clear(); //clears markers from previous parking search


                ImageView heartView = (ImageView) findViewById(R.id.heartView);
                heartView.setVisibility(View.INVISIBLE); //removes the heart icon after a new search

                getNearbyParking();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ripple_Bg.stopRippleAnimation();
                    }
                }, 1500);
            }
        });



        Button favouritesButton = findViewById(R.id.favouritesButton);
        //access favourites tab once favourites button is clicked
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavouritesTab();
            }
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsTab();
            }
        });

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistoryTab();
            }
        });
    }

    private void getNearbyParking()
    {
        //retrieves location of red marker on map and stores it in currentMarkerLocation
        LatLng currentMarkerLocation = mMap.getCameraPosition().target;
        double latitude = currentMarkerLocation.latitude;//extract latitude from current position
        double longitude = currentMarkerLocation.longitude;

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + latitude + "," + longitude +
                "&radius=" + searchRadius +
                "&type=parking" +
                "&key=Enter API-KEY";


        Object[] dataTransfer = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;


        GetNearbyPlaces getNearbyPlacesData = new GetNearbyPlaces();
        getNearbyPlacesData.execute(dataTransfer);
    }


    public void openFavouritesTab() { //function used to open the favourites tab
        Intent intent = new Intent(this, FavouritesTab.class);
        startActivity(intent);
    }

    public void openSettingsTab() {
        Intent intent = new Intent(this, SettingsTab.class);
        startActivity(intent);
    }

    public void openHistoryTab() {
        Intent intent = new Intent(this, HistoryTab.class);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) { //function called when map is ready and loaded
        mMap = googleMap; //load map into mMap
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true); //displays my location button


        //sets location button to bottom right corner
        if(mapView != null && mapView.findViewById(Integer.parseInt("1")) != null)
        {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);

        }

        //check if gps is enabled, request user to enable if not
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //pass locationRequest to locationSettingsRequest builder
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        //if it is on and there is no issue
        task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        //check to see if issue can be resolved if it is off
        task.addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MainActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        //resets search bar to default state if find my location button is clicked on map
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (materialSearchBar.isSuggestionsVisible())
                    materialSearchBar.clearSuggestions();
                if (materialSearchBar.isSearchOpened())
                    materialSearchBar.closeSearch();
                return false;
            }
        });

        //after user click favourites item from FavouritesTab
        //create variable to get intent and retrieve latitude and longitude to set marker
        Bundle extras = getIntent().getExtras();
        String markerPosition, markerName;
        double markerLat;
        double markerLng;
        if (extras != null)
        {
            markerName = extras.getString("FIND_MARKER_NAME");
            markerPosition = extras.getString("FIND_MARKER_POS");

            markerPosition = markerPosition.replace("lat/lng: (", "");
            markerPosition = markerPosition.replace(")", "");

            String[] location = markerPosition.split(",");
            markerLat = Double.parseDouble(location[0]);
            markerLng = Double.parseDouble(location[1]);

            LatLng latLng = new LatLng(markerLat, markerLng);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(markerName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            markerOptions.position(latLng);

            mMap.addMarker(markerOptions);
        }





        PrefManager prefManager = new PrefManager(this);
        PrefManagerHistory prefManagerHistory = new PrefManagerHistory(this);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                prefManagerHistory.setHistory(marker.getTitle(), marker.getPosition().toString());
                //all markers viewed get saved to history tab


                ImageView heartView = (ImageView) findViewById(R.id.heartView);
                FavouritesTab fav = new FavouritesTab();
                List<String> markerTitles = new ArrayList<String>();
                Map<String, ?> nameValues;


                nameValues = prefManager.getFavourites();

                for (Map.Entry<String, ?> entry : nameValues.entrySet()) {
                    markerTitles.add(entry.getKey()); //extract all existing fav titles from SharedPreferences
                }


                if (markerTitles.contains(marker.getTitle()))//check if current marker exist in favourites
                {
                    Log.w("mytag", "Marker already in fav");
                }
                else
                {
                    heartView.setVisibility(View.VISIBLE); //make heart icon visible if not
                }





                heartView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //save marker to to SharedPreferences to later load in fav tab
                        prefManager.setFavourites(marker.getTitle(), marker.getPosition().toString());
                    }
                });

                if(marker == marker){
                    Log.w("Click", "test");
                }
                return false;
            }
        });
    }

    //@Override
    protected void OnActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 51)
        {
            if (resultCode == RESULT_OK) //means gps is enabled
            {
                //find user current location and move map to that location
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        //Request last location
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        //if fetch last location is successful
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                            //if fetch last location is unsuccessful
                        } else {
                            Toast.makeText(MainActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
