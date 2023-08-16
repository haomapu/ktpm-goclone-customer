package com.example.ktpm_goclone_customer;

import okhttp3.*;
import java.io.IOException;

public class ApiCaller {
    private String BASE_URL = "http://192.168.11.182:8080";
    private static ApiCaller instance;
    private OkHttpClient client;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    // Private constructor to prevent direct instantiation
    private ApiCaller() {
        client = new OkHttpClient();
    }

    // Static method to get the singleton instance
    public static synchronized ApiCaller getInstance() {
        if (instance == null) {
            instance = new ApiCaller();
        }
        return instance;
    }

    public void post(String path, String req, Callback callback, String header) {
        RequestBody body = RequestBody.create(JSON, req);
        Request.Builder requestBuilder = new Request.Builder()
                .url(BASE_URL + path)
                .post(body);

        if (!header.equalsIgnoreCase("None")) {
            requestBuilder.addHeader("Authorization", "Bearer " + header);
        }

        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}

