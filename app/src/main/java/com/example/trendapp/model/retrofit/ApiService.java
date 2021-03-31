package com.example.trendapp.model.retrofit;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static Client instance;
    public static final String BASE_URL = "http://ootdku.web.id/";

    public static Client getInstance() {
        if (instance == null){
            OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            })
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new StethoInterceptor());

            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .create();


            instance = new Retrofit.Builder()
                    .client(client.build())
                    .baseUrl(BASE_URL)
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(Client.class);
        }

        return instance;
    }
}
