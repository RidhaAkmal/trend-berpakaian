package com.example.trendapp.model;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import okhttp3.OkHttpClient;

public class Clarifai {
    private static ClarifaiClient instance;

    public static ClarifaiClient getInstance() {
        if (instance == null){
            instance = new ClarifaiBuilder("655224221f814f388c1cddd96ff6b772")
                    .client(new OkHttpClient())
                    .buildSync();
        }
        return instance;
    }
}
