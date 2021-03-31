package com.example.trendapp.model.retrofit;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Client {
    @Multipart
    @POST("/insert")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("kota") String kota, @Part("fashion_item") String fashionItem,
                                 @Part("accuracy") double accuracy);

    @GET("/trend")
    Call<ResponseBody> getTrends();

    @GET("/trend")
    Call<ResponseBody> getTrendById(@Query("id") int id);

}
