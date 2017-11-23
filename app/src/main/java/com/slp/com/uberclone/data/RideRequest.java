package com.slp.com.uberclone.data;

/**
 * Created by Lakshmiprasad on 11/11/2017.
 */

public class RideRequest {
    private User user;
    private UberLatLng currentLocation;
    private UberLatLng destinationLocation;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UberLatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(UberLatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public UberLatLng getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(UberLatLng destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public RideRequest() {

    }

    public RideRequest(User user, UberLatLng currentLocation, UberLatLng destinationLocation) {
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
