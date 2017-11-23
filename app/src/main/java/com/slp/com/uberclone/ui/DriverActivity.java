package com.slp.com.uberclone.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.slp.com.uberclone.R;
import com.slp.com.uberclone.data.RideRequest;

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
                Toast.makeText(DriverActivity.this,
                        String.valueOf(dataSnapshot.getChildren()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
