package com.slp.com.uberclone.utils;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.slp.com.uberclone.data.User;

/**
 * Created by Lakshmiprasad on 11/11/2017.
 */

public class FirebaseUtils {
    public static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public static FirebaseUser getFirebaseUser(){
        return firebaseAuth.getCurrentUser();
    }

    public static String getUserUid(){
        return getFirebaseUser().getUid();
    }

    public static User getUser(Context context) {
        FirebaseUser user = getFirebaseUser();
        return new User(user.getDisplayName(), PreferenceUtils.isDriver(context), user.getEmail());
    }
}
