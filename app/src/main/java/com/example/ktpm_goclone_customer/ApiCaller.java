package com.example.ktpm_goclone_customer;

import okhttp3.*;
import java.io.IOException;

public class ApiCaller {
    private String BASE_URL = "http://192.168.1.41:8080";
    private static ApiCaller instance;
    private OkHttpClient client;

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

    public void post(String path, RequestBody req, Callback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYW9tYXB1MSIsImlhdCI6MTY5MTMwOTQ3MywiZXhwIjoxNjkxMzk1ODczfQ.fDELEVHsBl5sHro9Nb_UAgBK3hcKsJNO8S4JTy_R7VE")
                .post(req)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}

