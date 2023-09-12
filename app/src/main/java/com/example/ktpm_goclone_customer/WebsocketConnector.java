package com.example.ktpm_goclone_customer;

import static com.example.ktpm_goclone_customer.User.currentUser;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.stomped.stomped.client.StompedClient;
import com.stomped.stomped.component.StompedFrame;
import com.stomped.stomped.listener.StompedListener;

import org.json.JSONException;
import org.json.JSONObject;

public class WebsocketConnector {
    private static WebsocketConnector websocketConnector;
    public StompedClient stompedClient;
    private double latitude, longitude;
    private boolean isWaitingForResponse = false;
    public boolean driver = false;
    private Handler responseHandler; // For handling response timeout
    public Context context;
    private Handler handler = new Handler(Looper.getMainLooper());


    private WebsocketConnector(Context context){
        this.context = context;
        stompedClient = new StompedClient.StompedClientBuilder().build("ws://ktpm-goride.onrender.com/ws");

        stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/chat", new StompedListener(){
            @Override
            public void onNotify(final StompedFrame frame){
                Log.e("Hello", frame.getStompedBody().toString());
            }
        });

//        stompedClient.subscribe("/topic/driver/" + currentUser.getId() + "/location", new StompedListener(){
//            @Override
//            public void onNotify(final StompedFrame frame){
//                try {
//                    JSONObject jsonObject = new JSONObject(frame.getStompedBody().toString());
//                    Log.e("Hello", "Hao Map U123");
//                    ProgressActivity.updateDriverLocation(new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude")));
//
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        });
        stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/booking", new StompedListener(){
            @Override
            public void onNotify(final StompedFrame frame){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(frame.getStompedBody().toString());
                    websocketConnector.setLatitude(jsonObject.getDouble("latitude"));
                    websocketConnector.setLongitude(jsonObject.getDouble("longitude"));
                    driver = true;
                    MapsActivity.checkStatus = false;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

//        stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/update", new StompedListener(){
//            @Override
//            public void onNotify(final StompedFrame frame){
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(frame.getStompedBody().toString());
//                    ProgressActivity.updateDriverLocation(new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude")));
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

    }

    public static WebsocketConnector getInstance(Context context) {
        if (websocketConnector == null) {
            websocketConnector = new WebsocketConnector(context);
        }
        if (websocketConnector.context == null || websocketConnector.context != context){
            websocketConnector.context = context;
        }
        return websocketConnector;
    }

    public void send(String destination, String body){
        stompedClient.send(destination, body);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
