package com.example.android.guitar.web;

public class web {
    private double rate;
    private String description;
    private long mTimeInMilliseconds;
    private String mUrl;

    public web(double rate, String description, long timeInMilliseconds, String url) {
        this.rate = rate;
        this.description = description;
        this.mTimeInMilliseconds = timeInMilliseconds;
        this.mUrl = url;
    }
    public double getRate() {
        return rate;
    }
    public String getDescription() {
        return description;
    }
    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }
    public String getUrl() {
        return mUrl;
    }

}
