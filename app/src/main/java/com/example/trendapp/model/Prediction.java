package com.example.trendapp.model;

public class Prediction {
    private String name;
    private double accuracy;

    public Prediction(String name, double accuracy) {
        this.name = name;
        this.accuracy = accuracy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
