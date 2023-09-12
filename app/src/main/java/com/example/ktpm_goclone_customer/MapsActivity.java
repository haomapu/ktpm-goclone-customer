package com.example.ktpm_goclone_customer;

import static com.example.ktpm_goclone_customer.User.currentUser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.stomped.stomped.component.StompedFrame;
import com.stomped.stomped.listener.StompedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean locationPermissionGranted;
    private AutoCompleteTextView sourceAutoCompleteTextView, destinationAutoCompleteTextView;
    private Button searchButton;
    private GeoApiContext geoApiContext;

    private LatLng currentLatLng;
    private LatLng desLatLng;
    private double latitude, longitude;
    Thread thread;
    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    private Button header_Arrow_Image;
    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout confirm_button;
    private ProgressDialog progressDialog;
    private TextView priceTV;

    private String lastPrice;

    public static volatile boolean checkStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Places.initialize(this, getString(R.string.google_maps_api_key));

        // Initialize the GeoApiContext
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_api_key))
                .queryRateLimit(3)
                .retryTimeout(1000, TimeUnit.MILLISECONDS)
                .build();

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        // Get the SupportMapFragment and register for map callback
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize UI elements
        sourceAutoCompleteTextView = findViewById(R.id.sourceAutoCompleteTextView);
        destinationAutoCompleteTextView = findViewById(R.id.destinationAutoCompleteTextView);

        initSearchTextView(sourceAutoCompleteTextView, 201);
        initSearchTextView(destinationAutoCompleteTextView, 200);

        // Set the sourceAutoCompleteTextView to the current location at the first time

        if (currentLatLng == null) {
            setCurrentLocationToSource();
        }
        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                WebsocketConnector websocketConnector = WebsocketConnector.getInstance(getApplicationContext());
//                long delayMillis = 10000;
//
//                if (!(websocketConnector.getLatitude() == 0.0 && websocketConnector.getLongitude() == 0.0)) {
//                    LatLng latLng = new LatLng(websocketConnector.getLatitude(), websocketConnector.getLongitude());
//                    BitmapDescriptor driverIcon = BitmapDescriptorFactory.fromResource(R.drawable.driver);
//                    mMap.clear();
//                    if (websocketConnector.driver){
//                        mMap.addMarker(new MarkerOptions().position(latLng).title("Driver").icon(driverIcon));
//                        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Your Location")).showInfoWindow();
//                        fetchDirections(latLng, currentLatLng);
//                    } else {
//                        setCurrentLocationToSource();
//                        drawDirections(currentLatLng, desLatLng, "None");
//                    }
//
//                }
//                handler.postDelayed(this, delayMillis);
//            }
//        };
//        handler.post(runnable);
        confirm_button = findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(v -> {
            ApiCaller apiCaller = ApiCaller.getInstance();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            JSONObject jsonObject = new JSONObject();
            JSONObject sourceLocation = new JSONObject();
            JSONObject destinationLocation = new JSONObject();
            try {
                sourceLocation.put("latitude", currentLatLng.latitude);
                sourceLocation.put("longitude", currentLatLng.longitude);
                destinationLocation.put("latitude", desLatLng.latitude);
                destinationLocation.put("longitude", desLatLng.longitude);
                jsonObject.put("sourceLocation", sourceLocation);
                jsonObject.put("destinationLocation", destinationLocation);;
//                Intent intent = new Intent(this, ProgressActivity.class);
////                intent.putExtra("id", jsonObject.getString("senderID"));
//                startActivity(intent);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            apiCaller.post("/api/user/booking", jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Hello", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("drivers"));
                        checkStatus = true;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                showSpinnerPopup();
                                String message = lastPrice;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (!checkStatus) {
                                        break;
                                    }
                                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                                    if (jsonObject != null) {
                                        JSONObject body = new JSONObject();
                                        try {
                                            body.put("senderID", User.currentUser.getId());
                                            body.put("receiverID", jsonObject.getString("id"));
                                            body.put("latitude", currentLatLng.latitude);
                                            body.put("longitude", currentLatLng.longitude);
                                            body.put("desLat", desLatLng.latitude);
                                            body.put("desLng", desLatLng.longitude);
                                            body.put("message", message);
                                            body.put("bookingId", jsonObject1.getString("bookingId"));
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                        WebsocketConnector websocketConnector = WebsocketConnector.getInstance(getApplicationContext());
                                        websocketConnector.send("/app/sendLocation", body.toString());
                                        // Pause the thread for 10 seconds before the next iteration
                                        try {
                                            Thread.sleep(15000); // 10 seconds delay
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

//                                hideSpinnerPopup();

                            }
                        }).start();



                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
//                        JSONObject jsonObject = new JSONObject(response.body());
//                        Log.e("Hihi", jsonObject.toString());
                }
            }, token);
        });

        WebsocketConnector websocketConnector = WebsocketConnector.getInstance(getApplicationContext());
        websocketConnector.stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/accept", new StompedListener(){
        @Override
        public void onNotify(final StompedFrame frame){
            websocketConnector.driver = false;
            MapsActivity.checkStatus = false;
            Log.e("hello", "hello map");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(frame.getStompedBody().toString());
                        Intent intent = new Intent(websocketConnector.context, ProgressActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("id", jsonObject.getString("senderID"));
                        intent.putExtra("currentLat", currentLatLng.latitude);
                        intent.putExtra("currentLng", currentLatLng.longitude);
                        intent.putExtra("desLat", desLatLng.latitude);
                        intent.putExtra("desLng", desLatLng.longitude);

                        websocketConnector.context.startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }
    });
}


    private void showSpinnerPopup() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(MapsActivity.this);
                progressDialog.setMessage("Finding driver..."); // Set the message for the spinner
                progressDialog.setCancelable(false); // Prevent users from dismissing the popup
                progressDialog.show();
            }
        });
    }

    private void hideSpinnerPopup() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            destinationAutoCompleteTextView.setText(place.getAddress());
        } else if (requestCode == 201 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            currentLatLng = place.getLatLng();
            sourceAutoCompleteTextView.setText(place.getAddress());

        }
    }

    private void setCurrentLocationToSource() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Get the last known location (if available) and update the marker
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    User.currentUser.lat = location.getLatitude();
                    User.currentUser.lng = location.getLongitude();
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    currentLatLng = new LatLng(latitude, longitude);
                    // Add a marker at the current location and move the camera

                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
        mMap.addMarker(new MarkerOptions().position(sourceLatLng).title("Hi Location")).showInfoWindow();
        if (!destinationTitle.equalsIgnoreCase("None")){
            mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(destinationTitle)).showInfoWindow();
        } else {
            mMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination")).showInfoWindow();
        }

        // Fetch directions and draw route
        fetchDirections(sourceLatLng, destinationLatLng);
    }
    private boolean isPeakHour() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY); // Get current hour

        return currentHour >= 17 && currentHour < 19; // Check if current hour is between 17 and 19
    }
    private String formatPrice(int price) {
        price = price / 1000 * 1000;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        return numberFormat.format(price);
    }


    private void fetchDirections(LatLng sourceLatLng, LatLng destinationLatLng) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(new com.google.maps.model.LatLng(sourceLatLng.latitude, sourceLatLng.longitude))
                    .destination(new com.google.maps.model.LatLng(destinationLatLng.latitude, destinationLatLng.longitude))
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .await();
            double kilometersTraveled = calculateKilometers(result);
            boolean isPeakHour = isPeakHour(); // Implement this method to determine if it's peak hour
            double totalPrice = calculatePrice(kilometersTraveled, isPeakHour);
            priceTV = mBottomSheetLayout.findViewById(R.id.priceTV);
            lastPrice = formatPrice((int) totalPrice);
            priceTV.setText(lastPrice);
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
    private double calculatePrice(double km, boolean isPeakHour) {
        double basePricePerKm = 7000; // Example base price per kilometer
        double extraChargePercentage = isPeakHour ? 0.10 : 0.0; // 10% extra charge during peak hours
        double distanceKm = km;

        // Calculate the price
        double totalPrice = basePricePerKm * distanceKm * (1 + extraChargePercentage);

        return totalPrice;
    }

    private double calculateKilometers(DirectionsResult result) {
        if (result != null && result.routes != null && result.routes.length > 0) {
            DirectionsRoute route = result.routes[0];
            double distanceKm = route.legs[0].distance.inMeters * 0.001; // Distance in kilometers
            return distanceKm;
        }

        return 0.0; // Default value if no route is found
    }


    public AutoCompleteTextView initSearchTextView(AutoCompleteTextView autoCompleteTextView, int statusCode){
        autoCompleteTextView.setFocusable(false);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                double distanceInMeters = 5000; // 50 km radius from the center

                LatLngBounds bounds = new LatLngBounds(
                        new LatLng(latitude - distanceInMeters / 111700, longitude - distanceInMeters / (111700 * Math.cos(Math.toRadians(latitude)))),
                        new LatLng(longitude + distanceInMeters / 111700, longitude + distanceInMeters / (111700 * Math.cos(Math.toRadians(latitude)))));

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).setLocationBias(RectangularBounds.newInstance(bounds)).build(MapsActivity.this);
                startActivityForResult(intent, statusCode);
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                } else {
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
                if (destinationAutoCompleteTextView.getText() != null || sourceAutoCompleteTextView.getText() != null){
                    String sourcePlace = String.valueOf(sourceAutoCompleteTextView.getText());
                    String destinationPlace = String.valueOf((destinationAutoCompleteTextView).getText());
                    if (!sourcePlace.isEmpty() && !destinationPlace.isEmpty()) {
                        LatLng sourceLatLng = getLocationFromAddress(sourcePlace);
                        LatLng destinationLatLng = getLocationFromAddress(destinationPlace);
                        if (sourceLatLng != null && destinationLatLng != null) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            sheetBehavior.setPeekHeight(60);
                            currentLatLng = sourceLatLng;
                            desLatLng = destinationLatLng;
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(sourceLatLng);
                            builder.include(destinationLatLng);
                            LatLngBounds bounds = builder.build();

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                            if (mMap != null){
                                mMap.moveCamera(cameraUpdate);

                                drawDirections(sourceLatLng, destinationLatLng, destinationPlace);

                            }
                        }
                    }
                }
            }
        });
        return autoCompleteTextView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Enable the "My Location" layer on the map
            mMap.setMyLocationEnabled(true);

            // Get the last known location (if available) and update the marker
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
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocationPermission();
    }

    // Check and request location permissions if not granted
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            } else {
                // Handle the case when permission is not granted
            }
        }
    }
    private void dismissSpinnerPopup() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissSpinnerPopup();
    }
}
