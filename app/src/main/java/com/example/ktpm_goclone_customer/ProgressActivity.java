package com.example.ktpm_goclone_customer;

import static com.example.ktpm_goclone_customer.User.currentUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.stomped.stomped.component.StompedFrame;
import com.stomped.stomped.listener.StompedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;




public class ProgressActivity extends AppCompatActivity implements OnMapReadyCallback {
    public GoogleMap mMap;

    private static GeoApiContext geoApiContext;
    FusedLocationProviderClient fusedLocationClient;
    private static LatLng currentLatLng;
    private static LatLng desLatLng;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Places.initialize(this, getString(R.string.google_maps_api_key));

        // Initialize the GeoApiContext
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_api_key))
                .queryRateLimit(3)
                .retryTimeout(1000, TimeUnit.MILLISECONDS)
                .build();

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsFragment);
        mapFragment.getMapAsync(this);
        JSONObject jsonObject = new JSONObject();
        JSONObject sourceLocation = new JSONObject();
        JSONObject destinationLocation = new JSONObject();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        ApiCaller apiCaller = ApiCaller.getInstance();
        Intent intent = getIntent();
        Log.e("hello", intent.getStringExtra("id").toString());

        currentLatLng = new LatLng(intent.getDoubleExtra("currentLat", 0), intent.getDoubleExtra("currentLng", 0));
        desLatLng = new LatLng(intent.getDoubleExtra("desLat", 0), intent.getDoubleExtra("desLng", 0));
        Handler handler = new Handler();
        WebsocketConnector websocketConnector = WebsocketConnector.getInstance(getApplicationContext());

        websocketConnector.stompedClient.unsubscribe("/topic/user/" + currentUser.getId() + "/accept");
        websocketConnector.stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/accept", new StompedListener(){
            @Override
            public void onNotify(final StompedFrame frame){
//                websocketConnector.stompedClient.unsubscribe("/topic/driver/" + currentUser.getId() + "/location");
                try {
                    JSONObject jsonObject = new JSONObject(frame.getStompedBody().toString());
                Log.e("helloworld", jsonObject.toString());
                if (jsonObject.getString("message").equalsIgnoreCase("done")){
//                    Log.e("Hello", "sap xong r");
                    LocationServices.getFusedLocationProviderClient(ProgressActivity.this).removeLocationUpdates(locationCallback);
                    Intent intent1 = new Intent(ProgressActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        locationRequest = LocationRequest.create();
                        locationRequest.setInterval(4000); // Interval in milliseconds between updates
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        // Initialize locationCallback
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult != null) {
                                    Location location = locationResult.getLastLocation();
                                    if (location != null) {
                                        double currentLatitude = location.getLatitude();
                                        double currentLongitude = location.getLongitude();
                                        fetchDirections(new LatLng(currentLatitude, currentLongitude), desLatLng);
                                    }
                                }
                            }
                        };

                        // Request location updates
                        try {
                            LocationServices.getFusedLocationProviderClient(ProgressActivity.this)
                                    .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                        } catch (SecurityException e) {
                            // Handle permission denial
                        }


                    }
                });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        apiCaller.get("/api/user/driver/64da5bd8ce2d5e10d8dbfe58", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("Hello", response.body().string());
            }
        }, token);
//        Button backButton = findViewById(R.id.backButton);
//        Button cancelButton = findViewById(R.id.cancelButton);

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle cancel button click
//            }
//        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Enable the "My Location" layer on the map
            mMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    if (currentLatLng == null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        currentLatLng = new LatLng(latitude, longitude);
                    }

                    // Add a marker at the current location and move the camera
                    mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location")).showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            // sourceAutoCompleteTextView.setText(address.getAddressLine(0));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        WebsocketConnector websocketConnector = WebsocketConnector.getInstance(getApplicationContext());
        websocketConnector.stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/location", new StompedListener(){
            @Override
            public void onNotify(final StompedFrame frame){
                try {
                    JSONObject jsonObject = new JSONObject(frame.getStompedBody().toString());
                    final LatLng newLatLng = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                    final BitmapDescriptor driverIcon = BitmapDescriptorFactory.fromResource(R.drawable.driver);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mMap != null) {
                                mMap.addMarker(new MarkerOptions().position(newLatLng).title("Driver").icon(driverIcon));
                                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Your Location")).showInfoWindow();
                                fetchDirections(newLatLng, currentLatLng);
                            }
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }


    public void drawDirections(LatLng sourceLatLng, LatLng destinationLatLng, String destinationTitle) {
        mMap.clear(); // Clear existing markers and polylines
        mMap.addMarker(new MarkerOptions().position(sourceLatLng).title("Your Location")).showInfoWindow();
        if (!destinationTitle.equalsIgnoreCase("None")){
            mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(destinationTitle)).showInfoWindow();
        } else {
            mMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination")).showInfoWindow();
        }

        // Fetch directions and draw route
        fetchDirections(sourceLatLng, destinationLatLng);
    }

    public void fetchDirections(LatLng sourceLatLng, LatLng destinationLatLng) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(new com.google.maps.model.LatLng(sourceLatLng.latitude, sourceLatLng.longitude))
                    .destination(new com.google.maps.model.LatLng(destinationLatLng.latitude, destinationLatLng.longitude))
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .await();

            if (result != null && result.routes != null && result.routes.length > 0) {
                DirectionsRoute route = result.routes   [0];
                List<com.google.maps.model.LatLng> decodedPath = route.overviewPolyline.decodePath();
                int travelTimeSeconds = (int) route.legs[0].duration.inSeconds;

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




}

