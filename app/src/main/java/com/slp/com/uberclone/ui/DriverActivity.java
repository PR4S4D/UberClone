package com.slp.com.uberclone.ui;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.slp.com.uberclone.R;
import com.slp.com.uberclone.data.RideRequest;
import com.slp.com.uberclone.data.UberLatLng;
import com.slp.com.uberclone.utils.LocationUtils;

import java.util.List;

public class DriverActivity extends AppCompatActivity {

    DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference requestReference = rootReference.child("request");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> requests = dataSnapshot.getChildren();

                Geocoder geocoder = new Geocoder(getApplicationContext());
                List<Address> addresses = null;
                
                for (DataSnapshot request : requests) {
                    UberLatLng currentLocation = request.getValue(RideRequest.class).getCurrentLocation();
                    LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                    if(null != latLng){
                        String address = LocationUtils.getAddress(getApplicationContext(), latLng).getAddressLine(0);
                        Toast.makeText(getApplicationContext(),address,Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
