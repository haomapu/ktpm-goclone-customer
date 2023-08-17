package com.example.ktpm_goclone_customer;

import static com.example.ktpm_goclone_customer.User.currentUser;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.stomped.stomped.client.StompedClient;
import com.stomped.stomped.component.StompedFrame;
import com.stomped.stomped.listener.StompedListener;

import org.json.JSONException;
import org.json.JSONObject;

public class WebsocketConnector {
    private static WebsocketConnector websocketConnector;
    private StompedClient stompedClient;
    private double latitude, longitude;
    private boolean isWaitingForResponse = false;
    public boolean driver = false;
    private Handler responseHandler; // For handling response timeout


    private WebsocketConnector(){
        stompedClient = new StompedClient.StompedClientBuilder().build("ws://192.168.1.180:8080/ws");

        stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/chat", new StompedListener(){
            @Override
            public void onNotify(final StompedFrame frame){
                Log.e("Hello", frame.getStompedBody().toString());
            }
        });
        stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/booking", new StompedListener(){
            @Override
            public void onNotify(final StompedFrame frame){
                JSONObject jsonObject = null;
                try {
                    WebsocketConnector websocketConnector = WebsocketConnector.getInstance();
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
        stompedClient.subscribe("/topic/user/" + currentUser.getId() + "/pickup", new StompedListener(){
            @Override
            public void onNotify(final StompedFrame frame){
                driver = false;
                MapsActivity.checkStatus = false;
            }
        });
    }

    public static WebsocketConnector getInstance() {
        if (websocketConnector == null) {
            websocketConnector = new WebsocketConnector();
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
