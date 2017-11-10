package com.slp.com.uberclone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnSuccessListener<Location>, OnFailureListener {

    private static final String TAG = "RiderActivity:";
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final int LOCATION_ACCESS = 12;
    private LatLng currentLatLng;
    SupportMapFragment mapFragment;
    private boolean isLocationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (!isLocationAccessGranted(this)) {
            checkPermissions(this);
        } else{
            buildGoogleApi();

        }
    }


    private void buildGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .build();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (null != googleApiClient) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != googleApiClient) {
            googleApiClient.disconnect();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "on map ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        setCurrentLocationOnMap();


    }

    private void setCurrentLocationOnMap() {
        if (null != currentLatLng) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title(getAddress()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            isLocationSet = true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "on connected", Toast.LENGTH_SHORT).show();
        if (isLocationAccessGranted(this)) {
            accessLocation();
        } else {
            checkPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void accessLocation() {
        Log.i("onConnected: ", "connected");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000);
        Task<Location> newLocation = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        newLocation.addOnSuccessListener(this);
        newLocation.addOnFailureListener(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static void checkPermissions(Activity activity) {
        if (!isLocationAccessGranted(activity))
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_ACCESS);
    }

    public static boolean isLocationAccessGranted(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onSuccess(Location location) {
        Toast.makeText(this, "on success", Toast.LENGTH_SHORT).show();
        mapFragment.getMapAsync(this);
        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(!isLocationSet){
            setCurrentLocationOnMap();
        }

    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, "on failed", Toast.LENGTH_SHORT).show();
    }

    private String getAddress() {
        Geocoder geocoder = new Geocoder(this);
        List<Address> fromLocation = null;
        try {
            fromLocation = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null != fromLocation ? fromLocation.get(0).getAddressLine(0) : "Your location";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(isLocationAccessGranted(this)){
            buildGoogleApi();
            accessLocation();
        }
    }
}