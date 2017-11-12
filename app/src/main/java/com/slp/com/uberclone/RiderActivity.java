package com.slp.com.uberclone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.slp.com.uberclone.data.RideRequest;
import com.slp.com.uberclone.utils.FirebaseUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnSuccessListener<Location>, OnFailureListener {

    private static final String TAG = "RiderActivity:";
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final int LOCATION_ACCESS = 12;
    private LatLng currentLatLng;
    private LatLng destinationLatLng;
    SupportMapFragment mapFragment;
    private boolean isLocationSet = false;
    @BindView(R.id.request_ride)
    FloatingActionButton requestRideFAB;
    @BindView(R.id.destination_tv)
    TextView destinationTV;
    private boolean rideBooked = false;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        ButterKnife.bind(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (!isLocationAccessGranted(this)) {
            checkPermissions(this);
        } else {
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
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title(FirebaseUtils.getFirebaseUser().getDisplayName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            isLocationSet = true;
            requestRideFAB.setVisibility(View.VISIBLE);
            setAutoPlacePicker();
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
        Toast.makeText(this, "Accessing Location!", Toast.LENGTH_SHORT).show();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        if (!isLocationSet) {
            setCurrentLocationOnMap();
        }


    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, "on failed", Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    private String getAddress() {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null != addresses ? addresses.get(0).getAddressLine(0) : "Your location";
    }

    private Address getCurrentAddress() {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null != addresses ? addresses.get(0) : null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (isLocationAccessGranted(this)) {
            buildGoogleApi();
            accessLocation();
        }
    }

    public void bookRide(View view) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("request");
        if (!rideBooked) {
            Log.i(TAG, "bookRide: "+destinationLatLng);
            RideRequest rideRequest = new RideRequest(FirebaseUtils.getUser(), currentLatLng, destinationLatLng);
            Snackbar.make(view, "Searching for nearest Uber!", Snackbar.LENGTH_SHORT).show();
            databaseReference.child(FirebaseUtils.getUserUid()).setValue(rideRequest);
            rideBooked = true;
            requestRideFAB.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        } else {
            DatabaseReference request = databaseReference.child(FirebaseUtils.getUserUid());
            Snackbar.make(view, "Cancelling your Uber!", Snackbar.LENGTH_SHORT).show();
            request.removeValue();
            requestRideFAB.setImageResource(R.drawable.ic_car);
            rideBooked = false;
        }

    }

    public void selectDestination(View view) {
        selectDestination();
    }

    private void selectDestination() {


        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);


        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void setAutoPlacePicker() {
        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

       /* AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        autocompleteFragment.setFilter(typeFilter);*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                destinationLatLng = place.getLatLng();
                destinationTV.setText(place.getName());
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


}