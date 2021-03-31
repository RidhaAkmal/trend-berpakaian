package com.example.trendapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trend implements Parcelable {
    @SerializedName("kota")
    @Expose
    private String kota;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("fashion_item")
    @Expose
    private String fashionItem;
    @SerializedName("accuracy")
    @Expose
    private double accuracy;

    public Trend(String kota, String imageUrl, String fashionItem, double accuracy) {
        this.kota = kota;
        this.imageUrl = imageUrl;
        this.fashionItem = fashionItem;
        this.accuracy = accuracy;
    }

    protected Trend(Parcel in) {
        kota = in.readString();
        imageUrl = in.readString();
        fashionItem = in.readString();
        accuracy = in.readDouble();
    }

    public static final Creator<Trend> CREATOR = new Creator<Trend>() {
        @Override
        public Trend createFromParcel(Parcel in) {
            return new Trend(in);
        }

        @Override
        public Trend[] newArray(int size) {
            return new Trend[size];
        }
    };

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFashionItem() {
        return fashionItem;
    }

    public void setFashionItem(String fashionItem) {
        this.fashionItem = fashionItem;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kota);
        dest.writeString(imageUrl);
        dest.writeString(fashionItem);
        dest.writeDouble(accuracy);
    }
}
