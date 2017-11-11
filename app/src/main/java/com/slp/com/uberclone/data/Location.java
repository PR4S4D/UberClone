package com.slp.com.uberclone.data;

import android.location.Address;

/**
 * Created by Lakshmiprasad on 11/11/2017.
 */

public class Location {
    private User user;
    private Address currentAddress;
    private Address destination;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Location{" +
                "user=" + user +
                ", currentAddress=" + currentAddress +
                ", destination=" + destination +
                '}';
    }
}
