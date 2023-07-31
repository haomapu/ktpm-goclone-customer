package com.example.ktpm_goclone_customer;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean locationPermissionGranted;
    private AutoCompleteTextView sourceAutoCompleteTextView, destinationAutoCompleteTextView;
    private Button searchButton;
    private GeoApiContext geoApiContext;

    private LatLng currentLatLng;
    private double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(requireContext(), getString(R.string.google_maps_api_key));

        // Initialize the GeoApiContext
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_api_key))
                .queryRateLimit(3)
                .retryTimeout(1000, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Get the SupportMapFragment and register for map callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize UI elements

        sourceAutoCompleteTextView = rootView.findViewById(R.id.sourceAutoCompleteTextView);
        destinationAutoCompleteTextView = rootView.findViewById(R.id.destinationAutoCompleteTextView);
        destinationAutoCompleteTextView.setFocusable(false);
        destinationAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                List<String> countries = Arrays.asList("VN");
                double distanceInMeters = 5000; // 50 km radius from the center

                LatLngBounds bounds = new LatLngBounds(
                        new LatLng(latitude - distanceInMeters / 111700, longitude - distanceInMeters / (111700 * Math.cos(Math.toRadians(latitude)))),
                        new LatLng(longitude + distanceInMeters / 111700, longitude + distanceInMeters / (111700 * Math.cos(Math.toRadians(latitude)))));

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).setCountries(countries).setLocationBias(RectangularBounds.newInstance(bounds)).build(getContext());
                startActivityForResult(intent, 200);
            }
        });

        // Set the sourceAutoCompleteTextView to the current location at the first time
        setCurrentLocationToSource();
//        destinationAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                String sourcePlace = String.valueOf(sourceAutoCompleteTextView.getText());
//                String destinationPlace = String.valueOf((destinationAutoCompleteTextView).getText());
//                if (!sourcePlace.isEmpty() && !destinationPlace.isEmpty()) {
//                    LatLng sourceLatLng = getLocationFromAddress(sourcePlace);
//                    LatLng destinationLatLng = getLocationFromAddress(destinationPlace);
//                    if (sourceLatLng != null && destinationLatLng != null) {
//                        drawDirections(sourceLatLng, destinationLatLng);
//                    }
//                }
//            }
//        });
        destinationAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sourcePlace = String.valueOf(sourceAutoCompleteTextView.getText());
                String destinationPlace = String.valueOf((destinationAutoCompleteTextView).getText());
                if (!sourcePlace.isEmpty() && !destinationPlace.isEmpty()) {
                    LatLng sourceLatLng = getLocationFromAddress(sourcePlace);
                    LatLng destinationLatLng = getLocationFromAddress(destinationPlace);
                    if (sourceLatLng != null && destinationLatLng != null) {
                        drawDirections(sourceLatLng, destinationLatLng, destinationPlace);
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            destinationAutoCompleteTextView.setText(place.getAddress());
        }
    }

    private void setCurrentLocationToSource() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Get the last known location (if available) and update the marker
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    currentLatLng = new LatLng(latitude, longitude);
                    // Add a marker at the current location and move the camera


                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            String currentAddress = address.getAddressLine(0);
                            sourceAutoCompleteTextView.setText(currentAddress);
                            mMap.addMarker(new MarkerOptions().position(currentLatLng).title(currentAddress));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    

    private LatLng getLocationFromAddress(String address) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (!addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void drawDirections(LatLng sourceLatLng, LatLng destinationLatLng, String destinationTitle) {
        mMap.clear(); // Clear existing markers and polylines
        mMap.addMarker(new MarkerOptions().position(sourceLatLng).title("Current Location")).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(destinationTitle)).showInfoWindow();

        // Fetch directions and draw route
        fetchDirections(sourceLatLng, destinationLatLng);
    }

    private void fetchDirections(LatLng sourceLatLng, LatLng destinationLatLng) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(new com.google.maps.model.LatLng(sourceLatLng.latitude, sourceLatLng.longitude))
                    .destination(new com.google.maps.model.LatLng(destinationLatLng.latitude, destinationLatLng.longitude))
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .await();

            if (result != null && result.routes != null && result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];
                List<com.google.maps.model.LatLng> decodedPath = route.overviewPolyline.decodePath();
                List<LatLng> newDecodedPath = new ArrayList<>();

                for (com.google.maps.model.LatLng latLng : decodedPath) {
                    newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                }

                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(newDecodedPath)
                        .color(Color.BLUE)
                        .width(10f);
                Polyline polyline = mMap.addPolyline(polylineOptions);
            }

        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
    // Implement onMapReady() to handle map initialization
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Enable the "My Location" layer on the map
            mMap.setMyLocationEnabled(true);

            // Get the last known location (if available) and update the marker
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng currentLatLng = new LatLng(latitude, longitude);

                    // Add a marker at the current location and move the camera
                    mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location")).showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);
//                            sourceAutoCompleteTextView.setText(address.getAddressLine(0));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocationPermission();
    }

    // Check and request location permissions if not granted
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationPermissionGranted = true;
        }
    }

    // Handle location permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                // Get the map again after permission is granted
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            } else {
                // Handle the case when permission is not granted
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMap = null;
    }
}
