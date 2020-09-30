package com.avijitsamanta.wallpaperhub.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class Wallpaper implements Parcelable {
    private int id;
    private String photographerName, photographerUrl;
    private String originalUrl, mediumUrl;

    public Wallpaper() {
    }

    public Wallpaper(int id, String photographerName, String photographerUrl, String originalUrl, String mediumUrl) {
        this.id = id;
        this.photographerName = photographerName;
        this.photographerUrl = photographerUrl;
        this.originalUrl = originalUrl;
        this.mediumUrl = mediumUrl;
    }


    protected Wallpaper(Parcel in) {
        id = in.readInt();
        photographerName = in.readString();
        photographerUrl = in.readString();
        originalUrl = in.readString();
        mediumUrl = in.readString();
    }

    public static final Creator<Wallpaper> CREATOR = new Creator<Wallpaper>() {
        @Override
        public Wallpaper createFromParcel(Parcel in) {
            return new Wallpaper(in);
        }

        @Override
        public Wallpaper[] newArray(int size) {
            return new Wallpaper[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }

    public String getPhotographerUrl() {
        return photographerUrl;
    }

    public void setPhotographerUrl(String photographerUrl) {
        this.photographerUrl = photographerUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(photographerName);
        parcel.writeString(photographerUrl);
        parcel.writeString(originalUrl);
        parcel.writeString(mediumUrl);
    }
}
