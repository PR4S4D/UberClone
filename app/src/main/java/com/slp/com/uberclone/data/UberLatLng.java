package com.slp.com.uberclone.data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by slaks on 24/11/2017.
 */

public class UberLatLng {

    private double longitude;
    private double latitude;

    public UberLatLng() {
    }

    public UberLatLng(LatLng latLng) {
         this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
