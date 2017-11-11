package com.slp.com.uberclone.data;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lakshmiprasad on 11/11/2017.
 */

public class RideRequest {
    private User user;
    private LatLng currentLocation;
    private LatLng destinationLocation;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LatLng destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public RideRequest(User user, LatLng currentLocation, LatLng destinationLocation) {
        this.user = user;
        this.currentLocation = currentLocation;
        this.destinationLocation = destinationLocation;
    }

    @Override
    public String toString() {
        return "RideRequest{" +
                "user=" + user +
                ", currentLocation=" + currentLocation +
                ", destinationLocation=" + destinationLocation +
                '}';
    }
}
