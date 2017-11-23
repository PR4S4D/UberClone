package com.slp.com.uberclone.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by slaks on 24/11/2017.
 */

public class LocationUtils {

    public static Address getAddress(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null != addresses ? addresses.get(0) : null;
    }
}
